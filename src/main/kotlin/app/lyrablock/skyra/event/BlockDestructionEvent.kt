package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.event.Event
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

interface BlockDestructionEvent {
    data class Start(val pos: BlockPos, val direction: Direction) : Event
    data class Destroy(val pos: BlockPos) : Event
    class Stop : Event
}