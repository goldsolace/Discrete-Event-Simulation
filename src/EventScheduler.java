public class EventScheduler {

    private double maxTime;
    private Clock clock;
    private BoundedPriorityQueue<TimeEvent> timeEventQueue;

    /**
     * Create an EventScheduler with max time and max events.
     *
     * @param maxTime the event scheduler can schedule events for
     * @param maxEvents the event scheduler can hold
     */
    public EventScheduler(double maxTime, int maxEvents) {
        this.maxTime = maxTime;
        clock = new Clock();
        timeEventQueue = new BoundedPriorityQueue<>(maxEvents);
    }

    /**
     * Returns the scheduler's clock.
     *
     * @return clock
     */
    public Clock clock() {
        return clock;
    }

    /**
     * Add a time event to the event scheduler.
     *
     * @param timeEvent to be scheduled
     */
    public void schedule(TimeEvent timeEvent) {
        if (timeEventQueue.isFull()) throw new IllegalStateException();
        for (TimeEvent e : timeEventQueue) {
            if (e.getStage() == timeEvent.getStage()) {
                System.out.println(e.getStage());
                System.out.println(e);
                System.out.println(timeEvent);
            }
        }
        timeEventQueue.offer(timeEvent);
    }

    /**
     * Check if any events are scheduled.
     *
     * @return true if can execute scheduler
     */
    public boolean canExecute() {
        return !timeEventQueue.isEmpty();
    }

    /**
     * Check if scheduler can schedule any more events.
     *
     * @return true if can schedule an event
     */
    public boolean canSchedule() {
        return !timeEventQueue.isFull();
    }

    /**
     * Execute and return the next scheduled event.
     *
     * @return TimeEvent that
     */
    public TimeEvent execute() {
        if (timeEventQueue.isEmpty()) throw new IllegalStateException();
        double duration = Math.min(Math.max(maxTime - clock.getTime(), 0), timeEventQueue.peek().getRemaining());
        clock.tick(duration);
        for (TimeEvent timeEvent : timeEventQueue)
            timeEvent.elapse(duration);
        return timeEventQueue.poll();
    }

    /**
     * Block a time event based on it's stage and reschedule.
     *
     * @param timeEvent to be blocked
     */
    public void block(TimeEvent timeEvent) {
        double extendBy = Double.POSITIVE_INFINITY;
        // Block event until one of the event's stage successors completes
        if (timeEvent.getStage().getOutput() != null)
            // Find first successor which completes
            for (TimeEvent e : timeEventQueue) {
                if (e.getStage().getInput() == timeEvent.getStage().getOutput())
                    extendBy = Math.min(extendBy, e.getRemaining());
            }
            // Block event until next event completes
        else if (canExecute())
            extendBy = timeEventQueue.peek().getRemaining();
        // Extend time event duration for length of block
        timeEvent.extend(extendBy);
        schedule(timeEvent);
    }

    public void printStatus() {
        for (TimeEvent e : timeEventQueue)
            System.out.println(e);
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        for (TimeEvent e : timeEventQueue)
            sb.append(e).append("\n");
        return sb.toString();
    }
}
