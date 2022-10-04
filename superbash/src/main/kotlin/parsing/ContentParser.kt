package parsing

import Context
import entities.CLIEntity
import exceptions.ParseException
import parsing.helpers.ImportantChars
import parsing.helpers.splitBySpaceAndPipe


/**
 * A class that can parse user input
 */
class ContentParser {
    private val cliEntityCreator = CLIEntityCreator()

    /**
     * Accepts a string with the user's request and *context* (which just scrolls further)
     *
     * @return list of [CLIEntity], it uses [CLIEntityCreator] to create
     *
     * @throws ParseException if error occurred while trying to create any [CLIEntity]
     */
    fun parse(input: String, context: Context): List<CLIEntity> {
        val splitters = splitBySpaceAndPipe(input)
        if (splitters.isEmpty()) return listOf()

        val cliEntities = mutableListOf<CLIEntity>()
        var isFirst = true

        splitters.forEach { word -> when {
            isFirst -> {
                isFirst = false
                val firstEntity: CLIEntity = try {
                    cliEntityCreator.createKeyword(word, context)
                } catch (_: ParseException) {
                    cliEntityCreator.createInitialization(word)
                }
                cliEntities.add(firstEntity)
            }
            word == ImportantChars.PIPE.toString() -> isFirst = true
            else -> cliEntities.add(cliEntityCreator.createArgument(word))
        }}

        return cliEntities
    }
}
