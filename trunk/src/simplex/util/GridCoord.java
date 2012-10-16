package simplex.util;

/**
 * A simple encapsulation of two integers that will represent grid coordinates.
 * @author Emil
 * @author Samuel
 */
public class GridCoord {
    
    public int x;
    public int y;
    
    public GridCoord() {}

    /**
     * Create a GridCoord with the coordinates x and y.
     * @param x Coordinate x.
     * @param y Coordinate y.
     */
    public GridCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.x;
        hash = 83 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GridCoord other = (GridCoord) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }    
}
