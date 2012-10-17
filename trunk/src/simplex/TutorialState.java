package simplex;

/**
 *
 * @author Emil
 * @author Samuel
 */
public class TutorialState extends GameState {

    public TutorialState(int stateId) {
        super(stateId);
    }

    /**
     * Used by the GUI to switch to the next level.
     */
    @Override
    public void goToNext() {
        setNextState(Main.NEXT_TUTORIAL);
    }
}
