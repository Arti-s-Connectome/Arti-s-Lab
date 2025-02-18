package org.arti.artislab.gui.netdisplay;

import java.util.ArrayList;

/**
 * <p>public class <b>LineConnector</b><br>
 * extends {@link Object}</p>
 * 
 * <p>LineConnector class represents a line connector point on a neural network display object. LineConnectors have an identifier value that
 * is used to by other LineConnectors to define what a LineConnector can connect to.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class LineConnector {
	/**
	 * Excitatory input identifier value.
	 */
	public static final int EXCITATORY_IN_ID = 0x00000001;
	/**
	 * Excitatory output identifier value.
	 */
	public static final int EXCITATORY_OUT_ID = 0x00000101;
	/**
	 * Gap junction input identifier value.
	 */
	public static final int GAP_JUNCTION_IN_ID = 0x00000002;
	/**
	 * Gap junction output identifier value.
	 */
	public static final int GAP_JUNCTION_OUT_ID = 0x00000102;
	/**
	 * Inhibitory input identifier value.
	 */
	public static final int INHIBITORY_IN_ID = 0x00000003;
	/**
	 * Inhibitory output identifier value.
	 */
	public static final int INHIBITORY_OUT_ID = 0x00000103;
	
	// Line connector id's that this can connect to.
	private ArrayList<Integer> connectable;
	// Line connector's identifier value.
	private int id;
	// Line connector's x coordinate.
	private double x;
	// Parent's x coordinate.
	private double xParent;
	// Line connector's y coordinate.
	private double y;
	// Parent's y coordinate.
	private double yParent;
	
	/**
	 * Default constructor. Creates a line connector with an id of 0 that is allowed to connect to other line connectors with an id of 0.
	 */
	public LineConnector() {
		// Initialize variables
		connectable = new ArrayList<Integer>();
		connectable.add(0);
		id = 0;
		x = 0.0;
		xParent = 0.0;
		y = 0.0;
		yParent = 0.0;
	}
	
	/**
	 * Creates a line connector with the specified id.
	 * @param id - The identifier value.
	 */
	public LineConnector(int id) {
		// Initialize variables
		connectable = new ArrayList<Integer>();
		this.id = id;
		x = 0.0;
		xParent = 0.0;
		y = 0.0;
		yParent = 0.0;
	}
	
	/**
	 * Creates a line connector with the specified id and with the specified list of id's that this can connect to.
	 * @param id - The identifier value.
	 * @param connectable - The list of connectable identifier values.
	 */
	public LineConnector(int id, int ... connectable) {
		// Initialize variables
		this.connectable = new ArrayList<Integer>();
		
		for (int i = 0; i < connectable.length; ++i)
			this.connectable.add(connectable[i]);
		
		this.id = id;
		x = 0.0;
		xParent = 0.0;
		y = 0.0;
		yParent = 0.0;
	}
	
	/**
	 * Adds the specified identifier value to the list of connectable identifier values, if it is not already in the list.
	 * @param id - The identifier value to add.
	 */
	public void addConnectable(int id) {
		if (!connectable.contains(id))
			connectable.add(id);
	}
	
	/**
	 * Checks if the specified identifier value is one that this line connector can connect to.
	 * @param id - The identifier value to check for.
	 * @return True if id is connectable, otherwise false.
	 */
	public boolean isConnectable(int id) {
		return connectable.contains(id);
	}
	
	/**
	 * Returns the identifier value of this line connector.
	 * @return The id value.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Removes the specified identifier value from the list of connectable identifier values.
	 * @param id - The identifier value to remove.
	 * @return True if id was removed, otherwise false.
	 */
	public boolean removeConnectable(int id) {
		return connectable.remove(Integer.valueOf(id));
	}
	
	/**
	 * Returns this line connector's parent's x coordinate.
	 * @return The parent's x coordinate.
	 */
	public double parentX() {
		return xParent;
	}
	
	/**
	 * Returns this line connector's parent's y coordinate.
	 * @return The parent's y coordinate.
	 */
	public double parentY() {
		return yParent;
	}
	
	/**
	 * Set this line connector's parent's x coordinate.
	 * @param x - The new x coordinate.
	 */
	public void setParentX(double x) {
		this.xParent = x;
	}
	
	/**
	 * Set this line connector's parent's y coordinate.
	 * @param y - The new y coordinate.
	 */
	public void setParentY(double y) {
		this.yParent = y;
	}
	
	/**
	 * Sets this line connector's x coordinate.
	 * @param x - The new x coordinate.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets this line connector's y coordinate.
	 * @param y - The new y coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Returns this line connector's x coordinate.
	 * @return The x coordinate.
	 */
	public double x() {
		return x;
	}
	
	/**
	 * Returns this line connector's y coordinate.
	 * @return The y coordinate.
	 */
	public double y() {
		return y;
	}
}
