package app.lyrablock.skyra.utils

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.impl.base.event.EventFactoryImpl

inline fun <reified T: Any> createEvent(noinline invokerFactory: (Array<T>) -> T): Event<T> {
    return EventFactoryImpl.createArrayBacked<T>(T::class.java, invokerFactory)
}
