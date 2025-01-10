package org.arti.artilab.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

/**
 * <p>public class <b>Separator</b><br>
 * extends {@link HBox}</p>
 * 
 * <p>Separator class represents a vertical or horizontal separator line.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class Separator extends HBox {
	// Default height.
	private static final double DEF_HEIGHT = 10.0;
	// Default line size.
	private static final double DEF_LINE_SIZE = 2.0;
	// Default width.
	private static final double DEF_WIDTH = 10.0;
	
	/**
	 * Defines a horizontal separator line.
	 */
	public static final boolean HORIZONTAL = false;
	/**
	 * Defines a vertical separator line.
	 */
	public static final boolean VERTICAL = true;
	
	// The separator line's orientation.
	private boolean orientation;
	// The label used to draw the separator.
	private Label separatorLine;
	
	/**
	 * Default constructor. Creates a horizontal separator line.
	 */
	public Separator() {
		// Call parent constructor
		super();
		
		// Initialize variables
		orientation = HORIZONTAL;
		
		// Create the separator line
		separatorLine = new Label();
		if (orientation == HORIZONTAL) {
			separatorLine.setMinSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_LINE_SIZE);
			separatorLine.setMaxSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_LINE_SIZE);
		}
		else {
			separatorLine.setMinSize(DEF_LINE_SIZE, (getParent() != null) ? getParent().getLayoutY() : 0.0);
			separatorLine.setMaxSize(DEF_LINE_SIZE, (getParent() != null) ? getParent().getLayoutY() : 0.0);
		}
		separatorLine.setBackground(Background.fill(App.DEF_BORDER_COLOR));
		
		// Setup hbox
		if (orientation == HORIZONTAL) {
			setMinSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_HEIGHT);
			setMaxSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_HEIGHT);
		}
		else {
			setMinSize(DEF_WIDTH, (getParent() != null) ? getParent().getLayoutY() : 0.0);
			setMaxSize(DEF_WIDTH, (getParent() != null) ? getParent().getLayoutY() : 0.0);
		}
		setAlignment(Pos.CENTER);
		getChildren().add(separatorLine);
	}
	
	/**
	 * Creates a separator with the specified orientation.
	 * @param orientation - Horizontal or vertical orientation.
	 */
	public Separator(boolean orientation) {
		// Call parent constructor
		super();
		
		// Initialize variables
		this.orientation = orientation;
		
		// Create the separator line
		separatorLine = new Label();
		if (orientation == HORIZONTAL) {
			separatorLine.setMinSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_LINE_SIZE);
			separatorLine.setMaxSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_LINE_SIZE);
		}
		else {
			separatorLine.setMinSize(DEF_LINE_SIZE, (getParent() != null) ? getParent().getLayoutY() : 0.0);
			separatorLine.setMaxSize(DEF_LINE_SIZE, (getParent() != null) ? getParent().getLayoutY() : 0.0);
		}
		separatorLine.setBackground(Background.fill(App.DEF_BORDER_COLOR));
		
		// Setup hbox
		if (orientation == HORIZONTAL) {
			setMinSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_HEIGHT);
			setMaxSize((getParent() != null) ? getParent().getLayoutX() : 0.0, DEF_HEIGHT);
		}
		else {
			setMinSize(DEF_WIDTH, (getParent() != null) ? getParent().getLayoutY() : 0.0);
			setMaxSize(DEF_WIDTH, (getParent() != null) ? getParent().getLayoutY() : 0.0);
		}
		setAlignment(Pos.CENTER);
		getChildren().add(separatorLine);
	}
	
	/**
	 * Creates a separator with the specified orientation and specified size (height/width depending on orientation).
	 * @param orientation - Horizontal or vertical orientation.
	 * @param size - The separator's height/width depending on orientation.
	 */
	public Separator(boolean orientation, double size) {
		// Call parent constructor
		super();
		
		// Initialize variables
		this.orientation = orientation;
		
		// Create the separator line
		separatorLine = new Label();
		if (orientation == HORIZONTAL) {
			separatorLine.setMinSize(size, DEF_LINE_SIZE);
			separatorLine.setMaxSize(size, DEF_LINE_SIZE);
		}
		else {
			separatorLine.setMinSize(DEF_LINE_SIZE, size);
			separatorLine.setMaxSize(DEF_LINE_SIZE, size);
		}
		separatorLine.setBackground(Background.fill(App.DEF_BORDER_COLOR));
		
		// Setup hbox
		if (orientation == HORIZONTAL) {
			setMinSize(size, DEF_HEIGHT);
			setMaxSize(size, DEF_HEIGHT);
		}
		else {
			setMinSize(DEF_WIDTH, size);
			setMaxSize(DEF_WIDTH, size);
		}
		setAlignment(Pos.CENTER);
		getChildren().add(separatorLine);
	}
}
