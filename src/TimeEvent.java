public class TimeEvent {

    private ProductionStage stage;
    private double duration;
    private double remainingDuration;

    public TimeEvent(ProductionStage stage, double duration) {
        this.stage = stage;
        this.duration = duration;
        remainingDuration = duration;
    }

    public ProductionStage getStage() {
        return stage;
    }

    public double getduration() {
        return duration;
    }

    public double getRemainingDuration() {
        return remainingDuration;
    }

    public void updateRemainingDuration(double duration) {
        remainingDuration -= duration;
    }
}
