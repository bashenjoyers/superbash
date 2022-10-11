package parsing

import Context
import entities.executors.*
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class KeywordCreatorTest {
    private val context = Context()
    private val keywordCreator = KeywordCreator()

    @Test
    fun testCreateKeywordIfCat() {
        val keyword = keywordCreator.createKeyword(CAT, context)
        Assert.assertTrue(keyword is CatExecutor)
    }

    @Test
    fun testCreateKeywordIfEcho() {
        val keyword = keywordCreator.createKeyword(ECHO, context)
        Assert.assertTrue(keyword is EchoExecutor)
    }

    @Test
    fun testCreateKeywordIfWc() {
        val keyword = keywordCreator.createKeyword(WC, context)
        Assert.assertTrue(keyword is WCExecutor)
    }

    @Test
    fun testCreateKeywordIfPwd() {
        val keyword = keywordCreator.createKeyword(PWD, context)
        Assert.assertTrue(keyword is PwdExecutor)
    }

    companion object {

        private const val CAT = "cat"
        private const val ECHO = "echo"
        private const val WC = "wc"
        private const val PWD = "pwd"
    }
}
