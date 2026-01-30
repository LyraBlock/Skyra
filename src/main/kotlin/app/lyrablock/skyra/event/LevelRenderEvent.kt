package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.event.Event
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.core.BlockPos

interface LevelRenderEvent {
    data class RenderBreakingAnimation(
        val pos: BlockPos,
        var renderType: RenderType
    ) : Event
}