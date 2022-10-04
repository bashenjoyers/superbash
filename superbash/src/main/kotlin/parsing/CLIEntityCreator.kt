package parsing

import Context
import entities.*
import exceptions.ParseException


/**
 * A class that can create all possible *[CLIEntity]*
 */
class CLIEntityCreator {
    private val keywordCreator = KeywordCreator()

    /**
     * Creates [Initialization] by given *expression*
     *
     * @return [Initialization]
     */
    fun createInitialization(expression: String): Initialization {
        val parsedExpression = expression.split("=")
        if (parsedExpression.size != 2) throw ParseException("Can't parse expression: $expression")

        return Initialization(parsedExpression[0], parsedExpression[1])
    }

    /**
     * Creates [CLIArgument] by given *argument*
     *
     * @return [CLIArgument]
     */
    fun createArgument(argument: String): Argument = CLIArgument(argument)

    /**
     * Delegates creation [Keyword] [KeywordCreator]
     *
     * @return [Keyword]
     */
    fun createKeyword(keyword: String, context: Context): Keyword = keywordCreator.createKeyword(keyword, context)

}
