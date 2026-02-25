package assignments.Ex3;

import exe.ex3.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

public class Ex3AlgoTest {

    private Ex3Algo algo;

    @BeforeEach
    void setUp() {
        algo = new Ex3Algo();
    }

    @Test
    void testExtractPosition() {
        Point p = algo.extractPosition("25, 12");
        assertEquals(25, p.x);
        assertEquals(12, p.y);
    }

    @Test
    void testGetNeighborAndWrapAround() {
        int w = 20;
        int h = 10;

        Point edgeRight = new Point(19, 5);
        assertEquals(0, algo.getNeighbor(edgeRight, Game.RIGHT, w, h).x);

        Point edgeLeft = new Point(0, 5);
        assertEquals(19, algo.getNeighbor(edgeLeft, Game.LEFT, w, h).x);
    }
}