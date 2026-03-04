package assignments.Ex3.mypacmangame.entities;

/**
 * Represents the main player (Pacman) in the game.
 * This class serves as a data entity that tracks the player's dynamic state,
 * including current grid position, accumulated score, and remaining lives.
 */
public class PacmanEntity {

    private String position;
    private int score;
    private int lives;

    /**
     * Constructs a new Pacman player with the specified starting position.
     * Initializes the score to 0 and lives to 3 (standard arcade rules).
     *
     * @param startPosition The initial coordinates on the board in "x,y" format (e.g., "1,1").
     */
    public PacmanEntity(String startPosition) {
        this.position = startPosition;
        this.score = 0;
        this.lives = 3;
    }

    // --- Getters for the Server ---

    /**
     * Retrieves the current position of the player.
     *
     * @return The position string in "x,y" format.
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * Retrieves the player's current score.
     *
     * @return The score as an integer.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Retrieves the number of lives the player has left.
     *
     * @return The remaining lives.
     */
    public int getLives() {
        return this.lives;
    }

    // --- Setters and Action Methods ---

    /**
     * Updates the player's position on the board.
     *
     * @param position The new coordinates in "x,y" format.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Adds points to the player's current score.
     * Used when the player eats pellets or captures ghosts.
     *
     * @param points The amount of points to add.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Decrements the player's life count by 1.
     * Typically called when the player collides with a ghost.
     * Includes a check to ensure lives do not drop below zero.
     */
    public void loseLife() {
        if (this.lives > 0) {
            this.lives--;
        }
    }
}