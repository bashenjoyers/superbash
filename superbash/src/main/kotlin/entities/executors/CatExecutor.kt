package entities.executors

import entities.Argument
import entities.Keyword
import entities.PipeArgument
import exceptions.RunException
import java.io.FileNotFoundException
import java.lang.Exception
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.name

/**
 * Class to execute `cat` command
 * @param curPath stores current path from context
 */
class CatExecutor(private val curPath: Path): Keyword {
    /**
     * Method to execute `cat`
     * @param arguments stores files to cat
     * @return all files contents
     */
    override fun execute(arguments: List<Argument>): Optional<String> {
        val updatedArguments = if (arguments.size > 1 && arguments.last() is PipeArgument) {
            arguments.dropLast(1)
        } else arguments
        if (updatedArguments.isEmpty()) {
            return Optional.empty()
        }

        var output = ""

        for (argument in updatedArguments) {
            output += tryRead(argument.getArgument())
        }

        return Optional.of(output)
    }

    /**
     * Method to read a file
     * @param relPath stores path to file
     * @return file content
     */
    private fun tryRead(relPath: String): String {
        return try {
            val file = if (isAbsolute(relPath)) {
                Paths.get(relPath).toFile()
            } else {
                Paths.get(curPath.name + relPath).toFile()
            }
            file.readText()
        } catch (e: FileNotFoundException) {
            "cat: ${relPath}: No such file or directory\n"
        }
    }

    /**
     * Method to check is path is absolute
     * @param path stores path to file
     * @return true if path exists and it is absolute
     */
    private fun isAbsolute(path: String): Boolean {
        return try {
            val pathCheck = Paths.get(path)
            pathCheck.isAbsolute
        } catch (e: Exception) {
            false
        }
    }
}
