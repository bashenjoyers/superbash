package entities.executors

import Context
import entities.Argument
import entities.CLIArgument
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File
import java.nio.file.Paths
import java.util.Optional

@RunWith(PowerMockRunner::class)
class LsExecutorTest {
    private val curPath = Paths.get(File("").absolutePath)
    private val lsExecutor = LsExecutor(curPath)
    private val context = Context()


    @Test
    fun testLs1() {
        val expected = Optional.of(
            ".gitignore\n" +
                    ".gradle\n" +
                    ".idea\n" +
                    "build\n" +
                    "build.gradle\n" +
                    "gradle\n" +
                    "gradle.properties\n" +
                    "gradlew\n" +
                    "gradlew.bat\n" +
                    "settings.gradle\n" +
                    "src"
        )
        val result = lsExecutor.execute(listOf(CLIArgument("")), context)
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun testLs2() {
        val expected = Optional.of(
            "main\n" +
                    "test"
        )
        val result = lsExecutor.execute(listOf(CLIArgument("src")), context)
        Assertions.assertEquals(expected, result)
    }

}