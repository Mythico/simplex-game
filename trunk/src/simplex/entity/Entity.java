package simplex.entity;

import org.newdawn.slick.Graphics;

/**
 * 
 * @author Emil
 * @author Samuel
 */
public interface Entity {
    
    /**
     * Update the level
     * @param delta Time from last update.
     */
    public void update(int delta);
    
    
    /**
     * Render the Entity.
     * @param g The graphics the level will be rendered with.
     */
    public void render(Graphics g);
}
