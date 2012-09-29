package simplex.entity;

import java.util.ArrayList;

/**
 * A class containing the resource data.
 *
 * @author Emil
 * @author Samuel
 */
public class Resource {

    public final static Resource NIL = new Resource();

    public static Resource parse(int type, int rate) {
        return new Resource(type, rate);
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

    private Resource() {
        this(0, 0);
    }

    private Resource(int type, int rate) {
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

    public static Resource combine(Resource r1, Resource r2) {
        Resource r = new Resource();
        r.rate = (r1.rate + r2.rate) / 2;

        if (r1.type == BLACK || r2.type == BLACK) { //TODO: Need better combine method
            r.type = BLACK;
        } else if (r1.type == WHITE) {
            r.type = r2.getType();
        } else if (r2.type == WHITE) {
            r.type = r1.type;
        } else {
            r.type = (r1.type + r2.type) / 2;
        }
        return r;
    }
    
    public static ArrayList<Resource> split(Resource resource) {
        ArrayList<Resource> list = new ArrayList<>(); //TODO: Not implemented
        list.add(resource);
        return list;        
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.type;
        hash = 47 * hash + this.rate;
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
        final Resource other = (Resource) obj;
        if (this.type != other.type) {
            return false;
        }
        if (this.rate != other.rate) {
            return false;
        }
        return true;
    }
    
    
}
