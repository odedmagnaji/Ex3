package assignments.Ex3.mypacmangame;

import assignments.Ex3.mypacmangame.display.GameDisplay;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;

public class MyGameController {

    public static void main(String[] args) {
        // --- Configuration ---
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

        int ghostX = 1;
        int ghostY = 1;

        int score = 0;
        boolean isRunning = true; // Flag to control the game loop

        // --- Init Display ---
        GameDisplay display = new GameDisplay();
        display.initCanvas(board[0].length, board.length);

        // --- Game Loop ---
        while (isRunning) {
            // 1. Input Handling (Player)
            int dx = 0, dy = 0;
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    dy = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  dy = 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  dx = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;

            // 2. Logic: Player Movement
            if (board[pY + dy][pX + dx] != 1) {
                pX += dx;
                pY += dy;
            }

            // 3. Logic: Ghost Movement (Random AI)
            // Generate a random direction: 0=Up, 1=Down, 2=Left, 3=Right
            int gDx = 0, gDy = 0;
            int randDir = (int) (Math.random() * 4);

            if (randDir == 0) gDy = -1;       // Up
            else if (randDir == 1) gDy = 1;  // Down
            else if (randDir == 2) gDx = -1; // Left
            else if (randDir == 3) gDx = 1;  // Right

            // Ghost Collision Check (Walls)
            // Only move if the target cell is NOT a wall (1)
            if (board[ghostY + gDy][ghostX + gDx] != 1) {
                ghostX += gDx;
                ghostY += gDy;
            }

            // 4. Logic: Game Rules
            // A. Eating Points
            if (board[pY][pX] == 2 || board[pY][pX] == 3) {
                score++;
                board[pY][pX] = 0; // Remove item
                System.out.println("Score: " + score);
            }

            // B. Ghost Catches Player
            if (pX == ghostX && pY == ghostY) {
                System.out.println("GAME OVER! Rick caught Morty.");
                // For now, we just print. Later we can stop the game or restart.
                score -= 10; // Punishment
            }

            // 5. Rendering
            display.drawBoard(board, 1);

            // Draw Characters
            display.drawPlayer(pX, pY, playerSkin, board[0].length, board.length);
            display.drawGhost(ghostX, ghostY, ghostSkin, board[0].length, board.length);

            // Draw Score
            StdDraw.setPenColor(255, 255, 255);
            StdDraw.textLeft(0.05, 0.95, "Score: " + score);

            // 6. Show Frame
            StdDraw.show(150);
        }
    }
}