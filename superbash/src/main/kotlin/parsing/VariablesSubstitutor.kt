package parsing

import Context
import parsing.helpers.ImportantChars
import parsing.helpers.splitBySpaceAndPipe
import kotlin.math.sin

/**
 * A class that substitutes variables in content if content is in quote marks or without any marks
 */
class VariablesSubstitutor {
    private val bashVariableRegex = "\\$[a-zA-Z_][a-zA-Z_0-9]*".toRegex()

    /**
     * Make a substitution in `content` with variables in `context`
     */
    fun substitute(content: String, context: Context): String {
        val splittedContent = splitBySpaceAndPipe(content)
        val substitutedContent = splittedContent.map {
            when {
                it.isQuoteMarkString() || it.isNotMarkedString() -> substituteOneString(it, context)
                else -> it
            }
        }
        return substitutedContent.joinToString(" ")
    }

    /**
     * Make a substitution in one string
     */
    private fun substituteOneString(content: String, context: Context): String {
        var substitutedContent = content
        bashVariableRegex.findAll(content).toMutableList().reversed().forEach { match ->
            substitutedContent = context.variables[match.value.drop(1)]
                ?.let { substitutedContent.replaceRange(match.range, it) }
                ?: substitutedContent
        }
        return substitutedContent
    }

    /**
     * Checks if string is inside quote marks
     */
    private fun String.isQuoteMarkString(): Boolean {
        return first() == ImportantChars.QUOTE_MARK_CHAR && last() == ImportantChars.QUOTE_MARK_CHAR
    }

    /**
     * Checks if string is inside single marks
     */
    private fun String.isSingleMarkString(): Boolean {
        return first() == ImportantChars.SINGLE_MARK_CHAR && last() == ImportantChars.SINGLE_MARK_CHAR
    }

    /**
     * Check if string has no border marks
     */
    private fun String.isNotMarkedString(): Boolean {
        return !isQuoteMarkString() && !isSingleMarkString()
    }
}
