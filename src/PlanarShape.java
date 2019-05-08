/**
 * Abstract PlanarShape which are comparable with behaviour to calculate area and distance from origin.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public abstract class PlanarShape implements Comparable<PlanarShape> {
    protected static final String FORMAT = "%5.2f";

    /**
     * Calculate the area of the planarShape
     *
     * @return double area
     */
    public abstract double area();

    /**
     * Calculate the distance from the origin
     *
     * @return double distance to the origin
     */
    public abstract double originDistance();

    /**
     * Override compareTo allowing comparison of planarShape objects
     */
    @Override
    public int compareTo(PlanarShape other) {
        // If within 0.5% of each other's area then compare by distance to origin
        double threshold = Math.min(this.area(), other.area()) * 0.005;
        if (Math.abs(this.area() - other.area()) <= threshold) {
            return Double.compare(this.originDistance(), other.originDistance());
        }
        // Compare by area
        return Double.compare(this.area(), other.area());
    }

    /**
     * Override toString
     */
    @Override
    public abstract String toString();
}
