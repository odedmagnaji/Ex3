package assignments.Ex3.mypacmangame.core;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;
import assignments.Ex3.mypacmangame.entities.PacmanEntity;

/**
 * The core server implementation that manages the game state, board logic, and entity interactions.
 * This class implements the PacmanGame interface required by the Ex3 JAR file, acting
 * as the bridge between the game logic and the external game runner.
 *
 * It handles:
 * - Storing and retrieving the game board configuration.
 * - Managing the Pacman player's state and position.
 * - Tracking ghost entities on the board.
 * - Processing movement logic and collision detection with walls.
 */
public class PacmanServer implements PacmanGame {

    private int[][] board;
    private GhostCL[] ghosts;
    private PacmanEntity pacman;

    /**
     * Constructs a new PacmanServer with the given board layout.
     * Initializes the ghosts array and creates a default Pacman entity at (1,1).
     *
     * @param board A 2D integer array representing the game map (0=Empty, 1=Wall, etc.).
     */
    public PacmanServer(int[][] board) {
        this.board = board;
        this.ghosts = new GhostCL[0];
        this.pacman = new PacmanEntity("1,1");
    }

    /**
     * Initializes the list of ghosts currently active in the game.
     *
     * @param ghosts An array of GhostCL objects representing the ghosts.
     */
    public void initGhosts(GhostCL[] ghosts) {
        this.ghosts = ghosts;
    }

    /**
     * Sets the active Pacman entity for this game session.
     *
     * @param pacman The PacmanEntity object representing the player.
     */
    public void initPacman(PacmanEntity pacman) {
        this.pacman = pacman;
    }

    // =========================================================
    // EXACT INTERFACE METHODS (Based on the JAR definition)
    // =========================================================

    /**
     * Retrieves the current state of the game board.
     *
     * @param i An index parameter (unused in this implementation).
     * @return A 2D integer array representing the board.
     */
    @Override
    public int[][] getGame(int i) {
        return this.board;
    }

    /**
     * Retrieves the list of ghosts currently in the game.
     *
     * @param i An index parameter (unused).
     * @return An array of GhostCL objects.
     */
    @Override
    public GhostCL[] getGhosts(int i) {
        return this.ghosts;
    }

    /**
     * Gets the current position of the Pacman player as a string "x,y".
     *
     * @param i An index parameter (unused).
     * @return The position string (e.g., "5,10").
     */
    @Override
    public String getPos(int i) {
        return this.pacman.getPosition();
    }

    /**
     * Moves the Pacman player in the specified direction, if the move is valid.
     * Checks for boundaries and wall collisions before updating the position.
     *
     * @param dir The direction to move: 0=UP, 1=RIGHT, 2=DOWN, 3=LEFT.
     * @return The new position of Pacman as a String "x,y".
     */
    @Override
    public String move(int dir) {
        // Our custom movement engine mapped to the official method!
        if (this.pacman != null) {
            String[] posParts = this.pacman.getPosition().split(",");
            int currentX = Integer.parseInt(posParts[0]);
            int currentY = Integer.parseInt(posParts[1]);

            int nextX = currentX;
            int nextY = currentY;

            // Calculate the next coordinate (0=UP, 1=RIGHT, 2=DOWN, 3=LEFT)
            if (dir == 0) { nextY -= 1; }
            else if (dir == 1) { nextX += 1; }
            else if (dir == 2) { nextY += 1; }
            else if (dir == 3) { nextX -= 1; }

            // Check boundaries
            if (nextY >= 0 && nextY < this.board.length && nextX >= 0 && nextX < this.board[0].length) {
                // Check for wall collision (1 is a wall)
                if (this.board[nextY][nextX] != 1) {
                    this.pacman.setPosition(nextX + "," + nextY);
                }
            }
        }
        // The interface requires a String return. We return the updated position.
        return this.pacman.getPosition();
    }

    // --- The rest of the required interface methods (Stubs) ---

    @Override
    public Character getKeyChar() { return null; }

    @Override
    public void play() { }

    @Override
    public String end(int i) { return ""; }

    @Override
    public String getData(int i) { return ""; }

    @Override
    public int getStatus() { return 1; } // 1 usually means active/playing

    @Override
    public boolean isCyclic() { return false; }

    @Override
    public String init(int i, String s, boolean b, long l, double v, int i1, int i2) {
        return "";
    }
}