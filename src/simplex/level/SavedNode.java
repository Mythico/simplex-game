package simplex.level;

import simplex.entity.Node;

/**
 *
 * @author Emil
 * @author Samuel
 */
public abstract class SavedNode {

    private int id;
    private int x;
    private int y;

    public SavedNode() {
    }

    SavedNode(int x, int y, int id) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public abstract Node create();
    
}
