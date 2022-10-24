package entities.executors.utils

import java.io.File
import java.lang.Exception
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.pathString

class FileSystemHelper(val curPath: Path) {
    /**
     * Method to get a file
     * @param relPath stores path to file
     * @return file
     */
    fun tryGetFile(relPath: String): File = if (isAbsolute(relPath)) {
        Paths.get(relPath).toFile()
    } else {
        Paths.get(curPath.absolutePathString(), relPath).toFile()
    }

    /**
     * Method to get a path to existing file
     * @param relPath stores path to file
     * @return path
     */
    fun tryGetPath(relPath: String): Path {
        val path = if (isAbsolute(relPath)) {
            Paths.get(relPath)
        } else {
            Paths.get(curPath.absolutePathString(), relPath)
        }

        if (path.exists()) return path

        throw NoSuchFileException("No such file found by FileSystemHelper: $relPath")
    }

    /**
     * Method to check is path is absolute
     * @param path stores path to file
     * @return true if path exists and it is absolute
     */
    private fun isAbsolute(path: String): Boolean {
        return try {
            val pathCheck = Paths.get(path)
            pathCheck.isAbsolute
        } catch (e: Exception) {
            false
        }
    }
}