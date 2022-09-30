package parsing

import Context
import entities.CLIEntity
import entities.Exit
import exceptions.ParseException


/**
 * A class that can parse user input
 */
class ContentParser {
    object ImportantChars {
        const val SPACE = ' '
        const val SINGLE_MARK_CHAR = '\''
        const val QUOTE_MARK_CHAR = '"'
    }
    private enum class State {  // quotation marks or not
        STANDARD,
        SINGLE_MARK,
        QUOTE_MARK
    }

    private class MarksStateHolder {
        var state = State.STANDARD
        fun singleMark() = when(state) {
            State.STANDARD -> { state = State.SINGLE_MARK }
            State.SINGLE_MARK -> { state = State.STANDARD }
            else -> {}
        }

        fun quoteMark() = when(state) {
            State.STANDARD -> { state = State.QUOTE_MARK }
            State.QUOTE_MARK -> { state = State.STANDARD }
            else -> {}
        }

        val isStandard: Boolean
            get() = state == State.STANDARD
    }

    private val cliEntityCreator = CLIEntityCreator()

    private fun splitBySpace(input: String): List<String> {
        val builder = StringBuilder()
        val splitters = mutableListOf<String>()
        val marksStateHolder = MarksStateHolder()
        input.forEach { ch -> when(ch) {
            ImportantChars.SINGLE_MARK_CHAR -> {
                marksStateHolder.singleMark()
            }
            ImportantChars.QUOTE_MARK_CHAR -> {
                marksStateHolder.quoteMark()
            }
            ImportantChars.SPACE -> {
                if (builder.isNotEmpty()) {
                    if (marksStateHolder.isStandard) {
                        splitters.add(builder.toString())
                        builder.clear()
                    } else builder.append(ch)
                }
            }
            else -> builder.append(ch)
        }}
        if (builder.isNotEmpty()) splitters.add(builder.toString())
        return splitters
    }

    /**
     * Accepts a string with the user's request and *context* (which just scrolls further)
     *
     * @return list of [CLIEntity], it uses [CLIEntityCreator] to create
     *
     * @throws ParseException if error occurred while trying to create any [CLIEntity]
     */
    fun parse(input: String, context: Context): List<CLIEntity> {
        val splitters = splitBySpace(input)
        if (splitters.isEmpty()) return listOf()

        val cliEntities = mutableListOf<CLIEntity>()
        val actionCommand = splitters.first()

        val firstEntity: CLIEntity = when(actionCommand) {
            "exit" -> Exit()
            else -> try {
                cliEntityCreator.createKeyword(actionCommand, context)
            } catch (_: ParseException) {
                cliEntityCreator.createInitialization(actionCommand)
            }
        }

        cliEntities.add(firstEntity)
        splitters.drop(1).forEach { arg ->
            val entity = cliEntityCreator.createArgument(arg)
            cliEntities.add(entity)
        }
        return cliEntities
    }
}
