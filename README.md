# Ex3: Pac-Man is "Going Places"

## Abstract
[cite_start]This assignment focuses on OOP, code reuse, and putting it all together (aka integration)[cite: 5]. [cite_start]We will utilize a modified version of the Map2D algorithms (implemented in Ex2) in order to design and implement a complete version of the Pac-Man game[cite: 6].

## The Pac-Man Algorithm
[cite_start]The goal of the Pac-Man is to “eat” all the “pink” dots while avoiding the ghosts[cite: 14]. [cite_start]At each step, the Pac-Man can move to one of the four directions (UP, DOWN, LEFT, RIGHT)[cite: 15]. [cite_start]Upon eating a green dot, the ghosts become “eatable” for a few seconds (the ghosts’ icons get smaller)[cite: 16].

Our Pac-Man AI operates as a Finite State Machine (FSM) transitioning between three main states:

1. **Gather State (Default):** Pac-Man scans the board using BFS (Breadth-First Search) to find the shortest valid path to the nearest pink dot, navigating around walls.
2. **Evade State (Danger):** If a ghost enters a critical proximity radius, Pac-Man aborts gathering and selects a movement direction that maximizes the distance from the threat.
3. **Hunt State (Power-Up):** When a green dot is consumed, Pac-Man calculates the shortest path to the nearest vulnerable ghost to intercept it before the power-up timer expires.

## Project Structure
[cite_start]Ex3 has three main stages[cite: 7]:
* [cite_start]**(i) Design:** Design the Pac-Man algorithm[cite: 7].
* [cite_start]**(ii) Client Side:** Implement the Pac-Man module (the client side)[cite: 7].
* [cite_start]**(iii) Server Side:** Implement the game module (the server side)[cite: 8].

## Execution & Releases
The repository includes runnable JAR files for execution:
* [cite_start]`Ex3_2.jar`: the solution to part 2 - client[cite: 24].
* [cite_start]`Ex3_3.jar`: the full solution to part 3 - client + (your) server[cite: 25].
* [cite_start]A short (120 seconds max) video - explaining your implementation of the server-side and demonstrating the actual game of the Pac-Man on your implementation[cite: 27].
