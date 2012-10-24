package simplex.util;

import org.newdawn.slick.geom.Vector2f;

/**
 * A utility class for converting between different coordinate system.
 *
 * @author Emil
 * @author Samuel
 */
public class GridConversions {

    private static int gameWidth = 0;
    private static int gameHeight = 0;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static float gridWidth = 0;
    private static float gridHeight = 0;

    /**
     * Set the size of the screen. This size is used for calculating the
     * different coordinates.
     * @param width With of the screen.
     * @param height Height of the screen.
     */
    public static void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    /**
     * Set the game size. The game size defines how many nodes in a row
     * there can be on the screen.
     * @param width Number of nodes horizontal.
     * @param height  Number of nodes vertical.
     */
    public static void setGameSize(int width, int height) {
        gameWidth = width;
        gameHeight = height;
    }

    /**
     * Convert from logical GridCoord to actual screen coord.
     * @param coord The coordinate to be converted from.
     * @return The actual position in the window.
     */
    public static Vector2f gridToScreenCoord(GridCoord coord) {
        float fx = coord.x * (screenWidth / (float) gameWidth);
        float fy = coord.y * (screenHeight / (float) gameHeight);
        return new Vector2f(fx, fy);
    }

    /**
     * Create a GridCoord for the mouse position mx, my.
     *
     * @param mx The mouse position horizontal.
     * @param my The mouse position vertical.
     * @return
     */
    public static GridCoord mouseToGridCoord(int mx, int my) {
        int x = (mx / (screenWidth / gameWidth));
        int y = (my / (screenHeight / gameHeight));
        return new GridCoord(x, y);
    }

    /**
     * Get the grid width.
     * @return The width of the grid.
     */
    public static float getGridWidth() {
        if (gridWidth == 0) {
            gridWidth = screenWidth / gameWidth;
        }
        return gridWidth;
    }

    /**
     * Get the grid height.
     * @return The height of the grid.
     */
    public static float getGridHeight() {
        if (gridHeight == 0) {
            gridHeight = screenHeight / gameHeight;
        }
        return gridHeight;
    }
    
    /**
     * Get the game width.
     * @return The width of the game.
     */
    public static int getGameWidth() {
        return gameWidth;
    }

    /**
     * Get the game height.
     * @return The height of the game.
     */
    public static int getGameHeight() {
        return gameHeight;
    }

    /**
     * Convert from actual screen coordinates to a grid coord.
     * @param position The position on the screen.
     * @return The GridCoord that represents that area of the screen.
     */
    public static GridCoord screenToGrid(Vector2f position) {
        int x = (int) (position.x / (screenWidth / (float) gameWidth));
        int y = (int) (position.y / (screenHeight / (float) gameHeight));
        return new GridCoord(x, y);
    }
}
