package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;
import java.awt.Point;
import java.awt.Color;
import java.util.*;

/**
 * Level 4 Final Victory Implementation.
 * Strategy: Hard Safety Filters, Safe-Space Analysis, and 2-Step Memory.
 */
public class Ex3Algo implements PacManAlgo {

    private int step = 0;
    private int BLUE, PINK, GREEN;

    private int lastDir = -1;
    private Point lastPos = null;
    private Point lastPos2 = null;

    // Constants strictly tuned for Level 4 survival
    private static final int DANGER_RADIUS = 7;
    private static final int ESCAPE_TRIGGER = 4;
    private static final int SAFE_SPACE_LIMIT = 18;
    private static final int EDIBLE_TIME_BUFFER = 2;

    @Override
    public String getInfo() {
        return "Level 4 Victory: Hard Filters + Safe-Space BFS (Fixed Symbol Errors)";
    }

    @Override
    public int move(PacmanGame game) {
        int code = 0;
        int[][] board = game.getGame(code);
        int w = board.length, h = board[0].length;

        // Initialize tile colors
        if (step == 0) {
            BLUE  = Game.getIntColor(Color.BLUE, 0);
            PINK  = Game.getIntColor(Color.PINK, 0);
            GREEN = Game.getIntColor(Color.GREEN, 0);
        }

        Point me = extractPosition(game.getPos(code).toString());
        GhostCL[] ghosts = game.getGhosts(code);

        // 1. Danger Map: Minimum maze-distance from any dangerous ghost
        double[][] dangerMap = buildDangerMap(board, ghosts, BLUE);

        double curDanger = dangerMap[me.x][me.y];
        boolean powered = isPowered(ghosts);
        boolean escapeMode = (!powered && curDanger <= ESCAPE_TRIGGER);

        int bestDir = -1;
        double bestScore = Double.NEGATIVE_INFINITY;

        int[] dirs = {Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT};

        for (int dir : dirs) {
            Point next = getNeighbor(me, dir, w, h);
            if (!isLegal(next, board, BLUE)) continue;

            double nextDanger = dangerMap[next.x][next.y];

            // --- HARD SAFETY FILTERS ---
            if (!powered) {
                if (nextDanger <= 1) continue; // Direct contact prevention
                if (escapeMode && nextDanger <= 2) continue; // Aggressive survival
            }

            // --- EVALUATION ---
            double score = evaluate(next, board, dangerMap, ghosts, powered, PINK, GREEN, BLUE);

            // Stability: Prefer continuing current direction
            if (dir == lastDir) score += (escapeMode ? 10 : 120);

            // Reversal Penalty: Prevents back-and-forth "shaking"
            if (lastDir != -1 && dir == opposite(lastDir)) score -= (escapeMode ? 600 : 160);

            // ABAB Prevention: Penalty for returning to position from 2 steps ago
            if (lastPos2 != null && next.x == lastPos2.x && next.y == lastPos2.y) {
                score -= 500;
            }

            // Tactical Weights
            if (escapeMode) {
                score += nextDanger * 120000;
            } else {
                if (!powered && nextDanger < curDanger && curDanger <= DANGER_RADIUS) {
                    score -= (curDanger - nextDanger) * 35000;
                }
            }

            if (!powered && nextDanger < 6) score -= (6 - nextDanger) * 8000;

            if (score > bestScore) {
                bestScore = score;
                bestDir = dir;
            }
        }

        // Fallback: Always return a legal direction
        if (bestDir == -1) bestDir = firstLegalDir(me, board, w, h, BLUE);

        // Update Memory
        lastPos2 = lastPos;
        lastPos = me;
        lastDir = bestDir;
        step++;

        return bestDir;
    }

    private double evaluate(Point pos, int[][] board, double[][] danger, GhostCL[] ghosts, boolean powered, int pink, int green, int blue) {
        double score = 0;

        // 1) Safe Space (Anti-trap mobility)
        int safeSpace = countSafeSpace(pos, board, danger, SAFE_SPACE_LIMIT, blue);
        score += safeSpace * 900;

        // 2) Objective: Pink Pellets
        int[][] distMap = computeBFS(pos, board, blue);
        Point closestPink = findClosest(board, distMap, pink);
        if (closestPink == null) return 1e12; // Level cleared

        int dPink = distMap[closestPink.x][closestPink.y];
        score += 260000.0 / (dPink + 1);
        if (board[pos.x][pos.y] == pink) score += 140000;

        // 3) Secondary: Green power-ups
        if (!powered && danger[pos.x][pos.y] <= DANGER_RADIUS) {
            Point closestGreen = findClosest(board, distMap, green);
            if (closestGreen != null) {
                int dGreen = distMap[closestGreen.x][closestGreen.y];
                if (dGreen <= 4) {
                    score += 160000.0 / (dGreen + 1);
                    if (board[pos.x][pos.y] == green) score += 220000;
                }
            }
        }

        // 4) Pursuit: Powered Mode
        if (powered) {
            int dGhost = findNearestEdible(distMap, ghosts);
            if (dGhost != -1 && dGhost <= 8) score += 90000.0 / (dGhost + 1);
        }

        return score;
    }

    private int countSafeSpace(Point start, int[][] board, double[][] danger, int limit, int blue) {
        Queue<Point> q = new LinkedList<>();
        java.util.HashMap<Point, Integer> dists = new java.util.HashMap<>();
        q.add(start); dists.put(start, 0);
        int count = 0;
        int w = board.length, h = board[0].length;

        while (!q.isEmpty() && count < limit) {
            Point cur = q.poll();
            int d = dists.get(cur);
            count++;

            for (int dir : new int[]{Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT}) {
                Point n = getNeighbor(cur, dir, w, h);
                if (!isLegal(n, board, blue) || dists.containsKey(n)) continue;
                if (danger[n.x][n.y] <= d + 2) continue;
                dists.put(n, d + 1);
                q.add(n);
            }
        }
        return count;
    }

    private double[][] buildDangerMap(int[][] board, GhostCL[] ghosts, int blue) {
        int w = board.length, h = board[0].length;
        double[][] danger = new double[w][h];
        for (double[] r : danger) Arrays.fill(r, Double.POSITIVE_INFINITY);

        for (GhostCL g : ghosts) {
            if (g.getStatus() == 0) continue;
            double t = g.remainTimeAsEatable(0);
            if (t < 0 || t <= 2) {
                Point gp = extractPosition(g.getPos(0).toString());
                int[][] dMap = computeBFS(gp, board, blue);
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        if (dMap[x][y] != -1) danger[x][y] = Math.min(danger[x][y], (double)dMap[x][y]);
                    }
                }
            }
        }
        return danger;
    }

    private int[][] computeBFS(Point start, int[][] board, int blue) {
        int w = board.length, h = board[0].length;
        int[][] dists = new int[w][h];
        for (int[] r : dists) Arrays.fill(r, -1);
        Queue<Point> q = new LinkedList<>();
        q.add(start);
        dists[start.x][start.y] = 0;
        while (!q.isEmpty()) {
            Point curr = q.poll();
            for (int dir : new int[]{Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT}) {
                Point n = getNeighbor(curr, dir, w, h);
                if (isLegal(n, board, blue) && dists[n.x][n.y] == -1) {
                    dists[n.x][n.y] = dists[curr.x][curr.y] + 1;
                    q.add(n);
                }
            }
        }
        return dists;
    }

    private Point findClosest(int[][] board, int[][] distMap, int color) {
        Point best = null; int min = Integer.MAX_VALUE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == color && distMap[i][j] != -1 && distMap[i][j] < min) {
                    min = distMap[i][j]; best = new Point(i, j);
                }
            }
        }
        return best;
    }

    private int findNearestEdible(int[][] distMap, GhostCL[] ghosts) {
        int minD = Integer.MAX_VALUE;
        for (GhostCL g : ghosts) {
            if (g.getStatus() == 0 || g.remainTimeAsEatable(0) <= 0) continue;
            Point gp = extractPosition(g.getPos(0).toString());
            int d = distMap[gp.x][gp.y];
            if (d != -1 && d < minD) minD = d;
        }
        return (minD == Integer.MAX_VALUE) ? -1 : minD;
    }

    private Point getNeighbor(Point p, int dir, int w, int h) {
        int x = p.x, y = p.y;
        if (dir == Game.UP) y++; else if (dir == Game.DOWN) y--;
        else if (dir == Game.LEFT) x--; else if (dir == Game.RIGHT) x++;
        return new Point((x + w) % w, (y + h) % h);
    }

    private boolean isLegal(Point p, int[][] board, int blue) {
        return board[p.x][p.y] != blue && !isGhostHouse(p, board);
    }

    private boolean isGhostHouse(Point p, int[][] board) {
        int mx = board.length / 2, my = board[0].length / 2;
        return Math.abs(p.x - mx) < 4 && Math.abs(p.y - my) < 4 && board[p.x][p.y] == 0;
    }

    private boolean isPowered(GhostCL[] ghosts) {
        for (GhostCL g : ghosts) if (g.getStatus() != 0 && g.remainTimeAsEatable(0) > 0) return true;
        return false;
    }

    private int opposite(int dir) {
        if (dir == Game.UP) return Game.DOWN; if (dir == Game.DOWN) return Game.UP;
        if (dir == Game.LEFT) return Game.RIGHT; if (dir == Game.RIGHT) return Game.LEFT;
        return -1;
    }

    private int firstLegalDir(Point me, int[][] board, int w, int h, int blue) {
        for (int dir : new int[]{Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT}) {
            if (isLegal(getNeighbor(me, dir, w, h), board, blue)) return dir;
        }
        return Game.LEFT;
    }

    private Point extractPosition(String pos) {
        String[] parts = pos.trim().split(",");
        return new Point(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }
}