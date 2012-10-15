package simplex;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mdes.oxy.Desktop;
import mdes.oxy.Label;
import mdes.oxy.OxyException;
import mdes.oxy.Panel;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
    public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
        super.enter(gc, game);

        List<Connection> connections = level.getConnections();
        tempConnSwap.clear();
        for (Connection conn : connections) {
            conn.swapDirection();
            final Vector2f startPos = conn.getStartNode().getPosition();
            final Vector2f endPos = conn.getEndNode().getPosition();

            Vector2f middle = (startPos.copy().add(endPos)).scale(0.5f);

            Image img;
            if ((startPos.x - endPos.x) < 0) {
                img = ImageManager.connection_swap_right_icon;
            } else {
                img = ImageManager.connection_swap_left_icon;
            }

            tempConnSwap.add(new MouseOverArea(gc, img, (int) middle.x, (int) middle.y));
        }
        Panel panel = getGuiComponent("ScorePanel");
        panel.setVisible(false);
        startTime = System.currentTimeMillis();
        endTime = -1;
        clicks = 0;
    }

    @Override
    protected Desktop loadGui(GameContainer gc) throws SlickException {
        try {
            return Desktop.parse(GameState.this, gc, "gui/GameGui.xml");
        } catch (OxyException e) {
            System.err.println(e);
            throw new SlickException("Can't load gui");
        }
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
                final MouseOverArea moa = tempConnSwap.get(i);
                if (moa.isMouseOver()) {
                    final Connection conn = connections.get(i);
                    conn.swapDirection();
                    Vector2f startPos = conn.getStartNode().getPosition();
                    Vector2f endPos = conn.getEndNode().getPosition();
                    Image img;
                    if ((startPos.x - endPos.x) < 0) {
                        img = ImageManager.connection_swap_right_icon;
                    } else {
                        img = ImageManager.connection_swap_left_icon;
                    }
                    moa.setNormalImage(img);
                    moa.setMouseOverImage(img);
                }
            }
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        /*if (Input.KEY_P == key || Input.KEY_PAUSE == key) {
         gc.setPaused(!gc.isPaused());
         } else */ if (Input.KEY_ESCAPE == key) {
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
        saveHighScore();
        setNextState(Main.NEXT_GAME);
    }

    /**
     * Used by the GUI to switch to main menu.
     */
    public void goToMain() {
        saveHighScore();
        setNextState(Main.MAINMENUSTATE);
    }

    private void saveHighScore() {
        String time = this.<Label>getGuiComponent("time").getText();
        String clicks = this.<Label>getGuiComponent("clicks").getText();        
        HighScore.save(level.getName(), time, clicks);
    }
}
