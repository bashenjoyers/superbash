package io

/**
 * Class to manage console input
 */
class ConsoleContentInput : ContentInput {
    /**
     * Implementation of `getContent` interface method
     * @return content as `String`
     */
    override fun getContent(): String = readln()
}
