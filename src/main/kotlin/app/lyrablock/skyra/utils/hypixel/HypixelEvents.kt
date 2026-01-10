package app.lyrablock.skyra.utils.hypixel

import app.lyrablock.skyra.utils.createEvent

object HypixelEvents {
    fun interface LocationChange {
        fun onChange(newLocation: HypixelStatus.LocationState)
    }

    @JvmField
    val LOCATION_CHANGE = createEvent { listeners ->
        LocationChange { location ->
            listeners.forEach { it.onChange(location) }
        }
    }
}
