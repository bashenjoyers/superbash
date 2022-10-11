package exceptions

import java.lang.Exception

/**
 * Exception for parsing errors
 */
class ParseException(message: String): Exception(message)
