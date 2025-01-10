package org.arti.artilab.gui.netdisplay;

import org.arti.artilab.gui.NetworkDisplay;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>Line</b><br>
 * extends {@link Object}</p>
 * 
 * <p>Line class represents a drawable line used to connect neural network display objects together.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class Line {
	/**
	 * Excitatory line color.
	 */
	public static final Color EXCITATORY_LINE_COLOR = Color.rgb(0, 192, 0);
	// Excitatory y coordinate.
	private static final double EXCITATORY_Y = 75.0;
	/**
	 * Gap junction line color.
	 */
	public static final Color GAP_JUNCTION_LINE_COLOR = Color.rgb(0, 192, 192);
	// Gap junction y coordinate.
	private static final double GAP_JUNCTION_Y = 25.0;
	/**
	 * Inhibitory line color.
	 */
	public static final Color INHIBITORY_LINE_COLOR = Color.rgb(192, 0, 0);
	// Inhibitory y coordinate.
	private static final double INHIBITORY_Y = 50.0;
	
	// Down direction.
	private static final int DOWN = 1;
	// No direction.
	private static final int NONE = 0;
	// Up direction.
	private static final int UP = 4;
	
	// Starting line connector.
	private LineConnector start;
	// Starting direction.
	private int dirStart;
	// Ending line connector.
	private LineConnector end;
	
	/**
	 * Creates a line that connectors the starting line connector to the ending line connector.
	 * @param start - The starting line connector.
	 * @param end - The ending line connector.
	 * @throws IllegalArgumentException - Thrown if the two line connectors cannot be connected to each other.
	 * @throws NullPointerException - Thrown if start or end is null.
	 */
	public Line(LineConnector start, LineConnector end) {
		// Check parameters
		if (start == null)
			throw new NullPointerException("Error: Line's starting line connector cannot be null.");
		
		if (end == null)
			throw new NullPointerException("Error: Line's ending line connector cannot be null.");
		
		if (!start.isConnectable(end.id()))
			throw new IllegalArgumentException("Error: Line's starting line connector cannot connect to the line's ending line connector.");
		
		// Initialize variables
		this.start = start;
		dirStart = NONE;
		this.end = end;
	}
	
	/**
	 * Draws this line starting from the starting line connector and ending at the ending line connector, avoiding filled grid cells.
	 * @param gc - The graphics context.
	 * @return True if a line was drawn, otherwise false if a clear path was not found.
	 */
	public boolean draw(GraphicsContext gc) {
		// Draw excitatory line
		if (start.id() == LineConnector.EXCITATORY_IN_ID || start.id() == LineConnector.EXCITATORY_OUT_ID) 
			drawLine(gc, EXCITATORY_LINE_COLOR, DisplayNeuron.EXCITATORY_X, EXCITATORY_Y);
		// Draw gap junction line
		else if (start.id() == LineConnector.GAP_JUNCTION_IN_ID || start.id() == LineConnector.GAP_JUNCTION_OUT_ID) 
			drawLine(gc, GAP_JUNCTION_LINE_COLOR, DisplayNeuron.GAP_JUNCTION_X, GAP_JUNCTION_Y);
		// Draw inhibitory line
		else if (start.id() == LineConnector.INHIBITORY_IN_ID || start.id() == LineConnector.INHIBITORY_OUT_ID) 
			drawLine(gc, INHIBITORY_LINE_COLOR, DisplayNeuron.INHIBITORY_X, INHIBITORY_Y);
		
		return true;
	}
	
	// Draws the line with the specified color and x/y coordinates
	private void drawLine(GraphicsContext gc, Color color, double x, double y) {
		// Calculate starting and ending grid cells
		Point2D startCell = new Point2D((int)(start.x() / NetworkDisplay.GRID_CELL_SIZE) + (start.x() < 0.0 ? -1 : 0), 
				(int)(start.y() / NetworkDisplay.GRID_CELL_SIZE) + (start.y() < 0.0 ? -1 : 0));
		Point2D endCell = new Point2D((int)(end.x() / NetworkDisplay.GRID_CELL_SIZE) + (end.x() < 0.0 ? -1 : 0), 
				(int)(end.y() / NetworkDisplay.GRID_CELL_SIZE) + (end.y() < 0.0 ? -1 : 0));
		
		// Determine starting direction
		if (start.y() > start.parentY())
			dirStart = DOWN;
		else
			dirStart = UP;
		
		// Setup line drawing
		double sx = start.x();
		double sy = start.y();
		double ex = end.x();
		double ey = end.y();
		gc.setLineDashes(null);
		gc.setLineWidth(3);
		
		// Set color
		gc.setStroke(color);
		
		// If starting going down
		if (dirStart == DOWN) {
			// If on the same vertical path
			if (endCell.getX() == startCell.getX()) {
				// If end cell if higher that the start cell, draw a loop
				if (endCell.getY() < startCell.getY()) {
					gc.beginPath();
					gc.moveTo(sx, sy);
					gc.quadraticCurveTo(sx, sy + DisplayObject.OBJECT_SIZE - y - 1.0, 
							sx - x, sy + DisplayObject.OBJECT_SIZE - y - 1.0);
					gc.stroke();
					
					gc.beginPath();
					gc.moveTo(ex, ey);
					gc.quadraticCurveTo(ex, ey - y - 1.0, ex - x, ey - y - 1.0);
					gc.stroke();
					
					gc.beginPath();
					gc.moveTo(sx - x, sy + DisplayObject.OBJECT_SIZE - y - 1.0);
					gc.bezierCurveTo(sx - x - DisplayObject.OBJECT_SIZE, 
							sy + DisplayObject.OBJECT_SIZE - y, 
							sx - x - DisplayObject.OBJECT_SIZE,
							ey - y - 1.0,
							ex - x, 
							ey - y - 1.0);
				}
				// If end cell is lower than the start cell, draw a line
				else {
					gc.beginPath();
					gc.moveTo(sx, sy);
					gc.lineTo(ex, ey);
				}
				
				gc.stroke();
			}
			// If end cell is to the left of the start cell
			else if (endCell.getX() < startCell.getX()) {
				// Draw initial start and end segments pointing toward each other
				gc.beginPath();
				gc.moveTo(sx, sy);
				gc.quadraticCurveTo(sx, sy + DisplayObject.OBJECT_SIZE - y - 1.0, 
						sx - x, sy + DisplayObject.OBJECT_SIZE - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(ex, ey);
				gc.quadraticCurveTo(ex, ey - y - 1.0, 
						ex + DisplayObject.OBJECT_SIZE - x, ey - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(sx - x, sy + DisplayObject.OBJECT_SIZE - y - 1.0);
				
				// If on the same horizontal path, draw a line
				if (endCell.getY() == startCell.getY())
					gc.lineTo(ex + DisplayObject.OBJECT_SIZE - x, ey - y - 1.0);
				// If on a different horizontal path, draw a curve
				else
					gc.bezierCurveTo(
							(sx - x + ex + DisplayObject.OBJECT_SIZE - x) / 2.0, 
							sy + DisplayObject.OBJECT_SIZE - y - 1.0, 
							(sx - x + ex + DisplayObject.OBJECT_SIZE - x) / 2.0, 
							ey - y - 1.0, 
							ex + DisplayObject.OBJECT_SIZE - x, 
							ey - y - 1.0);
				
				gc.stroke();
			}
			// If end cell is to the right of the start cell
			else {
				// Draw initial start and end segments pointing toward each other
				gc.beginPath();
				gc.moveTo(sx, sy);
				gc.quadraticCurveTo(sx, sy + DisplayObject.OBJECT_SIZE - y - 1.0, 
						sx + DisplayObject.OBJECT_SIZE - x, 
						sy + DisplayObject.OBJECT_SIZE - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(ex, ey);
				gc.quadraticCurveTo(ex, ey - y - 1.0, ex - x, ey - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(sx + DisplayObject.OBJECT_SIZE - x, 
						sy + DisplayObject.OBJECT_SIZE - y - 1.0);
				
				// If on the same horizontal path, draw a line
				if (endCell.getY() == startCell.getY())
					gc.lineTo(ex - x, ey - y - 1.0);
				// If on a different horizontal path, draw a curve
				else
					gc.bezierCurveTo(
							(sx + DisplayObject.OBJECT_SIZE - x + ex - x) / 2.0, 
							sy + DisplayObject.OBJECT_SIZE - y - 1.0, 
							(sx + DisplayObject.OBJECT_SIZE - x + ex - x) / 2.0, 
							ey - y - 1.0, 
							ex - x, 
							ey - y - 1.0);
				
				gc.stroke();
			}
		}
		// If starting going up
		else {
			// If on the same vertical path
			if (endCell.getX() == startCell.getX()) {
				// If end cell is lower than the start cell, draw a loop
				if (endCell.getY() > startCell.getY()) {
					gc.beginPath();
					gc.moveTo(sx, sy);
					gc.quadraticCurveTo(sx, sy - y - 1.0, sx - x, sy - y - 1.0);
					gc.stroke();
					
					gc.beginPath();
					gc.moveTo(ex, ey);
					gc.quadraticCurveTo(ex, ey + DisplayObject.OBJECT_SIZE - y - 1.0, 
							ex - x, ey + DisplayObject.OBJECT_SIZE - y - 1.0);
					gc.stroke();
					
					gc.beginPath();
					gc.moveTo(sx - x, sy - y - 1.0);
					gc.bezierCurveTo(sx - x - DisplayObject.OBJECT_SIZE, 
							sy - y - 1.0, 
							sx - x - DisplayObject.OBJECT_SIZE,
							ey + DisplayObject.OBJECT_SIZE - y - 1.0,
							ex - x, 
							ey + DisplayObject.OBJECT_SIZE - y - 1.0);
				}
				// If end cell is higher than the start cell, draw a line
				else {
					gc.beginPath();
					gc.moveTo(sx, sy);
					gc.lineTo(ex, ey);
				}
				
				gc.stroke();
			}
			// If end cell is to the left of the start cell
			else if (endCell.getX() < startCell.getX()) {
				// Draw initial start and end segments pointing toward each other
				gc.beginPath();
				gc.moveTo(sx, sy);
				gc.quadraticCurveTo(sx, sy - y - 1.0, sx - x, sy - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(ex, ey);
				gc.quadraticCurveTo(ex, ey + DisplayObject.OBJECT_SIZE - y - 1.0, 
						ex + DisplayObject.OBJECT_SIZE - x, 
						ey + DisplayObject.OBJECT_SIZE - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(sx - x, sy - y - 1.0);
				
				// If on the same horizontal path, draw a line
				if (endCell.getY() == startCell.getY())
					gc.lineTo(ex + DisplayObject.OBJECT_SIZE - x, 
							ey + DisplayObject.OBJECT_SIZE - y - 1.0);
				// If on a different horizontal path, draw a curve
				else
					gc.bezierCurveTo(
							(sx - x + ex + DisplayObject.OBJECT_SIZE - x) / 2.0, 
							sy - y - 1.0, 
							(sx - x + ex + DisplayObject.OBJECT_SIZE - x) / 2.0, 
							ey + DisplayObject.OBJECT_SIZE - y - 1.0, 
							ex + DisplayObject.OBJECT_SIZE - x, 
							ey + DisplayObject.OBJECT_SIZE - y - 1.0);
				
				gc.stroke();
			}
			// If end cell is to the right of the start cell
			else {
				// Draw initial start and end segments pointing toward each other
				gc.beginPath();
				gc.moveTo(sx, sy);
				gc.quadraticCurveTo(sx, sy - y - 1.0, 
						sx + DisplayObject.OBJECT_SIZE - x, sy - y - 1.0);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(ex, ey);
				gc.quadraticCurveTo(ex, ey + DisplayObject.OBJECT_SIZE - y - 1.0, 
						ex - x, ey + DisplayObject.OBJECT_SIZE - y);
				gc.stroke();
				
				gc.beginPath();
				gc.moveTo(sx + DisplayObject.OBJECT_SIZE - x, 
						sy - y - 1.0);
				
				// If on the same horizontal path, draw a line
				if (endCell.getY() == startCell.getY())
					gc.lineTo(ex - x, 
							ey + DisplayObject.OBJECT_SIZE - y);
				// If on a different horizontal path, draw a curve
				else
					gc.bezierCurveTo(
							(sx + DisplayObject.OBJECT_SIZE - x + ex - x) / 2.0, 
							sy - y - 1.0, 
							(sx + DisplayObject.OBJECT_SIZE - x + ex - x) / 2.0, 
							ey + DisplayObject.OBJECT_SIZE - y, 
							ex - x, 
							ey + DisplayObject.OBJECT_SIZE - y);
				
				gc.stroke();
			}
		}
	}
}
