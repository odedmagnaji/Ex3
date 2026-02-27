package assignments.Ex3.mypacmangame.core;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;

// Importing our custom Pacman entity
import assignments.Ex3.mypacmangame.entities.PacmanEntity;

/**
 * The core server implementation that manages the game board, ghosts, and Pacman.
 */
public class PacmanServer implements PacmanGame {

    private int[][] board;
    private GhostCL[] ghosts;
    private PacmanEntity pacman; // Our main player entity

    /**
     * Initializes the server with a specific board layout.
     * @param board The 2D array representing the game map.
     */
    public PacmanServer(int[][] board) {
        this.board = board;
        this.ghosts = new GhostCL[0];
        this.pacman = new PacmanEntity("0,0"); // Default fallback
    }

    /**
     * Loads the ghost entities into the server state.
     * @param ghosts The array of ghosts.
     */
    public void initGhosts(GhostCL[] ghosts) {
        this.ghosts = ghosts;
    }

    /**
     * Loads the Pacman player into the server state.
     * @param pacman The main player entity.
     */
    public void initPacman(PacmanEntity pacman) {
        this.pacman = pacman;
    }

    @Override
    public int[][] getGame(int code) {
        return this.board;
    }

    @Override
    public String move(int i) {
        return "";
    }

    @Override
    public void play() {

    }

    @Override
    public String end(int i) {
        return "";
    }

    @Override
    public String getData(int i) {
        return "";
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public boolean isCyclic() {
        return false;
    }

    @Override
    public String init(int i, String s, boolean b, long l, double v, int i1, int i2) {
        return "";
    }

    @Override
    public GhostCL[] getGhosts(int code) {
        return this.ghosts;
    }

    @Override
    public Character getKeyChar() {
        return null;
    }

    @Override
    public String getPos(int code) {
        // The server now asks our actual Pacman entity for its position
        return this.pacman.getPosition();
    }

    // =========================================================
    // IMPORTANT ACTION REQUIRED AFTER PASTING:
    // If the class name 'PacmanServer' is underlined in red:
    // 1. Click on the word 'PacmanServer'.
    // 2. Press Alt + Enter.
    // 3. Select "Implement methods" and click OK.
    // 4. Leave the generated methods exactly as they are.
    // =========================================================
}