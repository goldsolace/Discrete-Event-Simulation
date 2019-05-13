public class Clock {
    private double time;

    public Clock() {
        time = 0;
    }

    public void tick(double amount) {
        time += amount;
    }

    public double getTime() {
        return time;
    }
}
