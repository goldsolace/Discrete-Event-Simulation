import java.util.concurrent.ArrayBlockingQueue;

/**
 * Abstract PlanarShape which are comparable with behaviour to calculate area and distance from origin.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class InterStageStorage implements ProductionUnit {

    private String name;
    private ArrayBlockingQueue<Item> itemQueue;
    private int in;
    private int out;

    public InterStageStorage(String name, int qMax) {
        this.name = name;
        itemQueue = new ArrayBlockingQueue<>(qMax);
        in = 0;
        out = 0;
    }

    public int getConnections(boolean isInput) {
        return isInput ? out : in;
    }

    public InterStageStorage connect(boolean isInput) {
        // Output of storage is used for input
        if (isInput) out++;
        else in++;
        return this;
    }

    public void disconnect(boolean isInput) {
        if (isInput) out--;
        else in--;
    }

    public boolean isFull() {
        return itemQueue.remainingCapacity() == 0;
    }

    public boolean isEmpty() {
        return itemQueue.isEmpty();
    }

    public Item poll() {
        return itemQueue.poll();
    }

    public boolean offer(Item item) {
        return itemQueue.offer(item);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Override toString
     */
    @Override
    public String toString() {
        return "Storage[" + name + "] - Capatity = " + itemQueue.size() + "/" + (itemQueue.size()+itemQueue.remainingCapacity());
    }
}