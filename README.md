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
This layer handles the user interface and graphical rendering using the `StdDraw` library.
* **Main Responsibilities:** Rendering the board, updating entity positions visually, and managing the main game loop.

## 4. The Pac-Man Algorithm (Ex3Algo)
The automatic gameplay is driven by a custom algorithm implemented in the client package. It utilizes modified Map2D algorithms from Ex2 for pathfinding. The AI operates on a dynamic state-machine logic:
* **Gather Mode:** Prioritizes consuming pink dots using shortest-path calculations (e.g., BFS).
* **Evade Mode:** Builds a dynamic danger map. When ghosts breach a safe radius, it calculates escape routes to maximize distance and avoid dead ends.
* **Hunt Mode:** Triggered by consuming green dots. The algorithm actively intercepts vulnerable, smaller ghosts before the power-up timer expires.

## 5. Testing Strategy
Robustness is ensured through a comprehensive JUnit testing suite for both the client algorithm and the server implementation.
* **Coverage:** Tests validate movement logic, pathfinding edge cases, and explicitly verify that the algorithm successfully beats level 4.

## 6. Build, Video, and Distribution
The project is packaged for easy distribution via GitHub Releases.
* **Executables:** Includes runnable JAR files (`Ex3_2.jar` for the client solution, `Ex3_3.jar` for the full client-server integration).
* **Demonstration:** A short video (max 120 seconds) explaining the server-side implementation and demonstrating live gameplay.
* **Source Files:** Includes full source code (`Ex3_all_src.zip`) and documentation archives (`Ex3_docs.zip`).
