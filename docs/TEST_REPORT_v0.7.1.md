# Test report v0.7.1

## Preview HTML

- avvio e rendering della mappa: superato;
- apertura della sezione Raccolti: superato;
- campi limitati a data, prodotto e grammi: superato;
- inserimento manuale `Pomodori` / `850`: superato;
- parsing voce simulata `pomodori 850 grammi`: superato;
- parsing voce simulata `zucchine quattrocentoventi grammi`: superato;
- parsing data `4 agosto 2026`: superato;
- nessun errore JavaScript rilevato durante il percorso di prova.

## Android

- parser vocale Kotlin compilato separatamente con `kotlinc`: superato;
- prove dei comandi e della data: superate;
- compilazione Gradle completa non eseguita: il wrapper richiede il download di Gradle, non disponibile nell'ambiente offline.
