import io.ConsoleContentInput
import io.ConsoleContentOutput

fun main(args: Array<String>) {
    val input = ConsoleContentInput()
    val output = ConsoleContentOutput()

    val cliManager = CLIManager()

    while (true) {
        output.printPrompt()
        val query = input.getContent()
        val result = cliManager.run(query)

        if (result.isPresent) {
            output.printContent(result.get())
        }
    }
}
