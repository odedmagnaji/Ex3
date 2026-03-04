package mypacmangame.entites;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.entities.PacmanEntity;

/**
 * JUnit test suite for the PacmanEntity class.
 * This class validates the state management and core actions of the player entity,
 * ensuring that position, score, and lives are handled correctly during gameplay.
 */
public class PacmanEntityTest {

    /**
     * Verifies that a new Pacman instance is initialized with the correct default values.
     * Checks the starting position, initial score (0), and starting lives (3).
     */
    @Test
    void testPacmanInitialState() {
        PacmanEntity pacman = new PacmanEntity("5,5");

        // Verify that Pacman spawns with correct defaults
        assertEquals("5,5", pacman.getPosition(), "Starting position should match.");
        assertEquals(0, pacman.getScore(), "Starting score should be 0.");
        assertEquals(3, pacman.getLives(), "Should start with 3 lives.");
    }

    /**
     * Tests standard player actions such as moving, gaining points, and losing lives.
     * Ensures that the entity's state updates accurately after each operation.
     */
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

    /**
     * Validates the boundary logic for the player's life count.
     * Ensures that the number of lives does not drop below zero, even if
     * additional life-loss events occur (Edge Case).
     */
    @Test
    void testLivesBoundary() {
        PacmanEntity pacman = new PacmanEntity("0,0");

        // Lose all 3 initial lives
        pacman.loseLife();
        pacman.loseLife();
        pacman.loseLife();
        assertEquals(0, pacman.getLives(), "Lives should be 0 after losing 3 times.");

        // Try to lose one more life (Edge Case) - should remain at 0
        pacman.loseLife();
        assertEquals(0, pacman.getLives(), "Lives should not go below 0 (check logic safeguard).");
    }
}