import entities.Argument
import entities.CLIEntity
import entities.Keyword
import exceptions.Constants
import exceptions.RunException
import io.ConsoleContentInput
import io.ConsoleContentOutput
import parsing.ContentParser
import java.security.Key
import java.util.*

/**
 * Class to manage interaction with user
 */
class CLIManager {

    private val consoleContentInput = ConsoleContentInput()
    private val consoleContentOutput = ConsoleContentOutput()
    private val context = Context()

    /**
     * Method to handle interaction with user
     */
    fun run() {
        while (true) {
            val result: Optional<String> = try {
                val parsedTokens = processInput()
                execute(parsedTokens)
            } catch (ex: Exception) {
                Optional.of(ex.message ?: Constants.UNKNOWN_ERROR)
            }
            processOutput(result)
        }
    }

    /**
     * Method to get user's command
     * @return List<CLIEntity> representing parsed command
     */
    private fun processInput(): List<CLIEntity> {
        val content = consoleContentInput.getContent()
        val parser = ContentParser()

        return parser.parse(content, context)
    }

    /**
     * Method to handle parsed entities
     * @param tokens List<CLIEntity> representing parsed command
     * @return Optional<String> containing possible result of execution
     */
    private fun execute(tokens: List<CLIEntity>): Optional<String> {
        if (tokens.isEmpty()) {
            return Optional.empty()
        }

        val firstToken = tokens.first() as Keyword
        return firstToken.execute(tokens.drop(1).map { it as Argument })
    }

    /**
     * Method to process result of keywords execution
     * @param executionResult String representing result of execution
     */
    private fun processOutput(executionResult: Optional<String>) {
        if (executionResult.isPresent) {
            consoleContentOutput.printContent(executionResult.get())
        }
    }
}
