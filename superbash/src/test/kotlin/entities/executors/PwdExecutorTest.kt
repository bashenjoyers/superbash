package entities.executors

import Context
import entities.Argument
import entities.CLIArgument
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import java.nio.file.Paths

@RunWith(PowerMockRunner::class)
class PwdExecutorTest {
    private val curPath = Paths.get(CUR_PATH_STRING)
    private val pwdExecutor = PwdExecutor(curPath)
    private val context = Context()

    @Test
    fun testPwdIfNoVariables() {
        val emptyArgs = listOf<Argument>()
        val result = pwdExecutor.execute(emptyArgs, context)
        Assert.assertEquals(CUR_PATH_STRING + "\n", result.get())
    }

    @Test
    fun testPwdIfManyVariable() {
        val args = listOf<Argument>(CLIArgument("1"), CLIArgument("2"))
        val result = pwdExecutor.execute(args, context)
        Assert.assertEquals(CUR_PATH_STRING + "\n", result.get())
    }

    companion object {
        private const val CUR_PATH_STRING = "/home/user"
    }
}