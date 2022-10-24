package entities.executors

import Context
import entities.Argument
import entities.Keyword
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.absolutePathString

class CdExecutor(): Keyword {
    override fun execute(arguments: List<Argument>, context: Context): Optional<String> {
        arguments.firstOrNull()?.let {
            context.directory = Paths.get(context.directory, it.getArgument()).normalize().absolutePathString()
        }
        return Optional.empty()
    }
}