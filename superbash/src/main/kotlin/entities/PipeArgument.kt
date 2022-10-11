package entities

class PipeArgument(private val argument: String): Argument {
    override fun getArgument(): String {
        return argument
    }
}
