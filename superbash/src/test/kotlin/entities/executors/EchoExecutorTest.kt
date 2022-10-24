package entities.executors

import Context
import entities.Argument
import entities.CLIArgument
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class EchoExecutorTest {

    private val echoExecutor = EchoExecutor()
    private val context = Context()


    @Test
    fun testEchoExecutorIfNoArguments() {
        val emptyArgsList = listOf<Argument>()
        val executionResult = echoExecutor.execute(emptyArgsList, context)
        Assert.assertEquals(NEW_LINE, executionResult.get())
    }

    @Test
    fun testEchoExecutorIfOneArgument() {
        val oneArgumentList = listOf<Argument>(CLIArgument("123"))
        val executionResult = echoExecutor.execute(oneArgumentList, context)
        Assert.assertEquals("123\n", executionResult.get())
    }

    @Test
    fun testEchoExecutorIfManyArguments() {
        val manyArgumentsList = listOf<Argument>(CLIArgument("1"), CLIArgument("2"), CLIArgument("3"))
        val executionResult = echoExecutor.execute(manyArgumentsList, context)
        Assert.assertEquals("1 2 3\n", executionResult.get())
    }

    companion object {
        const val NEW_LINE = "\n"
    }
}