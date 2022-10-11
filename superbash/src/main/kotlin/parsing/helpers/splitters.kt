package parsing.helpers


/**
 * Splits the string by spaces and pipes (taking into account the processing of quotes)
 */
fun splitBySpaceAndPipe(input: String): List<String> {
    val builder = StringBuilder()
    val splitters = mutableListOf<String>()
    val marksStateHolder = MarksStateHolder()
    input.forEach { ch -> when(ch) {
        ImportantChars.SINGLE_MARK_CHAR -> {
            marksStateHolder.singleMark()
             builder.append(ch)
        }
        ImportantChars.QUOTE_MARK_CHAR -> {
            marksStateHolder.quoteMark()
            builder.append(ch)
        }
        ImportantChars.SPACE -> {
            if (builder.isNotEmpty()) {
                if (marksStateHolder.isStandard) {
                    splitters.add(builder.toString())
                    builder.clear()
                } else builder.append(ch)
            }
        }
        ImportantChars.PIPE -> {
            if (marksStateHolder.isStandard) {
                if (builder.isNotEmpty()) {
                    splitters.add(builder.toString())
                }
                splitters.add(ch.toString())
                builder.clear()
            } else builder.append(ch)
        }
        else -> builder.append(ch)
    }}
    if (builder.isNotEmpty()) splitters.add(builder.toString())
    return splitters
}
