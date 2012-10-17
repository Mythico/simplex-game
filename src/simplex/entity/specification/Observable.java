package simplex.entity.specification;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import simplex.entity.Resource;

/**
 * A variant on the observable pattern which notifies each observer with
 * different resources. It will only notify as many observers as there are
 * resources. When more resources are added it will continue where it left of.
 *
 * @author Emil
 * @author Samuel
 */
public abstract class Observable {

    private final List<Observer> observers = new LinkedList<>();
    private ListIterator<Observer> iter = observers.listIterator();

    /**
     * Adds an observer to this observable object.
     *
     * @param obs Tha observer to be added.
     */
    public void addObserver(Observer obs) {
        iter.add(obs);
    }

    /**
     * Removes an observer from this observable object.
     *
     * @param obs The observer to be removed.
     */
    public void deleteObserver(Observer obs) {
        int index = iter.nextIndex();
        iter = observers.listIterator();
        while (iter.hasNext()) {
            int next_index = iter.nextIndex();
            if (obs.equals(iter.next())) {
                iter.remove();
                if(next_index <= index){
                    index--;
                }
                break;
            }
        }
        iter = observers.listIterator(index);
    }

    /**
     * Notify the observers in a sequential order and giving each of them a
     * different resource.
     *
     * @param resources The resources that will be redistributed.
     */
    public void notifyObservers(Queue<Resource> resources) {
        while (!resources.isEmpty()) {
            notifyObservers(resources.poll());
        }
    }

    /**
     * Notify the observers in a sequential order and giving them a resource.
     * The order will be saved between calls.
     *
     * @param resource The resource that will be redistributed.
     */
    public void notifyObservers(Resource resource) {
        if (resource == null || observers.isEmpty()) {
            return;
        }
        Observer obs;
        if (!iter.hasNext()) {
            iter = observers.listIterator();
        }
        obs = iter.next();
        obs.update(resource);

    }
}
