package mypacmangame;

import assignments.Ex3.mypacmangame.MyGameController;
import assignments.Ex3.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test suite for the MyGameController class.
 * This suite focuses on the core mathematical and algorithmic logic used by the controller,
 * including matrix transposition for coordinate mapping and BFS pathfinding for ghost AI.
 */
public class MyGameControllerTest {

    /**
     * Verifies the matrix transposition logic.
     * Transposition is essential for converting between visual board orientation (rows/cols)
     * and the logical map representation used by the pathfinding algorithms.
     */
    @Test
    public void testTranspose() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6}
        };

        // Expected result after transpose: 3 rows, 2 columns
        int[][] result = MyGameController.transpose(original);

        assertEquals(3, result.length, "Number of rows should become number of columns.");
        assertEquals(2, result[0].length, "Number of columns should become number of rows.");

        assertEquals(1, result[0][0]);
        assertEquals(4, result[0][1]);
        assertEquals(2, result[1][0]);
        assertEquals(5, result[1][1]);
        assertEquals(3, result[2][0]);
        assertEquals(6, result[2][1]);
    }

    /**
     * Tests the ghost AI pathfinding in an open environment.
     * Ensures that when no obstacles are present, the BFS algorithm correctly
     * identifies the next immediate step towards the target.
     */
    @Test
    public void testDirectPath() {
        // Visual representation: 5x3 board
        int[][] visualBoard = {
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1}, // Ghost at (1,1), Target at (3,1)
                {1, 1, 1, 1, 1}
        };

        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        // Start (1,1) -> Target (3,1)
        int[] nextMove = MyGameController.bfsGetNextStep(map, 1, 1, 3, 1);

        assertNotNull(nextMove, "Ghost should find a path in open space.");

        // Expected behavior: Move from x=1 to x=2
        assertEquals(2, nextMove[0], "Ghost X coordinate should increment to 2.");
        assertEquals(1, nextMove[1], "Ghost Y coordinate should remain 1.");
    }

    /**
     * Validates the ghost's ability to navigate around obstacles.
     * Ensures the BFS algorithm finds a valid path that bypasses walls
     * instead of walking into them.
     */
    @Test
    public void testObstacleBypass() {
        // Visual Board: Wall at (1,0) blocking the horizontal path
        int[][] visualBoard = {
                {0, 1, 0},
                {0, 0, 0}
        };

        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        // Start (0,0) -> Target (2,0)
        int[] nextMove = MyGameController.bfsGetNextStep(map, 0, 0, 2, 0);

        assertNotNull(nextMove, "Path should be found around the wall.");

        // Ensure it didn't walk into the wall at (1,0)
        boolean hitWall = (nextMove[0] == 1 && nextMove[1] == 0);
        assertFalse(hitWall, "Ghost walked into a wall!");

        // The only valid move around the wall is down to (0,1)
        assertEquals(0, nextMove[0]);
        assertEquals(1, nextMove[1]);
    }

    /**
     * Verifies that the pathfinding algorithm correctly handles unreachable targets.
     * Should return null if the target is completely enclosed by walls.
     */
    @Test
    public void testNoPath() {
        int[][] visualBoard = {
                {0, 1, 0},
                {1, 1, 1} // Wall barrier separating (0,0) from (2,0)
        };

        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        int[] nextMove = MyGameController.bfsGetNextStep(map, 0, 0, 2, 0);

        assertNull(nextMove, "Should return null when the target is unreachable.");
    }

    /**
     * Tests the edge case where the ghost is already at the target position.
     * The BFS algorithm should return null as no further movement is required.
     */
    @Test
    public void testAlreadyAtTarget() {
        int[][] visualBoard = {{0}};
        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        int[] nextMove = MyGameController.bfsGetNextStep(map, 0, 0, 0, 0);

        assertNull(nextMove, "Should return null when start position is equal to the target position.");
    }
}