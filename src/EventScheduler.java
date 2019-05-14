public class EventScheduler {

    private double maxTime;
    private Clock clock;
    private BoundedPriorityQueue<TimeEvent> timeEventQueue;

    public EventScheduler(double maxTime, int maxEvents) {
        this.maxTime = maxTime;
        clock = new Clock();
        timeEventQueue = new BoundedPriorityQueue<>(maxEvents);
    }

    public Clock getClock() {
        return clock;
    }

    public void schedule(TimeEvent timeEvent) {
        System.out.println(timeEventQueue.size());
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
        double duration = Math.min(maxTime - clock.getTime(), timeEventQueue.peek().getRemaining());
        clock.tick(duration);
        for (TimeEvent timeEvent : timeEventQueue)
            timeEvent.elapse(duration);
        return timeEventQueue.poll();
    }

    public void block(TimeEvent timeEvent) {
        double extendBy = Double.POSITIVE_INFINITY;
        if (timeEvent.getStage().getParallelism() > 1)
            for (TimeEvent e : timeEventQueue) {
                if (e.getStage().getInput() == timeEvent.getStage().getOutput()) {
                    extendBy = Math.min(extendBy, e.getRemaining());
                }
            }
        else
            if (canExecute())
                extendBy = timeEventQueue.peek().getRemaining();
        timeEvent.extend(extendBy);
        schedule(timeEvent);
    }

    public void printStatus() {
        for (TimeEvent e : timeEventQueue)
            System.out.println(e);
    }
}
