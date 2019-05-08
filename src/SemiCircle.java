/**
 * SemiCircle Class - Extension of PlanarShape defined by a center point and point on the semicircle
 * perpendicular to the base.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */
public class SemiCircle extends PlanarShape {

    private Point base;
    private Point arc;
    private Point extremityA;
    private Point extremityB;
    private double radius;

    /**
     * Constructs a semicirle from 2 points.
     *
     * @param base midpoint of the base
     * @param arc  point on the arc of the semicircle perpendicular to the base
     */
    public SemiCircle(Point base, Point arc) {
        this.base = base;
        this.arc = arc;
        // Radius is the distance between base and arc
        radius = base.distance(arc);

        // Calculate the endpoints of the base from normal vectors
        Point baseVector = new Point(arc.getX() - base.getX(), arc.getY() - base.getY());
        Point normalVectorA = new Point(-baseVector.getY(), baseVector.getX());
        Point normalVectorB = new Point(baseVector.getY(), -baseVector.getX());
        extremityA = new Point(base.getX() + normalVectorA.getX(), base.getY() + normalVectorA.getY());
        extremityB = new Point(base.getX() + normalVectorB.getX(), base.getY() + normalVectorB.getY());
    }

    /**
     * Calculate the area of the semicircle.
     *
     * @return double area given by 1/2 * Pi * r^2
     */
    @Override
    public double area() {
        return 0.5 * Math.PI * Math.pow(radius, 2);
    }

    /**
     * Calculate the distance from the origin
     *
     * @return double min origin distance of 4 points defining the semicircle.
     */
    @Override
    public double originDistance() {
        return Math.min(
                Math.min(base.originDistance(), arc.originDistance()),
                Math.min(extremityA.originDistance(), extremityB.originDistance())
        );
    }

    /**
     * Override toString to print the base and arc points of the semicircle.
     *
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SEMI=[");
        sb.append(base);
        sb.append(", ");
        sb.append(arc);
        sb.append("]: ");
        sb.append(String.format(FORMAT, area()));
        return sb.toString();
    }
}
