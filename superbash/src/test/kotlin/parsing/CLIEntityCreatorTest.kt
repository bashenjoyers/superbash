package parsing

import Context
import entities.Keyword
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(KeywordCreator::class, Keyword::class)
class CLIEntityCreatorTest {

    private val keywordCreator = PowerMockito.mock(KeywordCreator::class.java)
    private val keyword = PowerMockito.mock(Keyword::class.java)
    private val context = Context()
    private val cliEntityCreator = CLIEntityCreator()

    @Before
    fun setUp() {
        PowerMockito.whenNew(KeywordCreator::class.java)
            .withAnyArguments()
            .thenReturn(keywordCreator)
    }

    @Test
    fun testCreateArgument() {
        val someArgument = "some argument"
        val resultEntityArgument = cliEntityCreator.createArgument(someArgument)
        Assert.assertEquals(someArgument, resultEntityArgument.getArgument())
    }

    @Test
    fun testCreateKeyword() {
        val someKeyword = "cat"
        Mockito.`when`(keywordCreator.createKeyword(someKeyword, context)).thenReturn(keyword)
        cliEntityCreator.createKeyword(someKeyword, context)

        Mockito.verify(keywordCreator.createKeyword(someKeyword, context))
    }
}
