package parsing

import Context
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class VariablesSubstitutorTest {
    private val context = Context()
    private val variablesSubstitutor: VariablesSubstitutor = VariablesSubstitutor()


    @Test
    fun testSubstituteIfNoQuotes() {
        context.variables.put("x", "e")
        context.variables.put("y", "xit")
        val content = "\$x\$y"
        Assert.assertEquals("exit", variablesSubstitutor.substitute(content, context))
    }

    @Test
    fun testSubstituteIfDoubleQuotes() {
        context.variables.put("x", "e")
        context.variables.put("y", "xit")
        val content = "\"\$x\$y\""
        Assert.assertEquals("\"exit\"", variablesSubstitutor.substitute(content, context))
    }

    @Test
    fun testSubstituteIfSingleQuotes() {
        context.variables.put("x", "e")
        context.variables.put("y", "xit")
        val content = "\'\$x\$y\'"
        Assert.assertEquals("'\$x\$y'", variablesSubstitutor.substitute(content, context))
    }

    @Test
    fun testSubstituteIfEmptyContext() {
        val content = "\"\$x\$y\""
        Assert.assertEquals("\"\$x\$y\"", variablesSubstitutor.substitute(content, context))
    }
}
