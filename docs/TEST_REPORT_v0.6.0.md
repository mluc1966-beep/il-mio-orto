# Rapporto verifiche — v0.6.0

## Preview HTML

Esito: **superato**.

- Avvio della mappa: PASS
- Sei sezioni principali: PASS
- Sezione Colture: PASS
- 42 specie nella banca dati: PASS
- Consociazioni del pomodoro ampliate: PASS
- Indicazione della fonte pratica: PASS
- Sezione Rotazioni: PASS
- Registrazione di una coltura precedente: PASS
- Rilevamento stessa famiglia Solanacee: PASS
- Cura pomodoro, 100 cm tra le file: PASS
- Cura pomodoro, 40 cm sulla fila: PASS
- Indicazione irrigazione pomodoro: PASS
- Errori JavaScript: nessuno

## Livello dati Kotlin

Compilati con `kotlinc`:

- `GardenModels.kt`
- `CompanionData.kt`
- `CropGuideData.kt`

Test superati:

- 42 record nella banca dati;
- pomodoro con 100/40 cm;
- almeno 6 relazioni dirette o inverse per il pomodoro;
- applicazione delle distanze alle colture presenti;
- inserimento dello storico delle rotazioni.

## Compilazione Android completa

Non completata: il Gradle wrapper deve scaricare Gradle 8.13 e l'ambiente di generazione non dispone di accesso a Internet.
