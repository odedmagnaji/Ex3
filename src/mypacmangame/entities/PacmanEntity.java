package assignments.Ex3.mypacmangame.entities;

/**
 * Represents the main player (Pacman) in the game.
 * Manages the player's position, current score, and remaining lives.
 */
public class PacmanEntity {

    private String position;
    private int score;
    private int lives;

    /**
     * Initializes a new Pacman player.
     * * @param startPosition The initial coordinates on the board (e.g., "1,1").
     */
    public PacmanEntity(String startPosition) {
        this.position = startPosition;
        this.score = 0;       // Game starts with 0 points
        this.lives = 3;       // Classic arcade rules: start with 3 lives
    }

    // --- Getters for the Server ---

    public String getPosition() {
        return this.position;
    }

    public int getScore() {
        return this.score;
    }

    public int getLives() {
        return this.lives;
    }

    // --- Setters and Action Methods ---

    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Adds points to the current score (e.g., when eating a pellet).
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Reduces the life count by 1 (e.g., when caught by a ghost).
     */
    public void loseLife() {
        if (this.lives > 0) {
            this.lives--;
        }
    }
}