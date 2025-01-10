package org.arti.artilab.gui;

import javafx.scene.layout.Pane;

/**
 * <p>public class <b>NetworkLab</b><br>
 * extends {@link Pane}</p>
 * 
 * <p>NetworkLab class creates and controls the non-learning Neural Network Lab app in the Artilab application.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class NetworkLab extends Pane {
	// Default height.
	private static final double DEF_HEIGHT = App.DEF_WORKSPACE_HEIGHT;
	// Default width.
	private static final double DEF_WIDTH = App.DEF_WIDTH;
	
	private NetworkDisplay display;
	
	/**
	 * Default constructor. Creates the Neural Network Lab app.
	 */
	public NetworkLab() {
		super();
		
		setPrefHeight(DEF_HEIGHT);
		setPrefWidth(DEF_WIDTH);
		
		display = new NetworkDisplay();
		
		getChildren().add(display);
	}
	
	/**
	 * Returns the neural network display.
	 * @return The neural network display.
	 */
	public NetworkDisplay getDisplay() {
		return display;
	}
}
