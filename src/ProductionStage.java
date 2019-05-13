import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract PlanarShape which are comparable with behaviour to calculate area and distance from origin.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class ProductionStage implements ProductionUnit {

    private static Random r = new Random(5);

    private String name;
    private State state;
    private List<ProductionStage> predecessors;
    private List<ProductionStage> successors;
    private InterStageStorage input;
    private InterStageStorage output;
    private double M;
    private double N;

    public ProductionStage(String name, double mean, double range) {
        this.name = name;
        predecessors = new ArrayList<>();
        successors = new ArrayList<>();
        input = null;
        output = null;
        M = mean;
        N = range;
        state = State.STARVED;
    }

    private double generateDuration() {
        if (true) return 1000;
        return M + (N * (r.nextDouble() - 0.5));
    }

    public Item consume() {
        switch (state) {
            case IDLE:
            case STARVED:
                return getNextItem();
            case BLOCKED:
                if (!output.isFull())
                    return getNextItem();
        }
        return null;
    }

    public void produce(Item item) {
        state = State.IDLE;
        output.offer(item);
    }

    private Item getNextItem() {
        Item item;
        if (input == null) {
            item = new Item();
            //System.out.println("Item["+item.getId()+"] ---- CREATED");
        } else if (input.isEmpty()) {
            state = State.STARVED;
            return null;
        } else {
            item = input.poll();
        }
        state = State.PRODUCING;
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
        this.state = state;
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
        BLOCKED,
        PRODUCING,
        STARVED
    }
}
