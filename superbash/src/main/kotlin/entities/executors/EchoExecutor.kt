package entities.executors

import Context
import entities.Argument
import entities.Keyword
import entities.PipeArgument
import java.util.*

/**
 * Class to execute `echo` command
 */
class EchoExecutor: Keyword {
    /**
     * Method to execute `echo`
     * @param arguments stores strings to `echo`
     * @return string to print
     */
    override fun execute(arguments: List<Argument>,context: Context): Optional<String> {
        val updatedArguments = if (arguments.isNotEmpty() && arguments.last() is PipeArgument) {
             arguments.dropLast(1)
        } else arguments
        val output = updatedArguments.map { it.getArgument() }.joinToString(" ") + "\n"
        return Optional.of(output)
    }
}
