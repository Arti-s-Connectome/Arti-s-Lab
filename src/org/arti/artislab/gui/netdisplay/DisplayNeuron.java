package org.arti.artislab.gui.netdisplay;

import java.util.ArrayList;

import org.arti.artislab.gui.events.DisplayNeuronEvent;
import org.arti.artislab.gui.events.DisplayNeuronListener;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * <p>public class <b>DisplayNeuron</b><br>
 * extends {@link DisplayObject}</p>
 * 
 * <p>DisplayNeuron class represents a neuron that can be drawn on a neural network display.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class DisplayNeuron extends DisplayObject {
	// Connector size.
	private static final double CONNECTOR_SIZE = 20.0;
	// Connector x offset.
	private static final double CONNECTOR_X_OFFSET = 10.0;
	/**
	 * Excitatory x coordinate.
	 */
	public static final double EXCITATORY_X = 25.0;
	/**
	 * Gap junction x coordinate.
	 */
	public static final double GAP_JUNCTION_X = 75.0;
	/**
	 * Inhibitory x coordinate.
	 */
	public static final double INHIBITORY_X = 50.0;
	
	// Excitatory color.
	private static final Color EXCITATORY_COLOR = Color.rgb(0, 192, 0);
	// Excitatory hovered color.
	private static final Color EXCITATORY_HOVER_COLOR = Color.rgb(0, 255, 0);
	// Gap junction color.
	private static final Color GAP_JUNCTION_COLOR = Color.rgb(0, 192, 192);
	// Gap junction hovered color.
	private static final Color GAP_JUNCTION_HOVER_COLOR = Color.rgb(0, 255, 255);
	// Inhibitory color
	private static final Color INHIBITORY_COLOR = Color.rgb(192, 0, 0);
	// Inhibitory hovered color
	private static final Color INHIBITORY_HOVER_COLOR = Color.rgb(255, 0, 0);
	// Neuron radial gradient.
	private static final RadialGradient NEURON_GRADIENT = new RadialGradient(215.0, 0.5, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, 
			new Stop(0.0, Color.rgb(236, 154, 223)),
			new Stop(1.0, Color.rgb(172, 31, 150)));
	// Excitatory radial gradient.
	private static final RadialGradient EXCITATORY_GRADIENT = new RadialGradient(215.0, 0.5, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, 
			new Stop(0.0, EXCITATORY_HOVER_COLOR),
			new Stop(1.0, EXCITATORY_COLOR));
	// Gap junction radial gradient.
	private static final RadialGradient GAP_JUNCTION_GRADIENT = new RadialGradient(215.0, 0.5, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, 
			new Stop(0.0, GAP_JUNCTION_HOVER_COLOR),
			new Stop(1.0, GAP_JUNCTION_COLOR));
	// Inhibitory radial gradient.
	private static final RadialGradient INHIBITORY_GRADIENT = new RadialGradient(215.0, 0.5, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, 
			new Stop(0.0, INHIBITORY_HOVER_COLOR),
			new Stop(1.0, INHIBITORY_COLOR));
	// Text linear gradient.
	private static final LinearGradient TEXT_GRADIENT = new LinearGradient(0, 0, 0, 1, true, CycleMethod.REFLECT, 
			new Stop(0.0, Color.rgb(165, 29, 144)),
			new Stop(1.0, Color.rgb(48, 8, 42)));
	
	/**
	 * <p>public enum <b>Action</b></p>
	 * 
	 * <p>Action enum contains the list of actions that can trigger a DisplayNeuronEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum Action {
		/**
		 * Line connector was selected.
		 */
		LINE_CONNECTOR_SELECTED
	}
	
	// Excitatory input line connector.
	private LineConnector exConnector;
	// Excitatory output line connector.
	private LineConnector eyConnector;
	// Gap junction input line connector.
	private LineConnector gxConnector;
	// Gap junction output line connector.
	private LineConnector gyConnector;
	// Inhibitory input line connector.
	private LineConnector ixConnector;
	// Inhibitory output line connector.
	private LineConnector iyConnector;
	// Display neuron event listeners.
	private ArrayList<DisplayNeuronListener> listener;
	// The selected line connector.
	private LineConnector selectedConnector;
	
	/**
	 * Default constructor. Creates a display neuron.
	 */
	public DisplayNeuron() {
		// Call parent constructor.
		super();
		
		// Initialize variables
		exConnector = new LineConnector(LineConnector.EXCITATORY_IN_ID, LineConnector.EXCITATORY_OUT_ID);
		eyConnector = new LineConnector(LineConnector.EXCITATORY_OUT_ID, LineConnector.EXCITATORY_IN_ID);
		gxConnector = new LineConnector(LineConnector.GAP_JUNCTION_IN_ID, LineConnector.GAP_JUNCTION_OUT_ID);
		gyConnector = new LineConnector(LineConnector.GAP_JUNCTION_OUT_ID, LineConnector.GAP_JUNCTION_IN_ID);
		ixConnector = new LineConnector(LineConnector.INHIBITORY_IN_ID, LineConnector.INHIBITORY_OUT_ID);
		iyConnector = new LineConnector(LineConnector.INHIBITORY_OUT_ID, LineConnector.INHIBITORY_IN_ID);
		listener = new ArrayList<DisplayNeuronListener>();
		selectedConnector = null;
		
		// Set bounds
		bounds = new Rectangle(0.0, 0.0, OBJECT_SIZE, OBJECT_SIZE);
	}
	
	/**
	 * Adds the specified DisplayNeuronListener to the list of listeners.
	 * @param listener - The new DisplayNeuronListener.
	 */
	public void addListener(DisplayNeuronListener listener) {
		if (listener != null)
			this.listener.add(listener);
	}
	
	/**
	 * Sets the selected line connector to null.
	 */
	public void deselectConnector() {
		selectedConnector = null;
	}
	
	@Override
	public void drawAt(GraphicsContext gc, double x, double y) {
		// Calculate real x and y (top left point)
		double rx = x - 50.0;
		double ry = y - 50.0;
		
		// Update line connectors
		exConnector.setParentX(x);
		exConnector.setParentY(y);
		exConnector.setX(rx + EXCITATORY_X);
		exConnector.setY(ry - 1.0);
		eyConnector.setParentX(x);
		eyConnector.setParentY(y);
		eyConnector.setX(rx + EXCITATORY_X);
		eyConnector.setY(ry + OBJECT_SIZE + 1.0);
		gxConnector.setParentX(x);
		gxConnector.setParentY(y);
		gxConnector.setX(rx + GAP_JUNCTION_X);
		gxConnector.setY(ry - 1.0);
		gyConnector.setParentX(x);
		gyConnector.setParentY(y);
		gyConnector.setX(rx + GAP_JUNCTION_X);
		gyConnector.setY(ry + OBJECT_SIZE + 1.0);
		ixConnector.setParentX(x);
		ixConnector.setParentY(y);
		ixConnector.setX(rx + INHIBITORY_X);
		ixConnector.setY(ry - 1.0);
		iyConnector.setParentX(x);
		iyConnector.setParentY(y);
		iyConnector.setX(rx + INHIBITORY_X);
		iyConnector.setY(ry + OBJECT_SIZE + 1.0);
		
		// Update bounds
		bounds.setX(rx);
		bounds.setY(ry);
		
		// Draw neuron square
		gc.setFill(NEURON_GRADIENT);
		gc.fillOval(rx + 5.0, ry + 5.0, OBJECT_SIZE - 10.0, OBJECT_SIZE - 10.0);
		
		// Draw neuron text
		gc.setFill(TEXT_GRADIENT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(new Font("Arial Bold", 10));
		gc.fillText("Tonic Spiking", x, y - 20.0);
		gc.setFont(new Font("Arial Bold", 24));
		gc.fillText("1", x, y + 10.0);
		
		// Draw excitatory input connector
		if (xMouse >= rx + EXCITATORY_X - CONNECTOR_X_OFFSET && xMouse < rx + EXCITATORY_X + CONNECTOR_X_OFFSET && 
				yMouse >= ry && yMouse < ry + CONNECTOR_SIZE) {
			gc.setFill(EXCITATORY_HOVER_COLOR);
			gc.fillOval(rx + EXCITATORY_X - CONNECTOR_X_OFFSET, ry, CONNECTOR_SIZE, CONNECTOR_SIZE);
			
			if (rightClicked) {
				rightClicked = false;
				selectedConnector = exConnector;
				sendEvent(Action.LINE_CONNECTOR_SELECTED);
			}
		}
		else {
			gc.setFill(EXCITATORY_GRADIENT);
			gc.fillOval(rx + EXCITATORY_X - CONNECTOR_X_OFFSET, ry, CONNECTOR_SIZE, CONNECTOR_SIZE);
		}
		
		// Draw inhibitory input connector
		if (xMouse >= rx + INHIBITORY_X - CONNECTOR_X_OFFSET && xMouse < rx + INHIBITORY_X + CONNECTOR_X_OFFSET && 
				yMouse >= ry && yMouse < ry + CONNECTOR_SIZE) {
			gc.setFill(INHIBITORY_HOVER_COLOR);
			gc.fillOval(rx + INHIBITORY_X - CONNECTOR_X_OFFSET, ry, CONNECTOR_SIZE, CONNECTOR_SIZE);
			
			if (rightClicked) {
				rightClicked = false;
				selectedConnector = ixConnector;
				sendEvent(Action.LINE_CONNECTOR_SELECTED);
			}
		}
		else {
			gc.setFill(INHIBITORY_GRADIENT);
			gc.fillOval(rx + INHIBITORY_X - CONNECTOR_X_OFFSET, ry, CONNECTOR_SIZE, CONNECTOR_SIZE);
		}
		
		// Draw gap junction input connector
		if (xMouse >= rx + GAP_JUNCTION_X - CONNECTOR_X_OFFSET && xMouse < rx + GAP_JUNCTION_X + CONNECTOR_X_OFFSET && 
				yMouse >= ry && yMouse < ry + CONNECTOR_SIZE) {
			gc.setFill(GAP_JUNCTION_HOVER_COLOR);
			gc.fillOval(rx + GAP_JUNCTION_X - CONNECTOR_X_OFFSET, ry, CONNECTOR_SIZE, CONNECTOR_SIZE);
			
			if (rightClicked) {
				rightClicked = false;
				selectedConnector = gxConnector;
				sendEvent(Action.LINE_CONNECTOR_SELECTED);
			}
		}
		else {
			gc.setFill(GAP_JUNCTION_GRADIENT);
			gc.fillOval(rx + GAP_JUNCTION_X - CONNECTOR_X_OFFSET, ry, CONNECTOR_SIZE, CONNECTOR_SIZE);
		}
		
		// Draw excitatory output connector
		if (xMouse >= rx + EXCITATORY_X - CONNECTOR_X_OFFSET && xMouse < rx + EXCITATORY_X + CONNECTOR_X_OFFSET && 
				yMouse >= ry + OBJECT_SIZE - CONNECTOR_SIZE && yMouse < ry + OBJECT_SIZE) {
			gc.setFill(EXCITATORY_HOVER_COLOR);
			gc.fillOval(rx + EXCITATORY_X - CONNECTOR_X_OFFSET, ry + OBJECT_SIZE - CONNECTOR_SIZE, CONNECTOR_SIZE, CONNECTOR_SIZE);
			
			if (rightClicked) {
				rightClicked = false;
				selectedConnector = eyConnector;
				sendEvent(Action.LINE_CONNECTOR_SELECTED);
			}
		}
		else {
			gc.setFill(EXCITATORY_GRADIENT);
			gc.fillOval(rx + EXCITATORY_X - CONNECTOR_X_OFFSET, ry + OBJECT_SIZE - CONNECTOR_SIZE, CONNECTOR_SIZE, CONNECTOR_SIZE);
		}
		
		// Draw inhibitory output connector
		if (xMouse >= rx + INHIBITORY_X - CONNECTOR_X_OFFSET && xMouse < rx + INHIBITORY_X + CONNECTOR_X_OFFSET && 
				yMouse >= ry + OBJECT_SIZE - CONNECTOR_SIZE && yMouse < ry + OBJECT_SIZE) {
			gc.setFill(INHIBITORY_HOVER_COLOR);
			gc.fillOval(rx + INHIBITORY_X - CONNECTOR_X_OFFSET, ry + OBJECT_SIZE - CONNECTOR_SIZE, CONNECTOR_SIZE, CONNECTOR_SIZE);
			
			if (rightClicked) {
				rightClicked = false;
				selectedConnector = iyConnector;
				sendEvent(Action.LINE_CONNECTOR_SELECTED);
			}
		}
		else {
			gc.setFill(INHIBITORY_GRADIENT);
			gc.fillOval(rx + INHIBITORY_X - CONNECTOR_X_OFFSET, ry + OBJECT_SIZE - CONNECTOR_SIZE, CONNECTOR_SIZE, CONNECTOR_SIZE);
		}
		
		// Draw gap junction output connector
		if (xMouse >= rx + GAP_JUNCTION_X - CONNECTOR_X_OFFSET && xMouse < rx + GAP_JUNCTION_X + CONNECTOR_X_OFFSET && 
				yMouse >= ry + OBJECT_SIZE - CONNECTOR_SIZE && yMouse < ry + OBJECT_SIZE) {
			gc.setFill(GAP_JUNCTION_HOVER_COLOR);
			gc.fillOval(rx + GAP_JUNCTION_X - CONNECTOR_X_OFFSET, ry + OBJECT_SIZE - CONNECTOR_SIZE, CONNECTOR_SIZE, CONNECTOR_SIZE);
			
			if (rightClicked) {
				rightClicked = false;
				selectedConnector = gyConnector;
				sendEvent(Action.LINE_CONNECTOR_SELECTED);
			}
		}
		else {
			gc.setFill(GAP_JUNCTION_GRADIENT);
			gc.fillOval(rx + GAP_JUNCTION_X - CONNECTOR_X_OFFSET, ry + OBJECT_SIZE - CONNECTOR_SIZE, CONNECTOR_SIZE, CONNECTOR_SIZE);
		}
	}
	
	/**
	 * Removes the specified DisplayNeuronListener from the list of listeners.
	 * @param listener - The DisplayNeuronListener to remove.
	 * @return True if listener was removed, otherwise false.
	 */
	public boolean removeListener(DisplayNeuronListener listener) {
		return this.listener.remove(listener);
	}
	
	/**
	 * Returns the currently selected line connector, or null if none is selected.
	 * @return The selected line connector, or null if none is selected.
	 */
	public LineConnector selectedConnector() {
		return selectedConnector;
	}
	
	/**
	 * Sends a DisplayNeuronEvent to all DisplayNeuronListeners with the specified action.
	 * @param action - The triggered action.
	 */
	private void sendEvent(Action action) {
		DisplayNeuronEvent e = new DisplayNeuronEvent(this, action);
		
		for (DisplayNeuronListener l : listener)
			l.actionTriggered(e);
	}
}
