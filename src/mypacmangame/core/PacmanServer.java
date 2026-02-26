package assignments.Ex3.mypacmangame.core;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;

/**
 * The core server implementation that manages the game board and state.
 */
public class PacmanServer implements PacmanGame {

    private int[][] board;
    private GhostCL[] ghosts;

    /**
     * Initializes the server with a specific board layout.
     * @param board The 2D array representing the game map.
     */
    public PacmanServer(int[][] board) {
        this.board = board;
        this.ghosts = new GhostCL[0];
    }

    /**
     * Loads the ghost entities into the server state.
     * @param ghosts The array of ghosts to be managed by the server.
     */
    public void initGhosts(GhostCL[] ghosts) {
        this.ghosts = ghosts;
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
    public Character getKeyChar() {
        return null;
    }

    @Override
    public String getPos(int i) {
        return "";
    }

    @Override
    public GhostCL[] getGhosts(int code) {
        return this.ghosts;
    }


}