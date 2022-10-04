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
        Assert.assertEquals("123\n", result.get())
    }

    @Test
    fun testEchoSplitted() {
        val result = manager.run("echo 1 2 3")
        Assert.assertEquals("1 2 3\n", result.get())
    }

    @Disabled("Exit should be tested in child process")
    @Test
    fun testExit() {
        val result = manager.run("exit")
    }

    @Disabled("Exit should be tested in child process")
    @Test
    fun testExitOneParameter() {
        val result = manager.run("exit 1")
    }

    @Disabled("Exit should be tested in child process")
    @Test
    fun testExitTwoParameters() {
        val result = manager.run("exit 1 2")
    }

    @Test
    fun testPwd() {
        val result = manager.run("pwd")
        Assert.assertEquals(testDir, result.get())
    }

    @Test
    fun testPwdOneArgument() {
        val result = manager.run("pwd 1")
        Assert.assertEquals(testDir, result.get())
    }

    @Test
    fun testPwdThreeArguments() {
        val result = manager.run("pwd 1 2 3")
        Assert.assertEquals(testDir, result.get())
    }

    @Test
    fun testCat1() {
        val result = manager.run("cat $FILE1_TEST")
        Assert.assertEquals(FILE1_CONTENT, result.get())
    }

    @Test
    fun testCat2() {
        val result = manager.run("cat $FILE2_TEST")
        Assert.assertEquals(FILE2_CONTENT, result.get())
    }

    @Test
    fun testCatDouble() {
        val result = manager.run("cat $FILE1_TEST $FILE2_TEST")
        Assert.assertEquals(FILE1_CONTENT + FILE2_CONTENT, result.get())
    }

    @Test
    fun testCatSelfTriple() {
        val result = manager.run("cat $FILE1_TEST $FILE1_TEST $FILE1_TEST")
        Assert.assertEquals(FILE1_CONTENT + FILE1_CONTENT + FILE1_CONTENT, result.get())
    }

    @Test
    fun testCatError() {
        val result = manager.run("cat amogus")
        Assert.assertEquals("cat: amogus: No such file or directory\n", result.get())
    }

    @Test
    fun testCatWithError() {
        val result = manager.run("cat $FILE1_TEST amogus")
        Assert.assertEquals(FILE1_CONTENT + "cat: amogus: No such file or directory\n", result.get())
    }

    @Test
    fun testWC() {
        val result = manager.run("wc $FILE1_TEST")
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n", result.get())
    }

    @Test
    fun testWCDouble() {
        val result = manager.run("wc $FILE1_TEST $FILE2_TEST")
        Assert.assertEquals(
            "$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n${FILE2_LINES} $FILE2_WORDS $FILE2_BYTES test_file2.txt\n${TOTAL_LINES} $TOTAL_WORDS $TOTAL_BYTES total\n",
            result.get()
        )
    }

    @Test
    fun testWCDoubleWithError() {
        val result = manager.run("wc $FILE1_TEST amogus $FILE2_TEST")
        Assert.assertEquals(
            "$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\nwc: amogus: No such file or directory\n${FILE2_LINES} $FILE2_WORDS $FILE2_BYTES test_file2.txt\n${TOTAL_LINES} $TOTAL_WORDS $TOTAL_BYTES total\n",
            result.get()
        )
    }

    @Test
    fun testPhase2_1() {
        val result = manager.run("echo 2 | wc")
        Assert.assertEquals("1 1 2\n", result.get())
    }

    @Test
    fun testPhase2_2() {
        val result = manager.run("echo 2 | wc | echo 3")
        Assert.assertEquals("3\n", result.get())
    }

    @Test
    fun testPhase2_3() {
        val result = manager.run("wc $FILE1_TEST | echo 3")
        Assert.assertEquals("3\n", result.get())
    }

    @Test
    fun testPhase2_4() {
        val result = manager.run("cat $FILE1_TEST | wc")
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES\n", result.get())
    }

    @Test
    fun testPhase2_5() {
        val result = manager.run("cat $FILE1_TEST | wc")
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES\n", result.get())
    }

    @Test
    fun testPhase2_6() {
        val result = manager.run("echo 2 | pwd")
        Assert.assertEquals(testDir, result.get())
    }

    @Test
    fun testPhase2_7() {
        val result = manager.run("pwd | pwd")
        Assert.assertEquals(testDir, result.get())
    }

    @Test
    fun testPhase2_8() {
        val result = manager.run("pwd | pwd")
        Assert.assertEquals(testDir, result.get())
    }

    @Test
    fun testPhase2_9() {
        val result = manager.run("cat $FILE2_TEST | cat $FILE1_TEST")
        Assert.assertEquals(FILE1_CONTENT, result.get())
    }

    @Test
    fun testPhase2_10() {
        val result = manager.run("cat $FILE2_TEST | wc $FILE1_TEST")
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n", result.get())
    }

    @Test
    fun testPhase2_11() {
        val result = manager.run("echo 2 | wc $FILE1_TEST")
        Assert.assertEquals("$FILE1_LINES $FILE1_WORDS $FILE1_BYTES test_file1.txt\n", result.get())
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