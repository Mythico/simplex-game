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
        int rate = data2;
        switch (data1) {
            case 1:
                return new Resource(Color.red, rate);
            case 2:
                return new Resource(Color.green, rate);
            case 3:
                return new Resource(Color.blue, rate);
        }
        return new Resource(Color.white, rate);

    }
    private Color type;
    private int rate;

    public Resource() {
        this(Color.black, 0);
    }

    public Resource(Color type, int rate) {
        this.type = type;
        this.rate = rate;
    }

    public Color getType() {
        return type;
    }

    public void setType(Color type) {
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
        getType().add(other.getType());
        getType().scale(0.5f);

    }

}
