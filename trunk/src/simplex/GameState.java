/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Connection;
import simplex.entity.ConsumerSpecification;
import simplex.entity.Node;
import simplex.entity.NodeSpecification;
import simplex.level.Level;
import simplex.level.LevelFileHandler;
import simplex.util.GridCoord;
import simplex.util.ImageManager;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class GameState extends BasicGameState {

    private final int stateId;
    private int width = 16;
    private int height = 16;
    private Level level = new Level();
    private List<MouseOverArea> tempConnSwap = new LinkedList<>();
    private GameContainer gc;
    private int nextState = Main.GAMESTATE;

    public GameState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public int getID() {
        return stateId;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        this.gc = gc;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        try {
            LevelFileHandler lfh = new LevelFileHandler("level.yml");
            level = lfh.loadLevel();
        } catch (IOException ex) {
            Logger.getLogger(GameState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        List<Connection> connections = level.getConnections();
        tempConnSwap.clear();
        for (Connection conn : connections) {
            Vector2f middle = conn.getStartNode().getPosition();
            tempConnSwap.add(new MouseOverArea(gc,
                    ImageManager.connection_swap_icon,
                    (int) middle.x, (int) middle.y));
            conn.swapDirection();
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {

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


        level.render(g);

        for (MouseOverArea moa : tempConnSwap) {
            moa.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        if (levelIsDone()) {
            nextState = Main.NEXT_GAME;
        }

        if (delta == 0) {
            return;
        }

        if (level != null) {
            level.update(delta);
        }

        if (nextState != Main.GAMESTATE) {
            int state = nextState;
            nextState = Main.GAMESTATE;
            sbg.enterState(state);
        }

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            sbg.enterState(Main.MAINMENUSTATE);
        }


        List<Connection> connections = level.getConnections();
        if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            for (int i = 0; i < tempConnSwap.size(); i++) {
                if (tempConnSwap.get(i).isMouseOver()) {
                    connections.get(i).swapDirection();
                }
            }
        }

    }

    @Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_P == key || Input.KEY_PAUSE == key) {
            gc.setPaused(!gc.isPaused());
        }
    }

    private boolean levelIsDone() {        
        Map<GridCoord, Node> nodes = level.getNodes();
        //TODO: do a better check.
        for (Node node : nodes.values()) {
            NodeSpecification spec = node.getNodeSpecification();
            if (spec instanceof ConsumerSpecification && !((ConsumerSpecification) spec).isHappy()) {
                return false;
            }
        }
        return true;
    }
}
