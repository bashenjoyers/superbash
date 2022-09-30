package entities.executors

import entities.Argument
import entities.Keyword
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
        if (arguments.isEmpty()) {
            return Optional.empty()
        }

        var output = ""
        val totalStatistics = Statistics()

        for (argument in arguments) {
            try {
                val fileStat = processFile(Paths.get(argument.getArgument()))
                output += fileStat.toString() + " " + Paths.get(curPath.toString() + argument.getArgument()).fileName + "\n"
                totalStatistics += fileStat
            } catch (e: InvalidPathException) {
                output += "wc: ${argument.getArgument()}: No such file or directory"
            }
        }

        if (arguments.size > 1) output += "$totalStatistics total\n"

        return Optional.of(output)
    }

    /**
     * Method to count statistics for one file
     * @param relPath path to file (maybe relative)
     * @return statistics
     */
    private fun processFile(relPath: Path): Statistics {
        val file = if (relPath.isAbsolute) {
            relPath.toFile()
        } else {
            Paths.get(curPath.name + relPath).toFile()
        }
        val content = file.readText()
        return Statistics(content.count { it == '\n' }, content.split(' ').size, content.toByteArray().size)
    }
}
