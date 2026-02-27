package assignments.Ex3.mypacmangame.display;

import exe.ex3.game.StdDraw;

public class GameDisplay {

    public void initCanvas(int width, int height) {
        StdDraw.setCanvasSize();
    }

    public void drawBoard(int[][] board, int level) {
        int height = board.length;
        int width = board[0].length;

        StdDraw.setPenColor(135, 206, 235);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        StdDraw.setPenColor(30, 30, 200);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x] == 1) {

                    double drawX = (x + 0.5) / width;
                    double drawY = (height - y - 0.5) / height;
                    double halfWidth = 0.5 / width;
                    double halfHeight = 0.5 / height;

                    StdDraw.filledRectangle(drawX, drawY, halfWidth, halfHeight);
                }
            }
        }
    }

    public static void main(String[] args) {
        GameDisplay display = new GameDisplay();

        int[][] testBoard = {
                {1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1}
        };

        display.initCanvas(testBoard[0].length, testBoard.length);
        display.drawBoard(testBoard, 1);
    }
}