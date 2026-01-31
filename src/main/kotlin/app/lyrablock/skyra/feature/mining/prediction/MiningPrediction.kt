package app.lyrablock.skyra.feature.mining.prediction

import app.lyrablock.skyra.event.BlockDestructionEvent
import app.lyrablock.skyra.event.LevelRenderEvent
import app.lyrablock.skyra.event.ServerTickEvent
import app.lyrablock.skyra.feature.mining.Mineable
import app.lyrablock.skyra.utils.MC
import app.lyrablock.skyra.utils.event.EventBus
import app.lyrablock.skyra.utils.event.EventCollector
import net.minecraft.client.resources.model.ModelBakery
import net.minecraft.core.BlockPos
import org.apache.logging.log4j.LogManager
import org.koin.dsl.module
import kotlin.math.floor
import kotlin.properties.Delegates.observable

class MiningPrediction(
    val hiddenBlocksRegistry: HiddenBlocksRegistry
) : AutoCloseable {
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

    val eventCollector = EventCollector(EventBus)

    init {
        eventCollector.register(this::onStartDestroy)
        eventCollector.register<BlockDestructionEvent.Stop>(this::onStopDestroy)
        eventCollector.register<ServerTickEvent.EndTick>(this::onServerTick)
        eventCollector.register(this::modifyBreaking)
    }

    private fun modifyBreaking(event: LevelRenderEvent.RenderBreakingAnimation) {
        val pos = event.pos
        if (current?.pos == pos)
            event.renderType = ModelBakery.DESTROY_TYPES[stage ?: 0]
    }

    private fun onStartDestroy(event: BlockDestructionEvent.Start) {
        val pos = event.pos
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

    override fun close() {
        eventCollector.unregisterAll()
    }

    companion object {
        val module = module {
            single(createdAtStart = true) { MiningPrediction(get()) }
        }
    }
}