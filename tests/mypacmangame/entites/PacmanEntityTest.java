package mypacmangame.entites;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.entities.PacmanEntity;

/**
 * Validates the state management and actions of the PacmanEntity.
 */
public class PacmanEntityTest {

    @Test
    void testPacmanInitialState() {
        PacmanEntity pacman = new PacmanEntity("5,5");

        // Verify that Pacman spawns with correct defaults
        assertEquals("5,5", pacman.getPosition(), "Starting position should match.");
        assertEquals(0, pacman.getScore(), "Starting score should be 0.");
        assertEquals(3, pacman.getLives(), "Should start with 3 lives.");
    }

    @Test
    void testPacmanActions() {
        PacmanEntity pacman = new PacmanEntity("1,1");

        // Simulate moving, eating a pellet, and hitting a ghost
        pacman.setPosition("2,2");
        pacman.addScore(10);
        pacman.loseLife();

        // Verify the changes took effect
        assertEquals("2,2", pacman.getPosition(), "Position should be updated to 2,2.");
        assertEquals(10, pacman.getScore(), "Score should be updated to 10.");
        assertEquals(2, pacman.getLives(), "Lives should be reduced to 2.");
    }

    @Test
    void testLivesBoundary() {
        PacmanEntity pacman = new PacmanEntity("0,0");

        // Lose all 3 lives
        pacman.loseLife();
        pacman.loseLife();
        pacman.loseLife();
        assertEquals(0, pacman.getLives(), "Lives should be 0 after losing 3 times.");

        // Try to lose one more life (Edge Case)
        pacman.loseLife();
        assertEquals(0, pacman.getLives(), "Lives should not go below 0 (check logic safeguard).");
    }
}