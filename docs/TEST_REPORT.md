# Verifica prototipo 0.3.1

## Preview web

Percorso automatizzato:

1. apertura della mappa;
2. selezione di Proda 2;
3. inserimento di Pomodoro, varietà Mondesse, 3 piante, diametro 12 cm;
4. comparsa di tre icone con legenda `Ø 12 cm`;
5. modifica tramite `Dimensione icona` a 7 cm;
6. aggiornamento immediato di tutte e tre le icone e della legenda;
7. inserimento della stessa varietà in un’altra proda con diametro 18 cm;
8. propagazione automatica del diametro 18 cm anche alla coltura già esistente.

Esito: completato senza errori JavaScript.

## Progetto Android

Sono stati aggiornati:

- modello `Crop` con `iconDiameterCm`;
- backup JSON al formato 3, compatibile con i formati precedenti;
- dialoghi di aggiunta e modifica coltura;
- propagazione della misura a tutte le colture con uguale nome e varietà;
- dimensionamento grafico delle icone in base ai centimetri impostati;
- limiti di trascinamento calcolati sul raggio dell’icona;
- distanza libera fra le sagome delle piante vicine.

`GardenModels.kt` è stato compilato separatamente con il compilatore Kotlin. La compilazione Gradle completa non è stata eseguita perché l’ambiente non dispone dell’accesso di rete necessario a scaricare il wrapper Gradle.
