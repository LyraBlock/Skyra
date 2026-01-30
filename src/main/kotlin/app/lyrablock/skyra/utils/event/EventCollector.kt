package app.lyrablock.skyra.utils.event

/**
 * Collect event subscriptions to unregister them all at once.
 */
class EventCollector(val bus: EventBus) {
    val subscriptions = mutableListOf<EventSubscription>()

    inline fun <reified T : Event> register(noinline listener: (T) -> Unit) {
        subscriptions.add(bus.register(listener))
    }
    inline fun <reified T : Event> register(noinline listener: () -> Unit) {
        subscriptions.add(bus.register<T>(listener))
    }

    fun unregisterAll() {
        subscriptions.forEach { it.unregister() }
        subscriptions.clear()
    }
}