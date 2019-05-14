import java.util.ArrayList;
import java.util.List;

public class Item {

    private static int itemCount = 0;

    private int id;
    private double entryTime;
    private List<TimeEvent> timeEvents;
    private double exitTime;

    public Item() {
        id = itemCount++;
        this.entryTime = 0;
        this.exitTime = 0;
        timeEvents = new ArrayList<>();
    }

    public Item(double entryTime) {
        this();
        this.entryTime = entryTime;
    }

    public int getId() { return id; }

    public void setEntryTime(double time) {
        this.entryTime = time;
    }

    public void setExitTime(double time) {
        this.exitTime = time;
    }

    public List<TimeEvent> getTimeEvents() { return timeEvents; }

    public boolean addTimeEvent(TimeEvent timeEvent) { return timeEvents.add(timeEvent); }

    public String getPath() {
        StringBuilder sb = new StringBuilder();
        for (TimeEvent timeEvent : timeEvents) {
            sb.append(timeEvent.getStage().getName());
            if (timeEvent.getExitTime() != timeEvent.getItem().exitTime) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}
