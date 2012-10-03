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

    public static void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public static void setGameSize(int width, int height) {
        gameWidth = width;
        gameHeight = height;
    }

    public static Vector2f gridToScreenCoord(GridCoord coord) {
        float fx = coord.x * (screenWidth / (float)gameWidth);
        float fy = coord.y * (screenHeight / (float)gameHeight);
        return new Vector2f(fx, fy);
    }

    public static GridCoord mouseToGridCoord(int mx, int my) {
        int x = (mx / (screenWidth / gameWidth));
        int y = (my / (screenHeight / gameHeight));
        return new GridCoord(x, y);
    }
    
    public static float getGridWidth(){
        return screenWidth / gameWidth;
    }
    
    public static float getGridHeight(){
        return screenHeight / gameHeight;
    }

    public static GridCoord screenToGrid(Vector2f position) {
        int x = (int) (position.x / (screenWidth / (float)gameWidth));
        int y = (int) (position.y / (screenHeight / (float)gameHeight));
        return new GridCoord(x, y);
    }
    
    
}
