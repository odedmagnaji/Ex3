package mypacmangame.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.core.PacmanServer;
import assignments.Ex3.mypacmangame.entities.GhostEntity;
import assignments.Ex3.mypacmangame.entities.PacmanEntity;
import exe.ex3.game.GhostCL;

/**
 * JUnit test suite for the PacmanServer class.
 * This class ensures that the game server correctly manages the board state,
 * handles entity integration, and strictly enforces movement rules and boundaries.
 */
public class PacmanServerTest {

    /**
     * Verifies that the server correctly stores the game board matrix
     * and returns it accurately upon request.
     */
    @Test
    void testInitialBoardStorage() {
        int[][] mockBoard = {{1, 1, 1}, {1, 0, 1}};
        PacmanServer server = new PacmanServer(mockBoard);
        assertArrayEquals(mockBoard, server.getGame(0), "Server should store and return the board correctly.");
    }

    /**
     * Ensures that ghost entities are correctly initialized within the server
     * and that their properties (like position) are preserved.
     */
    @Test
    void testGhostIntegration() {
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);
        GhostEntity blinky = new GhostEntity("5,5", 0, 1);
        server.initGhosts(new GhostCL[]{blinky});

        GhostCL[] retrievedGhosts = server.getGhosts(0);
        assertNotNull(retrievedGhosts, "Ghosts array should not be null.");
        assertEquals(1, retrievedGhosts.length, "Should have exactly 1 ghost.");
        assertEquals("5,5", retrievedGhosts[0].getPos(0), "Ghost position should match.");
    }

    /**
     * Validates that the Pacman entity is successfully linked to the server
     * and starts at the designated coordinate.
     */
    @Test
    void testPacmanIntegration() {
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);
        server.initPacman(new PacmanEntity("7,7"));
        assertEquals("7,7", server.getPos(0), "Pacman initialized position should be 7,7.");
    }

    /**
     * Tests standard movement logic.
     * Verifies that Pacman can move from its current position into an adjacent empty cell.
     */
    @Test
    void testPacmanMovementValid() {
        // Board layout:
        // 1 1 1
        // 1 0 0  <- Pacman at (1,1), Empty at (2,1)
        // 1 1 1
        int[][] board = {
                {1, 1, 1},
                {1, 0, 0},
                {1, 1, 1}
        };
        PacmanServer server = new PacmanServer(board);
        server.initPacman(new PacmanEntity("1,1"));

        // Move RIGHT (dir 1) -> Should move to (2,1)
        String newPos = server.move(1);
        assertEquals("2,1", newPos, "Pacman should successfully move right to empty space.");
    }

    /**
     * Verifies wall collision logic.
     * Ensures Pacman remains stationary when attempting to move into a wall cell (value 1).
     */
    @Test
    void testPacmanMovementIntoWall() {
        int[][] board = {
                {1, 1, 1},
                {1, 0, 0},
                {1, 1, 1}
        };
        PacmanServer server = new PacmanServer(board);
        server.initPacman(new PacmanEntity("1,1"));

        // Move UP (dir 0) -> Into Wall at (1,0)
        String newPos = server.move(0);
        assertEquals("1,1", newPos, "Pacman should NOT move into a wall.");
    }

    /**
     * Checks boundary constraints.
     * Ensures that the server prevents Pacman from moving outside the array's index range.
     */
    @Test
    void testPacmanMovementOutOfBounds() {
        // 1x1 Board
        int[][] board = {{0}};
        PacmanServer server = new PacmanServer(board);
        server.initPacman(new PacmanEntity("0,0"));

        // Try to move LEFT (dir 3) -> Out of bounds (-1, 0)
        String newPos = server.move(3);
        assertEquals("0,0", newPos, "Pacman should NOT move out of board boundaries.");

        // Try to move UP (dir 0) -> Out of bounds (0, -1)
        newPos = server.move(0);
        assertEquals("0,0", newPos, "Pacman should NOT move out of board boundaries.");
    }
}