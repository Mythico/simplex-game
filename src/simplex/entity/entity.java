/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.entity;

import org.newdawn.slick.Graphics;

/**
 *
 * @author Emil
 * @author Samuel
 */
public interface Entity {
    public void update(int delta);
    public void render(Graphics g);
}
