# Test report — Il mio Orto 0.5.0

## Preview HTML

Test eseguiti con Chromium in modalità headless:

- PASS — avvio della mappa;
- PASS — sei sezioni nella barra inferiore;
- PASS — sezione Consociazioni e schede del pomodoro;
- PASS — collegamenti alle fonti;
- PASS — blocco del progetto incompatibile 12 pomodori + 3 zucchini;
- PASS — suggerimento di spostare 3 zucchini;
- PASS — nessuna modifica delle posizioni in caso di fallimento;
- PASS — progetto compatibile 6 pomodori + 2 zucchini;
- PASS — assenza di sovrapposizioni nel progetto compatibile;
- PASS — zucchini collocati lungo il bordo;
- PASS — basilico collocato vicino ai pomodori;
- PASS — nessun errore JavaScript.

## Kotlin

`GardenModels.kt` e `CompanionData.kt` compilati con `kotlinc`.

Test eseguito:

- una combinazione incompatibile resta invariata;
- una combinazione compatibile viene riposizionata;
- il progetto compatibile non genera avvisi di sovrapposizione.

## Compilazione Android

Non eseguita integralmente: Gradle e le dipendenze Android non erano disponibili offline nell'ambiente di generazione.
