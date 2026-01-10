package app.lyrablock.skyra.mixin;

import app.lyrablock.skyra.event.LevelRenderEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @ModifyArg(
            method = "renderBlockDestroyAnimation",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;getBuffer(Lnet/minecraft/client/renderer/rendertype/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;")
    )
    RenderType skyra$getRenderType(
            RenderType renderType, @Local(name = "pos") BlockPos pos
    ) {
        return LevelRenderEvents.MODIFY_BREAKING_ANIMATION.invoker().modify(pos, renderType);
    }
}
