package it.luca.ilmiorto.data

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HarvestVoiceParserTest {
    @Test
    fun parsesDigitsAndGrams() {
        assertEquals(ParsedHarvestSpeech("Pomodori", 850), parseHarvestSpeech("pomodori 850 grammi"))
    }

    @Test
    fun parsesSpokenNumber() {
        assertEquals(ParsedHarvestSpeech("Zucchine", 420), parseHarvestSpeech("zucchine quattrocentoventi grammi"))
    }

    @Test
    fun rejectsMissingWeight() {
        assertNull(parseHarvestSpeech("pomodori"))
    }

    @Test
    fun parsesItalianDate() {
        assertEquals(LocalDate.of(2026, 8, 4), parseSpokenDate("4 agosto 2026"))
        assertEquals(LocalDate.of(2026, 8, 4), parseSpokenDate("oggi", LocalDate.of(2026, 8, 4)))
    }
}
