package parsing

import Context
import entities.Keyword
import entities.executors.*
import java.nio.file.Paths


/**
 * A class that can create all possible [Keyword]
 */
class KeywordCreator {
    /**
     * Creates [Keyword] by given *keyword* and *[Context]*
     *
     * @return [Keyword]
     */
    fun createKeyword(keyword: String, context: Context): Keyword {
        val curPath = Paths.get(context.directory)
        return when(keyword) {
            "cat"   -> CatExecutor(curPath)
            "echo"  -> EchoExecutor()
            "wc"    -> WCExecutor(curPath)
            "pwd"   -> PwdExecutor(curPath)
            else    -> ExternalExecutor(curPath, context, Paths.get(keyword))  // throw ParseException
        }
    }
}
