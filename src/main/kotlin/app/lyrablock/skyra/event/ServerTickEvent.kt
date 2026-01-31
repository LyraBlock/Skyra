package app.lyrablock.skyra.event

import app.lyrablock.skyra.utils.event.Event
import net.minecraft.server.MinecraftServer

interface ServerTickEvent {
    data class StartTick(val server: MinecraftServer) : Event
    data class EndTick(val server: MinecraftServer) : Event
}