# Test report v0.7.0

## Database

- 50 entità caricate.
- 224 relazioni caricate: 186 B, 24 C, 14 M.
- 42 specie Cura/Rotazioni e 92 alias nel workbook normalizzato.
- `ConsociationMatrixData.kt` compilato con Kotlin CLI.

## Preview HTML

- controllo sintattico JavaScript con `node --check`: superato;
- avvio con DOM simulato: superato;
- schermata Mappa renderizzata: superata;
- schermata Consociazioni per Pomodoro: 21 relazioni, filtri presenti;
- conflitto Pomodoro–Patata rilevato come C specifica;
- sezioni Cura e Rotazioni mantenute dalla v0.6.1;
- formato dello stato locale invariato, quindi i dati precedenti restano compatibili.

## Limiti

La compilazione APK completa richiede Android Studio/Gradle con le dipendenze Android. In questo ambiente è stato compilato separatamente il nuovo livello dati Kotlin.
