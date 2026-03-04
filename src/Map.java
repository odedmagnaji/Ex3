package assignments.Ex3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
    private int[][] _map;
    private boolean _cyclicFlag = true;

    /**
     * Constructs a w*h 2D raster map with an init value v.
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w,h, v);
    }
    /**
     * Constructs a square map (size*size).
     * @param size
     */
    public Map(int size) {
        this(size,size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }

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

    @Override
    public int getWidth() {
        return (_map == null || _map.length == 0) ? 0:_map.length ;
    }

    @Override
    public int getHeight() {
        return (this._map == null || this._map.length == 0 || this._map[0] == null) ? 0 : this._map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            throw new RuntimeException("common.Map2D getPixel error: out of bounds");
        return _map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        return this.getPixel(p.getX(),p.getY());
    }

    @Override
    public void setPixel(int x, int y, int v) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
            this._map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        if (p == null)
            throw new RuntimeException("common.Map2D setPixel error: null pixel");
        this.setPixel(p.getX(), p.getY(), v);
    }

    @Override
    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     */
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

    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        // FIXED: Delegate to the full method using the class's cyclic flag
        return shortestPath(p1, p2, obsColor, this._cyclicFlag);
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        // FIXED: Delegate to the full method using the class's cyclic flag
        return allDistance(start, obsColor, this._cyclicFlag);
    }

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

    @Override
    public boolean isInside(Pixel2D p) {
        return !(p == null || p.getX() < 0 || p.getX() >= this.getWidth() || p.getY() < 0 || p.getY() >= this.getHeight());
    }

    @Override
    public boolean isCyclic() {
        return this._cyclicFlag;
    }

    @Override
    public void setCyclic(boolean cy) {
        this._cyclicFlag = cy;
    }

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