package assignments.Ex3.mypacmangame;

import assignments.Ex3.Map;
import assignments.Ex3.mypacmangame.display.GameDisplay;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;

public class MyGameController {

    // --- Game State ---
    private static int score = 0;
    private static int lives = 3;
    private static int currentLevelIndex = 0;
    private static int pelletsLeft = 0;
    private static boolean isPaused = false;
    private static boolean gameStarted = false;

    // --- Entities ---
    private static int pX, pY, startPx, startPy;
    private static int ghostX, ghostY, startGx, startGy;

    // --- Assets & Graphics ---
    private static Map gameMap;
    private static int[][] visualBoard;
    private static GameDisplay display;

    // Skins (File names)
    private static String playerSkin = "morty.png";
    private static String ghostSkin = "rick.png";

    // UI Image Constants
    private static final String HEART_IMG = "heart.png";
    private static final String TITLE_IMG = "title.png";
    private static final String GAMEOVER_IMG = "gameover.png";

    // Sound Constants
    private static final String SOUND_EAT = "eat.wav";
    private static final String SOUND_HIT = "hit.wav";
    private static final String SOUND_WIN = "win.wav";

    public static void main(String[] args) {

        String[] levels = {
                "###########\n" +
                        "#M.......R#\n" +
                        "#.#######.#\n" +
                        "#.........#\n" +
                        "###########",

                "###################\n" +
                        "#M......#.........#\n" +
                        "#.##.##.#.##.####.#\n" +
                        "#.................#\n" +
                        "#####.#.R.#.#####.#\n" +
                        "#.......#.........#\n" +
                        "###################",

                "###################\n" +
                        "#M#...#...#...#..R#\n" +
                        "#.#.#.#.#.#.#.#.#.#\n" +
                        "#.................#\n" +
                        "#.###.#####.###.###\n" +
                        "#.................#\n" +
                        "###################"
        };

        // Initialize display and data
        loadLevel(levels[0]);
        display = new GameDisplay();
        display.initCanvas(visualBoard[0].length, visualBoard.length);

        // 1. Show Menu and let user customize skins
        chooseSkinsMenu();
        gameStarted = true;

        boolean isRunning = true;

        // 2. Main Game Loop
        while (isRunning) {

            // Toggle Pause with SPACE
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                isPaused = !isPaused;
                StdDraw.show(300); // Prevent rapid toggling
            }

            if (!isPaused && gameStarted) {
                // Movement Input
                int dx = 0, dy = 0;
                if (StdDraw.isKeyPressed(KeyEvent.VK_UP))    dy = -1;
                if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))  dy = 1;
                if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))  dx = -1;
                if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;

                // Move Player
                if (gameMap.getPixel(pX + dx, pY + dy) != 1) {
                    pX += dx;
                    pY += dy;
                }

                // AI Pathfinding
                int[] nextMove = bfsGetNextStep(gameMap, ghostX, ghostY, pX, pY);
                if (nextMove != null) {
                    ghostX = nextMove[0];
                    ghostY = nextMove[1];
                }

                // Check Eating
                int cell = gameMap.getPixel(pX, pY);
                if (cell == 2 || cell == 3) {
                    score++;
                    pelletsLeft--;
                    gameMap.setPixel(pX, pY, 0);
                    visualBoard[pY][pX] = 0;
                    AudioPlayer.playSound(SOUND_EAT);
                }

                // Win logic
                if (pelletsLeft <= 0) {
                    AudioPlayer.playSound(SOUND_WIN);
                    currentLevelIndex++;
                    if (currentLevelIndex < levels.length) {
                        loadLevel(levels[currentLevelIndex]);
                        display.initCanvas(visualBoard[0].length, visualBoard.length);
                        waitForSpace("LEVEL COMPLETE!");
                    } else {
                        showFinalScreen(TITLE_IMG, "YOU WIN! SCORE: " + score);
                        isRunning = false;
                    }
                }

                // Collision logic
                if (pX == ghostX && pY == ghostY) {
                    lives--;
                    AudioPlayer.playSound(SOUND_HIT);
                    drawGameScene(); // Show overlap before reset
                    StdDraw.show(600);
                    if (lives > 0) {
                        pX = startPx; pY = startPy;
                        ghostX = startGx; ghostY = startGy;
                        waitForSpace("HIT! PRESS SPACE");
                    } else {
                        showFinalScreen(GAMEOVER_IMG, "GAME OVER");
                        isRunning = false;
                    }
                }
            }

            // Always render current state
            if (isRunning) {
                drawGameScene();
                if (isPaused) {
                    StdDraw.setPenColor(255, 255, 0);
                    StdDraw.text(visualBoard[0].length / 2.0, visualBoard.length / 2.0, "PAUSED", 0);
                }
                StdDraw.show(120);
            }
        }
    }

    /**
     * Start screen using normalized coordinates (0.0 to 1.0)
     * for skin selection and visibility.
     */
    private static void chooseSkinsMenu() {
        while (true) {
            // Exit menu with SPACE
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) break;

            // Player Skin Selection (1, 2, 3)
            if (StdDraw.isKeyPressed(KeyEvent.VK_1)) playerSkin = "morty.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_2)) playerSkin = "pacman.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_3)) playerSkin = "pikachu.png";

            // Ghost Skin Selection (4, 5)
            if (StdDraw.isKeyPressed(KeyEvent.VK_4)) ghostSkin = "rick.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_5)) ghostSkin = "ghost.png";

            // Draw Background (Absolute black)
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.filledRectangle(0.5, 0.5, 1.0, 1.0);

            // Draw Title Image using relative coords
            if (new File(TITLE_IMG).exists()) {
                StdDraw.picture(0.5, 0.75, TITLE_IMG, 0.6, 0.35);
            }

            // Display current configuration
            StdDraw.setPenColor(0, 255, 255);
            StdDraw.text(0.5, 0.55, "PLAYER (1:Morty, 2:Pacman, 3:Pikachu)", 0);
            StdDraw.text(0.5, 0.51, "Selected: " + playerSkin.replace(".png", ""), 0);

            StdDraw.setPenColor(255, 255, 255);
            StdDraw.text(0.5, 0.42, "GHOST (4:Rick, 5:Ghost)", 0);
            StdDraw.text(0.5, 0.38, "Selected: " + ghostSkin.replace(".png", ""), 0);

            // Flashing Prompt
            if ((System.currentTimeMillis() / 500) % 2 == 0) {
                StdDraw.setPenColor(255, 255, 0);
                StdDraw.text(0.5, 0.2, "PRESS SPACE TO START", 0);
            }

            StdDraw.show(50);
        }
        StdDraw.show(300); // Initial delay
    }

    private static void drawGameScene() {
        // Draw maze and characters
        display.drawBoard(visualBoard, 1);
        display.drawPlayer(pX, pY, playerSkin, visualBoard[0].length, visualBoard.length);
        display.drawGhost(ghostX, ghostY, ghostSkin, visualBoard[0].length, visualBoard.length);

        double w = visualBoard[0].length;
        double h = visualBoard.length;

        // UI Header: Score at Top Left
        StdDraw.setPenColor(255, 255, 0);
        StdDraw.text(w * 0.15, h - 0.5, "SCORE: " + score, 0);

        // UI Header: HEARTS representation at Top Right
        if (new File(HEART_IMG).exists()) {
            for (int i = 0; i < lives; i++) {
                // Place hearts in a row starting from the right
                StdDraw.picture(w - 1.0 - i, h - 0.5, HEART_IMG, 0.7, 0.7);
            }
        } else {
            // Text fallback if image is missing
            StdDraw.setPenColor(255, 0, 0);
            StdDraw.text(w * 0.85, h - 0.5, "LIVES: " + lives, 0);
        }
    }

    private static void showFinalScreen(String imgPath, String msg) {
        StdDraw.setPenColor(0, 0, 0);
        StdDraw.filledRectangle(0.5, 0.5, 1.0, 1.0);
        if (new File(imgPath).exists()) {
            StdDraw.picture(0.5, 0.6, imgPath, 0.7, 0.5);
        }
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.text(0.5, 0.3, msg, 0);
        StdDraw.show(5000);
    }

    private static void waitForSpace(String message) {
        while(StdDraw.hasNextKeyTyped()) { StdDraw.nextKeyTyped(); }
        while (!StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
            drawGameScene();
            double midX = visualBoard[0].length / 2.0;
            double midY = visualBoard.length / 2.0;
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.filledRectangle(midX, midY, visualBoard[0].length / 4.0, 0.8);
            StdDraw.setPenColor(255, 255, 0);
            StdDraw.text(midX, midY, message, 0);
            StdDraw.show(50);
        }
        StdDraw.show(300);
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
                if (c == '#') visualBoard[y][x] = 1;
                else if (c == '.') { visualBoard[y][x] = 2; pelletsLeft++; }
                else if (c == 'A') { visualBoard[y][x] = 3; pelletsLeft++; }
                else if (c == 'M') { visualBoard[y][x] = 0; startPx=x; startPy=y; pX=x; pY=y; }
                else if (c == 'R') { visualBoard[y][x] = 0; startGx=x; startGy=y; ghostX=x; ghostY=y; }
                else visualBoard[y][x] = 0;
            }
        }
        int[][] logicalBoard = transpose(visualBoard);
        gameMap = new Map(logicalBoard);
        gameMap.setCyclic(false);
    }

    public static int[] bfsGetNextStep(Map map, int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) return null;
        int width = map.getWidth();
        int height = map.getHeight();
        boolean[][] visited = new boolean[width][height];
        int[][] parent = new int[width][height];
        for(int i=0; i<width; i++) for(int j=0; j<height; j++) parent[i][j] = -1;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        boolean found = false;
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0]; int cy = curr[1];
            if (cx == targetX && cy == targetY) { found = true; break; }
            int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            for (int[] d : dirs) {
                int nx = cx + d[0]; int ny = cy + d[1];
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
        int currX = targetX; int currY = targetY;
        while (true) {
            int pVal = parent[currX][currY];
            if (pVal == -1) return null;
            int pX = pVal / 1000; int pY = pVal % 1000;
            if (pX == startX && pY == startY) return new int[]{currX, currY};
            currX = pX; currY = pY;
        }
    }

    public static int[][] transpose(int[][] matrix) {
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