import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract PlanarShape which are comparable with behaviour to calculate area and distance from origin.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class ProductionStage implements ProductionUnit {

    private String name;
    private List<ProductionStage> predecessors;
    private List<ProductionStage> successors;
    private InterStageStorage input;
    private InterStageStorage output;
    private Item item;
    private double stateTime;
    private State state;
    private Map<State, Double> stateStats;

    public ProductionStage(String name) {
        this.name = name;
        predecessors = new ArrayList<>();
        successors = new ArrayList<>();
        input = null;
        output = null;
        item = null;
        stateStats = new HashMap<>();
        updateState(State.STARVED);
    }

    /**
     * Production stage consumes an item it wishes to process depending on it's current state.
     *
     * @param time current time consume is being called
     * @return Item to consume
     */
    public Item consume(double time) {
        switch (state) {
            case IDLE:
            case STARVED:
                getNextItem(time);
                break;
            case BLOCKED:
                // Check if can unblock
                if (output == null || !output.isFull())
                    getNextItem(time);
                break;
            case PRODUCING:
                return null;
        }
        return item;
    }

    /**
     * Production stage produces an item it has finished processing.
     *
     * @param time
     * @return
     */
    public void produce(double time) {
        if (output == null || item == null) return;
        updateState(State.IDLE, time);
        output.offer(item, time);
        item = null;
    }

    /**
     *
     *
     * @param time
     */
    private void getNextItem(double time) {
        if (input == null) {
            updateState(State.PRODUCING, time);
            item = new Item();
        } else if (input.isEmpty()) {
            updateState(State.STARVED, time);
            item = null;
        } else {
            updateState(State.PRODUCING, time);
            item = input.poll(time);
        }
    }

    public List<ProductionStage> getPredecessors() {
        return predecessors;
    }

    public List<ProductionStage> getSuccessors() {
        return successors;
    }

    public void addPredecessor(ProductionStage stage) {
        predecessors.add(stage);
    }

    public void addSuccessor(ProductionStage stage) {
        if (stage == null) {
            System.out.println(" s");
        }
        successors.add(stage);
    }

    public InterStageStorage getInput() {
        return input;
    }

    public int getParallelism() {
        return input == null ? 1 : input.getConnections(true);
    }

    public InterStageStorage getOutput() {
        return output;
    }

    public void setInput(InterStageStorage storage) {
        input = storage.connect(true);
    }

    public void setOutput(InterStageStorage storage) {
        output = storage.connect(false);
    }

    @Override
    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public void updateState(State state) {
        updateState(state, 0D);
    }

    public void updateState(State state, double time) {
        if (this.state != null)
            stateStats.put(this.state, stateStats.getOrDefault(this.state, 0D) + time - stateTime);
        stateTime = time;
        if (this.state == state) return;
        this.state = state;

    }

    public Map<State, Double> getStateStats() {
        return stateStats;
    }

    /**
     * Override toString
     */
    @Override
    public String toString() {
        return "ProductionStage[" + name + "] - " + state;
    }

    public enum State {
        IDLE,
        PRODUCING,
        STARVED,
        BLOCKED
    }
}
