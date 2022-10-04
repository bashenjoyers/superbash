package entities

/**
 * Interface that describes command arguments
 */
interface Argument: CLIEntity {
    fun getArgument(): String
}
