package simplex;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mdes.oxy.Label;
import mdes.oxy.Panel;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;
import simplex.entity.Connection;
import simplex.entity.Node;
import simplex.entity.specification.ConsumerSpecification;
import simplex.entity.specification.NodeSpecification;
import simplex.util.GridCoord;
import simplex.util.ImageManager;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class GameState extends EngineState {

    private List<MouseOverArea> tempConnSwap = new LinkedList<>();
    private long startTime;
    private int clicks;
    private long endTime;

    public GameState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        super.init(gc, sbg);
        loadGui(gc, "GameGui.xml");

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);

        List<Connection> connections = level.getConnections();
        tempConnSwap.clear();
        for (Connection conn : connections) {
            Vector2f middle = conn.getStartNode().getPosition();
            tempConnSwap.add(new MouseOverArea(gc, ImageManager.connection_swap_icon, (int) middle.x, (int) middle.y));
            conn.swapDirection();
        }
        Panel panel = getGuiComponent("ScorePanel");
        panel.setVisible(false);
        startTime = System.currentTimeMillis();
        endTime = -1;
        clicks = 0;
    }

    @Override
    protected void renderContent(GameContainer gc, StateBasedGame sbg,
            Graphics g) {
        super.renderContent(gc, sbg, g);
        for (MouseOverArea moa : tempConnSwap) {
            moa.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {        
        super.update(gc, sbg, delta);
        if (delta == 0 || endTime != -1) {
            return;
        }
        
        if (levelIsDone()) {
            Panel panel = getGuiComponent("ScorePanel");
            endTime = (System.currentTimeMillis() - startTime) / 1000;
            this.<Label>getGuiComponent("time").setText("" + endTime);
            this.<Label>getGuiComponent("clicks").setText("" + clicks);
            panel.setVisible(true);
        }

        List<Connection> connections = level.getConnections();

        //TODO: Make the switching a part of connection
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
        } else if (Input.KEY_ESCAPE == key) {
            setNextState(Main.MAINMENUSTATE);
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        clicks++;
    }
    
    

    private boolean levelIsDone() {
        Map<GridCoord, Node> nodes = level.getNodes();
        //TODO: do a better check.
        for (Node node : nodes.values()) {
            NodeSpecification spec = node.getNodeSpecification();
            if (spec instanceof ConsumerSpecification
                    && !((ConsumerSpecification) spec).isHappy()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used by the GUI to switch to the next level.
     */
    public void goToNext() {
        setNextState(Main.NEXT_GAME);
    }

    /**
     * Used by the GUI to switch to main menu.
     */
    public void goToMain() {
        setNextState(Main.MAINMENUSTATE);
    }
}
