package entities.executors

import Context
import entities.Argument
import entities.Keyword
import entities.PipeArgument
import entities.executors.utils.FileSystemHelper
import java.lang.Exception
import java.nio.file.Path
import java.util.*
import kotlin.io.path.name

/**
 * Class to execute `cat` command
 * @param curPath stores current path from context
 */
class CatExecutor(val curPath: Path): Keyword {
    private val fileSystemHelper = FileSystemHelper(curPath)

    /**
     * Method to execute `cat`
     * @param arguments stores files to cat
     * @return all files contents
     */
    override fun execute(arguments: List<Argument>, context: Context): Optional<String> {
        val updatedArguments = if (arguments.size > 1 && arguments.last() is PipeArgument) {
            arguments.dropLast(1)
        } else arguments
        if (updatedArguments.isEmpty()) {
            return Optional.empty()
        }

        var output = ""
        for (argument in updatedArguments) {
            output += try {
                fileSystemHelper.tryGetFile(argument.getArgument()).readText()
            } catch (e: Exception) {
                "cat: ${argument.getArgument()}: No such file or directory\n"
            }
        }
        return Optional.of(output)
    }
}
