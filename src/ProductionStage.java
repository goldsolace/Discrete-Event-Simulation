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
    private double stateTime;
    private State state;
    private Map<State, Double> stateStats;
    private List<ProductionStage> predecessors;
    private List<ProductionStage> successors;
    private InterStageStorage input;
    private InterStageStorage output;

    public ProductionStage(String name) {
        this.name = name;
        predecessors = new ArrayList<>();
        successors = new ArrayList<>();
        input = null;
        output = null;
        stateStats = new HashMap<>();
        updateState(State.STARVED);
    }

    public Item consume(double time) {
        switch (state) {
            case IDLE:
            case STARVED:
                return getNextItem(time);
            case BLOCKED:
                if (!output.isFull())
                    return getNextItem(time);
        }
        return null;
    }

    public boolean produce(Item item, double time) {
        if (output == null) return false;
        updateState(State.IDLE, time);
        return output.offer(item, time);
    }

    private Item getNextItem(double time) {
        Item item;
        if (input == null) {
            item = new Item();
            //System.out.println("Item["+item.getId()+"] ---- CREATED");
        } else if (input.isEmpty()) {
            updateState(State.STARVED, time);
            return null;
        } else {
            item = input.poll(time);
        }
        updateState(State.PRODUCING, time);
        return item;
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
