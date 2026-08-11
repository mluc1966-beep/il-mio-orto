package it.luca.ilmiorto.data

import java.text.Normalizer
import java.time.LocalDate

/** Result of a spoken harvest command such as "pomodori 850 grammi". */
data class ParsedHarvestSpeech(
    val product: String,
    val grams: Int,
)

fun parseHarvestSpeech(raw: String): ParsedHarvestSpeech? {
    val cleaned = normalizeSpeech(raw)
        .replace(Regex("\\b(grammi|grammo|gr|g)\\b"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()

    val digitMatches = Regex("\\d+(?:[.,]\\d+)?").findAll(cleaned).toList()
    if (digitMatches.isNotEmpty()) {
        val match = digitMatches.last()
        val grams = match.value.replace(',', '.').toDoubleOrNull()?.toInt() ?: 0
        val product = cleanProduct(cleaned.substring(0, match.range.first))
        if (product.isNotBlank() && grams > 0) {
            return ParsedHarvestSpeech(product.replaceFirstChar { it.uppercase() }, grams)
        }
    }

    val tokens = cleaned.split(' ').filter { it.isNotBlank() }
    val start = (tokens.size - 5).coerceAtLeast(0)
    for (index in start until tokens.size) {
        val grams = ITALIAN_NUMBER_WORDS[tokens.drop(index).joinToString("")] ?: continue
        val product = cleanProduct(tokens.take(index).joinToString(" "))
        if (product.isNotBlank() && grams > 0) {
            return ParsedHarvestSpeech(product.replaceFirstChar { it.uppercase() }, grams)
        }
    }
    return null
}

fun parseSpokenDate(raw: String, today: LocalDate = LocalDate.now()): LocalDate? {
    val text = normalizeSpeech(raw)
    if ("oggi" in text) return today
    if ("ieri" in text) return today.minusDays(1)

    Regex("\\b(\\d{1,2})[/.\\-](\\d{1,2})(?:[/.\\-](\\d{2,4}))?\\b")
        .find(text)
        ?.let { match ->
            val day = match.groupValues[1].toInt()
            val month = match.groupValues[2].toInt()
            var year = match.groupValues[3].toIntOrNull() ?: today.year
            if (year < 100) year += 2000
            return runCatching { LocalDate.of(year, month, day) }.getOrNull()
        }

    val months = mapOf(
        "gennaio" to 1, "febbraio" to 2, "marzo" to 3, "aprile" to 4,
        "maggio" to 5, "giugno" to 6, "luglio" to 7, "agosto" to 8,
        "settembre" to 9, "ottobre" to 10, "novembre" to 11, "dicembre" to 12,
    )
    Regex("\\b(\\d{1,2})\\s+(gennaio|febbraio|marzo|aprile|maggio|giugno|luglio|agosto|settembre|ottobre|novembre|dicembre)(?:\\s+(\\d{4}))?\\b")
        .find(text)
        ?.let { match ->
            val day = match.groupValues[1].toInt()
            val month = months.getValue(match.groupValues[2])
            val year = match.groupValues[3].toIntOrNull() ?: today.year
            return runCatching { LocalDate.of(year, month, day) }.getOrNull()
        }
    return null
}

private fun cleanProduct(value: String): String = value
    .trim()
    .replace(Regex("^(ho raccolto|raccolto|registra|aggiungi)\\s+"), "")
    .trim()

private fun normalizeSpeech(value: String): String = Normalizer
    .normalize(value.lowercase(), Normalizer.Form.NFD)
    .replace(Regex("\\p{Mn}+"), "")
    .replace(Regex("[^a-z0-9/., \\-]+"), " ")
    .replace(Regex("\\s+"), " ")
    .trim()

private val ITALIAN_NUMBER_WORDS: Map<String, Int> by lazy {
    (1..9_999).associateBy(::italianNumberWord)
}

private fun italianNumberWord(number: Int): String {
    val units = listOf("", "uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove")
    val teens = listOf("dieci", "undici", "dodici", "tredici", "quattordici", "quindici", "sedici", "diciassette", "diciotto", "diciannove")
    val tens = listOf("", "", "venti", "trenta", "quaranta", "cinquanta", "sessanta", "settanta", "ottanta", "novanta")
    return when {
        number < 10 -> units[number]
        number < 20 -> teens[number - 10]
        number < 100 -> {
            val unit = number % 10
            var prefix = tens[number / 10]
            if (unit == 1 || unit == 8) prefix = prefix.dropLast(1)
            prefix + units[unit]
        }
        number < 1_000 -> {
            val remainder = number % 100
            var prefix = if (number / 100 == 1) "cento" else units[number / 100] + "cento"
            if (remainder in 80..89) prefix = prefix.dropLast(1)
            prefix + if (remainder > 0) italianNumberWord(remainder) else ""
        }
        else -> {
            val thousands = number / 1_000
            val remainder = number % 1_000
            val prefix = if (thousands == 1) "mille" else italianNumberWord(thousands) + "mila"
            prefix + if (remainder > 0) italianNumberWord(remainder) else ""
        }
    }
}
