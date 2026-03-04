package assignments.Ex3.mypacmangame.display;

import exe.ex3.game.StdDraw;

/**
 * This class handles the graphical rendering of the Pacman game.
 * It uses the StdDraw library to draw the board, the player (Pacman), and the ghosts.
 * It is responsible for converting logical grid coordinates (integers) into visual canvas coordinates (0.0 to 1.0).
 */
public class GameDisplay {

    /**
     * Initializes the game canvas and drawing settings.
     * Sets up the window buffering to ensure smooth animations.
     *
     * @param width  The logical width of the game board (unused in canvas size but good for context).
     * @param height The logical height of the game board.
     */
    public void initCanvas(int width, int height) {
        StdDraw.setCanvasSize();
        // Default 0-1 scale is used to ensure compatibility with the JAR and standard drawing methods.

        try {
            // Enable double buffering for smoother animations (prevents flickering).
            StdDraw.enableDoubleBuffering(0);
        } catch (Exception e) {
            // Ignore if double buffering fails on specific hardware.
        }
    }

    /**
     * Draws the static game board, including walls, empty spaces, and food (pellets).
     * Iterates through the board matrix and renders each cell based on its value.
     *
     * @param board A 2D integer array representing the game state (1=Wall, 2=Dot, 3=PowerUp, etc.).
     * @param level The current level number (can be used for visual theming in the future).
     */
    public void drawBoard(int[][] board, int level) {
        int height = board.length;
        int width = board[0].length;

        // Draw the background color (Dark Blue/Black theme)
        StdDraw.setPenColor(10, 10, 30);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Convert grid coordinates to normalized (0.0 - 1.0) canvas coordinates
                // We flip the Y axis because StdDraw (0,0) is bottom-left, but matrix (0,0) is top-left.
                double drawX = (x + 0.5) / width;
                double drawY = (height - 1 - y + 0.5) / height;
                double halfW = 0.5 / width;
                double halfH = 0.5 / height;

                if (board[y][x] == 1) {
                    // Draw Wall
                    try {
                        StdDraw.picture(drawX, drawY, "wall.png", halfW * 2, halfH * 2);
                    } catch (Exception e) {
                        // Fallback: Geometric shapes if image fails
                        StdDraw.setPenColor(0, 100, 255);
                        StdDraw.filledRectangle(drawX, drawY, halfW * 0.95, halfH * 0.95);
                        StdDraw.setPenColor(0, 0, 0);
                        StdDraw.filledRectangle(drawX, drawY, halfW * 0.8, halfH * 0.8);
                    }
                } else if (board[y][x] == 2) {
                    // Draw Pellet (Dot)
                    StdDraw.setPenColor(0, 255, 127);
                    StdDraw.filledRectangle(drawX, drawY, halfW / 4, halfH / 4);
                } else if (board[y][x] == 3) {
                    // Draw Power-Up (Apple)
                    try {
                        StdDraw.picture(drawX, drawY, "apple.png", halfW * 1.5, halfH * 1.5);
                    } catch (Exception e) {
                        StdDraw.setPenColor(200, 0, 0);
                        StdDraw.filledRectangle(drawX, drawY, halfW / 2, halfH / 2);
                    }
                }
            }
        }
    }

    /**
     * Draws the player (Pacman) at the specified logical coordinates.
     * Handles the conversion from grid position to screen position.
     *
     * @param x       The logical X coordinate on the grid.
     * @param y       The logical Y coordinate on the grid.
     * @param skin    The filename of the image to use for the player (e.g., "pacman.png").
     * @param boardW  The total width of the board (used for scaling).
     * @param boardH  The total height of the board (used for scaling).
     */
    public void drawPlayer(int x, int y, String skin, int boardW, int boardH) {
        double drawX = (x + 0.5) / boardW;
        double drawY = (boardH - 1 - y + 0.5) / boardH;
        double sizeW = 1.0 / boardW;
        double sizeH = 1.0 / boardH;

        try {
            StdDraw.picture(drawX, drawY, skin, sizeW * 0.85, sizeH * 0.85);
        } catch (Exception e) {
            // Fallback: Yellow square if image not found
            StdDraw.setPenColor(255, 255, 0);
            StdDraw.filledRectangle(drawX, drawY, sizeW / 3, sizeH / 3);
        }
    }

    /**
     * Draws a ghost at the specified logical coordinates.
     *
     * @param x       The logical X coordinate on the grid.
     * @param y       The logical Y coordinate on the grid.
     * @param skin    The filename of the image to use for the ghost (e.g., "rick.png").
     * @param boardW  The total width of the board (used for scaling).
     * @param boardH  The total height of the board (used for scaling).
     */
    public void drawGhost(int x, int y, String skin, int boardW, int boardH) {
        double drawX = (x + 0.5) / boardW;
        double drawY = (boardH - 1 - y + 0.5) / boardH;
        double sizeW = 1.0 / boardW;
        double sizeH = 1.0 / boardH;

        try {
            // Draw the ghost skin
            StdDraw.picture(drawX, drawY, skin, sizeW * 0.9, sizeH * 0.9);
        } catch (Exception e) {
            // Fallback: Red square if image not found
            StdDraw.setPenColor(255, 0, 0);
            StdDraw.filledRectangle(drawX, drawY, sizeW / 3, sizeH / 3);
        }
    }
}