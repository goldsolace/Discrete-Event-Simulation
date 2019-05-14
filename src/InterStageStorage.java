import java.util.HashMap;
import java.util.NoSuchElementException;
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
    private double cumulativeTime;
    private double cumulativeItems;
    private double cumulativeOperations;
    private HashMap<Item, Double> timer;

    public InterStageStorage(String name, int qMax) {
        this.name = name;
        itemQueue = new ArrayBlockingQueue<>(qMax);
        in = 0;
        out = 0;
        cumulativeTime = 0;
        timer = new HashMap<>();
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

    public Item poll(double time) {
        if (isEmpty()) throw new NoSuchElementException("Storage is empty");
        Item item = itemQueue.poll();
        cumulativeTime += timer.getOrDefault(item, 0D);
        timer.remove(item);
        cumulativeItems += itemQueue.size();
        cumulativeOperations++;
        return item;
    }

    public boolean offer(Item item, double time) {
        if (isFull()) throw new IllegalStateException("Storage is full");
        timer.put(item, time);
        itemQueue.offer(item);
        cumulativeItems += itemQueue.size();
        cumulativeOperations++;
        return true;
    }

    public double getCumulativeTime() {
        return cumulativeTime;
    }

    public double getAverageItems() {
        System.out.println(cumulativeItems);
        System.out.println(cumulativeOperations);
        return cumulativeItems / cumulativeOperations;
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