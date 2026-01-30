package app.lyrablock.skyra.utils.event

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.reflect.KClass

sealed class EventBus {
    val subscriptions = ConcurrentHashMap<KClass<*>, CopyOnWriteArraySet<(Any) -> Unit>>()

    /**
     * Register an event listener that is cancelable.
     */
    inline fun <reified T : Event> register(noinline listener: (T) -> Unit): EventSubscription {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST")
        val erasedListener = listener as (Any) -> Unit
        val listeners = subscriptions.getOrPut(kClass) { CopyOnWriteArraySet() }
        listeners.add(erasedListener)

        return EventSubscription {
            subscriptions[kClass]?.remove(erasedListener)

            if (subscriptions[kClass]?.isEmpty() == true) {
                subscriptions.remove(kClass)
            }
        }
    }

    inline fun <reified T: Event> register(crossinline listener: () -> Unit) =
        register { _: T -> listener() }

    /**
     * Register an event listener that is NOT cancelable.
     */
    inline fun <reified T : Event> registerGlobal(noinline listener: (T) -> Unit) {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST")
        val erasedListener = listener as (Any) -> Unit
        val listeners = subscriptions.getOrPut(kClass) { CopyOnWriteArraySet() }
        listeners.add(erasedListener)
    }

    fun dispatch(event: Event) {
        val kClass = event::class
        subscriptions[kClass]?.forEach {
            it.invoke(event)
        }
    }

    // Reserved for future use.
    fun dispatchSync(event: Event) = dispatch(event)

    companion object Default : EventBus()
}