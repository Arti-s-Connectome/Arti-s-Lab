package org.arti.artislab.gui;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>AppHome</b><br>
 * extends {@link Pane}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>AppHome class creates and controls the Arti's Lab's home workspace.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppHome extends StackPane implements AppSizeListener {
	/**
	 * Default window height.
	 */
	public static final double DEF_HEIGHT = App.DEF_WORKSPACE_HEIGHT;
	/**
	 * Default window width.
	 */
	public static final double DEF_WIDTH = App.DEF_WORKSPACE_WIDTH;
	
	/**
	 * Default background color.
	 */
	public static final Color DEF_BG_COLOR = App.DEF_BG_COLOR;
	
	// Arti icon box.
	private HBox artiBox;
	// Arti icon label.
	private Label artiLabel;
	// Arti icon label's opacity.
	private double artiOpacity;
	
	/**
	 * Default constructor. Creates the Arti's Lab home workspace.
	 */
	public AppHome() {
		// Call parent constructor
		super();
		
		// Initialize variables
		artiOpacity = 0.0;
		
		// Setup layout
		setMinHeight(DEF_HEIGHT);
		setMaxHeight(DEF_HEIGHT);
		setMinWidth(DEF_WIDTH);
		setMaxWidth(DEF_WIDTH);
		
		// Create Arti icon label
		artiLabel = new Label();
		artiLabel.setGraphic(new Icon("logo-large.png"));
		artiLabel.setMinHeight(DEF_HEIGHT);
		artiLabel.setMaxHeight(DEF_HEIGHT);
		artiLabel.setMinWidth(DEF_WIDTH);
		artiLabel.setMaxWidth(DEF_WIDTH);
		artiLabel.setAlignment(Pos.CENTER);
		artiLabel.setOpacity(artiOpacity);
		
		artiBox = new HBox(artiLabel);
		artiBox.setMinHeight(DEF_HEIGHT);
		artiBox.setMaxHeight(DEF_HEIGHT);
		artiBox.setMinWidth(DEF_WIDTH);
		artiBox.setMaxWidth(DEF_WIDTH);
		HBox.setHgrow(artiBox, Priority.ALWAYS);
		
		getChildren().add(artiBox);
	}
	
	/**
	 * Calculates the next opacity value of the Arti logo.
	 * @param delta - The amount and direction to change the opacity.
	 */
	public void nextArtiFade(double delta) {
		artiOpacity = Math.min(1.0, Math.max(0.0, artiOpacity + delta));
		artiLabel.setOpacity(artiOpacity);
	}
	
	/**
	 * Shuts down the Arti's Lab app home screen.
	 */
	public void shutDown() {
		
	}

	@Override
	public void sizeChanged(AppSizeEvent e) {
		setMinHeight(e.getWorkspaceHeight());
		setMaxHeight(e.getWorkspaceHeight());
		setMinWidth(e.getWorkspaceWidth());
		setMaxWidth(e.getWorkspaceWidth());
		artiBox.setMinHeight(e.getWorkspaceHeight());
		artiBox.setMaxHeight(e.getWorkspaceHeight());
		artiBox.setMinWidth(e.getWorkspaceWidth());
		artiBox.setMaxWidth(e.getWorkspaceWidth());
		artiLabel.setMinHeight(e.getWorkspaceHeight());
		artiLabel.setMaxHeight(e.getWorkspaceHeight());
		artiLabel.setMinWidth(e.getWorkspaceWidth());
		artiLabel.setMaxWidth(e.getWorkspaceWidth());
	}
}
