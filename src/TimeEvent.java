public class TimeEvent implements Comparable<TimeEvent> {

    private ProductionStage stage;
    private Item item;
    private double duration;
    private double remaining;

    public TimeEvent(ProductionStage stage, Item item, double duration) {
        this.stage = stage;
        this.item = item;
        this.duration = duration;
        remaining = duration;
    }

    public ProductionStage getStage() {
        return stage;
    }

    public Item getItem() { return item; }

    public double getDuration() { return duration; }

    public double getRemaining() {
        return remaining;
    }

    public void elapse(double duration) {
        remaining -= Math.min(remaining, duration);
    }

    public void extend(double duration) {
        remaining += duration;
    }

    @Override
    public int compareTo(TimeEvent o) {
        return Double.compare(remaining, o.remaining);
    }

    @Override
    public String toString() {
        return "TimeEvent["+stage.getName()+", "+item.getId()+"] - " + remaining + " / " + duration;
    }
}
