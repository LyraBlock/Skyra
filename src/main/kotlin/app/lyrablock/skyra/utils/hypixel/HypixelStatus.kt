package app.lyrablock.skyra.utils.hypixel

import app.lyrablock.skyra.utils.MC
import net.hypixel.data.region.Environment
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HypixelStatus {
    val hypixelAPI: HypixelModAPI = HypixelModAPI.getInstance()

    init {
        // Fuck Hypixel if they don't send sound data.
        // We hereby assume it is.
        hypixelAPI.createHandler(ClientboundLocationPacket::class.java, ::onLocationPacket)
        hypixelAPI.createHandler(ClientboundPartyInfoPacket::class.java, ::onPartyInfoPacket)
        hypixelAPI.createHandler(ClientboundHelloPacket::class.java, ::onHello)
    }

    data class LocationState(
        val serverType: String?,
        val raw: String?
    ) {
        val onSkyblock = serverType == "SKYBLOCK"
        val skyblockLocation = SkyblockLocation.fromId(raw)
        val inDungeons = skyblockLocation == SkyblockLocation.DUNGEON
    }

    data class PartyState(
        val isInParty: Boolean?,
        val size: Int?,
        val leaderUUID: UUID?
    ) {
        val isLeader = leaderUUID != null && leaderUUID == MC.player?.uuid
    }

    @Volatile
    var environment: Environment? = null
        private set

    @Volatile
    var location = LocationState(null, null)
        private set

    @Volatile
    var party = PartyState(null, null, null)
        private set

    private fun onLocationPacket(packet: ClientboundLocationPacket) {
        val serverType = packet.serverType.getOrNull()?.name
        val rawLocation = packet.mode.getOrNull()
        val newLocation = LocationState(serverType, rawLocation)
        HypixelEvents.LOCATION_CHANGE.invoker().onChange(newLocation)
        this.location = newLocation
    }

    private fun onPartyInfoPacket(packet: ClientboundPartyInfoPacket) {
        val isInParty = packet.isInParty
        val partySize = packet.members.size
        party = PartyState(isInParty, partySize, packet.leader.getOrNull())
    }

    private fun onHello(packet: ClientboundHelloPacket) {
        environment = packet.environment
        // Every time the player rejoin the server we should invalidate the party info.
        hypixelAPI.sendPacket(ServerboundPartyInfoPacket())
    }
}
