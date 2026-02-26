package mypacmangame.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Important: Importing our class from its specific package
import assignments.Ex3.mypacmangame.core.PacmanServer;

/**
 * Integration tests for the PacmanServer logic.
 */
public class PacmanServerTest {

    /**
     * Verifies that the server correctly initializes and retrieves the board layout.
     */
    @Test
    void testInitialBoardStorage() {
        // Create a small mock board
        int[][] mockBoard = {
                {1, 1, 1},
                {1, 0, 1}
        };

        // Initialize our custom server
        PacmanServer server = new PacmanServer(mockBoard);

        // Assert that the returned board matches our mock
        int[][] result = server.getGame(0);
        assertArrayEquals(mockBoard, result);
        assertEquals(2, result.length);
    }
}