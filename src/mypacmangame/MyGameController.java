package assignments.Ex3.mypacmangame;

import assignments.Ex3.mypacmangame.display.GameDisplay;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;

public class MyGameController {

    public static void main(String[] args) {
        // 1. Define the Map (0=Empty, 1=Wall, 2=Point, 3=Apple)
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

        // 2. Setup Variables
        int pX = 7;
        int pY = 5;
        int score = 0;

        // 3. Initialize Display
        GameDisplay display = new GameDisplay();
        display.initCanvas(board[0].length, board.length);

        // 4. Game Loop
        while (true) {
            // Input
            int dx = 0, dy = 0;
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    dy = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  dy = 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  dx = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;

            // Collision Check (Wall)
            if (board[pY + dy][pX + dx] != 1) {
                pX += dx;
                pY += dy;
            }

            // Eating Logic
            if (board[pY][pX] == 2 || board[pY][pX] == 3) {
                score++;
                board[pY][pX] = 0; // Clear item
                System.out.println("Score: " + score);
            }

            // Draw
            display.drawBoard(board, 1);
            display.drawPlayer(pX, pY, "morty.png", board[0].length, board.length);

            // Draw Score on Screen
            StdDraw.setPenColor(255, 255, 255);
            StdDraw.textLeft(0.05, 0.95, "Score: " + score);

            // Show Frame
            StdDraw.show(150);
        }
    }
}