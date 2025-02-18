package org.arti.artislab.gui;

import java.util.ArrayList;

import org.arti.artislab.gui.events.DialogTitleBarEvent;
import org.arti.artislab.gui.events.DialogTitleBarListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>DialogTitleBar</b><br>
 * extends {@link HBox}</p>
 * 
 * <p>DialogTitleBar class creates and controls the title bars for dialog windows in the Arti's Lab app.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class DialogTitleBar extends HBox {
	/**
	 * Height of the title bar.
	 */
	public static final double HEIGHT = 32.0;
	
	/**
	 * Close window icon location.
	 */
	private static final String CLOSE_ICON = "icons/icons8-close-window-32.png";
	/**
	 * Close window hover icon location.
	 */
	private static final String CLOSE_HOVER_ICON = "icons/icons8-close-window-hover-32.png";
	/**
	 * Close window pressed icon location.
	 */
	private static final String CLOSE_PRESSED_ICON = "icons/icons8-close-window-pressed-32.png";
	/**
	 * Logo icon location.
	 */
	private static final String LOGO_ICON = "icons/logo-icon.png";
	
	/**
	 * <p>public enum <b>Action</b></p>
	 * 
	 * <p>Action enum contains the list of actions that can trigger an DialogTitleBarEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum Action {
		/**
		 * Close button action.
		 */
		CLOSE,
		/**
		 * Mouse dragging action.
		 */
		DRAGGING,
		/**
		 * Mouse pressed action.
		 */
		PRESSED
	}
	
	// Button box layout.
	private HBox buttonBox;
	// Close button.
	private Button closeButton;
	// The DialogTitleBarEvent listeners.
	private ArrayList<DialogTitleBarListener> listener;
	// Title box layout.
	private HBox titleBox;
	// App title label.
	private Label titleLabel;
	
	/**
	 * Creates a dialog window title bar with the specified title.
	 * @param title - The title of the dialog window.
	 */
	public DialogTitleBar(String title) {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = new ArrayList<DialogTitleBarListener>();
		
		// Initialize title bar
		setMaxHeight(HEIGHT);
		setPrefHeight(HEIGHT);
		setBackground(Background.fill(Color.TRANSPARENT));
		
		// Load icon images
		Image closeImg = new Image(CLOSE_ICON);
		ImageView closeView = new ImageView(closeImg);
		
		Image closeHoverImg = new Image(CLOSE_HOVER_ICON);
		ImageView closeHoverView = new ImageView(closeHoverImg);
		
		Image closePressedImg = new Image(CLOSE_PRESSED_ICON);
		ImageView closePressedView = new ImageView(closePressedImg);
		
		Image logoImg = new Image(LOGO_ICON);
		ImageView logoView = new ImageView(logoImg);
		
		// Create title label
		titleLabel = new Label(title);
		titleLabel.setGraphic(logoView);
		titleLabel.setTextFill(Color.WHITE);
		titleLabel.setMinWidth(150.0);
		
		// Create close button
		closeButton = new Button();
		closeButton.setGraphic(closeView);
		closeButton.setPadding(new Insets(0, 0, 0, 0));
		closeButton.setBackground(Background.EMPTY);
		closeButton.setPrefWidth(48.0);
		closeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					closeButton.setGraphic(closePressedView);
				else
					closeButton.setGraphic(closeHoverView);
			}
		});
		closeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					closeButton.setGraphic(closePressedView);
				else
					closeButton.setGraphic(closeView);
			}
		});
		closeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				closeButton.setGraphic(closePressedView);
			}
		});
		closeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = closeButton.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					closeButton.setGraphic(closeHoverView);
				else
					closeButton.setGraphic(closeView);
			}
		});
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Action.CLOSE);
			}
		});
		
		// Handle mouse pressed and dragging title bar
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sendEvent(Action.PRESSED);
			}
		});
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sendEvent(Action.DRAGGING);
			}
		});
		
		// Add components
		titleBox = new HBox(titleLabel);
		HBox.setHgrow(titleBox, Priority.ALWAYS);
		
		buttonBox = new HBox(closeButton);
		
		getChildren().addAll(titleBox, buttonBox);
	}
	
	/**
	 * Creates a dialog window title bar with the specified title and image.
	 * @param title - The title of the dialog window.
	 * @param image - The title image to use.
	 */
	public DialogTitleBar(String title, ImageView image) {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = new ArrayList<DialogTitleBarListener>();
		
		// Initialize title bar
		setMaxHeight(HEIGHT);
		setPrefHeight(HEIGHT);
		setBackground(Background.fill(Color.TRANSPARENT));
		
		// Load icon images
		Image closeImg = new Image(CLOSE_ICON);
		ImageView closeView = new ImageView(closeImg);
		
		Image closeHoverImg = new Image(CLOSE_HOVER_ICON);
		ImageView closeHoverView = new ImageView(closeHoverImg);
		
		Image closePressedImg = new Image(CLOSE_PRESSED_ICON);
		ImageView closePressedView = new ImageView(closePressedImg);
		
		Image logoImg = new Image(LOGO_ICON);
		ImageView logoView = (image != null) ? image : new ImageView(logoImg);
		
		// Create title label
		titleLabel = new Label(title);
		titleLabel.setGraphic(logoView);
		titleLabel.setTextFill(Color.WHITE);
		titleLabel.setMinWidth(150.0);
		
		// Create close button
		closeButton = new Button();
		closeButton.setGraphic(closeView);
		closeButton.setPadding(new Insets(0, 0, 0, 0));
		closeButton.setBackground(Background.EMPTY);
		closeButton.setPrefWidth(48.0);
		closeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					closeButton.setGraphic(closePressedView);
				else
					closeButton.setGraphic(closeHoverView);
			}
		});
		closeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					closeButton.setGraphic(closePressedView);
				else
					closeButton.setGraphic(closeView);
			}
		});
		closeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				closeButton.setGraphic(closePressedView);
			}
		});
		closeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = closeButton.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					closeButton.setGraphic(closeHoverView);
				else
					closeButton.setGraphic(closeView);
			}
		});
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Action.CLOSE);
			}
		});
		
		// Handle mouse pressed and dragging title bar
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sendEvent(Action.PRESSED);
			}
		});
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sendEvent(Action.DRAGGING);
			}
		});
		
		// Add components
		titleBox = new HBox(titleLabel);
		HBox.setHgrow(titleBox, Priority.ALWAYS);
		
		buttonBox = new HBox(closeButton);
		
		getChildren().addAll(titleBox, buttonBox);
	}
	
	/**
	 * Adds the specified DialogTitleBarListener to the list of listeners.
	 * @param listener - The DialogTitleBarListener to add.
	 */
	public void addListener(DialogTitleBarListener listener) {
		if (listener == null)
			throw new NullPointerException("Error: Cannot add DialogTitleBarListener. listener == null.");
		
		this.listener.add(listener);
	}
	
	/**
	 * Returns the current title of this dialog window.
	 * @return The title.
	 */
	public String getTitle() {
		return titleLabel.getText();
	}
	
	/**
	 * Sends an DialogTitleBarEvent to all DialogTitleBarListeners with the specified action.
	 * @param action - The triggered action.
	 */
	private void sendEvent(Action action) {
		DialogTitleBarEvent e = new DialogTitleBarEvent(this, action);
		
		for (DialogTitleBarListener l : listener)
			l.actionTriggered(e);
	}
	
	/**
	 * Sets the title of this title bar to the specified title.
	 * @param title - The new title.
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
