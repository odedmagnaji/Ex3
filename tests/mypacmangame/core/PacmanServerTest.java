package mypacmangame.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.core.PacmanServer;
import assignments.Ex3.mypacmangame.entities.GhostEntity;
import exe.ex3.game.GhostCL;

/**
 * Integration tests for the PacmanServer logic.
 */
public class PacmanServerTest {

    @Test
    void testInitialBoardStorage() {
        int[][] mockBoard = {
                {1, 1, 1},
                {1, 0, 1}
        };
        PacmanServer server = new PacmanServer(mockBoard);

        int[][] result = server.getGame(0);
        assertArrayEquals(mockBoard, result);
        assertEquals(2, result.length);
    }

    /**
     * Verifies that the server correctly receives, stores, and returns ghosts.
     */
    @Test
    void testGhostIntegration() {
        // Create an empty board
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);

        // Create a test ghost and put it in an array
        GhostEntity blinky = new GhostEntity("5,5", 0, 1);
        GhostCL[] myGhosts = { blinky };

        // Load the ghost array into the server
        server.initGhosts(myGhosts);

        // Retrieve the ghosts from the server
        GhostCL[] retrievedGhosts = server.getGhosts(0);

        // Validate that the server properly managed the ghosts
        assertEquals(1, retrievedGhosts.length, "Server should contain exactly 1 ghost.");
        assertEquals("5,5", retrievedGhosts[0].getPos(0), "The ghost position should match what we set.");
    }
}