package simplex.entity;

import org.newdawn.slick.Graphics;

/**
 * A superclass for the different entities we have.
 * 
 * @author Emil
 * @author Samuel
 */
public interface Entity {
    
    /**
     * Returns an unique id for this entity
     * @return The id of this entity.
     */
    public int getId();
    
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
