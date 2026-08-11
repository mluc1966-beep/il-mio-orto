# Il mio Orto v0.8.0 — GitHub + PWA + APK automatico

Questo repository contiene due versioni coordinate di **Il mio Orto**:

- `web/` — PWA pubblicabile con GitHub Pages. È la versione più rapida da provare sul telefono.
- `app/` — applicazione Android nativa in Kotlin/Jetpack Compose.

## 1. Primo caricamento su GitHub

1. Crea un nuovo repository chiamato, ad esempio, `il-mio-orto`.
2. Carica **il contenuto di questa cartella**, non la cartella ZIP.
3. Usa `main` come branch principale.

Se il repository è pubblico, evita di inserire backup personali o altri file con dati privati.

## 2. Attivare GitHub Pages

Una sola volta:

1. Apri il repository su GitHub.
2. Vai in **Settings → Pages**.
3. In **Build and deployment → Source**, scegli **GitHub Actions**.
4. Apri la scheda **Actions** e attendi il workflow `Pubblica PWA su GitHub Pages`.

Quando il workflow termina, il sito sarà normalmente disponibile all'indirizzo:

`https://TUO-USERNAME.github.io/il-mio-orto/`

Sul telefono apri l'indirizzo con Chrome e usa **Aggiungi a schermata Home / Installa app**.

La PWA usa HTTPS su GitHub Pages; il pulsante vocale viene mostrato solo quando il browser espone `SpeechRecognition`.

## 3. Ottenere l'APK senza Android Studio

Il workflow `Genera APK Android` parte automaticamente quando cambia il codice Android e può anche essere lanciato manualmente:

1. Repository → **Actions**.
2. Apri **Genera APK Android**.
3. Premi **Run workflow** se vuoi avviarlo manualmente.
4. Al termine apri l'esecuzione.
5. In fondo alla pagina, sezione **Artifacts**, scarica `IlMioOrto-v0.8.0-debug-apk`.
6. Estrai il file ZIP dell'artifact e installa `IlMioOrto-v0.8.0-debug.apk` sul telefono.

Questo APK è una build `debug`, adatta all'uso personale e ai test. Per una futura distribuzione pubblica creeremo una build `release` firmata con una chiave privata.

## 4. Aggiornamenti

- Modifica a `web/` → nuova PWA pubblicata automaticamente.
- Modifica ad `app/` → nuovo APK generato automaticamente.
- I dati personali della PWA restano nel browser del dispositivo e non vengono salvati nel repository.

## 5. Sicurezza

Non caricare mai nel repository pubblico:

- backup dell'orto contenenti informazioni che vuoi mantenere private;
- file `.jks` / `.keystore`;
- password, token o chiavi API.

Il `.gitignore` incluso esclude già i principali file di firma Android.
