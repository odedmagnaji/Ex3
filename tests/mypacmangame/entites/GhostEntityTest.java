package mypacmangame.entites;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.entities.GhostEntity;

/**
 * JUnit test suite for the GhostEntity class.
 * This class validates the state management, coordinate parsing,
 * and behavioral logic of ghost entities within the game.
 */
public class GhostEntityTest {

    /**
     * Verifies that the ghost's initial state is correctly set upon construction
     * and that updates to its position and status are handled properly.
     */
    @Test
    void testGhostStateManagement() {
        // Initialize a ghost at coordinate 10,10
        GhostEntity ghost = new GhostEntity("10,10", 0, 1);

        // Verify initial state
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

    /**
     * Tests the parsing logic that converts the string-based position
     * (e.g., "5,8") into separate integer X and Y coordinates.
     */
    @Test
    void testCoordinateParsing() {
        GhostEntity ghost = new GhostEntity("5,8", 0, 1);

        // Test parsing logic (getX / getY)
        assertEquals(5, ghost.getX(), "Should correctly parse X coordinate.");
        assertEquals(8, ghost.getY(), "Should correctly parse Y coordinate.");
    }

    /**
     * Validates the timing logic for the "edible" state.
     * Ensures that the remaining time is 6.0 seconds when the ghost is frightened
     * and 0.0 seconds during normal status.
     */
    @Test
    void testEatableTimeLogic() {
        GhostEntity ghost = new GhostEntity("0,0", 0, 1); // Normal status

        // Should be 0.0 when normal
        assertEquals(0.0, ghost.remainTimeAsEatable(0), "Time should be 0 when not frightened.");

        // Change to Frightened
        ghost.setStatus(GhostEntity.STATUS_FRIGHTENED);

        // Should be 6.0 (as per the implementation requirements)
        assertEquals(6.0, ghost.remainTimeAsEatable(0), "Time should be 6.0 when frightened.");
    }

    /**
     * Ensures that the getInfo() method returns a formatted string containing
     * all essential ghost data, including type, status, and position.
     */
    @Test
    void testInfoString() {
        GhostEntity ghost = new GhostEntity("2,2", 1, 3);
        String info = ghost.getInfo();

        // Verify the info string contains key data for debugging and tracking
        assertTrue(info.contains("Type:3"));
        assertTrue(info.contains("Status:1"));
        assertTrue(info.contains("Pos:2,2"));
    }
}