package assignments.Ex3.mypacmangame;

import assignments.Ex3.mypacmangame.display.GameDisplay;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;

public class MyGameController {

    public static void main(String[] args) {
        // --- Configuration ---
        // You can change these to "pikachu.png" or "ghost.png" to switch skins
        String playerSkin = "morty.png";
        String ghostSkin = "rick.png";

        // --- Map Definition ---
        // 0=Empty, 1=Wall, 2=Point, 3=Apple
        int[][] board = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 0, 0, 1, 2, 1, 2, 1, 0, 0, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 3, 0, 3, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 0, 0, 1, 2, 1, 2, 1, 0, 0, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        // --- Game Variables ---
        int pX = 7;
        int pY = 5;

        // Ghost Position (Static for now)
        int ghostX = 1;
        int ghostY = 1;

        int score = 0;

        // --- Init Display ---
        GameDisplay display = new GameDisplay();
        display.initCanvas(board[0].length, board.length);

        // --- Game Loop ---
        while (true) {
            // 1. Input Handling
            int dx = 0, dy = 0;
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    dy = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  dy = 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  dx = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;

            // 2. Logic: Movement & Collision
            if (board[pY + dy][pX + dx] != 1) {
                pX += dx;
                pY += dy;
            }

            // 3. Logic: Eating Points
            if (board[pY][pX] == 2 || board[pY][pX] == 3) {
                score++;
                board[pY][pX] = 0; // Remove item
                System.out.println("Score: " + score);
            }

            // 4. Rendering
            display.drawBoard(board, 1);

            // Draw Characters with Configured Skins
            display.drawPlayer(pX, pY, playerSkin, board[0].length, board.length);
            display.drawGhost(ghostX, ghostY, ghostSkin, board[0].length, board.length);

            // Draw Score
            StdDraw.setPenColor(255, 255, 255);
            StdDraw.textLeft(0.05, 0.95, "Score: " + score);

            // 5. Show Frame
            StdDraw.show(150);
        }
    }
}