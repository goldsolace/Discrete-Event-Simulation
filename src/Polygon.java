/**
 * Polygon Class - Extension of PlanarShape defined by an array of points with closure (first point = last point).
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class Polygon extends PlanarShape {

	private Point[] points;

	/**
	 * Constructs a polygon from an array of points
	 *
	 * @param points list of points that define the polygon
	 */
	public Polygon(Point[] points) {
		this.points = points;
	}

	/**
	 * Calculate the area of the polygon
	 *
	 * @return double area given by formula in document specification
	 */
	@Override
	public double area() {
		double sum = 0;
		for (int i = 0; i < points.length-1; i++) {
			sum += (points[i+1].getX() + points[i].getX()) * (points[i+1].getY() - points[i].getY());
		}
		return 0.5 * Math.abs(sum);
	}

	/**
	 * Calculate the distance from the origin
	 *
	 * @return double min origin distance of all points of polygon
	 */
	@Override
	public double originDistance() {
		int minDistIndex = 0;
		for (int i = 1; i < points.length-1; i++) {
			if (points[i].originDistance() < points[minDistIndex].originDistance())
				minDistIndex = i;
		}
		return points[minDistIndex].originDistance();
	}

	/**
	 * Override toString to print the points and area of the polygon
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("POLY=[");
		for (int i = 0; i < points.length-1; i++) {
			sb.append(points[i]);
			if (i == points.length-2) {
				sb.append("]");
				break;
			}
			sb.append(", ");
		}
		sb.append(": ");
		sb.append(String.format(FORMAT, area()));
		return sb.toString();
	}
}
