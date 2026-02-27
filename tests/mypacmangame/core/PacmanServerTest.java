package mypacmangame.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.core.PacmanServer;
import assignments.Ex3.mypacmangame.entities.GhostEntity;
import assignments.Ex3.mypacmangame.entities.PacmanEntity;
import exe.ex3.game.GhostCL;

/**
 * Integration tests for the PacmanServer logic.
 */
public class PacmanServerTest {

    @Test
    void testInitialBoardStorage() {
        int[][] mockBoard = {{1, 1, 1}, {1, 0, 1}};
        PacmanServer server = new PacmanServer(mockBoard);

        int[][] result = server.getGame(0);
        assertArrayEquals(mockBoard, result);
    }

    @Test
    void testGhostIntegration() {
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);

        GhostEntity blinky = new GhostEntity("5,5", 0, 1);
        GhostCL[] myGhosts = { blinky };
        server.initGhosts(myGhosts);

        GhostCL[] retrievedGhosts = server.getGhosts(0);
        assertEquals(1, retrievedGhosts.length, "Server should contain exactly 1 ghost.");
        assertEquals("5,5", retrievedGhosts[0].getPos(0), "The ghost position should match.");
    }

    /**
     * Verifies that the server correctly receives and tracks Pacman.
     */
    @Test
    void testPacmanIntegration() {
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);

        // Create our Pacman actor
        PacmanEntity myPacman = new PacmanEntity("7,7");

        // Load Pacman into the server
        server.initPacman(myPacman);

        // Ask the server where Pacman is
        String serverReportedPos = server.getPos(0);

        // Verify the server reports the correct position
        assertEquals("7,7", serverReportedPos, "Server should report Pacman's correct position.");
    }
}