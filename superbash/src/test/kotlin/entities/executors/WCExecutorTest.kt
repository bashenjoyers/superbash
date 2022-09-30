package entities.executors

import entities.Argument
import entities.CLIArgument
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import java.nio.file.Paths
import kotlin.io.path.absolute

@RunWith(PowerMockRunner::class)
class WCExecutorTest {
    private val curPath = Paths.get("/")
    private val wcExecutor = WCExecutor(curPath)

    @Test
    fun testWcFile1() {
        val file1 = listOf<Argument>(CLIArgument(FILE1_TEST.toString()))
        val result = wcExecutor.execute(file1)
        Assert.assertEquals("1 1 13 test_file1.txt\n", result.get())
    }

    @Test
    fun testWcFile1AndFile2() {
        val file1AndFile2 = listOf<Argument>(CLIArgument(FILE1_TEST.toString()), CLIArgument(FILE2_TEST.toString()))
        val result = wcExecutor.execute(file1AndFile2)
        Assert.assertEquals("1 1 13 test_file1.txt\n1 1 8 test_file2.txt\n2 2 21 total\n", result.get())
    }

    companion object {
        private val FILE1_TEST = Paths.get("src/test/resources/test_file1.txt").absolute().toFile()
        private val FILE2_TEST = Paths.get("src/test/resources/test_file2.txt").absolute().toFile()
    }
}