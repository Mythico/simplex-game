package simplex.entity;

/**
 * A class containing the resource data.
 *
 * @author Emil
 * @author Samuel
 */
public class Resource {

    public final static Resource NIL = new NilResource();

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

    protected Resource() {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.type;
        hash = 31 * hash + this.rate;
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

    public Resource copy() {
        return new Resource(type, rate);
    }
}

final class NilResource extends Resource {   
    
    /**
     * Can't copy the nil resource.
     * @return 
     */
    @Override
    public Resource copy() {
        return this;
    }

    @Override
    public int getType() {
        return WHITE;
    }

    @Override
    public int getRate() {
        return 0;
    }
}
