package entities.executors

import Context
import entities.Argument
import entities.Keyword
import entities.executors.utils.FileSystemHelper
import exceptions.ParseException
import java.lang.Exception
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.name

/**
 * Class to execute external command (binary)
 * @param curPath stores current path in context
 * @param context stores context from CLIManager
 * @param relBinaryPath stores path to binary to execute (maybe relative)
 */
class ExternalExecutor(curPath: Path, private val context: Context, private val relBinaryPath: Path): Keyword {
    /**
     * Absolute path to binary got from `PATH` variable or checked relative path
     */
    private val fileSystemHelper = FileSystemHelper(curPath)
    private val binaryPath = getBinaryPath()

    /**
     * Method to execute external binary
     * @param arguments stores arguments for binary
     * @return Binary output
     */
    override fun execute(arguments: List<Argument>, context: Context): Optional<String> {
        val process = ProcessBuilder(binaryPath.name, *arguments.map { it.getArgument() }.toTypedArray())
            .directory(fileSystemHelper.curPath.toFile())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        process.waitFor()
        val output = process.inputStream.bufferedReader().readText()

        return Optional.of(output)
    }

    /**
     * Method to get binary path from `PATH` or from relative path
     * @return path or null
     */
    private fun getBinaryPath() : Path {
        tryGetInPathVariable()?.let { return it }

        return try {
            fileSystemHelper.tryGetPath(relBinaryPath.toString())
        } catch (e: Exception) {
            throw ParseException("No such binary: ${relBinaryPath.name}")
        }
    }

    /**
     * Method to get binary path from `PATH`
     * @return path or null
     */
    private fun tryGetInPathVariable() : Path? {
        val pathVariables = context.variables["PATH"]?.split(';')

        if (pathVariables != null) {
            for (pathString in pathVariables) {
                val path = Paths.get(pathString)
                val files = path.toFile().listFiles() ?: continue
                for (file in files) {
                    if (file.name.equals(relBinaryPath.name)) return file.toPath()
                }
            }
        }

        return null
    }
}
