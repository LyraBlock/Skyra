package app.lyrablock.skyra.mixin;

import app.lyrablock.skyra.event.ClientPacketEvent;
import app.lyrablock.skyra.utils.event.EventBus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
abstract public class ClientPacketListenerMixin {
    @Final
    @Shadow
    private Set<PlayerInfo> listedPlayers;

    @Inject(method = "handlePlayerInfoUpdate", at = @At("TAIL"))
    void skyra$handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket packet, CallbackInfo ci) {
        EventBus.Default.dispatch(new ClientPacketEvent.PlayerInfoUpdate(packet, listedPlayers));
    }
}
