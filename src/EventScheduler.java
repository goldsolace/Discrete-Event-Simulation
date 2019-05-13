import java.util.concurrent.PriorityBlockingQueue;

public class EventScheduler {

    private Clock clock;
    private BoundedPriorityQueue<TimeEvent> timeEventQueue;

    public EventScheduler(int maxEvents) {
        clock = new Clock();
        timeEventQueue = new BoundedPriorityQueue<>(maxEvents);
    }

    public Clock getClock() {
        return clock;
    }

    public void schedule(TimeEvent timeEvent) {
        if (timeEventQueue.isFull()) throw new IllegalStateException();
            timeEventQueue.offer(timeEvent);
    }

    public boolean canExecute() {
        return !timeEventQueue.isEmpty();
    }

    public boolean canSchedule() {
        return !timeEventQueue.isFull();
    }

    public TimeEvent execute() {
        if (timeEventQueue.isEmpty()) throw new IllegalStateException();
        double duration = timeEventQueue.peek().getRemaining();
        clock.tick(duration);
        for (TimeEvent timeEvent : timeEventQueue)
            timeEvent.elapse(duration);
        return timeEventQueue.poll();
    }

    public void printStatus() {
        for (TimeEvent e : timeEventQueue)
            System.out.println(e);
    }

    public void extend(TimeEvent timeEvent) {
        double min = Double.POSITIVE_INFINITY;
        for (TimeEvent e : timeEventQueue) {
            if (e.getStage().getInput() == timeEvent.getStage().getOutput()) {
                //if (ProductionLine.block > 2) System.out.println(e);
                min = Math.min(min, e.getRemaining());
            }
        }
        timeEvent.extend(min);
        //if (!timeEventQueue.isEmpty())
            //timeEvent.extend(timeEventQueue.peek().getRemaining());
        schedule(timeEvent);
    }
}
