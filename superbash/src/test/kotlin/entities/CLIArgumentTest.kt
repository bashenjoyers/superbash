package entities

import org.junit.Assert
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.jupiter.api.Test

@RunWith(JUnit4::class)
class CLIArgumentTest {

    @Test
    fun testGetArgumentIfEmptyString() {
        val emptyString = String()
        val cliArgument = CLIArgument(emptyString)

        Assert.assertEquals(cliArgument.getArgument(), emptyString)
    }

    @Test
    fun testGetArgumentIfNotEmptyString() {
        val notEmptyString = "not empty string"
        val cliArgument = CLIArgument(notEmptyString)

        Assert.assertEquals(cliArgument.getArgument(), notEmptyString)
    }
}
