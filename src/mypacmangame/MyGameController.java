package assignments.Ex3.mypacmangame;

import assignments.Ex3.Map;
import assignments.Ex3.mypacmangame.display.GameDisplay;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class MyGameController {

    // Global Game State
    private static int score = 0;
    private static int lives = 3;
    private static int currentLevelIndex = 0;
    private static int pelletsLeft = 0;

    // Entities
    private static int pX, pY, startPx, startPy;
    private static int ghostX, ghostY, startGx, startGy;

    // Map & Display
    private static Map gameMap;
    private static int[][] visualBoard;
    private static GameDisplay display;
    private static String playerSkin = "morty.png";
    private static String ghostSkin = "rick.png";

    public static void main(String[] args) {

        // --- 1. Define Levels ---
        String[] levels = {
                // Level 1: Easy
                "###########\n" +
                        "#M.......R#\n" +
                        "#.#######.#\n" +
                        "#.........#\n" +
                        "###########",

                // Level 2: Medium
                "###################\n" +
                        "#M......#.........#\n" +
                        "#.##.##.#.##.####.#\n" +
                        "#.................#\n" +
                        "#####.#.R.#.#####.#\n" +
                        "#.......#.........#\n" +
                        "###################",

                // Level 3: Hard
                "###################\n" +
                        "#M#...#...#...#..R#\n" +
                        "#.#.#.#.#.#.#.#.#.#\n" +
                        "#.................#\n" +
                        "#.###.#####.###.###\n" +
                        "#.................#\n" +
                        "###################"
        };

        // --- 2. Initialize First Level ---
        loadLevel(levels[currentLevelIndex]);

        display = new GameDisplay();
        display.initCanvas(visualBoard[0].length, visualBoard.length);

        boolean isRunning = true;

        // --- 3. Main Game Loop ---
        while (isRunning) {
            // A. Input
            int dx = 0, dy = 0;
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    dy = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  dy = 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  dx = -1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;

            // B. Player Move
            if (gameMap.getPixel(pX + dx, pY + dy) != 1) {
                pX += dx;
                pY += dy;
            }

            // C. Ghost AI
            int[] nextMove = bfsGetNextStep(gameMap, ghostX, ghostY, pX, pY);
            if (nextMove != null) {
                ghostX = nextMove[0];
                ghostY = nextMove[1];
            } else {
                int gDx = 0, gDy = 0;
                if (Math.random() < 0.5) gDx = (Math.random() < 0.5) ? 1 : -1;
                else gDy = (Math.random() < 0.5) ? 1 : -1;
                if (gameMap.getPixel(ghostX + gDx, ghostY + gDy) != 1) {
                    ghostX += gDx;
                    ghostY += gDy;
                }
            }

            // D. Logic: Eating & Level Complete
            int currentCell = gameMap.getPixel(pX, pY);
            if (currentCell == 2 || currentCell == 3) {
                score++;
                pelletsLeft--;
                gameMap.setPixel(pX, pY, 0);
                visualBoard[pY][pX] = 0;
                System.out.println("Score: " + score + " | Left: " + pelletsLeft);
            }

            // CHECK WIN CONDITION
            if (pelletsLeft <= 0) {
                System.out.println("LEVEL COMPLETE!");
                currentLevelIndex++;

                if (currentLevelIndex < levels.length) {
                    loadLevel(levels[currentLevelIndex]);
                    display.initCanvas(visualBoard[0].length, visualBoard.length);

                    // Fixed: Using show instead of pause
                    StdDraw.show(1000);
                } else {
                    System.out.println("YOU WIN THE GAME!");
                    System.out.println("Final Score: " + score);
                    isRunning = false;
                }
            }

            // CHECK LOSS CONDITION
            if (pX == ghostX && pY == ghostY) {
                lives--;
                System.out.println("HIT! Lives left: " + lives);

                if (lives > 0) {
                    pX = startPx;
                    pY = startPy;
                    ghostX = startGx;
                    ghostY = startGy;

                    // Fixed: Using show instead of pause
                    StdDraw.show(1000);
                } else {
                    System.out.println("GAME OVER");
                    isRunning = false;
                }
            }

            // E. Render
            if (isRunning) {
                display.drawBoard(visualBoard, 1);
                display.drawPlayer(pX, pY, playerSkin, visualBoard[0].length, visualBoard.length);
                display.drawGhost(ghostX, ghostY, ghostSkin, visualBoard[0].length, visualBoard.length);

                StdDraw.setPenColor(255, 255, 255);
                StdDraw.textLeft(0.05, 0.95, "Lvl: " + (currentLevelIndex+1) + " | Score: " + score + " | Lives: " + lives);

                StdDraw.show(200);
            }
        }
    }

    private static void loadLevel(String levelStr) {
        String[] rows = levelStr.split("\n");
        int h = rows.length;
        int w = rows[0].length();
        visualBoard = new int[h][w];
        pelletsLeft = 0;

        for (int y = 0; y < h; y++) {
            String row = rows[y];
            for (int x = 0; x < w; x++) {
                char c = (x < row.length()) ? row.charAt(x) : ' ';

                if (c == '#') {
                    visualBoard[y][x] = 1;
                } else if (c == '.') {
                    visualBoard[y][x] = 2;
                    pelletsLeft++;
                } else if (c == 'A') {
                    visualBoard[y][x] = 3;
                    pelletsLeft++;
                } else if (c == 'M') {
                    visualBoard[y][x] = 0;
                    startPx = x; startPy = y;
                    pX = x; pY = y;
                } else if (c == 'R') {
                    visualBoard[y][x] = 0;
                    startGx = x; startGy = y;
                    ghostX = x; ghostY = y;
                } else {
                    visualBoard[y][x] = 0;
                }
            }
        }

        int[][] logicalBoard = transpose(visualBoard);
        gameMap = new Map(logicalBoard);
        gameMap.setCyclic(false);

        System.out.println("Level Loaded. Pellets: " + pelletsLeft);
    }

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