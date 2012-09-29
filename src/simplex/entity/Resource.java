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
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int RED = 2;
    public static final int BLUE = 3;
    public static final int GREEN = 4;
    public static final int PURPLE = 5;
    public static final int YELLOW = 6;
    public static final int TEAL = 7;
    public static final int ORANGE = 8;
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
        if (type == other.type || other.type == WHITE) {
            return;
        }

        if (type == BLACK || other.getType() == BLACK) {
            type = BLACK;
        } else if (type == WHITE) {
            type = other.type;
        } else if (type == RED) {
            switch (other.getType()) {
                case BLUE:
                    type = PURPLE;
                    break;
                case GREEN:
                    type = YELLOW;
                    break;
                case YELLOW:
                    type = ORANGE;
                    break;
                default:
                    type = BLACK;
                    break;
            }
        } else if(type == BLUE){
            switch (other.getType()) {
                case RED:
                    type = PURPLE;
                    break;
                case GREEN:
                    type = TEAL;
                    break;
                case YELLOW:
                    type = GREEN;
                    break;
                default:
                    type = BLACK;
                    break;
            }            
        } else if(type == GREEN){
            switch (other.getType()) {
                case RED:
                    type = YELLOW;
                    break;
                case BLUE:
                    type = TEAL;
                    break;
                default:
                    type = BLACK;
                    break;
            }
        } else{
            type = BLACK;
        }
        

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
