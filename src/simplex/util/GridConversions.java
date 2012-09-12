/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex.util;

import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Emil
 */
public class GridConversions {
 
    public static Vector2f gridToScreenCoord(int x, int y, Vector2f gameSize, 
	    Vector2f screenSize){
	float fx = x * (screenSize.x / gameSize.x);
	float fy = y * (screenSize.y / gameSize.y);
	return new Vector2f(fx, fy);
    }
}
