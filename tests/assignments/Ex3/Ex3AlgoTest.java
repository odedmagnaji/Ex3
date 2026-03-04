package assignments.Ex3;

import exe.ex3.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Point;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test suite for the Ex3Algo class.
 * This class validates the core logic used for Level 4 AI, including
 * pathfinding, movement mechanics, and safety assessments.
 */
public class Ex3AlgoTest {

    private Ex3Algo algo;

    /**
     * Initializes a new instance of the algorithm before each test case.
     */
    @BeforeEach
    void setUp() {
        algo = new Ex3Algo();
    }

    /**
     * Verifies that coordinate strings (e.g., "25, 12") received from the
     * game server are correctly parsed into Point objects.
     */
    @Test
    void testExtractPosition() {
        Point p = algo.extractPosition("25, 12");
        assertEquals(25, p.x);
        assertEquals(12, p.y);
    }

    /**
     * Validates the neighbor calculation logic, specifically the cyclic
     * wrap-around mechanics of the board edges.
     */
    @Test
    void testGetNeighborAndWrapAround() {
        int w = 20;
        int h = 10;

        Point edgeRight = new Point(19, 5);
        assertEquals(0, algo.getNeighbor(edgeRight, Game.RIGHT, w, h).x);

        Point edgeLeft = new Point(0, 5);
        assertEquals(19, algo.getNeighbor(edgeLeft, Game.LEFT, w, h).x);
    }

    /**
     * Ensures the algorithm correctly identifies opposite directions
     * to prevent oscillation (moving back and forth between two cells).
     */
    @Test
    void testOpposite() {
        assertEquals(Game.DOWN, algo.opposite(Game.UP));
        assertEquals(Game.UP, algo.opposite(Game.DOWN));
        assertEquals(Game.RIGHT, algo.opposite(Game.LEFT));
        assertEquals(Game.LEFT, algo.opposite(Game.RIGHT));
    }

    /**
     * Tests obstacle detection, ensuring that move legality is correctly
     * assessed based on the board's color data (e.g., blue walls).
     */
    @Test
    void testIsLegal() {
        int[][] board = new int[20][20];
        int blue = -1;
        board[5][5] = blue;

        assertFalse(algo.isLegal(new Point(5, 5), board, blue));
        assertTrue(algo.isLegal(new Point(1, 1), board, blue));
    }

    /**
     * Verifies that the restricted center area (ghost house) is correctly
     * identified to prevent Pac-Man from entering.
     */
    @Test
    void testIsGhostHouse() {
        int[][] board = new int[20][20];

        assertTrue(algo.isGhostHouse(new Point(10, 10), board));
        assertFalse(algo.isGhostHouse(new Point(0, 0), board));
    }

    /**
     * Tests the core Breadth-First Search (BFS) logic to ensure it accurately
     * calculates distances and navigates around obstacles.
     */
    @Test
    void testComputeBFS() {
        int w = 30;
        int h = 30;
        int[][] board = new int[w][h];
        int blue = -1;

        board[1][0] = blue;
        board[1][1] = blue;
        board[1][2] = blue;

        int[][] dists = algo.computeBFS(new Point(0, 0), board, blue);

        assertEquals(0, dists[0][0]);
        assertNotEquals(-1, dists[2][0]);
        assertTrue(dists[2][0] > 2);
    }

    /**
     * Validates the Safe-Space counting mechanism used to evaluate
     * available movement area and avoid traps or dead ends.
     */
    @Test
    void testCountSafeSpace() {
        int w = 30;
        int h = 30;
        int[][] board = new int[w][h];
        double[][] dangerMap = new double[w][h];
        for (double[] r : dangerMap) {
            Arrays.fill(r, 10.0);
        }
        int blue = -1;

        int space = algo.countSafeSpace(new Point(5, 5), board, dangerMap, 15, blue);
        assertEquals(15, space);

        board[4][5] = blue;
        board[6][5] = blue;
        board[5][4] = blue;
        board[5][6] = blue;

        int trappedSpace = algo.countSafeSpace(new Point(5, 5), board, dangerMap, 15, blue);
        assertEquals(1, trappedSpace);
    }
}