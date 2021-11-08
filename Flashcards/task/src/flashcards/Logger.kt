package flashcards

import java.io.File

object Logger {
    private val log = mutableListOf<String>()

    fun log(message: String) {
        log.add(message)
    }

    fun saveLogToFile() {
        printMessage("File name:")
        try {
            val filename = readMessage()
            printMessage("The log has been saved")
            File(filename).writeText(log.joinToString("\n"))
        } catch (e: Exception) {
            printMessage("Error! Cannot save log file!")
        }
    }
}