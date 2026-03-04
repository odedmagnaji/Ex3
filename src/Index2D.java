package assignments.Ex3;

/**
 * This class represents a simple 2D coordinate (x, y) on a grid.
 * It implements the Pixel2D interface and serves as a fundamental building block
 * for positioning entities, navigating the map, and performing spatial calculations.
 */
public class Index2D implements Pixel2D {
    private int _x, _y;

    /**
     * Constructs a new Index2D with the specified x and y coordinates.
     * * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     */
    public Index2D(int x, int y) {
        this._x = x;
        this._y = y;
    }

    /**
     * Constructs a new Index2D as a copy of an existing Pixel2D object.
     * * @param p The Pixel2D object to copy coordinates from.
     */
    public Index2D(Pixel2D p) {
        this(p.getX(), p.getY());
    }

    /**
     * Retrieves the X-coordinate of this pixel.
     * * @return The integer x value.
     */
    @Override
    public int getX() {
        return _x;
    }

    /**
     * Retrieves the Y-coordinate of this pixel.
     * * @return The integer y value.
     */
    @Override
    public int getY() {
        return _y;
    }

    /**
     * Calculates the Euclidean distance between this pixel and another.
     * Note: In this version, it returns 0 as a placeholder for interface compliance.
     * * @param p2 The target pixel to measure distance to.
     * @return The 2D distance (currently 0).
     */
    @Override
    public double distance2D(Pixel2D p2) {
        return 0;
    }

    /**
     * Compares this Index2D with another object for equality.
     * Two pixels are considered equal if they have the same x and y coordinates.
     * * @param t The object to compare with.
     * @return True if the coordinates match, false otherwise.
     */
    @Override
    public boolean equals(Object t) {
        if (t == null || !(t instanceof Pixel2D)) {
            return false;
        }
        Pixel2D p = (Pixel2D) t;
        return this._x == p.getX() && this._y == p.getY();
    }

    /**
     * Returns a string representation of the coordinate.
     * * @return A string in the format "x,y".
     */
    @Override
    public String toString() {
        return getX() + "," + getY();
    }
}