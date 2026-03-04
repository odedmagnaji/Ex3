package assignments.Ex3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test suite for the Map class.
 * This class validates the core spatial algorithms and map manipulation logic,
 * including initialization, pixel management, pathfinding, and area filling.
 */
public class MapTest {

    /**
     * Tests the initialization of the map from a 2D array and verifies
     * that pixels are retrieved correctly. Also checks boundary validation.
     */
    @Test
    public void testInitAndGetPixel() {
        int[][] data = {
                {1, 0, 1},
                {0, 0, 0},
                {1, 1, 1}
        };
        Map map = new Map(data);

        assertEquals(3, map.getWidth());
        assertEquals(3, map.getHeight());
        assertEquals(1, map.getPixel(0, 0));
        assertEquals(0, map.getPixel(1, 1));

        // Ensure out-of-bounds access throws the expected exception
        assertThrows(RuntimeException.class, () -> map.getPixel(-1, 0));
        assertThrows(RuntimeException.class, () -> map.getPixel(0, 5));
    }

    /**
     * Verifies the ability to update specific pixel values on the map.
     * Also ensures that invalid coordinate updates do not crash the system.
     */
    @Test
    public void testSetPixel() {
        Map map = new Map(3, 3, 0);
        map.setPixel(1, 1, 5);
        assertEquals(5, map.getPixel(1, 1));

        // Safeguard check for out-of-bounds setting
        map.setPixel(-1, 0, 5);
        assertEquals(0, map.getPixel(0, 0));
    }

    /**
     * Tests the Flood Fill algorithm implementation.
     * Verifies that a connected area of the same value is correctly filled
     * with a new value and returns the correct count of affected pixels.
     */
    @Test
    public void testFill() {
        int[][] data = {
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        Map map = new Map(data);
        Pixel2D start = new Index2D(1, 1);

        int filledCount = map.fill(start, 2);

        assertEquals(1, filledCount);
        assertEquals(2, map.getPixel(1, 1));
        assertEquals(1, map.getPixel(0, 0));
    }

    /**
     * Validates the shortest path algorithm (BFS) in a simple open environment.
     * Checks that the path exists, starts at the source, and ends at the target.
     */
    @Test
    public void testShortestPathSimple() {
        int[][] data = {
                {0, 0, 0},
                {1, 1, 0},
                {0, 0, 0}
        };
        Map map = new Map(data);
        map.setCyclic(false);

        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(0, 2);

        Pixel2D[] path = map.shortestPath(start, end, 1);

        assertNotNull(path);
        assertTrue(path.length > 0);
        assertEquals(start, path[0]);
        assertEquals(end, path[path.length - 1]);
    }

    /**
     * Verifies that the shortest path algorithm correctly returns null
     * when the target is completely blocked by obstacles (walls).
     */
    @Test
    public void testShortestPathNoPath() {
        int[][] data = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 1, 0}
        };
        Map map = new Map(data);
        map.setCyclic(false);

        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(0, 2);

        Pixel2D[] path = map.shortestPath(start, end, 1);
        assertNull(path);
    }

    /**
     * Tests the generation of a distance map from a starting point.
     * Ensures that distances to adjacent pixels are calculated accurately.
     */
    @Test
    public void testAllDistance() {
        int[][] data = {
                {0, 0},
                {0, 0}
        };
        Map map = new Map(data);
        map.setCyclic(false);

        Pixel2D start = new Index2D(0, 0);
        Map2D distMap = map.allDistance(start, 1);

        assertEquals(0, distMap.getPixel(0, 0));
        assertEquals(1, distMap.getPixel(0, 1));
        assertEquals(1, distMap.getPixel(1, 0));
    }

    /**
     * Verifies the management of the cyclic (wrap-around) behavior flag.
     */
    @Test
    public void testIsCyclic() {
        Map map = new Map(5, 5, 0);
        assertTrue(map.isCyclic());

        map.setCyclic(false);
        assertFalse(map.isCyclic());
    }

    /**
     * Validates the boundary detection logic for various coordinate points.
     */
    @Test
    public void testIsInside() {
        Map map = new Map(3, 3, 0);
        assertTrue(map.isInside(new Index2D(1, 1)));
        assertFalse(map.isInside(new Index2D(3, 3)));
        assertFalse(map.isInside(new Index2D(-1, 0)));
    }
}