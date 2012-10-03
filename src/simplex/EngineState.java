package simplex;

import java.io.IOException;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import simplex.level.Level;
import simplex.level.LevelFileHandler;

/**
 *
 * @author Emil
 * @author Samuel
 */
public abstract class EngineState extends BaseState {

    protected int height = 16;
    protected int width = 16;
    protected Level level = new Level();

    public EngineState(int stateId) {
        super(stateId);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
        renderGrid(gc, g);
        level.render(g);
    }

    private void renderGrid(GameContainer gc, Graphics g) {
        final int gwidth = gc.getWidth() / width;
        final int gheight = gc.getHeight() / height;
        // Draw grid
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Toggle colour
                g.setColor(((i + j) % 2 == 0) ? Color.darkGray : Color.gray);
                g.fillRect(j * gwidth, i * gheight, gwidth, gheight);
            }
        }
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
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        super.enter(container, game);
        try {
            LevelFileHandler lfh = new LevelFileHandler();
            level = lfh.loadLevel();
        } catch (IOException ex) {
            Logger.getLogger(GameState.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
