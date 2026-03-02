package mypacmangame; //

import assignments.Ex3.mypacmangame.MyGameController;
import assignments.Ex3.Map; // 3. ייבוא של המפה
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyGameControllerTest {

    /**
     * Test 1: Verify Matrix Transpose Logic.
     */
    @Test
    public void testTranspose() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6}
        };

        // Expected result after transpose: 3 rows, 2 cols
        int[][] result = MyGameController.transpose(original);

        assertEquals(3, result.length, "Number of rows should become number of columns");
        assertEquals(2, result[0].length, "Number of columns should become number of rows");

        assertEquals(1, result[0][0]);
        assertEquals(4, result[0][1]);
        assertEquals(2, result[1][0]);
        assertEquals(5, result[1][1]);
        assertEquals(3, result[2][0]);
        assertEquals(6, result[2][1]);
    }

    /**
     * Test 2: Ghost finds a direct path in open space.
     */
    @Test
    public void testDirectPath() {
        // Visual representation:
        int[][] visualBoard = {
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1}, // Ghost at (1,1), Target at (3,1)
                {1, 1, 1, 1, 1}
        };

        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        // Start (1,1) -> Target (3,1)
        int[] nextMove = MyGameController.bfsGetNextStep(map, 1, 1, 3, 1);

        assertNotNull(nextMove, "Ghost should find a path in open space");

        // Expected behavior: Move from x=1 to x=2
        assertEquals(2, nextMove[0], "Ghost X should increment to 2");
        assertEquals(1, nextMove[1], "Ghost Y should remain 1");
    }

    /**
     * Test 3: Ghost avoids obstacles (Walls).
     */
    @Test
    public void testObstacleBypass() {
        // Visual Board: Wall at (1,0)
        int[][] visualBoard = {
                {0, 1, 0},
                {0, 0, 0}
        };

        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        // Start (0,0) -> Target (2,0)
        int[] nextMove = MyGameController.bfsGetNextStep(map, 0, 0, 2, 0);

        assertNotNull(nextMove, "Path should be found around the wall");

        // Ensure it didn't walk into the wall
        boolean hitWall = (nextMove[0] == 1 && nextMove[1] == 0);
        assertFalse(hitWall, "Ghost walked into a wall!");

        // The only valid move is down to (0,1)
        assertEquals(0, nextMove[0]);
        assertEquals(1, nextMove[1]);
    }

    /**
     * Test 4: No Path Available.
     */
    @Test
    public void testNoPath() {
        int[][] visualBoard = {
                {0, 1, 0},
                {1, 1, 1}
        };

        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        int[] nextMove = MyGameController.bfsGetNextStep(map, 0, 0, 2, 0);

        assertNull(nextMove, "Should return null when target is unreachable");
    }

    /**
     * Test 5: Start equals Target.
     */
    @Test
    public void testAlreadyAtTarget() {
        int[][] visualBoard = {{0}};
        int[][] logicalBoard = MyGameController.transpose(visualBoard);
        Map map = new Map(logicalBoard);

        int[] nextMove = MyGameController.bfsGetNextStep(map, 0, 0, 0, 0);

        assertNull(nextMove, "Should return null when start == target");
    }
}