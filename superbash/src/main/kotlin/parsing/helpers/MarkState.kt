package parsing.helpers

/**
 * State during parsing
 */
enum class MarkState {
    STANDARD,           // outside quotes
    SINGLE_QUOTE_MARK,  // inside single quotes
    QUOTE_MARK          // inside quotes
}
