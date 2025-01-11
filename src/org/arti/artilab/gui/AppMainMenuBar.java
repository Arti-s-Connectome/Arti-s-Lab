package org.arti.artilab.gui;

import java.util.ArrayList;

import org.arti.artilab.gui.events.AppMainMenuBarEvent;
import org.arti.artilab.gui.events.AppMainMenuBarListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>AppMainMenuBar</b><br>
 * extends {@link MenuBar}</p>
 * 
 * <p>AppMainMenuBar class creates and controls the main menu bar of the Artilab application.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppMainMenuBar extends MenuBar {
	/**
	 * Maximum height of the menu bar.
	 */
	public static final int MAX_HEIGHT = 27;
	
	/**
	 * Default window background color.
	 */
	public static final Color DEF_BG_COLOR = Color.rgb(42,  79,  110);
	
	/**
	 * Empty icon location.
	 */
	private static final String EMPTY_ICON = "icons/icon-empty-16.png";
	/**
	 * Izhikevich icon location.
	 */
	private static final String IZHIKEVICH_ICON = "icons/icons8-izhikevich-16.png";
	/**
	 * Izhikevich icon location.
	 */
	private static final String NEURAL_NET_ICON = "icons/icons8-neural-network-16.png";
	
	/**
	 * <p>public enum <b>Item</b></p>
	 * 
	 * <p>Item enum contains the list of menu items that can be selected and trigger an AppMainMenuBarEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum Item {
		/**
		 * File menu exit item.
		 */
		FILE_EXIT,
		/**
		 * Izhikevich lab item.
		 */
		IZHIKEVICH_LAB,
		/**
		 * Neural network lab item.
		 */
		NEURAL_NET_LAB
	}
	
	// The AppMainMenuBarEvent listeners.
	private ArrayList<AppMainMenuBarListener> listener;
	
	/**
	 * Default constructor. Creates the main menu bar for the Artilab application.
	 */
	public AppMainMenuBar() {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = new ArrayList<AppMainMenuBarListener>();
		
		// Load images
		Image emptyImg = new Image(EMPTY_ICON);
		ImageView emptyView = new ImageView(emptyImg);
		
		Image izhikevichImg = new Image(IZHIKEVICH_ICON);
		ImageView izhikevichView = new ImageView(izhikevichImg);
		
		Image neuralNetImg = new Image(NEURAL_NET_ICON);
		ImageView neuralNetView = new ImageView(neuralNetImg);
		
		// Initialize main menu bar
		setBorder(new Border(new BorderStroke(App.DEF_BORDER_COLOR, null, null, null, 
				BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
				CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(0.0, 0.0, 0.0, 0.0))));
		setBackground(Background.fill(DEF_BG_COLOR));
		setMaxHeight(MAX_HEIGHT);
		setPrefHeight(MAX_HEIGHT);
		
		// Create File menu
		Menu file = new Menu();
		Label fileLabel = new Label("File");
		fileLabel.setStyle("-fx-text-fill: white;");
		file.setGraphic(fileLabel);
		
		// File menu exit item
		MenuItem fileExit = new MenuItem();
		Label fileExitLabel = new Label("Exit");
		fileExitLabel.setStyle("-fx-text-fill: white;");
		fileExitLabel.setGraphic(emptyView);
		fileExit.setGraphic(fileExitLabel);
		fileExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.FILE_EXIT);
			}
		});
		
		// Create Labs menu
		Menu lab = new Menu();
		Label labLabel = new Label("Lab");
		labLabel.setStyle("-fx-text-fill: white;");
		lab.setGraphic(labLabel);
		
		// Lab Izhikevich menu item
		MenuItem labIzhikevich = new MenuItem();
		Label izhikevichLabel = new Label("Izhikevich");
		izhikevichLabel.setStyle("-fx-text-fill: white;");
		izhikevichLabel.setGraphic(izhikevichView);
		labIzhikevich.setGraphic(izhikevichLabel);
		labIzhikevich.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.IZHIKEVICH_LAB);
			}
		});
		
		// Lab neural network menu item
		MenuItem labNeuralNet = new MenuItem();
		Label neuralNetLabel = new Label("Neural Network");
		neuralNetLabel.setStyle("-fx-text-fill: white;");
		neuralNetLabel.setGraphic(neuralNetView);
		labNeuralNet.setGraphic(neuralNetLabel);
		labNeuralNet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURAL_NET_LAB);
			}
		});
		
		// Add items to file menu
		file.getItems().add(fileExit);
		
		// Add items to lab menu
		lab.getItems().addAll(labIzhikevich, labNeuralNet);
		
		// Add all menus to the menu bar
		getMenus().addAll(file, lab);
	}
	
	/**
	 * Adds the specified AppMainMenuBarListener to the list of listeners.
	 * @param listener - The AppMainMenuBarListener to add.
	 */
	public void addListener(AppMainMenuBarListener listener) {
		if (listener == null)
			throw new NullPointerException("Error: Cannot add AppMainMenuBarListener. listener == null.");
		
		this.listener.add(listener);
	}
	
	/**
	 * Sends an AppMainMenuBarEvent to all AppMainMenuBarListeners with the specified selected menu item.
	 * @param item - The selected menu item.
	 */
	private void sendEvent(Item item) {
		AppMainMenuBarEvent e = new AppMainMenuBarEvent(this, item);
		
		for (AppMainMenuBarListener l : listener)
			l.menuItemSelected(e);
	}
}
