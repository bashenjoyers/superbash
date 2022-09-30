package exceptions

import java.lang.Exception

/**
 * Exception for errors occured in runtime
 */
class RunException(message: String): Exception(message)
