package io

/**
 * Interface to manage input
 */
interface ContentInput {
    /**
     * Reads content
     * @return content as `String`
     */
    fun getContent(): String
}
