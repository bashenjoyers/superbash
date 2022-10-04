import entities.*
import exceptions.Constants
import parsing.ContentParser
import parsing.VariablesSubstitutor
import java.util.*
import kotlin.jvm.optionals.getOrDefault

/**
 * Class to manage interaction with user
 */
class CLIManager(startDirectory: String = "/") {
    private val context = Context()

    init {
        context.directory = startDirectory
        context.variables["PATH"] = ""
    }

    private val parser = ContentParser()
    private val substitutor = VariablesSubstitutor()

    /**
     * Method to execute query
     * @param query from user
     * @return output for query and flag to handle `exit`
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun run(query: String): Optional<String> = try {
        val substitutedContent = substitutor.substitute(query, context)
        val parsedTokens = parser.parse(substitutedContent, context)

        var pipeArgument: PipeArgument? = null
        val arguments = mutableListOf<Argument>()
        var keyword: Keyword? = null
        for (token in parsedTokens) {
            when(token) {
                is Initialization -> {
                    context.variables[token.valueName] = token.value
                    pipeArgument = PipeArgument("")
                }
                is Keyword -> {
                    if (keyword != null) {
                        pipeArgument?.run { arguments.add(this) }
                        pipeArgument = PipeArgument(keyword.execute(arguments).getOrDefault(""))
                        arguments.clear()
                    }
                    keyword = token
                }
                is Argument -> {
                    arguments.add(token)
                }
            }
        }
        pipeArgument?.run { arguments.add(this) }
        keyword?.execute(arguments) ?: Optional.empty()
    } catch (ex: Exception) {
        Optional.of((ex.message ?: Constants.UNKNOWN_ERROR) + '\n')
    }
}
