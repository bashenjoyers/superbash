package entities.executors

import Context
import entities.Argument
import entities.Keyword
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.absolutePathString


class LsExecutor(val curPath: Path) : Keyword {
    override fun execute(arguments: List<Argument>, context: Context): Optional<String> {
        val path = Paths.get(curPath.absolutePathString() , (arguments.firstOrNull()?.getArgument() ?: ""))
        val res = path.toFile().listFiles()?.joinToString(separator = "\n") { file -> file.name }
            ?: throw NoSuchFileException("Invalid path.")
        return Optional.of(res)
    }
}