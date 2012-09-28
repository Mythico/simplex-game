package simplex.entity;

import org.newdawn.slick.Color;

/**
 * A class containing the resource data.
 *
 * @author Emil
 * @author Samuel
 */
public class Resource {

    public static Resource parse(int data1, int data2) {
        return new Resource(data1, data2);
    }
    private int type;
    private int rate;

    public Resource() {
        this(0, 0);
    }

    public Resource(int type, int rate) {
        this.type = type;
        this.rate = rate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void add(Resource other) {
        rate += other.rate;
        //TODO: Add type addition.

    }

    public Color getColorType() {        
        switch (type) {
            case 1:
                return Color.red;
            case 2:
                return Color.green;
            case 3:
                return Color.blue;
        }
        return Color.white;
    }
}
