package io

/**
 * Class to manage console output
 */
class ConsoleContentOutput : ContentOutput {
    /**
     * Prompt value
     */
    private val prompt = "$> "

    /**
     * Method to print a prompt
     */
    fun printPrompt() = print(prompt)

    /**
     * Implementation of `printContent` interface method
     * @param content String to print
     */
    override fun printContent(content: String) {
        print(content)
    }
}
