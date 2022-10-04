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
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n", result.get())
    }

    @Test
    fun testWcFile1AndFile2() {
        val file1AndFile2 = listOf<Argument>(CLIArgument(FILE1_TEST.toString()), CLIArgument(FILE2_TEST.toString()))
        val result = wcExecutor.execute(file1AndFile2)
        Assert.assertEquals(
            "$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n$FILE2_LINES $FILE2_WORDS $FILE2_BYTES test_file2.txt\n$TOTAL_LINES $TOTAL_WORDS $TOTAL_BYTES total\n",
            result.get()
        )
    }

    companion object {
        private val FILE1_TEST = Paths.get("src/test/resources/test_file1.txt").absolute().toFile()
        private val FILE1_CONTENT = FILE1_TEST.readText()
        private val FILE1_WORDS = FILE1_CONTENT.split(' ').size
        private val FILE1_LINES = FILE1_CONTENT.count { it == '\n' }
        private val FILE1_BYTES = FILE1_CONTENT.toByteArray().size

        private val FILE2_TEST = Paths.get("src/test/resources/test_file2.txt").absolute().toFile()
        private val FILE2_CONTENT = FILE2_TEST.readText()
        private val FILE2_WORDS = FILE2_CONTENT.split(' ').size
        private val FILE2_LINES = FILE2_CONTENT.count { it == '\n' }
        private val FILE2_BYTES = FILE2_CONTENT.toByteArray().size

        private val TOTAL_WORDS = FILE1_WORDS + FILE2_WORDS
        private val TOTAL_LINES = FILE1_LINES + FILE2_LINES
        private val TOTAL_BYTES = FILE1_BYTES + FILE2_BYTES
    }
}