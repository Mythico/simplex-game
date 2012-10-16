package simplex.util;

import org.newdawn.slick.geom.Vector2f;

/**
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

    public static void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public static void setGameSize(int width, int height) {
        gameWidth = width;
        gameHeight = height;
    }

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

    public static float getGridWidth() {
        if (gridWidth == 0) {
            gridWidth = screenWidth / gameWidth;
        }
        return gridWidth;
    }

    public static float getGridHeight() {
        if (gridHeight == 0) {
            gridHeight = screenHeight / gameHeight;
        }
        return gridHeight;
    }

    public static int getGameWidth() {
        return gameWidth;
    }

    public static int getGameHeight() {
        return gameHeight;
    }

    public static GridCoord screenToGrid(Vector2f position) {
        int x = (int) (position.x / (screenWidth / (float) gameWidth));
        int y = (int) (position.y / (screenHeight / (float) gameHeight));
        return new GridCoord(x, y);
    }
}
