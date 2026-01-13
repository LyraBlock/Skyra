package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.createEvent
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket

object ClientPacketEvents {
    fun interface PlayerInfoUpdate {
        fun onPacket(packet: ClientboundPlayerInfoUpdatePacket, listedPlayers: Set<PlayerInfo>)
    }

    @JvmField
    val PLAYER_INFO_UPDATE = createEvent { listeners ->
        PlayerInfoUpdate { packet, listedPlayers ->
            listeners.forEach { it.onPacket(packet, listedPlayers) }
        }
    }
}