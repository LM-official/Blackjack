<div align="center">

# 🃏 JBlackJack

**A desktop Blackjack game with AI opponents, accounts and avatars — written in pure Java Swing.**
Play against the dealer and up to three computer players, level up your profile
as you win, and watch the cards being dealt one at a time in a fully animated table.

![Language](https://img.shields.io/badge/Language-Java%2017-blue?style=flat-square)
![GUI](https://img.shields.io/badge/GUI-Swing-orange?style=flat-square)
![Build](https://img.shields.io/badge/Build-javac%20%2F%20Eclipse-informational?style=flat-square)
![Pattern](https://img.shields.io/badge/Pattern-MVC-4c1?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square)

</div>

---

## 🃏 What it does

A single, self-contained Swing application that plays Blackjack against the
house:

- **🎴 Classic Blackjack** — a double 52-card deck, face cards worth 10 and aces worth 1 **or** 11 (whichever is best). Go over 21 and you bust.
- **🤖 AI opponents** — choose 0–3 computer players; each one targets a random "stand" score and plays itself out, followed by the dealer (which always hits below 17).
- **🧵 Animated, non-blocking dealing** — the AI and dealer turns run on a background thread, so cards appear one at a time on a ~1 s beat while the window stays fully responsive.
- **👤 Accounts & avatars** — register or log in, pick one of 15 avatars, and your stats persist between sessions.
- **📈 Stats & levels** — every game updates games-played / won / lost, awards XP (more for a win), and levels you up once you cross the threshold.
- **🔊 Sound & feel** — looping soundtracks for the home and table, sound effects for cards, clicks and results, and a custom mouse cursor.
- **👁️ Live MVC updates** — the table view observes each hand through an Observer/Observable link, so every draw is reflected on screen immediately.

> 🗣️ The **dealt cards and results** are shown live in the window, while account
> data is persisted to `~/.JBlackJack/accounts.txt`, so the game can be followed
> on screen and your progress survives a restart.

---

## 🗂️ Project structure

```
.
├── README.md
├── LICENSE
├── MDP_progetto_2023_2024.pdf        # the project assignment
├── MercuriRelazioneProgetto.pdf      # project report
├── MercuriDiagrammaDelleClassi.pdf   # UML class diagram
├── doc/                              # generated Javadoc (open doc/index.html)
└── src/
    ├── module-info.java              # module JBlackJack (requires java.desktop)
    ├── READ_ME.txt                   # accounts.txt format
    ├── accounts.txt                  # default seed accounts
    ├── model/                        # game logic  (Card, Deck, Hand, …)
    ├── controller/                   # Controller  (orchestrates a game)
    ├── view/                         # Swing UI    (MainWindow, Home, Game, …)
    ├── img/                          # cards, avatars, cursors, logo, icon
    └── sounds/                       # .wav effects & soundtracks
```

The code follows a strict **MVC** split: `model` knows the rules but nothing
about Swing, `view` is the UI, and `controller` is the bridge that drives a
single match.

---

## ⚙️ Building

There is no Maven/Gradle — it's a plain Java 17 module. Any JDK ≥ 17 works
(use `--release 17` if yours is newer).

| Command | Action |
| :------ | :----- |
| `find src -name '*.java' > sources.txt` | 📝 Collect the source list |
| `javac --release 17 -d bin @sources.txt` | 🔨 Compile every class into `bin/` |
| `java -cp bin view.JBlackJack` | ▶️ Run the game |
| Import into **Eclipse** → *Run* | 🖥️ Or just open it as an existing Java project |

One-liner build & run:

```sh
find src -name '*.java' > /tmp/sources.txt
javac --release 17 -d bin @/tmp/sources.txt
java -cp bin view.JBlackJack
```

> ℹ️ Image and sound assets live under `src/` and are loaded from the
> **classpath** (`Resources`), which Eclipse/`javac` copy into `bin/`. Thanks to
> that the game runs from **any** working directory — no need to `cd` into the
> project root.

---

## 🎮 Usage

The game takes no command-line arguments — just launch it:

```sh
java -cp bin view.JBlackJack
```

### How to play

1. **🏠 Home** — press **Play**, or open the **User** area to log in / register.
2. **🤖 Players** — choose how many AI players join the table (**0–3**).
3. **🃏 Table** — you get two cards; press **Hit** to draw another or **Stand** to hold.
4. **🏆 Result** — once you stand (or bust) the AI players and dealer finish, and the outcome — **You won / Tie / You lost** — is shown. If you're logged in, your stats are saved.

> ⚠️ **Bust rule.** Going over 21 ends your turn immediately as a loss — the
> dealer doesn't even need to play. Aces are always counted in your favour
> (1 or 11), so you only bust when no combination keeps you at 21 or under.

### Accounts

User profiles are stored in `~/.JBlackJack/accounts.txt`, seeded on first run
from the bundled defaults. Register a new player from the **User → Sign up**
screen and pick an avatar; passwords must be **> 8 characters** with an
uppercase, a lowercase, a number and a special character.

---

## 🔄 How it works

```
        ┌──────────────────────────┐                 ┌──────────────────────────┐
        │           VIEW           │                 │        CONTROLLER        │
        ├──────────────────────────┤                 ├──────────────────────────┤
        │ Home → HomeAI → Game     │   user actions  │ builds the Deck (2×52)   │
        │                          │ ──────────────▶ │ deals 2 cards each       │
        │ Hit / Stand buttons      │   Hit / Stand   │ runs AI + dealer turns   │
        │                          │                 │   on a background thread │
        │ GameObserver renders     │ ◀────────────── │ pauses ~1 s per card,    │
        │   each hand on update()  │  update() (EDT) │   marshals UI to the EDT │
        │ GameResult shows outcome │                 │ computes the winner      │
        └──────────────────────────┘                 └──────────────────────────┘
                    ▲                                              │
                    │   Observer / Observable (model ⇄ view)      ▼
        ┌──────────────────────────────────────────────────────────────────────┐
        │  MODEL:  Deck → Hand (PlayerHand / DealerHand / AIHand) → Card        │
        │  score = best ace handling (1 or 11), never over-counting             │
        └──────────────────────────────────────────────────────────────────────┘
```

When you press **Stand** (or bust), the `Controller` spawns a daemon thread that
plays out every `AIHand` and then the `DealerHand`. Between cards it `pause()`s
on that thread and pushes each draw to the Swing thread with
`SwingUtilities.invokeAndWait`, so the table animates one card at a time without
ever freezing. Each `Hand` is `Observable`; the `GameObserver` panels are the
`Observer`s and redraw themselves on every `update()`.

| # | Step | Who |
| - | :--- | :-- |
| 1 | Read input (number of players, key presses) | `view` |
| 2 | Build a 2×52 `Deck`, shuffle, deal two cards each | `controller` |
| 3 | Player hits/stands; busting ends the turn as a loss | `controller` |
| 4 | AI players then the dealer play out on a worker thread | `controller` |
| 5 | Winner decided; `GameResult` shown; stats persisted | `controller` + `view` |

---

## 🧱 Architecture

The classes are grouped by responsibility and documented in Doxygen/Javadoc
style (see [`doc/index.html`](doc/index.html)):

| Type | Role |
| :--- | :--- |
| `model.Card` | A single card: `CardValue`, `CardSuit`, face-up state, front/back icon. |
| `model.Deck` | Shuffled list of `Card`s (`Collections.shuffle`); draws from the top. |
| `model.Hand` | Abstract hand: holds cards and computes the best score; is `Observable`. |
| `model.PlayerHand` · `DealerHand` · `AIHand` | The concrete hands (human, house, computer). |
| `controller.Controller` | Orchestrates one match: deck, hands, turn order, result. |
| `view.JBlackJack` | **Singleton** entry point / `main`; swaps the active screen. |
| `view.MainWindow` | Full-screen `JFrame`, shared fonts/colors and the custom cursor. |
| `view.Home` · `HomeAI` · `HomeRegistered` | Home screen, AI-count picker, logged-in home. |
| `view.Game` · `GameObserver` · `GameResult` | The table, the per-hand observers, the outcome panel. |
| `view.Account` · `AccountAvatar` · `AccountRegistered` | Login/registration, avatar picker, stats screen. |
| `view.AudioManager` | **Singleton** sound player (clips keyed by classpath path). |
| `view.Resources` | Loads images/sounds from the classpath. |

**Patterns:** MVC, **Singleton** (`JBlackJack`, `AudioManager`) and
**Observer/Observable** (hands ⇄ table panels).

---

## 🗃️ Accounts file

Each line of `accounts.txt` is one account, fields separated by `:` in this
order:

| # | Field | Description |
| - | :---- | :---------- |
| 1 | `nickname` | Display name / login. |
| 2 | `password` | > 8 chars, upper + lower + digit + symbol. |
| 3 | `avatarId` | Chosen avatar, **1–15**. |
| 4 | `gamesPlayed` | Total games played. |
| 5 | `gamesWon` | Games won. |
| 6 | `gamesLost` | Games lost. |
| 7 | `xp` | Experience toward the next level. |
| 8 | `level` | Current level (XP threshold = `150·(level−1)+50`). |

---

## ⚠️ Notes & limitations

- **Plain-text accounts.** Passwords are stored unhashed in `~/.JBlackJack/accounts.txt` — fine for a local coursework project, not for anything real.
- **Single, full-screen window.** `MainWindow` is undecorated and sized to the screen; the layout is tuned around the primary display resolution.
- **No natural-blackjack bonus.** An opening 21 wins as a normal high score; there is no special payout or instant win.
- **Desktop only.** Requires a graphical (non-headless) environment with audio; it's a Swing app, so there's no CLI mode.

---

## 👥 Authors

JBlackJack was designed and built by:

- **Mercuri Lorenzo** — Matricola 2145783 · **[LM-official](https://github.com/LM-official)**

---

## 📜 License

Released under the MIT License. See [LICENSE](LICENSE).
