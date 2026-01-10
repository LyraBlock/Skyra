package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.createEvent
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

@Suppress("unused")
object DestroyBlockEvents {
    fun interface StartDestroy {
        fun onStartDestroy(pos: BlockPos, direction: Direction)
    }

    fun interface Destroy {
        fun onDestroy(pos: BlockPos)
    }

    fun interface StopDestroy {
        fun onStopDestroy()
    }

    /**
     * Called when the player starts destroying a block.
     */
    @JvmField
    val START_DESTROY = createEvent { listeners ->
        StartDestroy { pos, direction ->
            listeners.forEach { it.onStartDestroy(pos, direction) }
        }
    }

    /**
     * Called when the player destroys a block finishing the breaking process.
     */
    @JvmField
    val DESTROY = createEvent { listeners ->
        Destroy { pos ->
            listeners.forEach { it.onDestroy(pos) }
        }
    }

    /**
     * Called when the player actively stops destroying a block.
     * This is called only if the player **releases left-click** or **moves away**,
     * and thus is **not** called if the targeted block IS destroyed.
     */
    @JvmField
    val STOP_DESTROY = createEvent<StopDestroy> { listeners ->
        {
            listeners.forEach { it.onStopDestroy() }
        }
    }
}