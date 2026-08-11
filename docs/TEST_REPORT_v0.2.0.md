# Verifica prototipo 0.2.0

## Preview web

Percorso provato:

1. apertura della mappa;
2. selezione di Proda 2;
3. inserimento di Pomodoro, varietà Mondesse, 3 piante;
4. comparsa di tre icone di pomodoro;
5. selezione della prima pianta;
6. visualizzazione delle distanze dai bordi e dalla pianta più vicina;
7. trascinamento della terza pianta;
8. aggiornamento e salvataggio delle nuove distanze.

Esito: completato senza errori JavaScript rilevati.

Valori iniziali osservati per la prima pianta nella proda da 1,20 × 3,00 m:

- bordo sinistro: 40 cm;
- bordo superiore: 100 cm;
- pianta più vicina: 40 cm.

Dopo il trascinamento della terza pianta, le distanze sono state ricalcolate correttamente.

## Progetto Android

Sono stati aggiornati:

- modello dati con coordinate delle singole piante;
- generazione automatica delle posizioni iniziali;
- salvataggio/importazione JSON formato 2;
- migrazione dei backup formato 1;
- interfaccia Compose per trascinamento e calcolo delle distanze.

Il file `GardenModels.kt` è stato compilato separatamente con il compilatore Kotlin. La compilazione APK completa non è stata eseguita perché l'Android SDK non è disponibile nell'ambiente di generazione.
