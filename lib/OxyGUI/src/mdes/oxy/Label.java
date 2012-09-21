/*
 * Label.java
 *
 * Created on March 24, 2008, 11:55 AM
 */

package mdes.oxy;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author davedes
 */
public class Label extends Component {
    
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    
    private int textAlign = ALIGN_CENTER;
    private String text;
    private boolean autoPackEnabled = true;
    
    private int textWidth, textHeight;
    private Image image;
    private Color foregroundCopy = null;
        
    /** Creates a new instance of Label */
    public Label(Desktop desktop) {
        super(desktop);
    }
    
    public void setTextAlign(int align) {
        if (align!=ALIGN_LEFT && align!=ALIGN_CENTER && align!=ALIGN_RIGHT)
            throw new IllegalArgumentException("invalid align param");
        this.textAlign = align;
    }
    
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "image") {
            if (value.equals("null")) 
                setImage(null);
            else {
                Object obj = getDesktop().getDoc().getSkinElement(value);
                if (!(obj instanceof Image))
                    throw new OxyException("image must point to Slick Image object");
                setImage((Image)obj);
            }
            return true;
        } else if (bean == "textAlign") {
            int a = ALIGN_CENTER;
            if ("ALIGN_LEFT".equals(value))
                a = ALIGN_LEFT;
            else if ("ALIGN_RIGHT".equals(value))
                a = ALIGN_RIGHT;
            setTextAlign(a);
            return true;
        } else if (bean == "text") {
            setText(value);
            return true;
        } else {
            return super.handleSpecialSetter(bean, value);
        }
    }
    
    protected void onPaddingChange() {
        if (isAutoPackEnabled())
            pack();
    }
    
    public int getTextWidth() {
        return textWidth;
    }
    
    public int getTextHeight() {
        return textHeight;
    }
    
    public String getText() {
        return text;
    }
    
    public Image getImage() {
        return image;
    }
    
    protected void renderComponent(Graphics g) {
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            ui.render(this);
        }
        renderImageAndText(g, 0, 0);
    }
    
    public void setImage(Image image) {
        this.image = image;
        if (isAutoPackEnabled())
            pack();
    }
    
    public void setText(String text) {
        String old = this.text;
        this.text = text;
        if (old!=text) {
            updateTextSize();
            if (isAutoPackEnabled())
                pack();
        }
    }
    
    public void setFont(Font font) {
        Font old = this.getFont();
        super.setFont(font);
        if (old!=getFont()) {
            updateTextSize();
            if (isAutoPackEnabled())
                pack();
        }
    }
    
    protected void updateTextSize() {
        Font f = getFont();
        String s = getText();
        boolean empty = s==null||s.length()==0;
        textWidth = empty ? 0 : f.getWidth(s);
        textHeight = empty ? 0 : f.getLineHeight();
    }
    
    public void pack() {
        float objWidth = 0;
        float objHeight = 0;
                
        if (image!=null) {
            objWidth = image.getWidth();
            objHeight = image.getHeight();
        }
        
        String text = getText();
        Font font = getFont();
        if (text!=null && text.length()!=0) {
            objWidth = Math.max(objWidth, getTextWidth());
            objHeight = Math.max(objHeight, getTextHeight());
        }
        
        width.set(getLeft() + objWidth + getRight(), true);
        height.set(getTop() + objHeight + getBottom(), true);
    }
    
    public boolean isAutoPackEnabled() {
        return autoPackEnabled;
    }

    public void setAutoPackEnabled(boolean autoPackEnabled) {
        this.autoPackEnabled = autoPackEnabled;
    }
    
    protected void renderImageAndText(Graphics g, float offx, float offy) {
        float abx = getXOnScreen();
        float aby = getYOnScreen();
        float width = this.width.get();
        float height = this.height.get();
        
        //render image if it exists
        Image image = getImage();
        if (image!=null) {
            float cx = width/2f - image.getWidth()/2f;
            float cy = height/2f - image.getHeight()/2f;
            g.drawImage(image, (int)(getXOnScreen()+cx), (int)(getYOnScreen()+cy), alphaFilter);
        }
                
        Color foreground = isEnabled() ? getForeground() : getDisabledForeground();
        if (foreground!=null && text!=null) {
            Font font = getFont();
            String text = getText();
            float cx;
            float cy = height/2f - getTextHeight()/2f;
            if (textAlign==ALIGN_LEFT)
                cx = getLeft();
            else if (textAlign==ALIGN_CENTER)
                cx = width/2f - getTextWidth()/2f;
            else 
                cx = width-getRight()-getTextWidth();
            
            foregroundCopy = getAlphaCopy(foregroundCopy, foreground);
            
            g.setColor(foregroundCopy);
            g.setFont(font);
            g.drawString(text, (int)(getXOnScreen()+cx+offx), (int)(getYOnScreen()+cy+offy));
        }
    }
}
