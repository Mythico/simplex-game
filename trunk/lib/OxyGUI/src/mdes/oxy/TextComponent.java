/*
 * TextComponent.java
 *
 * Created on March 27, 2008, 11:59 AM
 */

package mdes.oxy;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Input;

/**
 *
 * @author davedes
 */
public abstract class TextComponent extends Component {
    
    private String textChangeAction = null;
    private String actionCommand = null;   
    private String text = null;
    private boolean editable = true;
    private int caretPos = 0;
    private int maxChars = Integer.MAX_VALUE;
    
    protected static final String COL_CHAR = "w";
    protected boolean renderCaret = false;
    protected Timer caretFlashTimer;
    
    private int maxUndos;
    protected List undos;
    private int undoPtr = 0;
    
    public float clipXOff, clipYOff, clipXOff2, clipYOff2;
    protected boolean widthChanged = false;
    protected int cols;
    
    protected RenderState active;
    protected RenderState inactive;
    protected RenderState disabled;
    
    /**
     * Creates a new instance of TextComponent. By default, no call to
     * updateAppearance is made in the construction of this component.
     */
    public TextComponent(Desktop desktop) {
        super(desktop);
        setFocusable(true);
        caretFlashTimer = new Timer(500);
        caretFlashTimer.setInitialDelay(800);
        caretFlashTimer.setRepeats(true);
        
        maxUndos = getMaxUndoStates();
        if (maxUndos>0) {
            undos = new ArrayList(maxUndos);
            undos.add("");
        }
    }
    
    public int getCols() {
        return cols;
    }
    
    public void setWidth(Position pos) {
        super.setWidth(pos);
        widthChanged = true;
    }
    
    public void onPostParse(OxyDoc doc) {
        if (!widthChanged)
            updateCols(getCols());
    }
        
    protected abstract void updateCols(int cols);
        
    protected int getMaxUndoStates() {
        return 10;
    }
        
    public int getCaretPosition() {
        return caretPos;
    }
    
    public void setClipOffsets(float xOff, float yOff, float xOff2, float yOff2) {
        this.clipXOff = xOff;
        this.clipYOff = yOff;
        this.clipXOff2 = xOff2;
        this.clipYOff2 = yOff2;
    }
    
    public void setCaretPosition(int caretPos) {
        int old = this.caretPos;
        this.caretPos = caretPos;
        if (old!=caretPos)
            onCaretPositionChange();
    }
    
    protected void onThemeChange() {
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            active = ui.getState("active");
            inactive = ui.getState("inactive");
            disabled = ui.getState("disabled");
            
            if (inactive==null)
                inactive = active;
            if (disabled==null)
                disabled = inactive;
        }
    }
        
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        if (bean == "cols") {
            try { cols = Integer.parseInt(value); }
            catch (NumberFormatException e) { 
                throw new OxyException("cannot parse cols with "+value); 
            }
            return true;
        } else if (bean == "text") {
            setText(value);
            return true;
        } else if (bean == "actionCommand") {
            setActionCommand(value);
            return true;
        } else if (bean == "textChangeAction") {
            setTextChangeAction(value);
            return true;
        } else
            return super.handleSpecialSetter(bean, value);
    }
    
    public void grabFocus() {
        super.grabFocus();
        caretFlashTimer.restart();
        renderCaret = true;
    }
    
    public void setTextChangeAction(String action) {
        this.textChangeAction = action;
    }
    
    public String getTextChangeAction() {
        return textChangeAction;
    }
    
    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }
    
    public String getActionCommand() {
        return actionCommand;
    }
    
    public String getText() {
        if (text == null)
            text = "";
        return text;
    }
    
    public void setText(String text) {
        String old = this.text;
        this.text = text;
        if (this.text == null)
            this.text = "";
        caretPos = this.text.length();
        if (old!=text) {
            onTextChange();
        }
    }
    
    public boolean canUndo() {
        if (undos==null)
            return false;
        return undoPtr > 0;
    }
    
    public boolean canRedo() {
        if (undos==null)
            return false;
        return undoPtr < undos.size()-1;
    }
    
    public void undo() {
         if (canUndo()) {
             setText((String)undos.get(--undoPtr));
         }
    }
    
    public void redo() {
        if (canRedo()) {
            setText((String)undos.get(++undoPtr));
        }
    }
    
    protected void stepForward() {
        if (undos==null)
            return;
        //we trim every step so that canRedo returns false
        if (undos.size()>1)
            undos = undos.subList(0, undoPtr+1);
        
        undos.add(getText());
        
        if (undos.size()>maxUndos)
            undos.remove(0);

        undoPtr = undos.size()-1;
    }
    
    /**
     * Allows subclasses to tap into changed events directly without
     * the need for listeners.
     * 
     * @param newText the new text value
     * @param oldText the previous value of the text, before it was changed
     */
    protected void onTextChange() {
        caretFlashTimer.restart();
        renderCaret = true;
        if (textChangeAction!=null)
            execScript(textChangeAction);
    }
    
    protected void onCaretPositionChange() {
        caretFlashTimer.restart();
        renderCaret = true;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
        setFocusable(editable);
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }
    
    protected void updateComponent(int delta) {
        if (hasFocus()) {
            caretFlashTimer.update(getDesktop().getContext(), delta);

            if (caretFlashTimer.isAction())
                renderCaret = !renderCaret;
        }
    }        
    
    boolean step = false;
    
    protected boolean vetoTextChange(String newText, String oldText) {
        return false;
    }
    
    private void doRepeat(int key, char c) {
        if (!isEditable())
            return;

        if (text == null)
            text = "";
        
        String oldText = text;
        int oldCaret = caretPos;

        Input input = getDesktop().getInput();
        boolean paste = (key==Input.KEY_V &&  (input.isKeyDown(Input.KEY_LCONTROL) 
                    || input.isKeyDown(Input.KEY_RCONTROL)))
                        || (key==Input.KEY_INSERT && (input.isKeyDown(Input.KEY_LSHIFT) 
                    || input.isKeyDown(Input.KEY_RSHIFT)));
        if (paste) {
            String str = Desktop.getClipboard();
            if (str!=null) {
                char[] chrs = null;
                for (int i=0; i<str.length(); i++) {
                    char current = str.charAt(i);
                    if (current=='\n'||current=='\r'||current=='\t') {
                        if (chrs==null)
                            chrs = str.toCharArray();
                        chrs[i] = ' ';
                    }
                }
                if (chrs!=null)
                    str = new String(chrs);
            }
            
            String newText;
            if (caretPos < text.length()) {
                newText = text.substring(0, caretPos) + str + text.substring(caretPos);
            } else {
                newText = text.substring(0, caretPos) + str;
            }
            if (newText.length() <= getMaxChars()) {
                text = newText;
                caretPos += str.length();
            }
            step = true;
        } else if ( (key==Input.KEY_Z || key==Input.KEY_Y) 
                    && (input.isKeyDown(Input.KEY_LCONTROL) 
                    || input.isKeyDown(Input.KEY_RCONTROL)) ) {
            //undo/redo
            if (key == Input.KEY_Z)
                undo();
            else
                redo();
        } else if ( (c<127) && (c>31) && (text.length() < getMaxChars()) ) {
            if (caretPos < text.length()) {
                text = text.substring(0, caretPos) + c + text.substring(caretPos);
            } else {
                text = text.substring(0, caretPos) + c;
            }
            caretPos++;
            step = true;
        } else if (key == Input.KEY_LEFT) {
            if (caretPos > 0)
                caretPos--;
        } else if (key == Input.KEY_RIGHT) {
            if (caretPos < text.length())
                caretPos++;
        } else if (key == Input.KEY_BACK) {
            if ((caretPos>0) && (text.length()>0)) {
                if (caretPos < text.length())
                    text = text.substring(0, caretPos-1) + text.substring(caretPos);
                else
                    text = text.substring(0, caretPos-1);
                caretPos--;
                step = true;
            }
        } else if (key == Input.KEY_DELETE) {
            if (caretPos < text.length()) {
                text = text.substring(0, caretPos) + text.substring(caretPos+1);
                step = true;
            }
        }
        doRepeatImpl(key, c);

        if (oldText != text) { //changed
            if (!vetoTextChange(text, oldText)) {
                onTextChange();
            } else {
                step = false;
                text = oldText;
                caretPos = oldCaret;
            }
        }
        if (oldCaret != caretPos) {
            onCaretPositionChange(); 
        }
    }
    
    protected void doRepeatImpl(int key, char c) {
        //do nothing
    }
    
    protected void onKeyPress(int key, char c) {
        super.onKeyPress(key, c);
        doRepeat(key, c);
    }
    
    protected void onKeyRelease(int key, char c) {
        super.onKeyRelease(key, c);
        if (step) {
            stepForward();
            step = false;
        }
    }
        
    protected void onKeyRepeat(int key, char c) {
        super.onKeyRepeat(key, c);
        doRepeat(key, c);
    }
    
    protected boolean isTraversalAvailable(int key, char c) {
        return key==getDesktop().getControls().nextFocus;
    }
}