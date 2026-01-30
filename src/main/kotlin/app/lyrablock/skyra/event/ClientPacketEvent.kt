package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.event.Event
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket

interface ClientPacketEvent {
    data class PlayerInfoUpdate(val packet: ClientboundPlayerInfoUpdatePacket, val listedPlayers: Set<PlayerInfo>) : Event
}