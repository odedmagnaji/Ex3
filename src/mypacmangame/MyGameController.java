package assignments.Ex3.mypacmangame;

import assignments.Ex3.Map;
import assignments.Ex3.mypacmangame.display.GameDisplay;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class MyGameController {

    public static void main(String[] args) {
        // --- 1. Configuration ---
        String playerSkin = "morty.png";
        String ghostSkin = "rick.png";

        // --- 2. Visual Map Data ---
        // 0=Empty, 1=Wall, 2=Point, 3=Apple
        int[][] visualBoard = {
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

        // --- 3. Logic Map Setup ---
        // We transpose to ensure logical (x,y) matches visual [row][col]
        int[][] logicalBoard = transpose(visualBoard);
        Map gameMap = new Map(logicalBoard);
        gameMap.setCyclic(false); // Disable cyclic to prevent jitter

        // --- 4. Game Variables ---
        int pX = 7;
        int pY = 5;

        int ghostX = 1;
        int ghostY = 1;

        int score = 0;
        boolean isRunning = true;

        // --- 5. Init Display ---
        GameDisplay display = new GameDisplay();
        display.initCanvas(visualBoard[0].length, visualBoard.length);

        // --- 6. Game Loop ---
        while (isRunning) {
            // A. Input Handling
            int dx = 0, dy = 0;
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    dy = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  dy = 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  dx = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;

            // B. Player Movement
            if (gameMap.getPixel(pX + dx, pY + dy) != 1) {
                pX += dx;
                pY += dy;
            }

            // C. Ghost AI (Internal GPS)
            // We calculate the next step using a local BFS function
            // This bypasses any issues with external files
            int[] nextMove = bfsGetNextStep(gameMap, ghostX, ghostY, pX, pY);

            if (nextMove != null) {
                ghostX = nextMove[0];
                ghostY = nextMove[1];
            } else {
                // Fallback if no path (should not happen in open map)
                // Random move just in case
                int gDx = 0, gDy = 0;
                if (Math.random() < 0.5) gDx = (Math.random() < 0.5) ? 1 : -1;
                else gDy = (Math.random() < 0.5) ? 1 : -1;

                if (gameMap.getPixel(ghostX + gDx, ghostY + gDy) != 1) {
                    ghostX += gDx;
                    ghostY += gDy;
                }
            }

            // D. Game Rules
            int currentCell = gameMap.getPixel(pX, pY);

            // Eating
            if (currentCell == 2 || currentCell == 3) {
                score++;
                gameMap.setPixel(pX, pY, 0);
                visualBoard[pY][pX] = 0;
                System.out.println("Score: " + score);
            }

            // Collision
            if (pX == ghostX && pY == ghostY) {
                System.out.println("GAME OVER! Ghost caught Player.");
                score -= 10;
            }

            // E. Rendering
            display.drawBoard(visualBoard, 1);
            display.drawPlayer(pX, pY, playerSkin, visualBoard[0].length, visualBoard.length);
            display.drawGhost(ghostX, ghostY, ghostSkin, visualBoard[0].length, visualBoard.length);

            StdDraw.setPenColor(255, 255, 255);
            StdDraw.textLeft(0.05, 0.95, "Score: " + score);

            StdDraw.show(200);
        }
    }

    /**
     * Internal BFS to guarantee pathfinding works.
     * Returns int[]{nextX, nextY} or null if no path.
     */
    private static int[] bfsGetNextStep(Map map, int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) return null;

        int width = map.getWidth();
        int height = map.getHeight();
        boolean[][] visited = new boolean[width][height];
        // Parent array stores "from where did we come" to reconstruct path
        // We store it as encoded integer: x * 1000 + y
        int[][] parent = new int[width][height];
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++) parent[i][j] = -1;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        boolean found = false;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0];
            int cy = curr[1];

            if (cx == targetX && cy == targetY) {
                found = true;
                break;
            }

            // Directions: Up, Down, Left, Right
            int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

            for (int[] d : dirs) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    // Check if not wall (1) and not visited
                    if (map.getPixel(nx, ny) != 1 && !visited[nx][ny]) {
                        visited[nx][ny] = true;
                        parent[nx][ny] = cx * 1000 + cy; // Store parent coordinate
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }

        if (!found) return null;

        // Reconstruct path backwards from Target to Start
        int currX = targetX;
        int currY = targetY;

        // Loop until we find the node directly connected to start
        while (true) {
            int pVal = parent[currX][currY];
            if (pVal == -1) return null; // Should not happen if found is true

            int pX = pVal / 1000;
            int pY = pVal % 1000;

            if (pX == startX && pY == startY) {
                // Found the first step!
                return new int[]{currX, currY};
            }
            currX = pX;
            currY = pY;
        }
    }

    private static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transposed = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
}