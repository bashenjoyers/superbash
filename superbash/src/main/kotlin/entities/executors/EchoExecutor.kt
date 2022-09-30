package entities.executors

import entities.Argument
import entities.Keyword
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
    override fun execute(arguments: List<Argument>): Optional<String> {
        val output = arguments.map { it.getArgument() }.joinToString(" ") + "\n"
        return Optional.of(output)
    }
}
