package app.lyrablock.skyra.feature.mining.prediction

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.core.BlockPos
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HiddenBlocksRegistry {
    init {
        ClientTickEvents.END_CLIENT_TICK.register { removeOutdated() }
    }

    data class Data(val pos: BlockPos, val time: Long, val age: Long)

    val hiddenBlocks = mutableSetOf<Data>()

    private fun removeOutdated() {
        val now = System.nanoTime()
        hiddenBlocks.removeAll { now - it.time > it.age }
    }

    fun add(pos: BlockPos, age: Long) {
    }


    object Hook : KoinComponent {
        private val registry: HiddenBlocksRegistry by inject()

        fun isHidden(pos: BlockPos): Boolean {
            return registry.hiddenBlocks.any { it.pos == pos }
        }
    }
}