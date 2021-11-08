package flashcards

import java.io.File

val cards = mutableMapOf<String, String>()

fun main(args: Array<String>) {
    if (args.contains("-import"))
        importFromFile(args[args.indexOf("-import") + 1])

    while(true) {
        printMessage("Input the action (add, remove, import, export, ask, log, hardest card, reset stats, exit):")
        when(readMessage()) {
            "add" -> addCard()
            "remove" -> removeCard()
            "import" -> importCards()
            "export" -> exportCards()
            "ask" -> askCards()
            "log" -> Logger.saveLogToFile()
            "hardest card" -> Statistics.showHardestCard()
            "reset stats" -> Statistics.resetStatistics()
            "exit" -> {
                printMessage("Bye bye!")
                args["-export"]?.let(exportToFile(args[args.indexOf("-export") + 1]))
                if (args.contains("-export"))
                    exportToFile(args[args.indexOf("-export") + 1])
                break
            }
        }
    }
}

fun printMessage(message: String) {
    println(message)
    Logger.log(message)
}

fun readMessage(): String {
    val msg = readLine()!!
    Logger.log(msg)
    return msg
}

fun addCard() {
    printMessage("The card:")
    val term = readMessage()
    if (cards.containsKey(term)) {
        printMessage("The card \"$term\" already exists.")
        return
    }
    printMessage("The definition of the card:")
    val definition = readMessage()
    if (cards.containsValue(definition)) {
        printMessage("The definition \"$definition\" already exists.")
        return
    }
    cards[term] = definition
    printMessage("The pair (\"$term\":\"$definition\") has been added")
}

fun removeCard() {
    printMessage("Which card?")
    val cardToRemove = readLine()
    printMessage(if (cards.remove(cardToRemove) != null) "The card has been removed."
            else "Can't remove \"$cardToRemove\": there is no such card.")
}

fun importFromFile(fileName: String) {
    val file = File(fileName)
    if (file.exists()) {
        val lines = file.readLines()
        for (line in lines) {
            val (term, definition, numOfErrors) = line.split(":")
            cards[term] = definition
            if (numOfErrors != "null") {
                Statistics.setErrorCount(term, numOfErrors.toInt())
            }
        }
        printMessage("${lines.size} cards have been loaded.")
    } else printMessage("File not found.")
}

fun importCards() {
    printMessage("File name:")
    importFromFile(readMessage())
}

fun exportToFile(fileName: String) {
    File(fileName).writeText(cards.entries.joinToString("\n")
    { "${it.key}:${it.value}:${Statistics.getErrorCount(it.key)}" })
    printMessage("${cards.count()} cards have been saved.")
}

fun exportCards() {
    printMessage("File name:")
    exportToFile(readMessage())
}

fun askCards() {
    printMessage("How many times to ask?")
    val times = readMessage().toInt()
    for (i in 0 until times) {
        val term = cards.keys.elementAt(i)
        val def = cards[term]
        printMessage("Print the definition of \"$term\":")
        val answer = readMessage()
        printMessage(
            when {
                answer == def -> "Correct!"
                cards.containsValue(answer) -> {
                    Statistics.incrementErrorCount(term)
                    "Wrong. The right answer is \"$def\", " +
                        "but your definition is correct for \"${cards.filterValues { it == answer }.keys.first()}\"."}
                else -> {
                    Statistics.incrementErrorCount(term)
                    "Wrong. The right answer is \"$def\"."
                }
            }
        )
    }
}

