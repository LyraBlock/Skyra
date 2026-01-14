package app.lyrablock.skyra.base.tablist.parsers

import app.lyrablock.skyra.base.tablist.WidgetName

data class StatLine(val name: String, val icon: String, val value: Int) : ParsedLine {
    class Parser : TabListParser<StatLine> {
        override val targets: List<String>
            get() = arrayOf(WidgetName.STATS).asList()

        override fun parse(line: String): StatLine? {
            return STAT_LINE_REGEX.matchEntire(line)?.let {
                val (name, icon, value) = it.destructured
                StatLine(name, icon, value.toInt())
            }
        }
    }

    companion object {
        val STAT_LINE_REGEX = Regex("""^(?<name>[^:]+):\s*?(?<icon>.)(?<value>[0-9]+)$""")
    }
}