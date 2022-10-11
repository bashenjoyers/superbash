package parsing.helpers


/**
 * An internal entity that stores the current state during parsing and makes transitions to other
 */
class MarksStateHolder {
    private var state = MarkState.STANDARD
    val isStandard: Boolean
        get() = state == MarkState.STANDARD

    /**
     * Transition to another state when a single quote is encountered
     */
    fun singleMark() = when(state) {
        MarkState.STANDARD -> { state = MarkState.SINGLE_QUOTE_MARK }
        MarkState.SINGLE_QUOTE_MARK -> { state = MarkState.STANDARD }
        else -> {}
    }

    /**
     * Transition to another state when a quote is encountered
     */
    fun quoteMark() = when(state) {
        MarkState.STANDARD -> { state = MarkState.QUOTE_MARK }
        MarkState.QUOTE_MARK -> { state = MarkState.STANDARD }
        else -> {}
    }
}
