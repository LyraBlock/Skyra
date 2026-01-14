package app.lyrablock.skyra.base.tablist

import app.lyrablock.skyra.base.tablist.parsers.ParsedLine
import app.lyrablock.skyra.base.tablist.parsers.StatLine
import app.lyrablock.skyra.base.tablist.parsers.TabListParser
import app.lyrablock.skyra.base.tablist.parsers.getColonSeparate
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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

// TODO: After this is tested to be sound and robust, migrate to a scope to prevent performance loss.

class TabListRepository(val parsers: List<TabListParser<*>>) : KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
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

    // Ensures atomic updates to the exposed data.

    private data class Data(
        val widgets: Map<String, List<ParsedLine>> = emptyMap(),
        val widgetTitleInfo: Map<String, TitleInfo> = emptyMap(),
    )

    @Volatile
    private var data = Data()

    val widgets get() = data.widgets
    val widgetTitleInfo get() = data.widgetTitleInfo

    private fun onUpdate(listedPlayers: Set<PlayerInfo>) {
        val tabList = MC.gui.tabList ?: return
        // A fully loaded tab list in SkyBlock always consists of 80 lines.
        val rawListedPlayers = listedPlayers.takeIf { it.size == 80 } ?: return
        // Process these lines.
        val lines = rawListedPlayers.asSequence()
            // sort the listed players with Minecraft's default comparator
            .sortedWith(PLAYER_COMPARATOR)
            // convert the lines to Components, then to plain string
            .map { tabList.getNameForDisplay(it).string }
            .mapNotNull(ChatFormatting::stripFormatting)
            // chunk by 20, which is the size of each column
            .chunked(20)
            // remove columns that is not titled "Info" (hence titled "Players")
            // TODO: We may need a repo for this "Info" later on
            .filter { column -> column.firstOrNull()?.contains("Info") == true }
            // remove "Info" lines
            .flatMap { column -> column.asSequence().drop(1) }

        // We do not modify the exposed data for thread-safety.
        val widgets = mutableMapOf<String, MutableList<ParsedLine>>()
        val widgetTitleInfo = mutableMapOf<String, TitleInfo>()

        // Parse the lines.
        var activeWidgetTitle = ""
        for (line in lines) {
            if (line.isBlank()) continue
            // Handle the case of a widget title line.
            // That is, lines like "Stats:", "Skills:", or "Fire Sale: 23h".
            if (!line.startsWith(' ')) {
                val (key, value) = getColonSeparate(line)
                val widget = TitleInfo(key, value)
                widgetTitleInfo[key] = widget
                activeWidgetTitle = key
                continue
            }
            // This line is a normal line inside a widget.
            val trimmed = line.trim()

            val parsedLine = parsers.firstNotNullOfOrNull { it.parse(trimmed) }
                ?: ParsedLine.Invalid(trimmed)

            // We assert this is not empty. It does not cause any error, but indicates a logic flaw.
            assert(activeWidgetTitle != "")

            val siblingList = widgets.getOrPut(activeWidgetTitle, { mutableListOf() })
            siblingList.add(parsedLine)
        }

        // Convert to immutable types then storage
        data = Data(
            widgets.mapValues { it.value.toList() },
            widgetTitleInfo.toMap()
        )
    }

    /**
     * A widget in the tab list. Examples are "Stats" and "Fire Sale: 23h".
     *
     * @param name The actual name of the widget. For instance, "Stats" or "Fire Sale".
     * @param value The value of the widget, if exists. For instance, "23h" for "Fire Sale: 23h".
     */
    data class TitleInfo(val name: String, val value: String?)

    companion object {
        @Suppress("CAST_NEVER_SUCCEEDS")
        private val PLAYER_COMPARATOR by lazy {
            (MC.gui.tabList as PlayerTabOverlayAccessor).playerComparator
        }

        val module = module {
            singleOf(StatLine::Parser) bind TabListParser::class
            single { TabListRepository(getAll()) }
        }
    }
}