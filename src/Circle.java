/**
 * Circle Class - Extension of PlanarShape defined by a center point and radius.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class Circle extends PlanarShape {

    private Point centre;
    private double radius;

    /**
     * Constructs a circle from a point and radius
     *
     * @param centre point of the circle
     * @param radius of the circle
     */
    public Circle(Point centre, double radius) {
        this.centre = centre;
        this.radius = radius;
    }

    /**
     * Calculate the area of the circle
     *
     * @return double area given by Pi * r^2
     */
    @Override
    public double area() {
        return Math.PI * Math.pow(radius, 2);
    }

    /**
     * Calculate the distance from the origin
     *
     * @return double centre origin distance - radius
     */
    @Override
    public double originDistance() {
        return centre.originDistance() - radius;
    }

    /**
     * Override toString to print the center and radius of the circle.
     *
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CIRC=[");
        sb.append(centre);
        sb.append(", ");
        sb.append(radius);
        sb.append("]: ");
        sb.append(String.format(FORMAT, area()));
        return sb.toString();
    }
}
