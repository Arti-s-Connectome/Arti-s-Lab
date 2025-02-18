package org.arti.artislab.gui;

import javafx.stage.Stage;

/**
 * <p>public class <b>NeuralModelCreator</b><br>
 * extends {@link Stage}</p>
 * 
 * <p>NeuralModelCreator class creates and controls the neural model creator window where a user can create a new neural model in the Arti's Lab app.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class NeuralModelCreator extends DialogWindow {
	
	/**
	 * Default constructor. Creates the new model creator window with the selected neural model type initialized to the specified model type.
	 * @param modelType - The neural model type value.
	 */
	public NeuralModelCreator(int modelType) {
		// Call parent constructor
		super("Neural Model Creator", 800.0, 600.0);
	}
}
