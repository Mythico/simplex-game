package simplex;

import mdes.oxy.Desktop;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import simplex.level.Level;
import simplex.level.LevelFileHandler;
import simplex.util.GridConversions;

/**
 *
 * @author Emil
 * @author Samuel
 */
public abstract class EngineState extends BaseState {

    protected int height = 16;
    protected int width = 16;
    protected Level level = new Level();
    private Image background;

    public EngineState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.init(gc, sbg);
        GridConversions.setGameSize(width, height);
        GridConversions.setScreenSize(gc.getWidth(), gc.getHeight());
        background = new ImageBuffer(gc.getWidth(), gc.getHeight()).getImage();
        Graphics g = background.getGraphics();
        final int gwidth = gc.getWidth() / width;
        final int gheight = gc.getHeight() / height;
        // Draw grid
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j += 2) {
                int x = j + (i%2);
                g.setColor(Color.gray);
                g.fillRect(x * gwidth, i * gheight, gwidth, gheight);
            }
        }
    }

    @Override
    protected abstract Desktop loadGui(GameContainer gc) throws SlickException;

    @Override
    protected void renderBackground(GameContainer gc, StateBasedGame sbg,
            Graphics g) {
        g.drawImage(background, 0, 0);
    }

    @Override
    protected void renderContent(GameContainer gc, StateBasedGame sbg,
            Graphics g) {
        level.render(g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        super.update(gc, sbg, delta);
        if (level != null) {
            level.update(delta);
        }
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        super.enter(gc, sbg);
        level = LevelFileHandler.instance().loadLevel();
    }
}
