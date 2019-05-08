import java.util.*;

/**
 * Station class to act as a vertex in a rail network graph. It has a unique identifying
 * name+line, list of connecting edges. The station also stores an optimal path, time and changes
 * to be used in dijkstras algorithm and a crtieria for comparision.
 * 
 * @author Ashwin Deen Masi, Brice Purton
 * @studentID 3163458, 3180044
 * @lastModified: 26-10-2018
 */

public class Station implements Comparable<Station> {
	
	private String name;
	private String line;
	private ArrayList<Edge> edges;
	private ArrayList<Station> path;
	private int time;
	private int changes;
	private boolean criteria;

	/**
	 * Default Constructor.
	 */
	public Station() {
		name = "";
		line = "";
		edges = new ArrayList<Edge>();
		path = new ArrayList<Station>();
		criteria = true;
		time = Integer.MAX_VALUE;
		changes = 0;
	}
	
	/**
	 * Constructs an edge from parameters.
	 *
	 * @param name of the station
	 * @param line the station is on
	 * @param criteria true if optimise time, false for changes
	 */
	public Station(String name, String line, boolean criteria) {
		this();
		this.name = name;
		this.line = line;
		this.criteria = criteria;
		if (criteria) {
			time = Integer.MAX_VALUE;
			changes = 0;
		} else {
			changes = Integer.MAX_VALUE;
			time = 0;
		}
	}

	/**
	 * Returns name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns name.
	 *
	 * @return line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * Returns time.
	 *
	 * @return time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Set time.
	 *
	 * @param time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Returns change.
	 *
	 * @return change
	 */
	public int getChanges() {
		return changes;
	}

	/**
	 * Set change.
	 *
	 * @param change to set
	 */
	public void setChanges(int changes) {
		this.changes = changes;
	}

	/**
	 * Adds a connection to station.
	 *
	 * @param edge to be added
	 */
	public void addEdge(Edge e) {
		edges.add(e);
	}

	/**
	 * Returns edges.
	 *
	 * @return edges
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * Adds a station to path.
	 *
	 * @param s station to be added
	 */
	public void addToPath(Station s) {
		path.add(s);
	}

	/**
	 * Set the current path to this station from origin.
	 *
	 * @param path to be set
	 */
	public void setPath(ArrayList<Station> path) {
		this.path = path;
	}

	/**
	 * Returns path.
	 *
	 * @return path
	 */
	public ArrayList<Station> getPath() {
		return path;
	}

	/**
	 * Compare two stations.
	 *
	 * @param other station to compare to this
	 * 
	 * @return > 0 if station > other, 0 if equal, < 0 if station < other
	 */
	public int compareTo(Station other) {
		int time = Integer.compare(this.time, other.time);
		int changes = Integer.compare(this.changes, other.changes);
		if (criteria) {
			return time == 0 ? changes : time;
		} else {
			return changes == 0 ? time : changes;
		}	
	}

	/**
	 * Override toString to output station name and line.
	 *
	 * @return string
	 */
	@Override   
	public String toString() {
		return name+": "+line;
	}
}

