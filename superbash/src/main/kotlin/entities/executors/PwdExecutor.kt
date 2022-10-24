package entities.executors

import Context
import entities.Argument
import entities.Keyword
import java.nio.file.Path
import java.util.*
import kotlin.io.path.name

/**
 * Class for `pwd` execution
 * @param curPath stores current path from context
 */
class PwdExecutor(private val curPath: Path): Keyword {
    /**
     * Method to get current path (execute `pwd`)
     * @param arguments (ignored since pwd does not require any arguments)
     * @return current path String
     */
    override fun execute(arguments: List<Argument>, context: Context): Optional<String> = Optional.of(curPath.toString() + "\n")
}
