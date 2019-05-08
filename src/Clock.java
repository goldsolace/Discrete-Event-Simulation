import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Clock {
    private double time;
    private Queue<TimeEvent> timeEventQueue;

    public Clock(int maxEvents) {
        time = 0;
        timeEventQueue = new PriorityBlockingQueue<>(maxEvents);
    }

    public void tick(double amount) {
        time += amount;
    }

    public double getTime() {
        return time;
    }
}
