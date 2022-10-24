package entities
import Context
import java.util.Optional
/**
 * Interface that describes `keyword` -- command (like `cat`) or external execution
 */
interface Keyword: CLIEntity {
    fun execute(arguments: List<Argument>, context: Context): Optional<String>
}
