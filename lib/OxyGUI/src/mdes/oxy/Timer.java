/*
 * Timer.java
 *
 * Created on November 22, 2007, 2:32 PM
 */

package mdes.oxy;

import org.lwjgl.Sys;
import java.util.ArrayList;
import org.newdawn.slick.gui.GUIContext;

/**
 * <p>A simple Sui-based Timer for using delays and "waits"
 * within a GUI environment.</p>
 * 
 * @author davedes
 */
public class Timer {
    
    /** Whether to repeat this timer. */
    private boolean repeating = true;
    
    /** The last time since update. */
    protected long lastTime = getTime();
    
    protected boolean done = false;
    protected int delay;
    private int initialDelay;
    private boolean isAction;
    protected boolean active;
    protected float percent = 0f;
    private boolean firstRepeat = false;
    private long now;
    private GUIContext container;
    
    private ArrayList listeners;
    
    public Timer() {
        this(0);
    }
    
    /** Creates a new instance of Timer */
    public Timer(int delay, Listener listener) {
        if (delay<0)
            throw new IllegalArgumentException("delay must be >= 0");
        this.delay = delay;
        this.setInitialDelay(delay);
        if (listener!=null)
            this.addListener(listener);
    }
    
    public Timer(int delay) {
        this(delay, null);
    }
    
    public void addListener(Listener listener) {
        if (listeners==null)
            listeners = new ArrayList();
        listeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
        if (listeners!=null)
            listeners.remove(listener);
    }
    
    public float getPercent() {
        return percent;
    }
        
    public void update(GUIContext container, int delta) {
        this.container = container;
        isAction = false;
                
        //if we are stopped
        if (!active)
            return;
        //or if we have finished repeating (once)
        if (done && !repeating) {
            active = false;
            return;
        }
        
        now = (getTime()-delta)-lastTime;
        
        int delayCheck = delay;
        
        if (firstRepeat)
            delayCheck = getInitialDelay();
        
        if (now >= delayCheck) { //if the timer is done
            percent = 1f;
            //reset last time
            lastTime = getTime();
            
            //if we aren't repeating, we are finished
            if (!repeating) {
                done = true;
                active = false;
            }
            firstRepeat = false;
            isAction = true;
            notifyListeners();
        } else { //if we aren't done yet
            percent = now / (float)delay;
        }
    }
    
    public long getNow() {
        return now;
    }
    
    /** Restarts this timer. */
    public void restart() {
        lastTime = getTime();
        done = false;
        percent = 1f;
        active = true;
        firstRepeat = true;
    }

    public boolean isRepeats() {
        return repeating;
    }

    public void setRepeats(boolean repeating) {
        boolean old = this.repeating;
        this.repeating = repeating;
        if (this.repeating != old)
            done = false;
    }
    
    /**
     * Starts the Timer. 
     *
     */
    public void start() {
        boolean old = active;
        active = true;
        if (active != old) { //changed
            lastTime = getTime();
            done = false;
            percent = 1f;
            firstRepeat = true;
        }
    }
    
    /** 
     * Whether the timer is active. 
     *
     * @return whether the timer is active
     */
    public boolean isRunning() {
        return active;
    }
    
    /** 
     * Sets the delay of this timer. 
     *
     * @param delay the time in milliseconds
     */
    public void setDelay(int delay) {
        if (this.delay == this.initialDelay)
            this.initialDelay = delay;
        this.delay = delay;
    }
    
    /**
     * Gets the delay of this timer.
     *
     * @return the delay of this timer
     */
    public int getDelay() {
        return delay;
    }
    
    /**
     * 
     */
    public void stop() {
        active = false;
        percent = 0f;
    }
        
    /** 
     * Whether the action has occurred.
     *
     * @return <tt>true</tt> if the action has occurred */
    public boolean isAction() {
        return isAction;
    }

    private long getTime() {
        if (container!=null)
            return container.getTime();
        else
            return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }
    
    protected void notifyListeners() {
        if (listeners==null)
            return;
        for (int i=0; i<listeners.size(); i++)
            ((Listener)listeners.get(i)).onAction(this);
    }
    
    public static interface Listener {
        public void onAction(Timer timer);
    }
}