# Ex3: Pac-Man - Object-Oriented Programming & Gaming 

## 1. Architecture Overview
This project implements a complete Pac-Man game, emphasizing Object-Oriented Programming, code reuse, and system integration. To ensure a clean design and maintainability, the project is structured into distinct modules, separating the game logic (server) from the algorithm and visualization (client).

## 2. Server Layer (Game Engine)
The server module is responsible for the core game mechanics and state management, operating independently of the graphical display.
* **Main Responsibilities:**
  * Managing the 2D grid board and entity coordinates.
  * Handling ghost behaviors, spawning, and timers.
  * Detecting collisions between Pac-Man, ghosts, and dots.
  * Exposing a clean interface for the client to interact with.

## 3. Client Layer (Visualization)
This layer handles the user interface and graphical rendering using the StdDraw library.
* **Main Responsibilities:** Rendering the board, updating entity positions visually, and managing the main game loop.

## 4. The Pac-Man Algorithm (Ex3Algo)
The automatic gameplay is driven by an advanced strategic algorithm optimized for high-difficulty scenarios like Level 4. The AI utilizes a dynamic Weighted Scoring Engine and Safe-Space Analysis:

* **Global Danger Mapping:** Implements a Multi-Source BFS to calculate the true maze distance from all dangerous ghosts in real-time.
* **Safe-Space Reachability:** Before every move, the algorithm simulates local mobility to ensure Pac-Man avoids dead-ends and corridors where interception is likely.
* **Dynamic Decision Engine:**
  * **Gathering:** Prioritizes pink dots using a distance-weighted scoring system.
  * **Evasion:** Uses hard safety filters and exponential danger penalties to force escape routes when ghosts breach the safety radius.
  * **Hunting:** Actively tracks and intercepts edible ghosts during power-up phases.
* **Oscillation Control:** Features a 2-step position memory (Anti-ABAB) to eliminate repetitive back-and-forth movement and maintain momentum.

## 5. Testing Strategy
Robustness is ensured through a comprehensive JUnit testing suite for both the client algorithm and the server implementation.
* **Coverage:** Tests validate movement logic, pathfinding edge cases, and verify the algorithm successfully clears Level 4.

## 6. Build, Video, and Distribution
The project is packaged for easy distribution via GitHub Releases.
* **Executables:** Includes runnable JAR files (Ex3_2.jar for the client, Ex3_3.jar for full integration).
* **Demonstration:** A short video (max 120 seconds) explaining the server-side implementation and demonstrating gameplay.
* **Source Files:** Includes full source code (Ex3_all_src.zip) and documentation (Ex3_docs.zip).
