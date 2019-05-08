import java.util.ArrayList;
import java.util.List;

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

    public ProductionStage(String name) {
        this.name = name;
        predecessors = new ArrayList<>();
        successors = new ArrayList<>();
        input = null;
        output = null;
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

    public InterStageStorage getOutput() {
        return output;
    }

    public void setInput(InterStageStorage storage) {
        input = storage;
    }

    public void setOutput(InterStageStorage storage) {
        output = storage;
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
        return getName();
    }
}
