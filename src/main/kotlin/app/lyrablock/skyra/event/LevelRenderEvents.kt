package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.createEvent
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.core.BlockPos

object LevelRenderEvents {
    fun interface ModifyBreakingAnimation {
        fun modify(pos: BlockPos, renderType: RenderType): RenderType
    }

    @JvmField
    val MODIFY_BREAKING_ANIMATION = createEvent { listeners ->
        ModifyBreakingAnimation { pos, renderType ->
            var modifiedRenderType = renderType
            listeners.forEach {
                modifiedRenderType = it.modify(pos, modifiedRenderType)
            }

            modifiedRenderType
        }
    }
}