package entities

/**
 * Class that describes simple string argument
 */
class CLIArgument(private val argument: String): Argument {
    override fun getArgument(): String {
        return argument
    }
}