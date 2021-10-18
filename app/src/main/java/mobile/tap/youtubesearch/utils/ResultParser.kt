package mobile.tap.youtubesearch.utils

import java.util.regex.Pattern

/**
 * @desc Tips: ResultParser
 * @author Tim Yeo
 * @date 2021/10/17 21:22
 */
object ResultParser {
    private var prefix = """
        \{"twoColumnSearchResultsRenderer":\{"primaryContents":\{"sectionListRenderer":\{"contents":\[\{"itemSectionRenderer":\{"contents":
    """.trimIndent()

    private var suffix = """
        \],"trackingParams"
    """.trimIndent()

    private var itemReg = """
        \{"videoRenderer":\{"videoId":"[A-Za-z0-9]{1,}",
    """.trimIndent()

    private var reg = "$prefix(.+)$suffix"

    fun parse(text: String): List<String> {
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(text)
        val ids = mutableListOf<String>()
        if (matcher.find()) {
            val result = matcher.group()
            if (result.isNotBlank()) {
                val itemPattern = Pattern.compile(itemReg)
                val itemMatcher = itemPattern.matcher(result)
                while (itemMatcher.find()) {
                    itemMatcher.group().split("\"").dropLast(1).lastOrNull()?.let {
                        ids.add(it)
                    }
                }
            }
        }

        return ids
    }
}