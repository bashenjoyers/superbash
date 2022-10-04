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
class CatExecutorTest {
    private val curPath = Paths.get("/")
    private val catExecutor = CatExecutor(curPath)

    @Test
    fun testCatFile1() {
        val file1 = listOf<Argument>(CLIArgument(FILE1_TEST.toString()))
        val result = catExecutor.execute(file1)
        Assert.assertEquals(FILE1_CONTENT, result.get())
    }

    @Test
    fun testCatFile1AndFile2() {
        val file1AndFile2 = listOf<Argument>(CLIArgument(FILE1_TEST.toString()), CLIArgument(FILE2_TEST.toString()))
        val result = catExecutor.execute(file1AndFile2)
        Assert.assertEquals("$FILE1_CONTENT$FILE2_CONTENT", result.get())
    }

    companion object {
        private val FILE1_TEST = Paths.get("src/test/resources/test_file1.txt").absolute().toFile()
        private val FILE1_CONTENT = FILE1_TEST.readText()

        private val FILE2_TEST = Paths.get("src/test/resources/test_file2.txt").absolute().toFile()
        private val FILE2_CONTENT = FILE2_TEST.readText()
    }
}