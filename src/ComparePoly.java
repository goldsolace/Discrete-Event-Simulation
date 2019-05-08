/**
 * Interface for comparing polygons
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 26-02-2019
 */

public interface ComparePoly {
	/**
	 * Compare another polygon with this polygon
	 * < 0 if this polygon is lesser than other
	 * 0 if they are equal
	 * > 0 if this polygon is greater than other
	 *
	 * @param other another poylgon to compare with this polygon
	 */
	double compareTo(Polygon other);
}
