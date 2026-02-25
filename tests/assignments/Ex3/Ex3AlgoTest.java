package assignments.Ex3;

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
        assertEquals(25, p.x, "X coordinate should be 25");
        assertEquals(12, p.y, "Y coordinate should be 12");
    }
}