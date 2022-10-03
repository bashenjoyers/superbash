package entities.executors

import entities.Argument
import entities.Keyword
import entities.PipeArgument
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.name

/**
 * Class for `wc` execution
 * @param curPath stores current path from context
 */
class WCExecutor(private val curPath: Path): Keyword {
    /**
     * Class that stores statistics of files (lines, words and bytes)
     * @param lines is count of lines in file
     * @param words stores count of words in file
     * @param bytes stores bytes in file
     */
    private class Statistics(var lines: Int = 0, var words: Int = 0, var bytes: Int = 0) {
        override fun toString(): String = "$lines $words $bytes"

        operator fun plusAssign(other: Statistics) {
            lines += other.lines
            words += other.words
            bytes += other.bytes
        }
    }

    /**
     * Method to execute `wc`
     * @param arguments stores files on which run `wc`
     * @return statistics for every file
     */
    override fun execute(arguments: List<Argument>): Optional<String> {
        val updatedArguments = if (arguments.size > 1 && arguments.last() is PipeArgument) {
            arguments.dropLast(1)
        } else arguments
        if (updatedArguments.isEmpty()) {
            return Optional.empty()
        }

        var output = ""
        val totalStatistics = Statistics()

        for (argument in updatedArguments) {
            try {
                val file = getFile(argument.getArgument())
                val fileStat = processFile(file)
                output += fileStat.toString() + " " + Paths.get(curPath.toString() + argument.getArgument()).fileName + "\n"
                totalStatistics += fileStat
            } catch (e: FileNotFoundException) {
                output += "wc: ${argument.getArgument()}: No such file or directory\n"
            }
        }

        if (updatedArguments.size > 1) output += "$totalStatistics total\n"

        return Optional.of(output)
    }

    /**
     * Method to count statistics for one file
     * @param relPath path to file (maybe relative)
     * @return statistics
     */
    private fun processFile(file: File): Statistics {
        val content = file.readText()
        return Statistics(content.count { it == '\n' }, content.split(' ').size, content.toByteArray().size)
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

    /**
     * Method to get a file class based on curPath and path
     * @param path to a file
     * @return File based on configuration
     */
    private fun getFile(path: String): File {
        return if (isAbsolute(path)) {
            Paths.get(path).toFile()
        } else {
            Paths.get(curPath.name + path).toFile()
        }
    }
}
