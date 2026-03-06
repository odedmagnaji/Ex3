# Ex3: Pac-Man - From AI Algorithm to Full Game Integration
**Ariel University | School of Computer Science | 2026**

## Project Overview
This project implements a complete Pac-Man game environment, divided into two main tasks:
1.  **Client Side (AI):** An autonomous algorithm designed to solve complex mazes and evade ghosts.
2.  **Server Side (Game Engine):** A full GUI and game logic implementation with a custom "Rick and Morty" theme.

---

## 1. Task 1: The Pac-Man Algorithm (Ex3Algo)
The primary goal of this stage was to design and implement an autonomous agent capable of winning Level 4 while avoiding ghosts.

### Strategy & Logic for Level 4:
To survive the high difficulty of Level 4, the algorithm utilizes a multi-layered strategic engine:
* **Multi-Source BFS Danger Mapping**: Calculates the real-time distance from every dangerous ghost using a Breadth-First Search logic.
* **Safe-Space Analysis**: Performs a reachability check (`countSafeSpace`) before every move to ensure Pac-Man doesn't enter dead-ends or corridors where it can be easily trapped.
* **Dynamic Scoring Engine**:
    * **Pellet Gathering**: Prioritizes pink dots using a distance-weighted scoring system ($1 / distance$).
    * **Hard Safety Filters**: Implements an "Escape Trigger" that overrides all objectives if a ghost breaches a critical safety radius.
    * **Anti-Oscillation Logic**: Features a 2-step position memory (Anti-ABAB) to prevent repetitive back-and-forth movement.

### Level 4 Performance:
* **Status**: Level Cleared Successfully.
* **Strategy**: Consistent survival using safe-space verification and BFS pathfinding.

---

## 2. Task 2: Game Server & Interface Implementation
This stage involved building a complete game environment, providing artistic freedom to design the server-side logic and GUI.

### Server Side (Game Engine):
The server manages the core game state and logic independently of the display:
* **Board & Entity Management**: Uses `PacmanServer` to implement the `PacmanGame` interface, handling grid coordinates, wall collisions, and tracking `PacmanEntity` and `GhostEntity` states.
* **Game Logic**: The `MyGameController` orchestrates the game loop, level transitions, and collision detection between the player, pellets, and ghosts.

### Graphical Interface & Extras ("Going Crazy"):
The GUI was implemented using the `StdDraw` library with a customized **"Rick and Morty"** theme:
* **Interactive Skin System**: A custom start screen allows players to select between different skins (Morty, Classic, or Pikachu) and ghost themes (Rick, Standard).
* **Audio Integration**: An asynchronous `AudioPlayer` plays sound effects (eat, hit, win) during gameplay without blocking the main loop.
* **Visual Polish**: Edible ghosts feature a cyan "Power-Up Aura", and the HUD displays real-time score and lives.

---

## 3. Controls & Instructions
| Action | Key / Input |
| :--- | :--- |
| **Start Game** | `SPACE` |
| **Move Pac-Man (Manual)** | Arrow Keys |
| **Pause Game** | `P` |
| **Select Player Skin** | Keys `1`, `2`, `3` |
| **Select Ghost Skin** | Keys `4`, `5` |

---

## 4. Testing & Project Structure
* **JUnit Testing**: Comprehensive suites validate movement, pathfinding, and AI safety mechanisms.
    * `Ex3AlgoTest`: Validates Level 4 strategy and navigation.
    * `PacmanServerTest`: Ensures server-side integrity and movement rules.
* **Project Layers**:
    * `assignments.Ex3`: Core Map algorithms and AI.
    * `mypacmangame.core`: Server implementation logic.
    * `mypacmangame.entities`: Game entities (Ghost, Pacman).
    * `mypacmangame.display`: Rendering (StdDraw) and Audio.

---

## 5. Video Demonstrations
> **MUST WATCH:**
> * **[📺 Full Game Interface & Server Implementation (Task 2)](https://youtu.be/d-32KOw_sBk)**
>     *Demonstration of the "Rick and Morty" theme, Skin Selection, Audio, and Game Loop.*
> * **[📺 AI Algorithm Level 4 Victory (Task 1)](https://youtu.be/CpcauM8SZPk)**
>     *Proof of Level 4 clearance using the BFS Strategy.*

---

## 6. Deliverables (GitHub Release)
All required files are packed in the **[Latest Release Section](../../releases)**:

* 📦 **`Ex3_2.jar` (Algorithm)**: The AI Client solution (run with `java -jar Ex3_2.jar`).
* 🎮 **`Ex3_3.jar` (Full Game)**: The complete Server + Client solution with the custom theme.
* 📄 **`Ex3_docs.zip`**: Documentation, README, and Images.
* 💻 **`Ex3_all_src.zip`**: Full Source Code (src folder).
