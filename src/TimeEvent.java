public class TimeEvent implements Comparable<TimeEvent> {

    private ProductionStage stage;
    private Item item;
    private double entryTime;
    private double exitTime;
    private double duration;
    private double remaining;

    public TimeEvent(ProductionStage stage, Item item, double entryTime, double duration) {
        this.stage = stage;
        this.item = item;
        this.entryTime = entryTime;
        exitTime = entryTime + duration;
        this.duration = duration;
        remaining = duration;
        this.item.addTimeEvent(this);
    }

    public ProductionStage getStage() {
        return stage;
    }

    public Item getItem() { return item; }

    public double getDuration() { return duration; }

    public double getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(double time) {
        entryTime = time;
    }

    public double getExitTime() {
        return exitTime;
    }

    public void setExitTime(double time) {
        exitTime = time;
    }

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
