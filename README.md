# 📌 Dungeon Crawler

Dungeon Crawler è un gioco di ruolo (RPG) esplorativo e gestionale realizzato in Java con interfaccia JavaFX.
Il giocatore veste i panni di un eroe che si addentra in oscuri dungeon, affronta nemici (Banditi, Orchi, Draghi) e raccoglie tesori, dovendo al contempo gestire le proprie risorse: consumare cibo per non soccombere alla fatica, bere pozioni per recuperare salute ed equipaggiare spade e armature per sopravvivere. Lo stato di gioco può essere salvato e ripreso grazie alla persistenza su file in formato JSON.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 21 (LTS) o superiore
- Gradle (è incluso il wrapper `./gradlew`, quindi non è necessaria un'installazione separata)

### Istruzioni

```bash
git clone https://github.com/eliagismondi-create/MDP-Project-EliaGismondi-131023.git
cd MDP-Project-EliaGismondi-131023
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

---

## 🏛️ Architettura

Il progetto è organizzato secondo il pattern **MVC** e i principi **SOLID**, all'interno del package `it.unicam.cs.mpgc.rpg131023`:

- **`model`** — le entità di dominio e la logica di gioco (eroe, nemici, item, dungeon, combattimento, risorse). Non conosce nulla della UI.
- **`controller`** — orchestrazione del gioco (`GameManager`, `CombatManager`), gestione degli stati (`state`) e propagazione degli eventi (`events`).
- **`view`** — i controller JavaFX (`GameController`, `HeroStatsController`) e l'avvio dell'applicazione, che si limitano a presentare i dati e delegare le azioni al controller.
- **`persistence`** — salvataggio e caricamento dello stato di gioco tramite DTO e Gson.
- **`utils`** — servizi di caricamento dei dati statici (statistiche e dungeon).

La comunicazione fra modello e vista avviene in modo disaccoppiato tramite **Observer pattern** (`PropertyChangeSupport`), mentre il flusso di gioco è gestito da una macchina a stati. I dati statici (`stats.json`, `dungeons.json`) sono letti dalle *resources* via `getResourceAsStream`, mentre i salvataggi dinamici risiedono nella home dell'utente (`~/.dungeoncrawler`), senza alcun percorso assoluto cablato nel codice.

> 📌 Per una descrizione dettagliata delle funzionalità, delle responsabilità delle classi, della persistenza e delle modalità di estensione futura, consultare la **Wiki del repository**.

---

## 🤖 Uso di strumenti di AI

Per la realizzazione di questo progetto sono stati utilizzati strumenti di AI (LLM) **come supporto alla programmazione**, mai come sostituto della comprensione personale. Ogni frammento di codice suggerito è stato analizzato, rielaborato, adattato al contesto del progetto e verificato manualmente.

Nello specifico, l'AI è stata impiegata per:

* **Documentazione** — generazione e revisione dei commenti **Javadoc** delle classi e dei metodi.
* **Debugging** — supporto nell'individuazione e nella risoluzione di errori di compilazione e di logica.
* **Approfondimento didattico** — spiegazione di **come applicare al mio codice i concetti del corso**, partendo dai miei dubbi e adattando le risposte al caso concreto.
* **Codice meccanico e ripetitivo** — stesura di metodi *boilerplate* (es. getter/setter, costruttori, `equals()`/`hashCode()`), poi rivisti e integrati personalmente.
* **Studio e applicazione di pattern non trattati nel corso** — approfondimento personale e applicazione al progetto di pattern architetturali quali **Visitor**, **State**, **Factory**, **Observer** e dell'**architettura DTO** per la persistenza.

In tutte queste attività il livello di intervento personale è stato **attivo e prevalente**: l'AI ha accelerato il lavoro e chiarito i concetti, ma le decisioni di progettazione, l'integrazione nel codice e la verifica del risultato sono state svolte da me.

> 📌 Per una descrizione più dettagliata dell'uso dell'AI, consultare la **Wiki del repository**.

---

## 🛠️ Tecnologie utilizzate

- **Java** — linguaggio principale
- **JavaFX 21** — interfaccia grafica (FXML + CSS)
- **Gradle** — build automation e gestione delle dipendenze
- **Gson** — serializzazione/deserializzazione JSON per la persistenza
- **JUnit 5** — framework di test
