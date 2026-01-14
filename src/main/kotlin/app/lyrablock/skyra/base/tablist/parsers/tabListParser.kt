package app.lyrablock.skyra.base.tablist.parsers

sealed interface ParsedLine {
    data class Invalid(val raw: String) : ParsedLine
}

interface TabListParser<out T : ParsedLine> {
    /**
     * The widgets this parser is applicable on.
     */
    val targets: List<String>

    /**
     * @param line The line to parse, with whitespace and formats trimmed.
     */
    fun parse(line: String): T?
}

fun getColonSeparate(string: String): Pair<String, String?> {
    val colon = string.indexOf(':')
    if (colon == -1) return string to null
    val key = string.substring(0, colon).trim()
    val value = string.substring(colon + 1).trim()
    return key to value
}