package entities.executors

import entities.Argument
import entities.Keyword
import exceptions.RunException
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
        if (arguments.isEmpty()) {
            return Optional.empty()
        }

        var output = ""

        for (argument in arguments) {
            output += tryRead(Paths.get(argument.getArgument()))
        }

        return Optional.of(output)
    }

    /**
     * Method to read a file
     * @param relPath stores path to file
     * @return file content
     */
    private fun tryRead(relPath: Path): String {
        return try {
            val file = if (relPath.isAbsolute) {
                relPath.toFile()
            } else {
                Paths.get(curPath.name + relPath).toFile()
            }
            file.readText()
        } catch (e: InvalidPathException) {
            "cat: ${relPath}: No such file or directory\n"
        }
    }
}
