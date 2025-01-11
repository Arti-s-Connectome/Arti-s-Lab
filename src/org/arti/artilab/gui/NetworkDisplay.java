package org.arti.artilab.gui;

import java.util.ArrayList;

import org.arti.artilab.gui.events.AppToolBarEvent;
import org.arti.artilab.gui.events.AppToolBarListener;
import org.arti.artilab.gui.events.DisplayNeuronEvent;
import org.arti.artilab.gui.events.DisplayNeuronListener;
import org.arti.artilab.gui.netdisplay.DisplayNeuron;
import org.arti.artilab.gui.netdisplay.DisplayObject;
import org.arti.artilab.gui.netdisplay.Line;
import org.arti.artilab.gui.netdisplay.LineConnector;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>NetworkDisplay</b><br>
 * extends {@link Canvas}<br>
 * extends {@link AppToolBarListener}, {@link DisplayNeuronListener}</p>
 * 
 * <p>NetworkDisplay class creates and controls a neural network display for visually creating neural networks.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class NetworkDisplay extends StackPane implements AppToolBarListener, DisplayNeuronListener {
	// Default height.
	private static final double DEF_HEIGHT = App.DEF_WORKSPACE_HEIGHT;
	// Default width.
	private static final double DEF_WIDTH = App.DEF_WIDTH;
	
	// Default scale adjustment.
	private static final double DEF_SCALE_ADJ = 1.1;
	
	// Maximum scale.
	private static final double MAX_SCALE = 4.0;
	// Minimum scale.
	private static final double MIN_SCALE = 0.1;

	// Grid color.
	private static final Color GRID_COLOR = Color.rgb(57, 106, 146);
	// Overlay background color.
	private static final Color OVERLAY_COLOR = Color.rgb(42, 79, 110, 0.5);
	// Text color.
	private static final Color TEXT_COLOR = Color.rgb(239, 168, 228);
	
	/**
	 * Size of a grid cell.
	 */
	public static final double GRID_CELL_SIZE = 100.0;
	
	// Display's drawing canvas.
	private Canvas canvasDraw;
	// Display's grid canvas.
	private Canvas canvasGrid;
	// Display's overlay canvas.
	private Canvas canvasOverlay;
	// Mouse dragging flag.
	private boolean dragging;
	// Ending line connector.
	private LineConnector endConnector;
	// Filled grid cells.
	ArrayList<Point2D> filledCell;
	// Display's drawing graphics context.
	private GraphicsContext gcDraw;
	// Display's grid graphics context.
	private GraphicsContext gcGrid;
	// Display's overlay graphics context.
	private GraphicsContext gcOverlay;
	// Display height.
	private double height;
	// List of lines.
	private ArrayList<Line> line;
	// Display object list.
	private ArrayList<DisplayObject> object;
	// Selected display object.
	private DisplayObject objectSelected;
	// Display scale.
	private double scale;
	// Starting line connector.
	private LineConnector startConnector;
	// Display width.
	private double width;
	// Display x center screen position.
	private double xOrigin;
	// Display grid cell y position.
	private double xCell;
	// Mouse x position.
	private double xMouse;
	// Display y center screen position.
	private double yOrigin;
	// Display grid cell y position.
	private double yCell;
	// Mouse y position.
	private double yMouse;
	
	/**
	 * Default constructor. Creates a neural network display.
	 */
	public NetworkDisplay() {
		// Call parent constructor
		super();
		
		// Initialize variables
		canvasDraw = new Canvas(DEF_WIDTH, DEF_HEIGHT);
		canvasGrid = new Canvas(DEF_WIDTH, DEF_HEIGHT);
		canvasOverlay = new Canvas(DEF_WIDTH, DEF_HEIGHT);
		dragging = false;
		endConnector = null;
		filledCell = new ArrayList<Point2D>();
		gcDraw = canvasDraw.getGraphicsContext2D();
		gcGrid = canvasGrid.getGraphicsContext2D();
		gcOverlay = canvasOverlay.getGraphicsContext2D();
		height = DEF_HEIGHT;
		line = new ArrayList<Line>();
		object = new ArrayList<DisplayObject>();
		objectSelected = null;
		scale = 1.0;
		startConnector = null;
		width = DEF_WIDTH;
		xOrigin = DEF_WIDTH / 2.0;
		xCell = 0.0;
		xMouse = 0.0;
		yOrigin = DEF_HEIGHT / 2.0;
		yCell = 0.0;
		yMouse = 0.0;
		
		// Set size
		setPrefHeight(DEF_HEIGHT);
		setPrefWidth(DEF_WIDTH);
		
		// Handle mouse wheel (zooming)
		setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent arg0) {
				if (arg0.getDeltaY() > 0 && scale < MAX_SCALE) {
					scale *= DEF_SCALE_ADJ;
					gcDraw.scale(DEF_SCALE_ADJ, DEF_SCALE_ADJ);
					gcGrid.scale(DEF_SCALE_ADJ, DEF_SCALE_ADJ);
					redraw();
					redrawGrid();
					redrawOverlay();
				}
				else if (arg0.getDeltaY() < 0 && scale > MIN_SCALE) {
					scale /= DEF_SCALE_ADJ;
					gcDraw.scale(1.0 / DEF_SCALE_ADJ, 1.0 / DEF_SCALE_ADJ);
					gcGrid.scale(1.0 / DEF_SCALE_ADJ, 1.0 / DEF_SCALE_ADJ);
					redraw();
					redrawGrid();
					redrawOverlay();
				}
			}
		});
		
		// Handle mouse clicks (select objects / zoom to 100%)
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// If dragging
				if (dragging) {
					dragging = false;
				}
				// If clicked
				else if (arg0.getClickCount() == 1) {
					// If left mouse button is clicked
					if (arg0.getButton() == MouseButton.PRIMARY) {
						// If a display object is selected, stop moving it
						if (objectSelected != null) {
							// Check if cell is already filled, and if not then fill it
							boolean filled = false;
							for (int i = 0; i < filledCell.size(); ++i) {
								if (filledCell.get(i).getX() >= xCell - 1 && filledCell.get(i).getX() <= xCell + 1 && 
										filledCell.get(i).getY() >= yCell - 1 && filledCell.get(i).getY() <= yCell + 1) {
									filled = true;
									break;
								}
							}
							
							// If cell is not filled
							if (!filled) {
								// Fill cell and update display
								filledCell.add(new Point2D(xCell, yCell));
								objectSelected.mouseClicked(xCell, yCell);
								objectSelected = null;
								redraw();
							}
							// If cell is filled, display error message
							else {
								App.alertError("Grid Area Occupied", "Objects must have at least one square space between them.");
							}
						}
						else {
							// Send click message to the object the mouse is hovering over, if any
							for (int i = 0; i < object.size(); ++i) {
								if (object.get(i).isHovering()) 
									object.get(i).mouseClicked(xCell, yCell);
							}
						}
					}
					// If right mouse button is clicked
					else if (arg0.getButton() == MouseButton.SECONDARY) {
						boolean objectClicked = false;
						
						// Send right click message to the object the mouse is hovering over, if any
						for (int i = 0; i < object.size(); ++i) {
							if (object.get(i).isHovering()) {
								object.get(i).mouseRightClicked();
								objectClicked = true;
							}
						}
						
						// If no object was right clicked
						if (!objectClicked) {
							// Clear connector selection
							startConnector = null;
							redraw();
						}
					}
				}
				// If double clicked
				else if (arg0.getClickCount() == 2) {
					// If left mouse button is clicked
					if (arg0.getButton() == MouseButton.PRIMARY) {
						// If no display object is selected
						if (objectSelected == null) {
							// Select the object the mouse is hovering over, if any
							for (int i = 0; i < object.size(); ++i) {
								if (object.get(i).isHovering()) {
									objectSelected = object.get(i);
									object.get(i).mouseDoubleClicked();
									
									// Remove filled position at the selected object's location
									for (int j = 0; j < filledCell.size(); ++j) {
										if (filledCell.get(j).getX() == objectSelected.xCell() && 
												filledCell.get(j).getY() == objectSelected.yCell()) {
											filledCell.remove(j);
											break;
										}
									}
								}
							}
						}
					}
					// If mouse wheel is clicked
					else if (arg0.getButton() == MouseButton.MIDDLE) {
						// Zoom to 100%
						gcDraw.scale(1.0 / scale, 1.0 / scale);
						gcGrid.scale(1.0 / scale, 1.0 / scale);
						scale = 1.0;
						redraw();
						redrawGrid();
						redrawOverlay();
					}
				}
			}
		});
		
		// Handle mouse movement (track mouse location)
		setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				xMouse = arg0.getX();
				yMouse = arg0.getY();
				
				for (int i = 0; i < object.size(); ++i)
					object.get(i).mouseMoved(gcDraw, (xMouse - gcDraw.getTransform().getTx()) / scale, 
							(yMouse - gcDraw.getTransform().getTy()) / scale);
				
				redraw();
				redrawGrid();
				redrawOverlay();
			}
		});
		
		// Handle mouse dragging (translate display)
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				dragging = true;
				double xDelta = (xMouse - arg0.getX());
				double yDelta = (yMouse - arg0.getY());
				gcDraw.translate(-xDelta, -yDelta);
				gcGrid.translate(-xDelta, -yDelta);
				xMouse = arg0.getX();
				yMouse = arg0.getY();
				redraw();
				redrawGrid();
			}
		});
		
		// Add canvases
		getChildren().addAll(canvasGrid, canvasDraw, canvasOverlay);
		
		// Set origin to center of the display and draw the display
		gcDraw.transform(scale, 0.0, 0.0, scale, xOrigin, yOrigin);
		gcGrid.transform(scale, 0.0, 0.0, scale, xOrigin, yOrigin);
		redraw();
		redrawGrid();
		redrawOverlay();
	}
	
	@Override
	public void actionTriggered(DisplayNeuronEvent e) {
		// Check action
		switch (e.getAction()) {
		// Line connector selected
		case LINE_CONNECTOR_SELECTED:
			if (startConnector == null) {
				startConnector = ((DisplayNeuron)e.getSource()).selectedConnector();
			}
			else {
				if (startConnector.isConnectable(((DisplayNeuron)e.getSource()).selectedConnector().id())) {
					endConnector = ((DisplayNeuron)e.getSource()).selectedConnector();
					line.add(new Line(startConnector, endConnector));
				}
			}
			
			redraw();
			redrawGrid();
			redrawOverlay();
			break;
		// Do nothing
		default:
			break;
		}
	}
	
	/**
	 * Returns the grid cell containing the specified x and y coordinates.
	 * @param x - The x coordinate.
	 * @param y - The y coordinate.
	 * @return The grid cell of x and y.
	 */
	public Point2D getGridCell(double x, double y) {
		return new Point2D((int)(x / 100.0) + (x < 0.0 ? -1 : 0), (int)(y / 100.0) + (y < 0.0 ? -1 : 0));
	}
	
	@Override
	public void itemSelected(AppToolBarEvent e) {
		// Check which item was selected
		switch (e.getItem()) {
		// Dendrite menu item
		case DENDRITE:
			switch (e.getDetail()) {
			// Create distal dendrite display object
			case DENDRITE_DISTAL:
				break;
			// Create proximal dendrite display object
			case DENDRITE_PROXIMAL:
				break;
			// Do nothing
			default:
				break;
			}
			break;
		// Neuron menu item
		case NEURON:
			switch (e.getDetail()) {
			// Create accomodation neuron display object
			case NEURON_ACCOMODATION:
				break;
			// Create bistability neuron display object
			case NEURON_BISTABILITY:
				break;
			// Create class I excitable neuron display object
			case NEURON_CLASS_I_EXCITABLE:
				break;
			// Create class II excitable neuron display object
			case NEURON_CLASS_II_EXCITABLE:
				break;
			// Create depolarizing after potential neuron display object
			case NEURON_DEPOLARIZING_AFTER_POTENTIAL:
				break;
			// Create inhibition-induced bursting neuron display object
			case NEURON_INHIBITION_INDUCED_BURSTING:
				break;
			// Create inhibition-induced spiking neuron display object
			case NEURON_INHIBITION_INDUCED_SPIKING:
				break;
			// Create integrator neuron display object
			case NEURON_INTEGRATOR:
				break;
			// Create mixed mode neuron display object
			case NEURON_MIXED_MODE:
				break;
			// Create phasic bursting neuron display object
			case NEURON_PHASIC_BURSTING:
				break;
			// Create phasic spiking neuron display object
			case NEURON_PHASIC_SPIKING:
				break;
			// Create rebound bursting neuron display object
			case NEURON_REBOUND_BURSTING:
				break;
			// Create rebound spiking neuron display object
			case NEURON_REBOUND_SPIKING:
				break;
			// Create resonator neuron display object
			case NEURON_RESONATOR:
				break;
			// Create spike frequency adaptation neuron display object
			case NEURON_SPIKE_FREQUENCY_ADAPTATION:
				break;
			// Create spike latency neuron display object
			case NEURON_SPIKE_LATENCY:
				break;
			// Create subthreshold oscillations neuron display object
			case NEURON_SUBTHRESHOLD_OSCILLATIONS:
				break;
			// Create threshold variability neuron display object
			case NEURON_THRESHOLD_VARIABILITY:
				break;
			// Create tonic bursting neuron display object
			case NEURON_TONIC_BURSTING:
				break;
			// Create tonic spiking neuron display object
			case NEURON_TONIC_SPIKING:
				object.add(new DisplayNeuron());
				object.getLast().created(xMouse, yMouse);
				((DisplayNeuron)object.getLast()).addListener(this);
				objectSelected = object.getLast();
				redraw();
				redrawGrid();
				redrawOverlay();
				break;
			// Let user select from all available neuron spiking models, including user created models
			case NEURON_OTHER:
				break;
			// Do nothing
			default:
				break;
			}
			break;
		// Zoom to 100%
		case ZOOM_100:
			gcDraw.scale(1.0 / scale, 1.0 / scale);
			gcGrid.scale(1.0 / scale, 1.0 / scale);
			scale = 1.0;
			redraw();
			redrawGrid();
			redrawOverlay();
			break;
		// Zoom in
		case ZOOM_IN:
			if (scale < MAX_SCALE) {
				scale *= DEF_SCALE_ADJ;
				gcDraw.scale(DEF_SCALE_ADJ, DEF_SCALE_ADJ);
				gcGrid.scale(DEF_SCALE_ADJ, DEF_SCALE_ADJ);
				redraw();
				redrawGrid();
				redrawOverlay();
			}
			break;
		// Zoom out
		case ZOOM_OUT:
			if (scale > MIN_SCALE) {
				scale /= DEF_SCALE_ADJ;
				gcDraw.scale(1.0 / DEF_SCALE_ADJ, 1.0 / DEF_SCALE_ADJ);
				gcGrid.scale(1.0 / DEF_SCALE_ADJ, 1.0 / DEF_SCALE_ADJ);
				redraw();
				redrawGrid();
				redrawOverlay();
			}
			break;
		// Nothing selected
		default:
			break;
		}
	}
	
	/**
	 * Redraws the neural network display.
	 */
	private void redraw() {
		// Calculate real coordinates
		double rx = (xMouse - gcDraw.getTransform().getTx()) / scale;
		double ry = (yMouse - gcDraw.getTransform().getTy()) / scale;
		
		// Clear display
		gcDraw.clearRect((rx - width) / scale, (ry - height) / scale, width * 2.0 / scale, height * 2.0 / scale);
		
		// If a starting line connector is selected
		if (startConnector != null) {	
			// If an ending line connector is selected
			if (endConnector != null) {
				startConnector = null;
				endConnector = null;
			}
			// If an ending line connector is not selected
			else {
				// Draw a dashed line from the starting connector to the current mouse location
				if (startConnector.id() == LineConnector.EXCITATORY_IN_ID || startConnector.id() == LineConnector.EXCITATORY_OUT_ID)
					gcDraw.setStroke(Line.EXCITATORY_LINE_COLOR);
				else if (startConnector.id() == LineConnector.GAP_JUNCTION_IN_ID || startConnector.id() == LineConnector.GAP_JUNCTION_OUT_ID)
					gcDraw.setStroke(Line.GAP_JUNCTION_LINE_COLOR);
				else
					gcDraw.setStroke(Line.INHIBITORY_LINE_COLOR);
				gcDraw.setLineDashes(5.0, 5.0);
				gcDraw.setLineWidth(3);
				gcDraw.beginPath();
				gcDraw.moveTo(startConnector.x(), startConnector.y());
				gcDraw.lineTo(rx, ry);
				gcDraw.stroke();
			}
		}
		
		// Draw lines
		for (int i = 0; i < line.size(); ++i)
			line.get(i).draw(gcDraw);
		
		// Draw dislay objects
		for (int i = 0; i < object.size(); ++i) {
			if (!object.get(i).isMoving())
				object.get(i).drawToGrid(gcDraw, object.get(i).x(), object.get(i).y());
			else
				object.get(i).drawMoving(gcDraw, rx, ry);
		}
	}
	
	/**
	 * Redraws the neural network display's grid.
	 */
	public void redrawGrid() {
		// Calculate real coordinates
		double tx = xOrigin - gcDraw.getTransform().getTx();
		double ty = yOrigin - gcDraw.getTransform().getTy();
		double rx = (xMouse - gcDraw.getTransform().getTx()) / scale;
		double ry = (yMouse - gcDraw.getTransform().getTy()) / scale;
		
		// Clear display
		gcGrid.clearRect((tx - width) / scale, (ty - height) / scale, width * 2.0 / scale, height * 2.0 / scale);
		
		// Draw grid
		gcGrid.setStroke(GRID_COLOR);
		gcGrid.setLineDashes(1.0, 10.0);
		gcGrid.setLineWidth(1);
		
		for (int i = (int)((tx - width) / GRID_CELL_SIZE / scale); i <= (int)((tx + width) / GRID_CELL_SIZE / scale); ++i) {
			gcGrid.beginPath();
			gcGrid.moveTo((double)i * GRID_CELL_SIZE, ty - height);
			gcGrid.lineTo((double)i * GRID_CELL_SIZE, ty + height);
			gcGrid.stroke();
		}
		
		for (int j = (int)((ty - height) / GRID_CELL_SIZE / scale); j <= (int)((ty + height) / GRID_CELL_SIZE / scale); ++j) {
			gcGrid.beginPath();
			gcGrid.moveTo(tx - width, (double)j * GRID_CELL_SIZE);
			gcGrid.lineTo(tx + width, (double)j * GRID_CELL_SIZE);
			gcGrid.stroke();
		}
		
		// Highlight grid cell that is being hovered over
		for (int i = (int)((rx - width - GRID_CELL_SIZE) / GRID_CELL_SIZE / scale); i <= (int)((rx + width) / GRID_CELL_SIZE / scale); ++i) {
			for (int j = (int)((ry - height - GRID_CELL_SIZE) / GRID_CELL_SIZE / scale); 
					j <= (int)((ry + height) / GRID_CELL_SIZE / scale); ++j) {
				if (rx > (double)(i * GRID_CELL_SIZE) && rx <= (double)(i * GRID_CELL_SIZE + GRID_CELL_SIZE) && 
						ry > (double)(j * GRID_CELL_SIZE) && ry <= (double)(j * GRID_CELL_SIZE + GRID_CELL_SIZE)) {
					gcGrid.setFill(GRID_COLOR);
					gcGrid.fillRect((double)(i * GRID_CELL_SIZE), (double)(j * GRID_CELL_SIZE), GRID_CELL_SIZE, GRID_CELL_SIZE);
					
					// Mark hovered grid cell
					xCell = (double)i;
					yCell = (double)j;
				}
			}
		}
	}
	
	/**
	 * Redraws the neural network display's overlay.
	 */
	public void redrawOverlay() {
		gcOverlay.clearRect(0.0, 0.0, width, height);
		gcOverlay.setFill(OVERLAY_COLOR);
		gcOverlay.fillRect(0.0, 0.0, 115.0, 75.0);
		gcOverlay.setFill(TEXT_COLOR);
		gcOverlay.setTextBaseline(VPos.TOP);
		gcOverlay.fillText("Mouse: {" + (int)(xMouse) + ", " + (int)(yMouse) + "}", 0.0, 0.0);
		gcOverlay.fillText("Grid: {" + (int)((xMouse - gcDraw.getTransform().getTx()) / scale) + ", " + 
				(int)((yMouse - gcDraw.getTransform().getTy()) / scale) + "}", 0.0, 15.0);
		gcOverlay.fillText("Grid Cell: {" + (int)xCell + ", " + (int)yCell + "}", 0.0, 30.0);
		gcOverlay.fillText("Zoom: " + (int)(scale * GRID_CELL_SIZE) + "%", 0.0, 45.0);
	}
	
	/**
	 * Updates the size of this neural network display to the specified height and width.
	 * @param width - The new width.
	 * @param height - The new height.
	 */
	public void sizeChanged(double width, double height) {
		setPrefHeight(height);
		setPrefWidth(width);
		this.width = width;
		this.height = height;
		canvasDraw.setWidth(width);
		canvasDraw.setHeight(height);
		canvasGrid.setWidth(width);
		canvasGrid.setHeight(height);
		canvasOverlay.setWidth(width);
		canvasOverlay.setHeight(height);
		redraw();
		redrawGrid();
		redrawOverlay();
	}
}
