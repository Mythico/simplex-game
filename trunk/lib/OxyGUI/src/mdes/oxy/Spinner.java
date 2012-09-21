/*
 * Spinner.java
 *
 * Created on March 31, 2008, 9:20 PM
 */

package mdes.oxy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * A simple number spinner.
 * @author davedes
 */
public class Spinner extends Panel {
    
    private Comparable min, max;
    private Number step;
    private Number value;
    private Button nextButton;
    private Button previousButton;
    private TextField textField;
    private NumberFormat formatter = DecimalFormat.getNumberInstance();
    
    private Number next, previous;
    private String valueText;
    
    private RenderState active;
    private RenderState inactive;
    private RenderState disabled;
                
    public float f_xOff, f_yOff, f_xOff2, f_yOff2;
    private boolean buttonOnRight = true;
    private int cols = -1;
    
    private boolean boundsDirty = true;
    private String dstr, gstr;
    private Timer repeatTimer = new Timer();
    private int repeat = 0;
    
    class SpinnerButton extends Button {
        private int dir;
        
        public SpinnerButton(Desktop desktop, int dir) {
            super(desktop);
            this.dir = dir;
        }
        
        public Component getControlOwner() {
            return Spinner.this;
        }
          
        protected void onButtonDown() {
            repeatTimer.restart();
            repeat = dir;
            if (dir == +1)
                onIncrement();
            else if (dir == -1)
                onDecrement();
        }

        protected void onButtonUp() {
            repeatTimer.stop();
            repeat = 0;
        }
        
        protected void onKeyPress(int key, char c) {
            super.onKeyPress(key, c);
            if (key==Input.KEY_UP || key==Input.KEY_DOWN)
                updateField();
            repeat(key);
        }
        
        protected void onKeyRepeat(int key, char c) {
            super.onKeyPress(key, c);
            repeat(key);
        }
        
        protected boolean isActionKey(int key, char c) {
            return false;
        }
        
        protected boolean isTraversalAvailable(int key, char c) {
            return (key!=Input.KEY_UP && key!=Input.KEY_DOWN);
        }
    }
    
    class SpinnerTextField extends TextField {
        
        public SpinnerTextField(Desktop desktop) {
            super(desktop);
        }
        
        public Component getControlOwner() {
            return Spinner.this;
        }
        
        protected boolean vetoTextChange(String newText, String oldText) {
            return vetoTextEdit(newText, oldText);
        }

        protected int getMaxUndoStates() {
            return 0;
        }

        protected void doRepeatImpl(int key, char c) {
            if (key == Input.KEY_UP) {
                nextButton.doPress();
            } else if (key == Input.KEY_DOWN) {
                previousButton.doPress();
            }
        }
        
        protected void onFocusLost() {
            updateField();
        }

        protected void onKeyPress(int key, char c) {
            super.onKeyPress(key, c);
            if (key==Input.KEY_UP || key==Input.KEY_DOWN) {
                updateField();
            }
            repeat(key);
        }

        protected void onKeyRepeat(int key, char c) {
            super.onKeyPress(key, c);
            repeat(key);
        }

        protected void onAction() {
            super.onAction();
            updateField();
        }
    }
        
    public Spinner(Desktop desktop) {
        this(desktop, new Integer(0), null, null, new Integer(1));
    }
    
    /** Creates a new instance of Spinner */
    public Spinner(Desktop desktop, Number value, Comparable min, Comparable max, Number step) {
        super(desktop);
        
        repeatTimer.setDelay(70);
        repeatTimer.setInitialDelay(500);
        setFocusable(true);
        
        if ((value == null) || (step == null)) {
	    throw new IllegalArgumentException("value and step size must be non-null");
	}
	if (!(((min == null) || (min.compareTo(value) <= 0)) && 
	      ((max == null) || (max.compareTo(value) >= 0)))) {
	    throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
	}
	this.value = value;
	this.min = min;
	this.max = max;
	this.step = step;
        
        nextButton = new SpinnerButton(desktop, +1);
        previousButton = new SpinnerButton(desktop, -1);
        textField = new SpinnerTextField(desktop);
        
        add(textField);
        add(nextButton);
        add(previousButton);
        
        ensureBounds();
        updateText();
    }
    
    public boolean hasFocus() {
        if (!isEnabled())
            return super.hasFocus();
        return super.hasFocus() 
                || textField.hasFocus() 
                || nextButton.hasFocus() 
                || previousButton.hasFocus();
    }
    
    public void grabFocus() {
        textField.grabFocus();
    }
    
    protected boolean handleSpecialSetter(String bean, String value) throws OxyException {
        OxyDoc doc = getDesktop().getDoc();
        if (bean == "cols") {
            try {
                int c = Integer.parseInt(value);
                if (c<0)
                    throw new OxyException("cols must be >= 0");
                this.cols = c;
            } catch (NumberFormatException e) {
                throw new OxyException("cols must be a valid integer");
            }
            return true;
        } else if (bean == "min" || bean == "max") {
            Object obj = Doc.parseObject(doc, this, value);
            if (obj instanceof Doc.Null)
                obj = null;
            else if (!(obj instanceof Comparable))
                throw new OxyException("min and max must be instances of Comparable (eg. Number)");
            if (bean == "min")
                setMin((Comparable)obj);
            else
                setMax((Comparable)obj);
            return true;
        } else if (bean == "value" || bean == "step") {
            Object obj = Doc.parseObject(doc, this, value);
            if (obj instanceof Doc.Null)
                obj = null;
            else if (!(obj instanceof Number))
                throw new OxyException("value must be an instance of Number");
            if (bean == "value")
                setValue((Number)obj);
            else 
                setStep((Number)obj);
            return true;
        } else if (bean == "formatter") {
            setFormatter(new DecimalFormat(value));
            return true;
        } else if (bean == "decimals") {
            dstr = value;
            return true;
        } else if (bean == "grouping") {
            gstr = value;
            return true;
        } else {
            return super.handleSpecialSetter(bean, value);
        }
    }
    
    public void setFieldOffsets(float xOff, float yOff, float xOff2, float yOff2) {
        this.f_xOff = xOff;
        this.f_yOff = yOff;
        this.f_xOff2 = xOff2;
        this.f_yOff2 = yOff2;
    }
        
    /**
     * It is on the right by default.
     */
    public void setButtonOnRight(boolean buttonOnRight) {
        this.buttonOnRight = buttonOnRight;
    }
    
    public TextField getTextField() {
        return textField;
    }
    
    public Button getNextButton() {
        return nextButton;
    }
    
    public Button getPreviousButton() {
        return previousButton;
    }
    
    protected void onKeyPress(int key, char c) {
        super.onKeyPress(key, c);
        if (key==Input.KEY_UP || key==Input.KEY_DOWN)
            updateField();
        repeat(key);
    }

    protected void onKeyRepeat(int key, char c) {
        super.onKeyPress(key, c);
        repeat(key);
    }
        
    private void repeat(int key) {
        if (key == Input.KEY_UP) {
            onIncrement();
        } else if (key == Input.KEY_DOWN) {
            onDecrement();
        }
    }
    
    private void updateField() {
        String text = textField.getText();
        try {
            //First we attempt to parse, if successful
            //we will set the value.
            Number val = parseText(text);
            if (isInBounds(val))
                setValue(val);
            else {
                textField.setText(getValueText());
            }
        } catch (NumberFormatException e) {
            textField.setText(getValueText());
        }
        ensureBounds();
    }
    
    protected void renderComponent(Graphics g) {
        super.renderComponent(g);
        ComponentTheme ui = getTheme();
        if (ui!=null) {
            RenderState r = active;
            if (!isEnabled())
                r = disabled;
            else if (!hasFocus())
                r = inactive;
            ui.render(this, r);
        }
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
            
            OxyDoc doc = getDesktop().getDoc();
            ComponentTheme btnTheme = ui.getChild("nextButton");
            if (btnTheme==null)
                btnTheme = SkinDoc.getDefaultTheme(doc, "button");
            getNextButton().setTheme(btnTheme);
            
            ComponentTheme btnTheme2 = ui.getChild("previousButton");
            if (btnTheme2==null)
                btnTheme2 = SkinDoc.getDefaultTheme(doc, "button");
            getPreviousButton().setTheme(btnTheme2);
            
            ComponentTheme textField = ui.getChild("textField");
            if (textField==null)
                textField = SkinDoc.getDefaultTheme(doc, "textField");
            getTextField().setTheme(textField);
        }
    }
    
    protected void onResize() {
        super.onResize();
        textField.x.set(xOff, true);
        textField.y.set(yOff, true);
        textField.width.set(getWidth()-xOff-xOff2, true);
        textField.height.set(getHeight()-yOff-yOff2, true);
        
        float x = isButtonOnRight() ? getWidth()-getRight() : getLeft();
        int m = isButtonOnRight() ? -1 : 0;
        nextButton.x.set(x+m*nextButton.getWidth(), true);
        previousButton.x.set(x+m*previousButton.getWidth(), true);
    }
    
    public void onPostParse(OxyDoc doc) throws OxyException {
        if (cols!=-1) {
            textField.updateCols(cols);
            width.set(textField.getWidth(), true);
        }
        if (dstr!=null&&dstr.length()!=0) {
            try { 
                int i = Integer.parseInt(dstr);
                if (i>0 && !(value instanceof Float) && !(value instanceof Double))
                    throw new OxyException("decimals should only be used for floating point values");
                setDecimals(i); 
            } catch (NumberFormatException e) {
                throw new OxyException("cannot parse decimals attribute (integer)"); 
            }
        } 
        if (gstr!=null&&gstr.length()!=0) {
            if (gstr.equals("true"))
                setGrouping(true);
            else if (gstr.equals("false"))
                setGrouping(false);
            else
                throw new OxyException("cannot parse grouping attribute (boolean)");
        }
    }
    
    public boolean isButtonOnRight() {
        return buttonOnRight;
    }
        
    protected boolean vetoTextEdit(String text, String old) {
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }
    
    public String getValueText() {
        return valueText;
    }
    
    protected Number parseText(String text) throws NumberFormatException {
        if (text==null)
            text = "";
        Number n;
        try {
            n = getFormatter().parse(text);
        } catch (ParseException e) {
            throw new NumberFormatException();
        }
        
        if (n instanceof Long && value instanceof Long)
            return n;
        else if (n instanceof Double && value instanceof Double)
            return n;
        else if (value instanceof Float)
            return new Float(n.floatValue());
        else if (value instanceof Integer)
            return new Integer(n.intValue());
        else if (value instanceof Short)
            return new Short(n.shortValue());
        else
            return new Byte(n.byteValue());
    }
        
    private Number incr(int dir) {
        Number newValue;
	if ((value instanceof Float) || (value instanceof Double)) {
	    double v = value.doubleValue() + (step.doubleValue() * (double)dir);
	    if (value instanceof Double)
		newValue = new Double(v);
	    else
		newValue = new Float(v);
	} else {
	    long v = value.longValue() + (step.longValue() * (long)dir);
            if (value instanceof Long)
		newValue = new Long(v);
	    else if (value instanceof Integer)
		newValue = new Integer((int)v);
	    else if (value instanceof Short)
		newValue = new Short((short)v);
	    else
		newValue = new Byte((byte)v);
	}
	if ((max != null) && (max.compareTo(newValue) < 0)) {
	    return null;
	}
	if ((min != null) && (min.compareTo(newValue) > 0)) {
	    return null;
	} 
        
	return newValue;
    }
    
    protected void ensureBounds() {
        if (boundsDirty) {
            next = incr(+1);
            previous = incr(-1);
            //nextButton.setEnabled(next!=null);
            //previousButton.setEnabled(previous!=null);
            boundsDirty = false;
        }
    }
    
    private void updateText() {
        valueText = getFormatter().format(value);
        textField.setText(valueText);
    }
    
    protected void onChange() {
        boundsDirty = true;
        updateText();
    }
    
    public Comparable getMin() {
        return min;
    }
    
    public void setMin(Comparable min) {
        if ((min == null) ? (this.min != null) : !min.equals(this.min)) {
	    this.min = min;
	    boundsDirty = true;
	}      
    }
    
    public Comparable getMax() {
        return max;
    }

    public void setMax(Comparable max) {
        if ((max == null) ? (this.max != null) : !max.equals(this.max)) {
	    this.max = max;
	    boundsDirty = true;
	}
    }

    public Number getStep() {
        return step;
    }

    public void setStep(Number step) {
        if (step == null) {
	    throw new IllegalArgumentException("null step size");
	}
        if (!this.step.equals(step)) {
            this.step = step;
            boundsDirty = true;
        } 
    }

    public Number getValue() {
        return value;
    }
    
    public boolean isInBounds(Number value) {
        return ((min == null) || (min.compareTo(value) <= 0)) && 
	      ((max == null) || (max.compareTo(value) >= 0));
    }

    public void setValue(Number value) {
        if (value==null)
            throw new IllegalArgumentException("value cannot be null");    
        if (!this.value.equals(value)) {
            this.value = value;
            this.boundsDirty = true;
            onChange();
        } else
            textField.setText(valueText);
    }
    
    public void setFormatter(NumberFormat formatter) {
        if (formatter==null)
            throw new IllegalArgumentException("formatter is null");
        this.formatter = formatter;
    }
    
    public float getFloatValue() {
        return value.floatValue();
    }
    
    protected void updateComponent(int delta) {
        super.updateComponent(delta);
        ensureBounds();
        
        repeatTimer.update(getDesktop().getContext(), delta);
        
        if (repeat!=0 && repeatTimer.isAction()) {
            if (repeat == +1)
                onIncrement();
            else
                onDecrement();
        }
    }
    
    protected void onIncrement() {
        if (next!=null)
            setValue(next);
    }
    
    protected void onDecrement() {
        if (previous!=null)
            setValue(previous);
    }
    
    public int getIntValue() {
        return value.intValue();
    }
    
    /**
     * A convenience method for setting the number of decimals to show
     * using the current formatter.
     */
    public void setDecimals(int decimals) {
        NumberFormat f = getFormatter();
        f.setMinimumFractionDigits(decimals);
        f.setMaximumFractionDigits(decimals);
        updateText();
    }
    
    /**
     * A convenience method for setting the grouping value using the current
     * formatter.
     */
    public void setGrouping(boolean grouping) {
        NumberFormat f = getFormatter();
        f.setGroupingUsed(grouping);
        updateText();
    }
    
    public NumberFormat getFormatter() {
        return formatter;
    }
}
