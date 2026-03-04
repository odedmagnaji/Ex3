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

        // Verify initial state using the CORRECT getters from GhostEntity.java
        assertEquals("10,10", ghost.getPos(0), "Initial position should match.");
        assertEquals(0, ghost.getStatus(), "Initial status should be 0.");
        assertEquals(1, ghost.getType(), "Initial type should be 1.");

        // Simulate movement and status change
        ghost.setPosition("11,10");
        ghost.setStatus(1); // 1 = Frightened

        // Verify updated state
        assertEquals("11,10", ghost.getPos(0), "Updated position should match.");
        assertEquals(1, ghost.getStatus(), "Updated status should be 1.");
    }
}