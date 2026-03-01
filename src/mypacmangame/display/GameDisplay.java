package assignments.Ex3.mypacmangame.display;

import exe.ex3.game.StdDraw;

public class GameDisplay {

    public void initCanvas(int width, int height) {
        StdDraw.setCanvasSize();
        // Default 0-1 scale used to ensure compatibility with the JAR

        try {
            StdDraw.enableDoubleBuffering(0);
        } catch (Exception e) {
            // Ignore if double buffering fails
        }
    }

    public void drawBoard(int[][] board, int level) {
        int height = board.length;
        int width = board[0].length;

        // Background
        StdDraw.setPenColor(10, 10, 30);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double drawX = (x + 0.5) / width;
                double drawY = (height - 1 - y + 0.5) / height;
                double halfW = 0.5 / width;
                double halfH = 0.5 / height;

                if (board[y][x] == 1) {
                    try {
                        StdDraw.picture(drawX, drawY, "wall.png", halfW * 2, halfH * 2);
                    } catch (Exception e) {
                        StdDraw.setPenColor(0, 100, 255);
                        StdDraw.filledRectangle(drawX, drawY, halfW * 0.95, halfH * 0.95);
                        StdDraw.setPenColor(0, 0, 0);
                        StdDraw.filledRectangle(drawX, drawY, halfW * 0.8, halfH * 0.8);
                    }
                } else if (board[y][x] == 2) {
                    StdDraw.setPenColor(0, 255, 127);
                    StdDraw.filledRectangle(drawX, drawY, halfW / 4, halfH / 4);
                } else if (board[y][x] == 3) {
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

    public void drawPlayer(int x, int y, String skin, int boardW, int boardH) {
        double drawX = (x + 0.5) / boardW;
        double drawY = (boardH - 1 - y + 0.5) / boardH;
        double sizeW = 1.0 / boardW;
        double sizeH = 1.0 / boardH;

        try {
            StdDraw.picture(drawX, drawY, skin, sizeW * 0.85, sizeH * 0.85);
        } catch (Exception e) {
            StdDraw.setPenColor(255, 255, 0);
            StdDraw.filledRectangle(drawX, drawY, sizeW / 3, sizeH / 3);
        }
    }

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