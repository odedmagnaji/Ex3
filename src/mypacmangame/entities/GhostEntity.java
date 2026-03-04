package assignments.Ex3.mypacmangame.entities;

import exe.ex3.game.GhostCL;

/**
 * Represents a Ghost entity in the Pacman game.
 * This class implements the GhostCL interface, allowing the game engine to track
 * the ghost's position, type, and current status (Normal or Frightened).
 */
public class GhostEntity implements GhostCL {

    /** Constant representing the normal chasing behavior of the ghost. */
    public static final int STATUS_NORMAL = 0;

    /** Constant representing the frightened state (blue ghost) where it can be eaten. */
    public static final int STATUS_FRIGHTENED = 1;

    private String position;
    private int status;
    private int type;

    /**
     * Constructs a new GhostEntity with specific parameters.
     *
     * @param position The initial position string in "x,y" format (e.g., "10,10").
     * @param status   The initial status (0 for Normal, 1 for Frightened).
     * @param type     The type ID of the ghost (defines skin/behavior in some implementations).
     */
    public GhostEntity(String position, int status, int type) {
        this.position = position;
        this.status = status;
        this.type = type;
    }

    /**
     * Updates the position of the ghost.
     *
     * @param position A string representing the new coordinates "x,y".
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Updates the status of the ghost.
     *
     * @param status The new status code (e.g., STATUS_FRIGHTENED when Pacman eats a power pellet).
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Parses the X coordinate from the position string.
     *
     * @return The integer X coordinate, or 0 if parsing fails.
     */
    public int getX() {
        try {
            return Integer.parseInt(this.position.split(",")[0]);
        } catch (Exception e) { return 0; }
    }

    /**
     * Parses the Y coordinate from the position string.
     *
     * @return The integer Y coordinate, or 0 if parsing fails.
     */
    public int getY() {
        try {
            return Integer.parseInt(this.position.split(",")[1]);
        } catch (Exception e) { return 0; }
    }

    /**
     * Retrieves the current status of the ghost.
     *
     * @return The status code (0 = Normal, 1 = Frightened).
     */
    @Override
    public int getStatus() {
        return this.status;
    }

    /**
     * Retrieves the type identifier of the ghost.
     *
     * @return The type ID.
     */
    @Override
    public int getType() {
        return this.type;
    }

    /**
     * Retrieves the position string based on a specific code.
     *
     * @param code An internal code (unused in this implementation).
     * @return The position string "x,y".
     */
    @Override
    public String getPos(int code) {
        return this.position;
    }

    /**
     * Returns a debug string containing the ghost's details.
     *
     * @return A string with Type, Status, and Position.
     */
    @Override
    public String getInfo() {
        return "Type:" + type + ", Status:" + status + ", Pos:" + position;
    }

    /**
     * Calculates the remaining time the ghost is edible (blue).
     *
     * @param i An index parameter (unused).
     * @return 6.0 seconds if Frightened, 0.0 otherwise.
     */
    @Override
    public double remainTimeAsEatable(int i) {
        return (status == STATUS_FRIGHTENED) ? 6.0 : 0.0;
    }
}