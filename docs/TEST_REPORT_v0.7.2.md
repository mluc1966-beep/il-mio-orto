# Test report — v0.7.2

## Correzione microfono

- La preview rileva l'apertura Android tramite `content://` e non mostra pulsanti vocali destinati a fallire con `not-allowed`.
- In tale modalità mostra un messaggio esplicativo e lascia disponibili solo data, prodotto e grammi.
- Suggerisce il microfono della tastiera come dettatura di emergenza nella preview.
- Il progetto Android dichiara `RECORD_AUDIO`, richiede il permesso al primo uso e avvia il riconoscitore vocale di sistema.
- Versione Android aggiornata a 0.7.2 (versionCode 11).

## Verifiche statiche

- JavaScript della preview controllato con `node --check`.
- Presenza di permesso microfono e query per i servizi di riconoscimento verificata nel manifest.
- Compatibilità del modello dati e del backup invariata rispetto alla v0.7.1.

## Limite noto

Il file HTML locale non può usare direttamente il microfono quando Chrome lo apre con schema `content://`. La funzione vocale completa richiede l'app Android compilata oppure la pubblicazione della preview su HTTPS/localhost.
