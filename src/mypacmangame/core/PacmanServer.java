package assignments.Ex3.mypacmangame.core;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;
import assignments.Ex3.mypacmangame.entities.PacmanEntity;

/**
 * The core server implementation that manages the game board, ghosts, and Pacman.
 * Implements the exact methods required by the Ex3 PacmanGame interface.
 */
public class PacmanServer implements PacmanGame {

    private int[][] board;
    private GhostCL[] ghosts;
    private PacmanEntity pacman;

    public PacmanServer(int[][] board) {
        this.board = board;
        this.ghosts = new GhostCL[0];
        this.pacman = new PacmanEntity("1,1");
    }

    public void initGhosts(GhostCL[] ghosts) {
        this.ghosts = ghosts;
    }

    public void initPacman(PacmanEntity pacman) {
        this.pacman = pacman;
    }

    // =========================================================
    // EXACT INTERFACE METHODS (Based on the JAR definition)
    // =========================================================

    @Override
    public int[][] getGame(int i) {
        return this.board;
    }

    @Override
    public GhostCL[] getGhosts(int i) {
        return this.ghosts;
    }

    @Override
    public String getPos(int i) {
        return this.pacman.getPosition();
    }

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