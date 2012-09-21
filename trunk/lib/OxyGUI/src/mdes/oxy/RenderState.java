package mdes.oxy;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class RenderState {
    
    public static final int TOP = 2;
    public static final int LEFT = 4;
    public static final int BOTTOM = 8;
    public static final int RIGHT = 16;
    public static final int CENTER = 32;
    public static final String DEFAULT_TYPE = "default";
    
    String type;
    private RenderPart[] parts = null;
    private RenderPart center;
    private boolean noBorder = false;
    private boolean onlyHoriz = false;
    private boolean onlyVert = false;
    Image sheet = null;

    public RenderState() {
        this.type = DEFAULT_TYPE;
    }

    public RenderState(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public RenderState copy(String newType) {
        RenderState r = new RenderState(newType);
        r.parts = this.parts;
        r.center = this.center;
        r.noBorder = this.noBorder;
        r.onlyHoriz = this.onlyHoriz;
        r.onlyVert = this.onlyVert;
        return r;
    }

    public void simple(float x1, float y1, float x2, float y2, float corner) {
        float w = x2-x1;
        float h = y2-y1;
        if (parts==null)
            parts = new RenderPart[9];
        noBorder = false;
        parts[0] = new RenderPart(x1, y1, x1+corner, y1+corner);
        parts[1] = new RenderPart(x1+corner, y1, x2-corner, y1+corner);
        parts[2] = new RenderPart(x2-corner, y1, x2-corner, y1+corner);
        parts[3] = new RenderPart(x1, y1+corner, x1+corner, y2-corner);
        parts[4] = new RenderPart(x1+corner, y1+corner, x2-corner, y2-corner);
        parts[5] = new RenderPart(x2-corner, y1+corner, x2, y2-corner);
        parts[6] = new RenderPart(x1, y2-corner, x1+corner, y2);
        parts[7] = new RenderPart(x1+corner, y2-corner, x2-corner, y2-corner);
        parts[8] = new RenderPart(x2-corner, y2-corner, x2, y2);
    }

    //  0   1   2
    //  3   4   5
    //  6   7   8

    public void put(int align, RenderPart part) {
        if (align==CENTER) //if it's the center part
            center = part;
        else if (parts==null) //if it's one of the sides
            parts = new RenderPart[9];

        if (parts!=null) //if array exists, put in the element
            parts[indexOf(align)] = part;

        noBorder = hasNoBorder();
        if (!noBorder) { //something on border exists
            if (parts[0]==null && parts[1]==null && parts[2]==null
                    && parts[6]==null && parts[7]==null && parts[8]==null) {
                //top and bottom rows are empty
                onlyHoriz = true;
            } else if (parts[0]==null && parts[3]==null && parts[6]==null 
                    && parts[2]==null && parts[5]==null && parts[8]==null) {
                //left and right cols are empty
                onlyVert = true;
            }
        } else { //no border, cancel others
            onlyHoriz = false;
            onlyVert = false;
        }
    }

    private int indexOf(int align) {
        int x;
        if      (align == (TOP|LEFT))     x = 0;
        else if (align == TOP)            x = 1;
        else if (align == (TOP|RIGHT))    x = 2;
        else if (align == LEFT)           x = 3;
        else if (align == CENTER)         x = 4;
        else if (align == RIGHT)          x = 5;
        else if (align == (BOTTOM|LEFT))  x = 6;
        else if (align == BOTTOM)         x = 7;
        else if (align == (BOTTOM|RIGHT)) x = 8;
        else throw new IllegalArgumentException("invalid alignment");
        return x;
    }

    public RenderPart get(int align) {
        if (align==CENTER)
            return center;
        else if (parts==null)
            return null;
        else
            return parts[indexOf(align)];
    }

    //will delete borders if they are all null
    public void trimParts() {
        if (noBorder) {
            parts = null;
        }
    }

    private boolean hasNoBorder() {
        if (parts==null)
            return true;
        for (int i=0; i<parts.length; i++) {
            if (i!=4 && parts[i]!=null)
                return false;
        }
        return true;
    }

    void render(Image texture, float x, float y, float width, float height, Color filter) {
        //just draw the center to this scale
        if (noBorder && center!=null) {
            center.render(texture, x, y, width, height, filter);        
        } else if (parts!=null) {
            if (onlyHoriz) {
                float cw1 = parts[3].width;
                float cw2 = parts[5].width;

                parts[3].render(texture, x, y, cw1, height, filter);
                parts[4].render(texture, x+cw1, y, width-cw1-cw2, height, filter);
                parts[5].render(texture, x+width-cw2, y, cw2, height, filter);
            } else if (onlyVert) {
                float ch1 = parts[1].height;
                float ch2 = parts[7].height;
                parts[1].render(texture, x, y, width, ch1, filter);
                parts[4].render(texture, x, y+ch1, width, height-ch1-ch2, filter);
                parts[7].render(texture, x, y+height-ch2, width, ch2, filter);
            } else {
                //left corner width
                float cw1 = parts[0].width;
                //top corner height
                float ch1 = parts[0].height;
                //right corner width
                float cw2 = parts[8].width;
                //bottom corner height
                float ch2 = parts[8].height;

                //  0   1   2
                //  3   4   5
                //  6   7   8
                // render parts in order
                parts[0].render(texture, x, y, cw1, ch1, filter);
                parts[1].render(texture, x+cw1, y, width-cw2-cw1, ch1, filter);
                parts[2].render(texture, x+width-cw2, y, cw2, ch1, filter);
                parts[3].render(texture, x, y+ch1, cw1, height-ch2-ch1, filter);
                parts[4].render(texture, x+cw1, y+ch1, width-cw2-cw1, height-ch2-ch1, filter);
                parts[5].render(texture, x+width-cw2, y+ch1, cw2, height-ch2-ch1, filter);
                parts[6].render(texture, x, y+height-ch2, cw1, ch2, filter);
                parts[7].render(texture, x+cw1, y+height-ch2, width-cw2-cw1, ch2, filter);
                parts[8].render(texture, x+width-cw2, y+height-ch2, cw2, ch2, filter);                
            }                
        } //end if
    }

    public String toString() {
        return "state:"+type;
    }
}