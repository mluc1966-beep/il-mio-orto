# Rapporto di verifica — Il mio Orto 0.4.1

## Correzioni verificate

- La disposizione automatica lavora sull’intera zona e considera contemporaneamente colture diverse.
- Scenario di prova: Proda 2 da 1,20 × 3,00 m con 12 pomodori (Ø 20 cm) e 3 zucchini (Ø 36 cm).
- Risultato: 0 sovrapposizioni dopo la disposizione automatica.
- Il rilevatore confronta ogni coppia di piante, anche quando appartengono a specie diverse.
- Gli avvisi riportano il nome di entrambe le piante coinvolte.
- È applicato un margine grafico di sicurezza di 3 cm tra le icone.
- I dati salvati dalle versioni precedenti restano compatibili.

## Verifiche tecniche

- Sintassi JavaScript verificata con `node --check`.
- Algoritmo Kotlin compilato con `kotlinc`.
- Test Kotlin dello scenario misto: `overlaps=0`.
