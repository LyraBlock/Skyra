package app.lyrablock.skyra.mixin;

import app.lyrablock.skyra.event.ServerTickEvent;
import app.lyrablock.skyra.utils.event.EventBus;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/**
 * @see net.fabricmc.fabric.mixin.event.lifecycle.MinecraftServerMixin
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"), method = "tickServer")
    private void onStartTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        EventBus.Default.dispatch(new ServerTickEvent.StartTick((MinecraftServer) (Object) this));
    }

    @Inject(at = @At(value = "TAIL"), method = "tickServer")
    private void onStartTick1(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        EventBus.Default.dispatch(new ServerTickEvent.EndTick((MinecraftServer) (Object) this));
    }
}
