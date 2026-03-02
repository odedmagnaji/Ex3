package assignments.Ex3.mypacmangame;

import assignments.Ex3.Map;
import assignments.Ex3.mypacmangame.display.GameDisplay;
import assignments.Ex3.mypacmangame.entities.GhostEntity;
import exe.ex3.game.StdDraw;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

public class MyGameController {

    private static int score = 0;
    private static int lives = 3;
    private static int currentLevelIndex = 0;
    private static int pelletsLeft = 0;
    private static boolean isPaused = false;
    private static int frameCount = 0;

    private static int pX, pY, startPx, startPy;
    private static List<GhostEntity> activeGhosts = new ArrayList<>();
    private static int ghostHouseX, ghostHouseY;

    private static long powerUpEndTime = 0;
    private static final int POWER_UP_DURATION = 8000;

    private static Map gameMap;
    private static int[][] visualBoard;
    private static GameDisplay display;
    private static String playerSkin = "morty.png";
    private static String ghostSkin = "rick.png";
    private static Random random = new Random();

    private static final String TITLE_IMG = "title.png";
    private static final String GAMEOVER_IMG = "gameover.png";

    public static void main(String[] args) {
        String[] levels = {
                "###########\n#M........#\n#.#######.#\n#... R ...#\n###########",
                "###############\n#M............#\n#.###.###.###.#\n#..... R .....#\n#.###.###.###.#\n###############",
                "###################\n#M................#\n#.#####.###.#####.#\n#....... R .......#\n#.#####.###.#####.#\n#.................#\n###################",
                "#####################\n#M..................#\n#...................#\n#...###### ######...#\n#...#           #...#\n#...#     R     #...#\n#...#           #...#\n#...###### ######...#\n#...................#\n#...................#\n#####################"
        };

        display = new GameDisplay();
        loadLevel(levels[currentLevelIndex]);

        // אתחול הקנבס והגדרת סקאלה מנורמלת (0.0 עד 1.0)
        // שימוש ב-3 פרמטרים (min, max, margin) למניעת שגיאות קומפילציה
        display.initCanvas(visualBoard[0].length, visualBoard.length);
        StdDraw.setXscale(0.0, 1.0, 0);
        StdDraw.setYscale(0.0, 1.0, 0);

        showStartScreen();

        boolean isRunning = true;
        while (isRunning) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_P)) {
                isPaused = !isPaused;
                StdDraw.show(300);
            }

            if (!isPaused) {
                frameCount++;
                boolean isPowerUpActive = System.currentTimeMillis() < powerUpEndTime;

                handlePacmanInput();

                // תפוחים מהירים: כל 30 פריימים
                if (frameCount % 30 == 0) { spawnRandomApple(); }

                int speedDelay = Math.max(2, 5 - currentLevelIndex);
                if (frameCount % speedDelay == 0) { moveGhosts(isPowerUpActive); }

                int maxGhosts = currentLevelIndex + 3;
                if (frameCount % 150 == 0 && activeGhosts.size() < maxGhosts) { spawnGhost(); }

                handleEating();

                if (checkCollisions(isPowerUpActive)) {
                    if (lives <= 0) {
                        showFinalScreen(GAMEOVER_IMG, "GAME OVER");
                        isRunning = false;
                    } else {
                        resetPositions();
                        waitForSpace("HIT! PRESS SPACE");
                    }
                }

                if (pelletsLeft <= 0) { handleWinCondition(levels); }
            }

            if (isRunning) {
                drawGameScene();
                if (isPaused) {
                    StdDraw.setPenColor(255, 255, 0);
                    // טקסט עם 4 פרמטרים (האחרון הוא זווית 0.0)
                    StdDraw.text(0.5, 0.5, "PAUSED", 0.0);
                }
                StdDraw.show(80);
            }
        }
    }

    private static void spawnRandomApple() {
        int h = visualBoard.length, w = visualBoard[0].length;
        for (int i = 0; i < 20; i++) {
            int ry = random.nextInt(h), rx = random.nextInt(w);
            if (visualBoard[ry][rx] == 0 && (rx != pX || ry != pY)) {
                visualBoard[ry][rx] = 3;
                gameMap.setPixel(rx, ry, 3);
                break;
            }
        }
    }

    private static void handlePacmanInput() {
        int dx = 0, dy = 0;
        if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) dy = -1;
        else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) dy = 1;
        else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) dx = -1;
        else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) dx = 1;
        if (gameMap.getPixel(pX + dx, pY + dy) != 1) { pX += dx; pY += dy; }
    }

    private static void moveGhosts(boolean isPowerUpActive) {
        for (GhostEntity g : activeGhosts) {
            int targetX = pX, targetY = pY;
            if (isPowerUpActive && g.getStatus() == 1) {
                targetX = ghostHouseX; targetY = ghostHouseY;
            }
            int[] next = bfsGetNextStep(gameMap, g.getX(), g.getY(), targetX, targetY);
            if (next != null) { g.setPosition(next[0] + "," + next[1]); }
        }
    }

    private static void handleEating() {
        int cell = gameMap.getPixel(pX, pY);
        if (cell == 2 || cell == 3) {
            gameMap.setPixel(pX, pY, 0);
            visualBoard[pY][pX] = 0;
            if (cell == 2) { score += 10; pelletsLeft--; }
            else {
                score += 50;
                powerUpEndTime = System.currentTimeMillis() + POWER_UP_DURATION;
                for (GhostEntity g : activeGhosts) g.setStatus(1);
            }
        }
    }

    private static boolean checkCollisions(boolean isPowerUpActive) {
        for (int i = activeGhosts.size() - 1; i >= 0; i--) {
            GhostEntity g = activeGhosts.get(i);
            if (g.getX() == pX && g.getY() == pY) {
                if (isPowerUpActive && g.getStatus() == 1) {
                    activeGhosts.remove(i);
                    score += 200;
                    return false;
                } else {
                    lives--;
                    return true;
                }
            }
        }
        return false;
    }

    private static void loadLevel(String levelStr) {
        String[] rows = levelStr.split("\n");
        int h = rows.length, w = rows[0].length();
        visualBoard = new int[h][w];
        pelletsLeft = 0;
        activeGhosts.clear();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                char c = rows[y].charAt(x);
                if (c == '#') visualBoard[y][x] = 1;
                else if (c == '.') { visualBoard[y][x] = 2; pelletsLeft++; }
                else if (c == 'A') { visualBoard[y][x] = 3; }
                else if (c == 'M') { visualBoard[y][x] = 0; startPx=x; startPy=y; pX=x; pY=y; }
                else if (c == 'R') { visualBoard[y][x] = 0; ghostHouseX = x; ghostHouseY = y; }
                else visualBoard[y][x] = 0;
            }
        }
        gameMap = new Map(transpose(visualBoard));

        spawnGhost();
        if (currentLevelIndex >= 2) spawnGhost();
        if (currentLevelIndex >= 3) spawnGhost();
    }

    private static void spawnGhost() {
        activeGhosts.add(new GhostEntity(ghostHouseX + "," + ghostHouseY, 0, 1));
    }

    private static void resetPositions() {
        pX = startPx; pY = startPy;
        for (GhostEntity g : activeGhosts) {
            g.setPosition(ghostHouseX + "," + ghostHouseY);
            g.setStatus(0);
        }
    }

    private static void handleWinCondition(String[] levels) {
        currentLevelIndex++;
        if (currentLevelIndex < levels.length) {
            loadLevel(levels[currentLevelIndex]);
            waitForSpace("LEVEL COMPLETE!");
        } else {
            showFinalScreen(TITLE_IMG, "YOU WIN!");
            System.exit(0);
        }
    }

    private static void drawGameScene() {
        // רקע שחור מלא (0-1)
        StdDraw.setPenColor(0, 0, 0);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        // ציור הלוח והשחקן
        display.drawBoard(visualBoard, 1);
        display.drawPlayer(pX, pY, playerSkin, visualBoard[0].length, visualBoard.length);

        long now = System.currentTimeMillis();
        int boardW = visualBoard[0].length;
        int boardH = visualBoard.length;

        for (GhostEntity g : activeGhosts) {
            String currentSkin = ghostSkin;
            if (now < powerUpEndTime && g.getStatus() == 1) {
                currentSkin = ghostSkin.equals("rick.png") ? "rick2.png" : "greenghost.png";

                // ציור הילה - חישוב מנורמל (0-1)
                double drawX = (g.getX() + 0.5) / boardW;
                double drawY = (boardH - 1 - g.getY() + 0.5) / boardH;
                double halfW = (0.5 / boardW) * 0.9;
                double halfH = (0.5 / boardH) * 0.9;

                StdDraw.setPenColor(0, 255, 255);
                // שימוש ב-filledRectangle עם 4 פרמטרים (ללא זווית)
                StdDraw.filledRectangle(drawX, drawY, halfW, halfH);

            } else if (now >= powerUpEndTime) {
                g.setStatus(0);
            }
            display.drawGhost(g.getX(), g.getY(), currentSkin, boardW, boardH);
        }

        StdDraw.setPenColor(255, 255, 0);
        StdDraw.text(0.15, 0.95, "SCORE: " + score, 0.0);

        StdDraw.setPenColor(255, 0, 0);
        StdDraw.text(0.85, 0.95, "LIVES: " + lives, 0.0);
    }

    private static void showStartScreen() {
        while (true) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) break;
            if (StdDraw.isKeyPressed(KeyEvent.VK_1)) playerSkin = "morty.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_2)) playerSkin = "pacman.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_3)) playerSkin = "pikachu.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_4)) ghostSkin = "rick.png";
            if (StdDraw.isKeyPressed(KeyEvent.VK_5)) ghostSkin = "ghost.png";

            StdDraw.setPenColor(0, 0, 0);
            StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

            if (new File(TITLE_IMG).exists()) {
                try {
                    // תיקון: שימוש ב-5 פרמטרים (ללא זווית אם זה נכשל בקומפילציה, אבל נראה שהספרייה תומכת ב-5)
                    // אם זה יקרוס שוב, נשתמש ב-3 פרמטרים
                    StdDraw.picture(0.5, 0.75, TITLE_IMG, 0.6, 0.3);
                } catch(Exception e) {
                    // Fallback
                }
            }

            StdDraw.setPenColor(255, 255, 255);
            StdDraw.text(0.5, 0.50, "PLAYER: " + playerSkin.replace(".png", ""), 0.0);
            StdDraw.text(0.5, 0.42, "GHOST: " + ghostSkin.replace(".png", ""), 0.0);

            StdDraw.setPenColor(255, 255, 0);
            StdDraw.text(0.5, 0.20, "PRESS SPACE TO START", 0.0);

            StdDraw.show(50);
        }
    }

    private static void showFinalScreen(String imgPath, String msg) {
        StdDraw.setPenColor(0, 0, 0);
        StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);

        if (new File(imgPath).exists()) {
            try {
                StdDraw.picture(0.5, 0.5, imgPath, 0.7, 0.5);
            } catch(Exception e) {}
        }

        StdDraw.setPenColor(255, 255, 255);
        StdDraw.text(0.5, 0.2, msg + " | SCORE: " + score, 0.0);
        StdDraw.show(5000);
    }

    private static void waitForSpace(String message) {
        while(StdDraw.hasNextKeyTyped()) { StdDraw.nextKeyTyped(); }
        while (!StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
            drawGameScene();

            StdDraw.setPenColor(0, 0, 0);
            StdDraw.filledRectangle(0.5, 0.5, 0.4, 0.08);

            StdDraw.setPenColor(255, 255, 0);
            StdDraw.text(0.5, 0.5, message, 0.0);

            StdDraw.show(50);
        }
        StdDraw.show(300);
    }

    public static int[][] transpose(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int[][] res = new int[cols][rows];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[j][i] = matrix[i][j];
        return res;
    }

    public static int[] bfsGetNextStep(Map map, int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) return null;
        int width = map.getWidth(), height = map.getHeight();
        boolean[][] visited = new boolean[width][height];
        int[][] parent = new int[width][height];
        for(int i=0; i<width; i++) for(int j=0; j<height; j++) parent[i][j] = -1;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            if (curr[0] == targetX && curr[1] == targetY) {
                int cx = targetX, cy = targetY;
                while (true) {
                    int pVal = parent[cx][cy];
                    if (pVal == -1) return null;
                    int px = pVal / 1000, py = pVal % 1000;
                    if (px == startX && py == startY) return new int[]{cx, cy};
                    cx = px; cy = py;
                }
            }
            int[][] dirs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            for (int[] d : dirs) {
                int nx = curr[0] + d[0], ny = curr[1] + d[1];
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && map.getPixel(nx, ny) != 1 && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parent[nx][ny] = curr[0] * 1000 + curr[1];
                    q.add(new int[]{nx, ny});
                }
            }
        }
        return null;
    }
}