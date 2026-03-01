package assignments.Ex3.mypacmangame.display;

import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;

public class GameDisplay {

    public void initCanvas(int width, int height) {
        // 1. Set canvas size (0 arguments - verified)
        StdDraw.setCanvasSize();

        // 2. We DO NOT call setXscale/setYscale.
        // We accept the default 0.0 to 1.0 range to avoid JAR errors.

        // 3. Enable Double Buffering (Passing 0 as verified by your error log)
        try {
            StdDraw.enableDoubleBuffering(0);
        } catch (Exception e) {
            System.out.println("Warning: Could not enable double buffering.");
        }
    }

    public void drawBoard(int[][] board, int level) {
        int height = board.length;
        int width = board[0].length;

        // Draw Background (Standard 0.0-1.0 coordinates)
        StdDraw.setPenColor(10, 10, 30);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // MANUAL NORMALIZATION: Convert Grid(x,y) to Screen(0.0-1.0)
                double drawX = (x + 0.5) / width;
                // Invert Y because grid (0,0) is top-left, but StdDraw (0,0) is bottom-left
                double drawY = (height - 1 - y + 0.5) / height;

                double halfW = 0.5 / width;
                double halfH = 0.5 / height;

                if (board[y][x] == 1) {
                    try {
                        StdDraw.picture(drawX, drawY, "wall.png", halfW * 2, halfH * 2);
                    } catch (Exception e) {
                        // Fallback: Blue Wall
                        StdDraw.setPenColor(0, 100, 255);
                        StdDraw.filledRectangle(drawX, drawY, halfW * 0.95, halfH * 0.95);
                        // Inner black box to create "hollow" wall effect
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
        // Same Manual Normalization logic for the player
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

    public static void main(String[] args) {
        GameDisplay display = new GameDisplay();

        int[][] board = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 3, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 0, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        int pX = 4;
        int pY = 3;

        display.initCanvas(board[0].length, board.length);

        // === MOVEMENT LOOP ===
        while (true) {
            // 1. INPUT HANDLING
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                if (board[pY - 1][pX] != 1) pY--;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                if (board[pY + 1][pX] != 1) pY++;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                if (board[pY][pX - 1] != 1) pX--;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                if (board[pY][pX + 1] != 1) pX++;
            }

            // 2. RENDERING
            display.drawBoard(board, 1);
            display.drawPlayer(pX, pY, "morty.png", board[0].length, board.length);

            // 3. SHOW FRAME (Passing 150ms as required by your JAR)
            StdDraw.show(150);
        }
    }
}