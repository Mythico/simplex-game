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
 
    private static int gameWidth = 0;
    private static int gameHeight = 0;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    
    public static void setScreenSize(int width, int height){
	screenWidth = width;
	screenHeight = height;
    }
    public static void setGameSize(int width, int height){
	gameWidth = width;
	gameHeight = height;
    }
    
    
    public static Vector2f gridToScreenCoord(int x, int y){
	float fx = x * (screenWidth / gameWidth);
	float fy = y * (screenHeight / gameHeight);
	return new Vector2f(fx, fy);
    }

    public static int[] screenToGridCoord(int x, int y) {	
	int x2 = x / (screenWidth / gameWidth);
	int y2 = y / (screenHeight / gameHeight);	
	return new int[]{x2,y2};
    }
}
