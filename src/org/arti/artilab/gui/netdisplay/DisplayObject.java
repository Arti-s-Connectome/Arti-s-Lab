package org.arti.artilab.gui.netdisplay;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 * <p>public abstract class <b>DisplayObject</b><br>
 * extends {@link Object}</p>
 * 
 * <p>DisplayObject class is the base class for any drawable neural network display object.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22  
 */
public abstract class DisplayObject {
	// Display object size.
	protected static final double OBJECT_SIZE = 100.0;
	
	// Display object's rectangular bounds.
	protected Rectangle bounds;
	// Mouse hovering flag.
	protected boolean hovering;
	// Moving object flag.
	protected boolean moving;
	// Mouse right clicked flag.
	protected boolean rightClicked;
	// X display location.
	protected double x;
	// Mouse x location.
	protected double xMouse;
	// Y display location.
	protected double y;
	// Mouse y location.
	protected double yMouse;
	
	/**
	 * Default constructor. Creates a neural network display object.
	 */
	protected DisplayObject() {
		// Initialize variables
		bounds = new Rectangle();
		hovering = false;
		moving = false;
		rightClicked = false;
		x = 0.0;
		y = 0.0;
		xMouse = 0.0;
		yMouse = 0.0;
	}
	
	/**
	 * This is called when the neural network display object is initially created so that the object follows the mouse, since it does not have 
	 * an initial place on the display grid.
	 * @param xMouse - The x mouse coordinate.
	 * @param yMouse - The y mouse coordinate.
	 */
	public void created(double xMouse, double yMouse) {
		this.xMouse = xMouse;
		this.yMouse = yMouse;
		hovering = true;
		mouseDoubleClicked();
	}
	
	/**
	 * Draws this neural network display object at the specified coordinates using the specified graphics context.
	 * @param gc - The graphics context.
	 * @param x - The x coordinate.
	 * @param y - The y coordinate.
	 */
	public abstract void drawAt(GraphicsContext gc, double x, double y);
	
	/**
	 * Draws the neural network display object at the specified display grid coordinates using the specified graphics context.
	 * @param gc - The graphics context.
	 * @param xGrid - The x display grid coordinate.
	 * @param yGrid - The y display grid coordinate.
	 */
	public void drawToGrid(GraphicsContext gc, double xGrid, double yGrid) {
		drawAt(gc, xGrid, yGrid);
	}
	
	/**
	 * Draws this neural network display object at the specified mouse coordinates using the specified graphics context. This method is used 
	 * when this neural network display object is being moved.
	 * @param gc - The graphics context.
	 * @param xMouse - The x mouse coordinate.
	 * @param yMouse - The y mouse coordinate.
	 */
	public void drawMoving(GraphicsContext gc, double xMouse, double yMouse) {
		drawAt(gc, xMouse, yMouse);
	}
	
	/**
	 * Checks if the mouse is hovering over this neural network display object.
	 * @return True if the mouse is hovering over this, otherwise false.
	 */
	public boolean isHovering() {
		return hovering;
	}
	
	/**
	 * Checks if this neural network display object is being moved.
	 * @return True if moving, otherwise false.
	 */
	public boolean isMoving() {
		return moving;
	}
	
	/**
	 * Handles when the mouse is over this neural network display object and the left mouse button is clicked. The default behavior stops the
	 * movement of this neural network display object, if it was moving, and positions it at the specified grid cell coordinates.
	 * @param xCell - The grid cell's x coordinate.
	 * @param yCell - The grid cell's y coordinate.
	 */
	public void mouseClicked(double xCell, double yCell) {
		if (moving) {
			moving = false;
			x = xCell;
			y = yCell;
		}
	}
	
	/**
	 * Handles when the mouse if over this neural network display object and the left mouse button is double clicked. The default behavior 
	 * sets this neural network display object to moving, if it is not already moving and the mouse is double clicked over this neural network 
	 * display object.
	 */
	public void mouseDoubleClicked() {
		if (!moving && hovering)
			moving = true;
	}
	
	/**
	 * Handles when the mouse is over this neural network display object and the right mouse button is clicked.
	 */
	public void mouseRightClicked() {
		rightClicked = true;
	}
	
	/**
	 * Handles mouse movement. This updates the current mouse coordinates and checks if the mouse is hovering over this neural network display
	 * object.
	 * @param gc - The graphics context.
	 * @param xMouse - The x mouse coordinate.
	 * @param yMouse - The y mouse coordinate.
	 */
	public void mouseMoved(GraphicsContext gc, double xMouse, double yMouse) {
		this.xMouse = xMouse;
		this.yMouse = yMouse;
		
		hovering = bounds.contains(xMouse, yMouse);
		
		if (hovering)
			drawAt(gc, x, y);
	}
	
	/**
	 * Returns the x coordinate of this neural network display object.
	 * @return The x coordinate.
	 */
	public double x() {
		return x;
	}
	
	/**
	 * Returns the y coordinate of this neural network display object.
	 * @return The y coordinate.
	 */
	public double y() {
		return y;
	}
}
