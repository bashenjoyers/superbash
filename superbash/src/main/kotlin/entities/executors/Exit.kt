package entities.executors

import entities.Argument
import entities.Keyword
import java.lang.Exception
import java.util.*
import kotlin.system.exitProcess

/**
 * Class that describes `exit` command
 */
class Exit: Keyword {
    /**
     * Execute exiting from process
     * @param arguments stores arguments for exit
     * @return empty if it didn't exit
     */
    override fun execute(arguments: List<Argument>): Optional<String> {
        if (arguments.size > 1) {
            return Optional.of("exit: too many arguments")
        }

        val exitCode = try {
            if (arguments.isEmpty()) 0
            else arguments.first().getArgument().toInt()
        } catch (e: Exception) {
            -1
        }

        exitProcess(exitCode)
    }
}
