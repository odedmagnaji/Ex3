package assignments.Ex3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

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

        assertThrows(RuntimeException.class, () -> map.getPixel(-1, 0));
        assertThrows(RuntimeException.class, () -> map.getPixel(0, 5));
    }

    @Test
    public void testSetPixel() {
        Map map = new Map(3, 3, 0);
        map.setPixel(1, 1, 5);
        assertEquals(5, map.getPixel(1, 1));

        map.setPixel(-1, 0, 5);
        assertEquals(0, map.getPixel(0, 0));
    }

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

    @Test
    public void testIsCyclic() {
        Map map = new Map(5, 5, 0);
        assertTrue(map.isCyclic());

        map.setCyclic(false);
        assertFalse(map.isCyclic());
    }

    @Test
    public void testIsInside() {
        Map map = new Map(3, 3, 0);
        assertTrue(map.isInside(new Index2D(1, 1)));
        assertFalse(map.isInside(new Index2D(3, 3)));
        assertFalse(map.isInside(new Index2D(-1, 0)));
    }
}