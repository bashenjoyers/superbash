package entities.executors

import entities.Argument
import entities.Keyword
import entities.PipeArgument
import entities.executors.utils.FileSystemHelper
import java.nio.file.Path
import java.util.*
import kotlinx.cli.*
import java.lang.Exception

/**
 * Class for `grep` execution
 * @param curPath stores current path from context
 */
class GrepExecutor(curPath: Path): Keyword {
    private val fileSystemHelper = FileSystemHelper(curPath)
    /**
     * Method to get current path (execute `grep`)
     * @param arguments (all arguments to grep)
     * @return grep result String
     */
    override fun execute(arguments: List<Argument>): Optional<String> {
        val parser = ArgParser("superbash")
        var regex by parser.argument(ArgType.String, description = "regex")
        val files by parser.argument(ArgType.String, description = "vararg files").vararg()
        val wordRegexp by parser.option(ArgType.Boolean, fullName = "word-regexp", shortName = "w", description = "The expression is searched for as a word.").default(false)
        val ignoreCase by parser.option(ArgType.Boolean, fullName = "ignore-case", shortName = "i", description = "Perform case insensitive matching. By default, grep is case sensitive.").default(false)
        val afterContext by parser.option(ArgType.Int, fullName = "after-context", shortName = "A", description = "Print num lines of trailing context after each match.").default(0)
        parser.parse(arguments.map { it.getArgument() }.toTypedArray())
        val filesUpdated = files.toMutableList()
        if (filesUpdated.size > 1 && arguments.last() is PipeArgument) {
            filesUpdated.removeLast()
        }
        val fileContent = if (files.size == 1 && arguments.last() is PipeArgument) {
            val lastArgument = arguments.last().getArgument()
            filesUpdated.clear()
            lastArgument
        } else { null }

        if (regex.isNotEmpty() && ((regex.first() == '"' && regex.last() == '"') || (regex.first() == '\'' && regex.last() == '\''))) {
            regex = regex.substring(1, regex.length - 1)
        }
        val grep = Grep(fileSystemHelper=fileSystemHelper, regex=regex, files=filesUpdated, fileContent=fileContent, wordRegexp=wordRegexp, ignoreCase=ignoreCase, afterContext=afterContext)
        return grep.execute()
    }

    internal class Grep(val fileSystemHelper: FileSystemHelper, val regex: String, val files: List<String>, val fileContent: String?, val wordRegexp: Boolean, val ignoreCase: Boolean, val afterContext: Int) {
        private fun createRegex(): Regex {
            val regexOptions = mutableSetOf(RegexOption.MULTILINE)
            var regexStr = regex
            if (wordRegexp) {
                regexStr = "\\b${regexStr}\\b"
            }
            if (ignoreCase) {
                regexOptions.add(RegexOption.IGNORE_CASE)
            }
            return Regex(regexStr, regexOptions)
        }

        private fun executeContent(content: String, regex: Regex, builder: StringBuilder) {
            val indexes = mutableListOf<Int>()
            regex.findAll(content).toMutableList().forEach { match ->
                indexes.add(match.range.first)
            }
            if (indexes.isNotEmpty()) {
                val indexesSize = indexes.size
                var length = 0
                var ind = 0
                var needLines = 0
                for (line in content.lines()) {
                    length += line.length + 1
                    if (ind == indexesSize && needLines == 0)
                        break
                    while (ind < indexesSize && indexes[ind] < length) {
                        needLines = afterContext + 1
                        ind++
                    }
                    if (needLines != 0) {
                        builder.append(line)
                        if (afterContext == 0 || length != content.length) {
                            builder.append("\n")
                        }
                        if (afterContext != 0 && needLines == 1 && ind != indexesSize) {
                            builder.append("--\n")
                        }
                        needLines--
                    }
                }
            }
        }

        fun execute(): Optional<String> {
            assert(files.isEmpty() || fileContent == null) { "one of (\"files\", \"fileContent\") should be empty" }
            val builder = StringBuilder()
            val regex = createRegex()
            files.forEach { file ->
                val content = try {
                    fileSystemHelper.tryGetFile(file).readText()
                } catch (e: Exception) {
                    "grep: $file: No such file"
                }
                executeContent(content, regex, builder)
            }
            fileContent?.run {
                executeContent("$this\n", regex, builder)
            }

            return if (builder.isEmpty()) {
                Optional.empty<String>()
            } else {
                Optional.of(builder.toString())
            }
        }
    }
}