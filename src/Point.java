/**
 * Point Class
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 26-02-2019
 */

public class Point {
    private static final String FORMAT = "%4.2f";

    private double x;
    private double y;
    private double originDistance;

    /**
     * Constructs a point from parameters.
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        originDistance = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * Returns the x coordinate of the point
     *
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the point
     *
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the Euclidean originDistance from this point to the origin
     *
     * @return originDistance
     */
    public double originDistance() {
        return originDistance;
    }

    /**
     * Returns the Euclidean originDistance from this point to another point
     *
     * @return double
     */
    public double distance(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Override toString to print point in format (x,y)
     *
     * @return string
     */
    @Override
    public String toString() {
        return "(" + String.format(FORMAT, x) + "," + String.format(FORMAT, y) + ")";
    }


}

