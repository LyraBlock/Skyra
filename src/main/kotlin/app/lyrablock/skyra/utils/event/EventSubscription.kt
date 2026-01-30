package app.lyrablock.skyra.utils.event

import java.lang.AutoCloseable

fun interface EventSubscription: AutoCloseable {
    fun unregister()
    override fun close() = unregister()
}