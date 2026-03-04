package assignments.Ex3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a raster matrix or maze over integers.
 * It provides core spatial algorithms including Breadth-First Search (BFS) for pathfinding,
 * distance mapping, and flood fill logic.
 * The map supports both bounded and cyclic (wrap-around) movement.
 * * @author boaz.benmoshe
 */
public class Map implements Map2D {
    private int[][] _map;
    private boolean _cyclicFlag = true;

    /**
     * Constructs a w*h 2D raster map initialized with a specific value.
     * * @param w The width of the map.
     * @param h The height of the map.
     * @param v The initial value for all pixels.
     */
    public Map(int w, int h, int v) {
        init(w,h, v);
    }

    /**
     * Constructs a square map (size * size) initialized with zeros.
     * * @param size The width and height of the map.
     */
    public Map(int size) {
        this(size,size, 0);
    }

    /**
     * Constructs a map by copying a given 2D integer array.
     * * @param data The 2D array to initialize the map with.
     */
    public Map(int[][] data) {
        init(data);
    }

    /**
     * Initializes the map with specific dimensions and a default value.
     * * @param w Width of the map.
     * @param h Height of the map.
     * @param v Initial value for all cells.
     * @throws RuntimeException if dimensions are invalid.
     */
    @Override
    public void init(int w, int h, int v) {
        if (w <= 0 || h <= 0)
            throw new RuntimeException("common.Map2D init error: wrong dimensions");

        this._map = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this._map[i][j] = v;
            }
        }
    }

    /**
     * Initializes the map using a provided 2D array.
     * Performs a deep copy of the input data.
     * * @param arr The source 2D array.
     * @throws RuntimeException if the input array is null or empty.
     */
    @Override
    public void init(int[][] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null)
            throw new RuntimeException("common.Map2D init error: null or empty array");

        this._map = new int[arr.length][arr[0].length];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                this._map[i][j] = arr[i][j];
            }
        }
    }

    /**
     * Returns a deep copy of the internal map matrix.
     * * @return A 2D integer array representation of the map.
     */
    @Override
    public int[][] getMap() {
        if (this._map == null)
            return null;

        int w = this.getWidth();
        int h = this.getHeight();

        int[][] ans = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                ans[i][j] = this._map[i][j];
            }
        }
        return ans;
    }

    /** @return The width of the map. */
    @Override
    public int getWidth() {
        return (_map == null || _map.length == 0) ? 0:_map.length ;
    }

    /** @return The height of the map. */
    @Override
    public int getHeight() {
        return (this._map == null || this._map.length == 0 || this._map[0] == null) ? 0 : this._map[0].length;
    }

    /**
     * Retrieves the value of a specific pixel.
     * * @param x X-coordinate.
     * @param y Y-coordinate.
     * @return The value at (x,y).
     * @throws RuntimeException if coordinates are out of bounds.
     */
    @Override
    public int getPixel(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            throw new RuntimeException("common.Map2D getPixel error: out of bounds");
        return _map[x][y];
    }

    /**
     * Retrieves the value of a specific pixel using a Pixel2D object.
     * * @param p The pixel location.
     * @return The value at the pixel location.
     */
    @Override
    public int getPixel(Pixel2D p) {
        return this.getPixel(p.getX(),p.getY());
    }

    /**
     * Sets the value of a specific pixel.
     * * @param x X-coordinate.
     * @param y Y-coordinate.
     * @param v The value to set.
     */
    @Override
    public void setPixel(int x, int y, int v) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
            this._map[x][y] = v;
    }

    /**
     * Sets the value of a specific pixel using a Pixel2D object.
     * * @param p The pixel location.
     * @param v The value to set.
     */
    @Override
    public void setPixel(Pixel2D p, int v) {
        if (p == null)
            throw new RuntimeException("common.Map2D setPixel error: null pixel");
        this.setPixel(p.getX(), p.getY(), v);
    }

    /**
     * Fills a connected area of the same color with a new color, starting from a given pixel.
     * Implements the Flood Fill algorithm using a Queue (BFS).
     * * @param xy The starting pixel.
     * @param new_v The new color value.
     * @return The number of pixels that were changed.
     */
    @Override
    public int fill(Pixel2D xy, int new_v) {
        int ans=0;
        int width = getWidth();
        int height = getHeight();
        int oldColor = getPixel(xy.getX(), xy.getY());

        if (oldColor == new_v)
            return ans;

        Queue<Pixel2D> q = new LinkedList<>();
        boolean[][] visited = new boolean[width][height];

        q.add(xy);
        visited[xy.getX()][xy.getY()] = true;

        int[] dx = { 1, -1, 0, 0 };
        int[] dy = { 0, 0, 1, -1 };

        while (!q.isEmpty()) {
            Pixel2D curr = q.poll();
            setPixel(curr.getX(), curr.getY(), new_v);
            ans++;

            for (int i = 0; i < 4; i++) {
                Pixel2D next = getNextPixel(curr, dx[i], dy[i], width, height, this._cyclicFlag);

                if (next != null) {
                    int nx = next.getX();
                    int ny = next.getY();

                    if (!visited[nx][ny] && getPixel(nx, ny) == oldColor) {
                        visited[nx][ny] = true;
                        q.add(next);
                    }
                }
            }
        }
        return ans;
    }

    /**
     * Finds the shortest path between two pixels avoiding obstacles.
     * Uses the class's current cyclic setting.
     * * @param p1 Starting pixel.
     * @param p2 Target pixel.
     * @param obsColor The value representing an obstacle (wall).
     * @return An array of Pixel2D objects representing the path, or null if no path exists.
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        return shortestPath(p1, p2, obsColor, this._cyclicFlag);
    }

    /**
     * Generates a distance map from a starting pixel to all reachable areas.
     * Uses the class's current cyclic setting.
     * * @param start The starting pixel.
     * @param obsColor The value representing an obstacle.
     * @return A Map2D object where each pixel contains its distance from the start.
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        return allDistance(start, obsColor, this._cyclicFlag);
    }

    /**
     * Full implementation of the shortest path algorithm using BFS.
     * * @param p1 Starting pixel.
     * @param p2 Target pixel.
     * @param obsColor Obstacle value.
     * @param cyclic Whether the map should behave as cyclic (wrap around).
     * @return The shortest path as an array.
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        Pixel2D[] ans = null;
        if(p1 == null || p2 == null) return null;

        int width = getWidth();
        int height = getHeight();

        if (getPixel(p1.getX(), p1.getY()) == obsColor || getPixel(p2.getX(), p2.getY()) == obsColor) {
            return null;
        }

        if (p1.equals(p2)) {
            return new Pixel2D[] { p1 };
        }

        Queue<Pixel2D> q = new LinkedList<>();
        q.add(p1);

        Pixel2D[][] parent = new Pixel2D[width][height];
        parent[p1.getX()][p1.getY()] = p1;

        int[] dx = { 1, -1, 0, 0 };
        int[] dy = { 0, 0, 1, -1 };

        while (!q.isEmpty()) {
            Pixel2D curr = q.poll();

            if (curr.equals(p2)) {
                ans = reconstructPath(p1, p2, parent);
                break;
            }

            for (int i = 0; i < 4; i++) {
                Pixel2D next = getNextPixel(curr, dx[i], dy[i], width, height, cyclic);

                if (next != null) {
                    int nx = next.getX();
                    int ny = next.getY();

                    if (parent[nx][ny] == null && getPixel(nx, ny) != obsColor) {
                        parent[nx][ny] = curr;
                        q.add(next);
                    }
                }
            }
        }
        return ans;
    }

    /**
     * Checks if a pixel is within the map boundaries.
     * * @param p The pixel to check.
     * @return True if inside, false otherwise.
     */
    @Override
    public boolean isInside(Pixel2D p) {
        return !(p == null || p.getX() < 0 || p.getX() >= this.getWidth() || p.getY() < 0 || p.getY() >= this.getHeight());
    }

    /** @return True if the map is currently cyclic. */
    @Override
    public boolean isCyclic() {
        return this._cyclicFlag;
    }

    /** @param cy Sets whether the map should be cyclic. */
    @Override
    public void setCyclic(boolean cy) {
        this._cyclicFlag = cy;
    }

    /**
     * Full implementation of the distance mapping algorithm using BFS.
     * * @param start Starting pixel.
     * @param obsColor Obstacle value.
     * @param cyclic Whether the map is cyclic.
     * @return A map of distances.
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        Map2D ans = null;
        int width = getWidth();
        int height = getHeight();

        ans = new Map(width, height, -1);

        if (getPixel(start.getX(), start.getY()) == obsColor) {
            return ans;
        }

        Queue<Pixel2D> q = new LinkedList<>();
        boolean[][] visited = new boolean[width][height];

        q.add(start);
        ans.setPixel(start, 0);
        visited[start.getX()][start.getY()] = true;

        int[] dx = { 1, -1, 0, 0 };
        int[] dy = { 0, 0, 1, -1 };

        while (!q.isEmpty()) {
            Pixel2D curr = q.poll();
            int currDistance = ans.getPixel(curr);

            for (int i = 0; i < 4; i++) {
                Pixel2D next = getNextPixel(curr, dx[i], dy[i], width, height, cyclic);

                if (next != null) {
                    int nx = next.getX();
                    int ny = next.getY();

                    if (!visited[nx][ny] && getPixel(nx, ny) != obsColor) {
                        ans.setPixel(nx, ny, currDistance + 1);
                        visited[nx][ny] = true;
                        q.add(next);
                    }
                }
            }
        }
        return ans;
    }

    ////////////////////// Private Methods ///////////////////////

    /**
     * Calculates the next pixel based on direction and cyclic rules.
     */
    private Pixel2D getNextPixel(Pixel2D curr, int dx, int dy, int width, int height, boolean cyclic) {
        int nx = curr.getX() + dx;
        int ny = curr.getY() + dy;

        if (cyclic) {
            nx = (nx + width) % width;
            ny = (ny + height) % height;
        }
        else {
            if (nx < 0 || nx >= width || ny < 0 || ny >= height) {
                return null;
            }
        }
        return new Index2D(nx, ny);
    }

    /**
     * Backtracks from target to start using the parent matrix to build the path array.
     */
    private Pixel2D[] reconstructPath(Pixel2D p1, Pixel2D p2, Pixel2D[][] parent) {
        LinkedList<Pixel2D> pathList = new LinkedList<>();
        Pixel2D curr = p2;

        while (curr != null && !curr.equals(p1)) {
            pathList.addFirst(curr);
            curr = parent[curr.getX()][curr.getY()];
        }
        pathList.addFirst(p1);
        return pathList.toArray(new Pixel2D[0]);
    }
}