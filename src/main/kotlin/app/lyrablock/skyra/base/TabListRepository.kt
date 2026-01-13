package app.lyrablock.skyra.base

import app.lyrablock.skyra.event.ClientPacketEvents
import app.lyrablock.skyra.mixin.accessor.PlayerTabOverlayAccessor
import app.lyrablock.skyra.utils.MC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.minecraft.ChatFormatting
import net.minecraft.client.multiplayer.PlayerInfo
import org.koin.core.component.KoinComponent

// TODO: This should be in a scope so it does not run outside of SkyBlock.

class TabListRepository : KoinComponent {
    private val parseMutex = Mutex()

    init {
        ClientPacketEvents.PLAYER_INFO_UPDATE.register { _, listedPlayers ->
            scope.launch {
                parseMutex.withLock {
                    onUpdate(listedPlayers)
                }
            }
        }
    }

    private data class Data(
        val allEntries: List<TabListEntry> = emptyList(),
        val widgetNameToEntries: Map<String, List<TabListEntry>> = emptyMap(),
        val nameToWidget: Map<String, TabListWidget> = emptyMap()
    )


    @Volatile
    private var data = Data()

    // Here ensures thread-safety when reading
    val allEntries get() = data.allEntries
    val widgetNameToEntries get() = data.widgetNameToEntries
    val nameToWidget get() = data.nameToWidget

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private fun onUpdate(listedPlayers: Set<PlayerInfo>) {
        val tabList = MC.gui.tabList ?: return
        val rawListedPlayers = listedPlayers.takeIf { it.size > 20 } ?: return
        val lines = rawListedPlayers.asSequence()
            // Sort the listed players with Minecraft's default comparator
            .sortedWith(PLAYER_COMPARATOR)
            // Convert the lines to Components, then to plain string
            .map { tabList.getNameForDisplay(it).string }
            .mapNotNull(ChatFormatting::stripFormatting)
            // Chunk by 20, which is the size of each column
            .chunked(20)
            // Remove columns that is not titled "Info" (hence titled "Players")
            // TODO: We may need a repo for this "Info" later on
            .filter { column -> column.firstOrNull()?.contains("Info") == true }
            // Remove "Info" lines
            .flatMap { column -> column.asSequence().drop(1) }

        val widgetNameToEntries = mutableMapOf<String, MutableList<TabListEntry>>()
        val allEntries = mutableListOf<TabListEntry>()
        val nameToWidget = mutableMapOf<String, TabListWidget>()
        var activeWidgetName = ""
        for (line in lines) {
            if (line.isBlank()) continue
            if (!line.startsWith(' ')) {
                // TODO: Move this match elsewhere?
                val match = COLON_SEPARATE_KEY_VALUE_LINE.matchEntire(line.trim()) ?: continue
                val key = match.groups["key"]!!.value
                val value = match.groups["value"]?.value
                val widget = TabListWidget(key, value)
                nameToWidget[key] = widget
                activeWidgetName = key
                continue
            }
            // TODO: Handle this more soundly
            check(activeWidgetName != "")
            val entry = TabListEntry(line.trim(), activeWidgetName)
            val siblingList = widgetNameToEntries.getOrPut(activeWidgetName, { mutableListOf() })
            siblingList.add(entry)
            allEntries.add(entry)
        }

        // Convert to immutable types then storage
        data = Data(allEntries.toList(), widgetNameToEntries.mapValues { it.value.toList() }, nameToWidget.toMap())
    }

    data class TabListEntry(val line: String, val widget: String)

    data class TabListWidget(val name: String, val value: String?)

    companion object {
        val COLON_SEPARATE_KEY_VALUE_LINE = Regex("""^(?<key>[^:]+?)(?::\s+(?<value>.+?))?$""")

        @Suppress("CAST_NEVER_SUCCEEDS")
        private val PLAYER_COMPARATOR by lazy {
            (MC.gui.tabList as PlayerTabOverlayAccessor).playerComparator
        }
    }
}