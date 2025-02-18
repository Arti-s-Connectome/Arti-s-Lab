package org.arti.artislab.gui;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;
import org.arti.artislab.gui.events.SideToolBarEvent;
import org.arti.artislab.gui.events.SideToolBarListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>SideToolBar</b><br>
 * extends {@link ToolBar}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>SideToolBar class creates and controls the Arti's Lab application's side tool bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class SideToolBar extends ToolBar implements AppSizeListener {
	/** 
	 * Height of the tool bar.
	 */
	public static final double WIDTH = 64.0;
	
	/**
	 * Default window background color.
	 */
	public static final Color DEF_BG_COLOR = App.DEF_BG_COLOR;
	
	// Games lab icon name.
	private static final String GAMES_ICON = "icons8-games";
	// Home screen icon name.
	private static final String HOME_ICON = "icons8-home";
	// Neural model lab icon name.
	private static final String NEURAL_MODEL_ICON = "icons8-neuron";
	// Neural network lab icon name.
	private static final String NEURAL_NET_ICON = "icons8-neural-network";
	// Synapse lab icon name.
	private static final String SYNAPSE_ICON = "icons8-synapse";
	
	/**
	 * <p>public enum <b>Item</b></p>
	 * 
	 * <p>Item enum contains the list of tool bar items that can be selected and trigger a SideToolBarEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum Item {
		/**
		 * Games lab item.
		 */
		GAMES_LAB,
		/**
		 * Home screen item.
		 */
		HOME_SCREEN,
		/**
		 * Neural model lab item.
		 */
		NEURAL_MODEL_LAB,
		/**
		 * Neural network lab item.
		 */
		NEURAL_NET_LAB,
		/**
		 * Synapse lab item.
		 */
		SYNAPSE_LAB
	}
	
	// Tool box layout.
	private VBox toolBox;
	// SideToolBar listener.
	private SideToolBarListener listener;
	// Games lab button.
	private ImageButton gamesButton;
	// Home screen button.
	private ImageButton homeButton;
	// Neural model lab button.
	private ImageButton neuralModelButton;
	// Neural network lab button.
	private ImageButton neuralNetButton;
	// Synapse lab button.
	private ImageButton synapseButton;
	
	/**
	 * Default constructor. Creates the Arti's Lab application side tool bar.
	 */
	public SideToolBar() {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = null;
		toolBox = new VBox();
		
		// Create games lab button
		gamesButton = new ImageButton(GAMES_ICON);
		gamesButton.setTooltip(new Tooltip("Games Lab"));
		gamesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.GAMES_LAB);
			}
		});
		
		// Create home screen button
		homeButton = new ImageButton(HOME_ICON);
		homeButton.setTooltip(new Tooltip("Home Screen"));
		homeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.HOME_SCREEN);
			}
		});
		
		// Create neural model lab button
		neuralModelButton = new ImageButton(NEURAL_MODEL_ICON);
		neuralModelButton.setTooltip(new Tooltip("Neural Model Lab"));
		neuralModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURAL_MODEL_LAB);
			}
		});
		
		// Create neural network lab button
		neuralNetButton = new ImageButton(NEURAL_NET_ICON);
		neuralNetButton.setTooltip(new Tooltip("Neural Network Lab"));
		neuralNetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURAL_NET_LAB);
			}
		});
		
		// Create synapse lab button
		synapseButton = new ImageButton(SYNAPSE_ICON);
		synapseButton.setTooltip(new Tooltip("Synapse Lab"));
		synapseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SYNAPSE_LAB);
			}
		});
		
		// Initialize tool bar
		setBackground(Background.fill(DEF_BG_COLOR));
		setMaxWidth(WIDTH);
		setMinWidth(WIDTH);
		setPadding(new Insets(0, 0, 0, 0));
		VBox.setVgrow(toolBox, Priority.ALWAYS);
		toolBox.setAlignment(Pos.TOP_CENTER);
		
		getItems().add(toolBox);
	}
	
	/**
	 * Displays and empty tool bar.
	 */
	public void displayEmptyToolBar() {
		toolBox.getChildren().clear();
		
		listener = null;
	}
	
	
	/**
	 * Displays the tool bar for the Games Lab app.
	 */
	public void displayGamesToolBar() {
		// Clear old tool bar
		toolBox.getChildren().clear();
		
		// Add all buttons except the games lab button
		toolBox.getChildren().addAll(
				homeButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				synapseButton,
				neuralModelButton,
				neuralNetButton);
	}
	
	/**
	 * Displays the tool bar for the home screen.
	 */
	public void displayHomeToolBar() {
		// Clear old tool bar
		toolBox.getChildren().clear();
		
		// Add all buttons except the home screen button
		toolBox.getChildren().addAll(
				synapseButton,
				neuralModelButton,
				neuralNetButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				gamesButton);
	}
	
	/**
	 * Displays the tool bar for the Neural Model Lab app.
	 */
	public void displayNeuralModelToolBar() {
		// Clear old tool bar
		toolBox.getChildren().clear();
		
		// Add all buttons except the neural model lab button
		toolBox.getChildren().addAll(
				homeButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				synapseButton,
				neuralNetButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				gamesButton);
	}
	
	/**
	 * Displays the tool bar for the Neural Network Lab app.
	 */
	public void displayNeuralNetToolBar() {
		// Clear old tool bar
		toolBox.getChildren().clear();
		
		// Add all buttons except the neural network lab button
		toolBox.getChildren().addAll(
				homeButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				synapseButton,
				neuralModelButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				gamesButton);
	}
	
	/**
	 * Displays the tool bar for the Synapse Lab app.
	 */
	public void displaySynapseToolBar() {
		// Clear old tool bar
		toolBox.getChildren().clear();
		
		// Add all buttons except the synapse lab button
		toolBox.getChildren().addAll(
				homeButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				neuralModelButton,
				neuralNetButton,
				new Separator(Separator.HORIZONTAL, 62.0),
				gamesButton);
	}
	
	/**
	 * Sends a SideToolBarEvent to all TopToolBarListeners with the specified item.
	 * @param item - The selected item.
	 */
	private void sendEvent(Item item) {
		SideToolBarEvent event = new SideToolBarEvent(this, item);
		
		if (listener != null)
			listener.itemSelected(event);
	}
	
	/**
	 * Sets the SideToolBarListener to the specified listener.
	 * @param listener - The new SideToolBarListener.
	 */
	public void setListener(SideToolBarListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void sizeChanged(AppSizeEvent e) {
		setWidth(e.getWidth());
	}
}
