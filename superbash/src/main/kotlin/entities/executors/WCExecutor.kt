package entities.executors

import entities.Argument
import entities.Keyword
import entities.PipeArgument
import entities.executors.utils.FileSystemHelper
import java.io.FileNotFoundException
import java.nio.file.Path
import java.util.*

/**
 * Class for `wc` execution
 * @param curPath stores current path from context
 */
class WCExecutor(curPath: Path): Keyword {
    private val fileSystemHelper = FileSystemHelper(curPath)
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
        if (updatedArguments.size == 1 && updatedArguments.last() is PipeArgument) {
            val fileStat = processText(updatedArguments.first().getArgument())
            output += fileStat.toString() + "\n"
            return Optional.of(output)
        }
        val totalStatistics = Statistics()

        for (argument in updatedArguments) {
            try {
                val file = fileSystemHelper.tryGetFile(argument.getArgument())
                val fileStat = processText(file.readText())
                output += fileStat.toString() + " " + file.name + "\n"
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
    private fun processText(content: String): Statistics {
        return Statistics(content.count { it == '\n' }, content.split(' ').size, content.toByteArray().size)
    }
}
