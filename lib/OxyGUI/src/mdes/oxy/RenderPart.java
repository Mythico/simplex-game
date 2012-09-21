package mdes.oxy;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class RenderPart {

    public final float x1, y1, x2, y2;
    public final float width, height;
    public final boolean useImage;
    
    public RenderPart(boolean useImage, float x1, float y1, float x2, float y2) {
        this.useImage = useImage;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = this.x2-this.x1;
        this.height = this.y2-this.y1;
    }
    
    public RenderPart(float x1, float y1, float x2, float y2) {
        this(true, x1, y1, x2, y2);
    }
    
    public void render(Image texture, float x, float y, float width, float height, Color filter) {
        if (useImage)
            texture.drawEmbedded(x, y, x+width, y+height, x1, y1, x2, y2, filter);
    }
}