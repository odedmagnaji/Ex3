package mypacmangame.entites;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.entities.GhostEntity;

/**
 * Validates the state management of the GhostEntity class.
 */
public class GhostEntityTest {

    @Test
    void testGhostStateManagement() {
        // Initialize a ghost at coordinate 10,10
        GhostEntity ghost = new GhostEntity("10,10", 0, 1);

        // Verify initial state using our custom getters
        assertEquals("10,10", ghost.getGhostPosition(), "Initial position should match.");
        assertEquals(0, ghost.getGhostStatus(), "Initial status should be 0.");
        assertEquals(1, ghost.getGhostType(), "Initial type should be 1.");

        // Simulate movement and status change
        ghost.setPosition("11,10");
        ghost.setStatus(50);

        // Verify updated state
        assertEquals("11,10", ghost.getGhostPosition(), "Updated position should match.");
        assertEquals(50, ghost.getGhostStatus(), "Updated status should be 50.");
    }
}