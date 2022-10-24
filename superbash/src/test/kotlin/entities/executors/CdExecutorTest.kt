package entities.executors

import Context
import entities.CLIArgument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

@RunWith(PowerMockRunner::class)
class CdExecutorTest {
    private val curPath = File("").absolutePath
    private val cdExecutor = CdExecutor()
    private val context = Context()


    @BeforeEach
    fun resetDir() {
        context.directory = curPath
    }


    @Test
    fun testLs1() {
        cdExecutor.execute(listOf(CLIArgument("src")), context)
        val expected = Paths.get(curPath, "src").absolutePathString()
        Assertions.assertEquals(expected, context.directory)
    }

    @Test
    fun testLs2() {
        cdExecutor.execute(listOf(CLIArgument("src/..")), context)
        val expected = curPath
        Assertions.assertEquals(expected, context.directory)
    }

}