/*
 * TextField.java
 *
 * Created on March 27, 2008, 11:57 AM
 */

package mdes.oxy;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author davedes
 */
public class TextField extends TextComponent {
    
    private String action = null;     
    private char maskCharacter = '*';
    private boolean maskEnabled = false;
    private String maskText = "";
    private int cols = 0;
    private boolean widthChanged = false;
    private Color foregroundCopy;
        
    private boolean deleteFirst = false;
    private String firstText = null;
    private boolean checkFirst = true;
    private boolean deleted = false;
    
    /** Creates a new instance of TextField */
    public TextField(Desktop desktop) {
        super(desktop);
    }
    
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {        
        if (bean == "action") {
            setAction(value);
            return true;
        } else 
            return super.handleSpecialSetter(bean, value);
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }
           
    public void updateCols(int cols) {
        this.cols = cols;
        Font f = getFont();
        String text = getText();
        if (f!=null) {
            float w = 0;
            float oneCol = f.getWidth(COL_CHAR);
            if (cols<=0) {
                if (text.length()!=0)
                    w = getLeft()+f.getWidth(text)+oneCol+getRight();
            } else {
                w = getLeft() + oneCol * cols + getRight();
            }
            width.set(w+2, true);
        }
    }
    
    /**
     * Used by skins to render the text in a text field. This
     * text is sometimes masked 
     */
    public String getDisplayText() {
        return isMaskEnabled() ? maskText : getText();
    }
    
    public void reset() {
        String str = firstText;
        checkFirst = true;
        deleted = false;
        setText(str);
    }
    
    public String getFirstText() {
        return firstText;
    }
    
    public boolean isFirstText() {
        return !deleted;
    }
    
    protected void onTextChange() {
        if (firstText==null && checkFirst) {
            firstText = getText();
            checkFirst = false;
        }
        updateMaskText();
        super.onTextChange();
    }
        
    private void updateMaskText() {
        String text = getText();
        StringBuffer buf = new StringBuffer();
        char ch = getMaskCharacter();
        for (int i=0; i<text.length(); i++) {
            buf.append(ch);
        }
        maskText = buf.toString();
    }
    
    public void setFirstClickDelete(boolean deleteFirst) {
        this.deleteFirst = deleteFirst;
        checkFirst = deleteFirst;
    }
    
    public boolean isFirstClickDelete() {
        return deleteFirst;
    }
    
    protected void renderComponent(Graphics g) {
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            RenderState r = active;
            if (!isEnabled())
                r = disabled;
            else if (!hasFocus())
                r = inactive;
            ui.render(this, r);
        }
        
        String value = getDisplayText();
        Font font = getFont();
        int caretPos = getCaretPosition();
        Color foreground = isEnabled() ? getForeground() : getDisabledForeground();
        
        //RENDER IMAGES
        foregroundCopy = this.getAlphaCopy(foregroundCopy, foreground);
        
        //RENDER TEXT
        if (foreground==null)
            return;
        
        //use default font
        if (font==null)
            font = g.getFont();
        
        float x = getXOnScreen();
        float y = getYOnScreen();
        float clipX = x+clipXOff;
        float clipY = y+clipYOff;
        float clipWidth = width.get()-clipXOff2-clipXOff;
        float clipHeight = height.get()-clipYOff2-clipYOff;
        
        //current pos
        float cpos = getLeft()+font.getWidth(value.substring(0, caretPos));
        float tx = 0;
        if (cpos > width.get() - getRight()) {
            tx = width.get() - cpos - getRight();
        }
        Rectangle oldClip = g.getWorldClip();
        g.setWorldClip((int)clipX, (int)clipY, (int)clipWidth, (int)clipHeight);
        
        g.translate(tx,0);
        g.setFont(font);
                
        g.setColor(foregroundCopy);
        g.drawString(value, x+getLeft(), y+getTop());
        
        if (hasFocus() && renderCaret) {
            g.fillRect((int)(x+cpos+1), (int)(y+getTop()), 1, font.getLineHeight()-2);
        }

        g.translate(-tx, 0);
        g.setWorldClip(oldClip);
    }
    
    public boolean isMaskEnabled() {
        return maskEnabled;
    }

    public void setMaskEnabled(boolean maskEnabled) {
        this.maskEnabled = maskEnabled;
    }

    public char getMaskCharacter() {
        return maskCharacter;
    }

    public void setMaskCharacter(char maskCharacter) {
        this.maskCharacter = maskCharacter;
        updateMaskText();
    }
    
    protected void onAction() {
    }
    
    protected void onMousePress(int button, int x, int y, int mouseX, int mouseY) {
        super.onMousePress(button, x, y, mouseX, mouseY);
        
        if (!deleted && isEditable() && deleteFirst && firstText!=null && firstText.equals(getText())) {
            setText("");
            deleted = true;
        }
    }
    
    protected void onKeyPress(int key, char c) {
        super.onKeyPress(key, c);
        
        if (key == Input.KEY_ENTER) {
            onAction();
            if (action!=null)
                execScript(action);
        } else if (key == Input.KEY_HOME) {
            setCaretPosition(0);
        } else if (key == Input.KEY_END) {
            setCaretPosition(getText().length());
        }
    }
}
