import org.junit.Assert
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Paths
import kotlin.io.path.absolute

@RunWith(JUnit4::class)
class CLIManagerTest {
    private val testDir = "/"
    private val manager = CLIManager(testDir)

    @Test
    fun testEcho() {
        val result = manager.run("echo 123")
        Assert.assertEquals("123\n", result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testEchoSplitted() {
        val result = manager.run("echo 1 2 3")
        Assert.assertEquals("1 2 3\n", result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testExit() {
        val result = manager.run("exit")
        Assert.assertTrue(result.shouldExit)
    }

    @Disabled("Exit codes not implemented yet")
    @Test
    fun testExitOneParameter() {
        val result = manager.run("exit 1")
        Assert.assertTrue(result.shouldExit)
    }

    @Test
    fun testExitTwoParameters() {
        val result = manager.run("exit 1 2")
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testPwd() {
        val result = manager.run("pwd")
        Assert.assertEquals(testDir, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testPwdOneArgument() {
        val result = manager.run("pwd 1")
        Assert.assertEquals(testDir, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testPwdThreeArguments() {
        val result = manager.run("pwd 1 2 3")
        Assert.assertEquals(testDir, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testCat1() {
        val result = manager.run("cat $FILE1_TEST")
        Assert.assertEquals(FILE1_CONTENT, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testCat2() {
        val result = manager.run("cat $FILE2_TEST")
        Assert.assertEquals(FILE2_CONTENT, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testCatDouble() {
        val result = manager.run("cat $FILE1_TEST $FILE2_TEST")
        Assert.assertEquals(FILE1_CONTENT + FILE2_CONTENT, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testCatSelfTriple() {
        val result = manager.run("cat $FILE1_TEST $FILE1_TEST $FILE1_TEST")
        Assert.assertEquals(FILE1_CONTENT + FILE1_CONTENT + FILE1_CONTENT, result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testCatError() {
        val result = manager.run("cat amogus")
        Assert.assertEquals("cat: amogus: No such file or directory\n", result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testCatWithError() {
        val result = manager.run("cat $FILE1_TEST amogus")
        Assert.assertEquals(FILE1_CONTENT + "cat: amogus: No such file or directory\n", result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testWC() {
        val result = manager.run("wc $FILE1_TEST")
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n", result.output.get())
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testWCDouble() {
        val result = manager.run("wc $FILE1_TEST $FILE2_TEST")
        Assert.assertEquals(
            "$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n${FILE2_LINES} $FILE2_WORDS $FILE2_BYTES test_file2.txt\n${TOTAL_LINES} $TOTAL_WORDS $TOTAL_BYTES total\n",
            result.output.get()
        )
        Assert.assertFalse(result.shouldExit)
    }

    @Test
    fun testWCDoubleWithError() {
        val result = manager.run("wc $FILE1_TEST amogus $FILE2_TEST")
        Assert.assertEquals(
            "$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\nwc: amogus: No such file or directory\n${FILE2_LINES} $FILE2_WORDS $FILE2_BYTES test_file2.txt\n${TOTAL_LINES} $TOTAL_WORDS $TOTAL_BYTES total\n",
            result.output.get()
        )
        Assert.assertFalse(result.shouldExit)
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