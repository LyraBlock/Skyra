package app.lyrablock.skyra.mixin;

import app.lyrablock.skyra.event.DestroyBlockEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
@Environment(EnvType.CLIENT)
public class MultiPlayerGameModeMixin {
    @Shadow
    private boolean isDestroying;

    @Inject(method = "startDestroyBlock", at = @At("HEAD"))
    void skyra$startDestroyBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        DestroyBlockEvents.START_DESTROY.invoker().onStartDestroy(pos, direction);
    }

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    void skyra$destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (isDestroying)
            DestroyBlockEvents.DESTROY.invoker().onDestroy(pos);
    }

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
    void skyra$continueDestroyBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
    }

    @Inject(method = "stopDestroyBlock", at = @At("HEAD"))
    void stopDestroyBlock(CallbackInfo ci) {
        DestroyBlockEvents.STOP_DESTROY.invoker().onStopDestroy();
    }
}