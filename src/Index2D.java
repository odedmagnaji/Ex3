package assignments.Ex3;

public class Index2D implements Pixel2D {
    private int _x, _y;

    public Index2D(int x, int y) {
        this._x = x;
        this._y = y;
    }

    public Index2D(Pixel2D p) {
        this(p.getX(), p.getY());
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        return 0;
    }

    @Override
    public boolean equals(Object t) {
        if (t == null || !(t instanceof Pixel2D)) {
            return false;
        }
        Pixel2D p = (Pixel2D) t;
        return this._x == p.getX() && this._y == p.getY();
    }

    @Override
    public String toString() {
        return getX() + "," + getY();
    }
}