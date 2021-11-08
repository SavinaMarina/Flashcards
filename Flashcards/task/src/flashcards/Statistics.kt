package flashcards

object Statistics {

    private val errorCards = mutableMapOf<String, Int>()

    fun showHardestCard() {
        val maxErrors = errorCards.values.maxOrNull()
        val hardestCards = errorCards.filterValues { it == maxErrors }
        printMessage(when (hardestCards.size) {
            0 -> "There are no cards with errors."
            1 -> {
                val term = hardestCards.keys.elementAt(0)
                "The hardest card is \"$term\". You have $maxErrors errors answering it"
            }
            else -> {
                val terms = hardestCards.keys.joinToString() { "\"" + it + "\"" }
                "The hardest cards are $terms. You have $maxErrors errors answering them."
            }
        })
    }

    fun resetStatistics() {
        errorCards.clear()
        printMessage("Card statistics have been reset")
    }

    fun getErrorCount(term: String) = errorCards.getOrDefault(term, 0)

    fun incrementErrorCount(term: String) {
        errorCards[term] = getErrorCount(term) + 1
    }

    fun setErrorCount(term: String, errorCount: Int) {
        errorCards[term] = errorCount
    }
}