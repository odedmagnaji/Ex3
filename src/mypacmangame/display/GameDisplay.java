package assignments.Ex3.mypacmangame.display;

import exe.ex3.game.StdDraw;

public class GameDisplay {

    /**
     * Minimalist initialization to avoid any JAR conflicts.
     */
    public void initCanvas(int width, int height) {
        StdDraw.setCanvasSize();
        // We are NOT calling setXscale to prevent the "blue screen" loop.
    }

    /**
     * Converts a String position "x,y" to an integer array [x, y].
     * Part of the requested 2D mapping logic.
     */
    public int[] pos2Index(String pos) {
        if (pos == null || !pos.contains(",")) return new int[]{0, 0};
        String[] parts = pos.split(",");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    /**
     * Renders the game world using manual normalization.
     */
    public void drawBoard(int[][] board, int level) {
        int height = board.length;
        int width = board[0].length;

        // 1. Draw Background (Center: 0.5, 0.5 | Radius: 0.5, 0.5)
        StdDraw.setPenColor(10, 10, 30);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        // 2. Iterate and Draw
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Normalization Math: (index + 0.5) / total
                double drawX = (x + 0.5) / width;
                double drawY = (height - y - 0.5) / height;

                double halfW = 0.5 / width;
                double halfH = 0.5 / height;

                if (board[y][x] == 1) {
                    try {
                        StdDraw.picture(drawX, drawY, "wall.png", halfW * 2, halfH * 2);
                    } catch (Exception e) {
                        StdDraw.setPenColor(0, 150, 255);
                        StdDraw.filledRectangle(drawX, drawY, halfW * 0.9, halfH * 0.9);
                    }
                } else if (board[y][x] == 2) {
                    StdDraw.setPenColor(0, 255, 127);
                    StdDraw.filledRectangle(drawX, drawY, halfW / 5, halfH / 5);
                } else if (board[y][x] == 3) {
                    try {
                        // Drawing high-res Apple with padding (0.8 scale)
                        StdDraw.picture(drawX, drawY, "apple.png", halfW * 1.6, halfH * 1.6);
                    } catch (Exception e) {
                        StdDraw.setPenColor(255, 50, 50);
                        StdDraw.filledRectangle(drawX, drawY, halfW / 3, halfH / 3);
                    }
                }
            }
        }
    }

    /**
     * Draws the player using manual coordinate mapping.
     */
    public void drawPlayer(String pos, String skinFileName, int boardW, int boardH) {
        int[] index = pos2Index(pos);
        int x = index[0];
        int y = index[1];

        double drawX = (x + 0.5) / boardW;
        double drawY = (boardH - y - 0.5) / boardH;

        try {
            // Sharper rendering using exact 0.85 grid coverage
            StdDraw.picture(drawX, drawY, skinFileName, (1.0/boardW) * 0.85, (1.0/boardH) * 0.85);
        } catch (Exception e) {
            StdDraw.setPenColor(255, 255, 0);
            StdDraw.filledRectangle(drawX, drawY, 0.02, 0.02);
        }
    }

    public static void main(String[] args) {
        GameDisplay display = new GameDisplay();
        int[][] testBoard = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 3, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 0, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        display.initCanvas(testBoard[0].length, testBoard.length);
        display.drawBoard(testBoard, 1);
        display.drawPlayer("4,3", "morty.png", testBoard[0].length, testBoard.length);
    }
}