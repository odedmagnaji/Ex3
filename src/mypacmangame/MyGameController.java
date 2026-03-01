package assignments.Ex3.mypacmangame;

import assignments.Ex3.Map;
import assignments.Ex3.Pixel2D;
import assignments.Ex3.Index2D;
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

        // --- 2. Level Design (String Map) ---
        // # = Wall, . = Point, A = Apple, M = Morty (Start), R = Rick (Start)
        String levelStr =
                "###################\n" +
                        "#M......#.........#\n" +
                        "#.##.##.#.##.####.#\n" +
                        "#.................#\n" +
                        "#.###.#####.##.##.#\n" +
                        "#.....#...#.......#\n" +
                        "#####.#.A.#.#####.#\n" +
                        "#.......#.......R.#\n" +
                        "#.##.#######.####.#\n" +
                        "#.................#\n" +
                        "###################";

        // --- 3. Parsing the Level ---
        // Convert the String into a 2D array and find start positions
        String[] rows = levelStr.split("\n");
        int h = rows.length;
        int w = rows[0].length();
        int[][] visualBoard = new int[h][w];

        int startPx = 1, startPy = 1; // Default
        int startGx = 10, startGy = 10; // Default

        for (int y = 0; y < h; y++) {
            String row = rows[y];
            for (int x = 0; x < w; x++) {
                char c = (x < row.length()) ? row.charAt(x) : ' ';

                if (c == '#') {
                    visualBoard[y][x] = 1; // Wall
                } else if (c == '.') {
                    visualBoard[y][x] = 2; // Point
                } else if (c == 'A') {
                    visualBoard[y][x] = 3; // Apple
                } else if (c == 'M') {
                    visualBoard[y][x] = 0; // Empty floor
                    startPx = x;
                    startPy = y;
                } else if (c == 'R') {
                    visualBoard[y][x] = 0; // Empty floor
                    startGx = x;
                    startGy = y;
                } else {
                    visualBoard[y][x] = 0; // Empty
                }
            }
        }

        // --- 4. Logic Map Setup ---
        // Transpose for Map logic (x,y) vs Visual [row][col]
        int[][] logicalBoard = transpose(visualBoard);
        Map gameMap = new Map(logicalBoard);
        gameMap.setCyclic(false);

        // --- 5. Game Variables ---
        int pX = startPx;
        int pY = startPy;

        int ghostX = startGx;
        int ghostY = startGy;

        int score = 0;
        boolean isRunning = true;

        // --- 6. Init Display ---
        GameDisplay display = new GameDisplay();
        display.initCanvas(w, h);

        // --- 7. Game Loop ---
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
            int[] nextMove = bfsGetNextStep(gameMap, ghostX, ghostY, pX, pY);

            if (nextMove != null) {
                ghostX = nextMove[0];
                ghostY = nextMove[1];
            } else {
                // Fallback: Random move
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
                // Optional: Reset positions
                pX = startPx;
                pY = startPy;
                ghostX = startGx;
                ghostY = startGy;
            }

            // E. Rendering
            display.drawBoard(visualBoard, 1);
            display.drawPlayer(pX, pY, playerSkin, w, h);
            display.drawGhost(ghostX, ghostY, ghostSkin, w, h);

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
                    if (map.getPixel(nx, ny) != 1 && !visited[nx][ny]) {
                        visited[nx][ny] = true;
                        parent[nx][ny] = cx * 1000 + cy;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }

        if (!found) return null;

        int currX = targetX;
        int currY = targetY;

        while (true) {
            int pVal = parent[currX][currY];
            if (pVal == -1) return null;

            int pX = pVal / 1000;
            int pY = pVal % 1000;

            if (pX == startX && pY == startY) {
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