package app.lyrablock.skyra.mixin;

import app.lyrablock.skyra.event.LevelRenderEvent;
import app.lyrablock.skyra.utils.event.EventBus;
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
        final var event = new LevelRenderEvent.RenderBreakingAnimation(pos, renderType);
        EventBus.Default.dispatchSync(event);
        return event.getRenderType();
    }
}
