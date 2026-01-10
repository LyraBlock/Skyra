package app.lyrablock.skyra.feature.mining.prediction

import app.lyrablock.skyra.event.DestroyBlockEvents
import app.lyrablock.skyra.event.LevelRenderEvents
import app.lyrablock.skyra.feature.mining.Mineable
import app.lyrablock.skyra.utils.MC
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.resources.model.ModelBakery
import net.minecraft.core.BlockPos
import org.apache.logging.log4j.LogManager
import org.koin.dsl.module
import kotlin.math.floor
import kotlin.properties.Delegates.observable

class MiningPrediction(
    val hiddenBlocksRegistry: HiddenBlocksRegistry
) {
    private val logger = LogManager.getLogger("Skyra/MiningPrediction")
    private val shouldRun get() = true

    data class BlockInfo(val pos: BlockPos, val block: Mineable, val ticksToMine: Int)

    var current: BlockInfo? by observable(null) { _, _, _ -> elapsedTicks = 0 }
        private set

    private var elapsedTicks = 0
    val stage
        get() = current?.run {
            floor(9.0 * elapsedTicks / ticksToMine.toDouble()).toInt().coerceAtMost(9)
        }

    init {
        DestroyBlockEvents.START_DESTROY.register { pos, _ -> onStartDestroy(pos) }
        DestroyBlockEvents.STOP_DESTROY.register { onStopDestroy() }
        ServerTickEvents.END_SERVER_TICK.register { onServerTick() }
        LevelRenderEvents.MODIFY_BREAKING_ANIMATION.register(this::modifyBreaking)
    }

    private fun modifyBreaking(pos: BlockPos, type: RenderType): RenderType {
        if (current?.pos != pos) return type
        return ModelBakery.DESTROY_TYPES[stage ?: 0]
    }

    private fun onStartDestroy(pos: BlockPos) {
        if (!shouldRun) return
        // Reset elapsed ticks if the block has changed
        current?.let { if (it.pos != pos) elapsedTicks = 0 }
        val state = MC.level?.getBlockState(pos) ?: return
        val block = Mineable.fromBlock[state.block] ?: return
        current = BlockInfo(
            pos,
            block,
            block.timeToMine(5000)
        )
    }

    private fun onStopDestroy() {
        current = null
        elapsedTicks = 0
    }

    private fun onServerTick() {
        val (_, _, ticksToMine) = this.current ?: return
        if (elapsedTicks < ticksToMine)
            ++elapsedTicks

        if (elapsedTicks == ticksToMine) {
//            hiddenBlocksRegistry.add(pos, age = 1_000_000_000)
            ++elapsedTicks
        }

    }

    companion object {
        val module = module {
            single(createdAtStart = true) { MiningPrediction(get()) }
        }
    }
}