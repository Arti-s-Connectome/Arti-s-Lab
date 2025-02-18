package org.arti.artislab.gui;

import java.util.ArrayList;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;
import org.arti.artislab.gui.events.AppTitleBarEvent;
import org.arti.artislab.gui.events.AppTitleBarListener;

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
 * <p>public class <b>AppTitleBar</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>AppTitleBar class creates and controls the Arti's Lab application's title bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppTitleBar extends HBox implements AppSizeListener {
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
	 * Maximize window icon location.
	 */
	private static final String MAXIMIZE_ICON = "icons/icons8-maximize-window-32.png";
	/**
	 * Maximize window hover icon location.
	 */
	private static final String MAXIMIZE_HOVER_ICON = "icons/icons8-maximize-window-hover-32.png";
	/**
	 * Maximize window pressed icon location.
	 */
	private static final String MAXIMIZE_PRESSED_ICON = "icons/icons8-maximize-window-pressed-32.png";
	/**
	 * Minimize window icon location.
	 */
	private static final String MINIMIZE_ICON = "icons/icons8-minimize-window-32.png";
	/**
	 * Minimize window hover icon location.
	 */
	private static final String MINIMIZE_HOVER_ICON = "icons/icons8-minimize-window-hover-32.png";
	/**
	 * Minimize window pressed icon location.
	 */
	private static final String MINIMIZE_PRESSED_ICON = "icons/icons8-minimize-window-pressed-32.png";
	/**
	 * Restore window icon location.
	 */
	private static final String RESTORE_ICON = "icons/icons8-restore-window-32.png";
	/**
	 * Restore window hover icon location.
	 */
	private static final String RESTORE_HOVER_ICON = "icons/icons8-restore-window-hover-32.png";
	/**
	 * Restore window pressed icon location.
	 */
	private static final String RESTORE_PRESSED_ICON = "icons/icons8-restore-window-pressed-32.png";
	
	/**
	 * <p>public enum <b>Action</b></p>
	 * 
	 * <p>Action enum contains the list of actions that can trigger an AppTitleBarEvent.</p>
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
		 * Maximize button action.
		 */
		MAXIMIZE,
		/**
		 * Minimize button action.
		 */
		MINIMIZE,
		/**
		 * Mouse pressed action.
		 */
		PRESSED,
		/**
		 * Restore button action.
		 */
		RESTORE
	}
	
	// Button box layout.
	private HBox buttonBox;
	// Close button.
	private Button closeButton;
	// Maximize button.
	private Button maximizeButton;
	// Maximized flag.
	private boolean maximized;
	// Minimize button.
	private Button minimizeButton;
	// The AppTitleBarEvent listeners.
	private ArrayList<AppTitleBarListener> listener;
	// Title box layout.
	private HBox titleBox;
	// App title label.
	private Label titleLabel;
	
	/**
	 * Default constructor. Creates the Arti's Lab application title bar.
	 */
	public AppTitleBar() {
		// Call parent constructor
		super();
		
		// Initialize variables
		maximized = false;
		listener = new ArrayList<AppTitleBarListener>();
		
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
		
		Image maximizeImg = new Image(MAXIMIZE_ICON);
		ImageView maximizeView = new ImageView(maximizeImg);
		
		Image maximizeHoverImg = new Image(MAXIMIZE_HOVER_ICON);
		ImageView maximizeHoverView = new ImageView(maximizeHoverImg);
		
		Image maximizePressedImg = new Image(MAXIMIZE_PRESSED_ICON);
		ImageView maximizePressedView = new ImageView(maximizePressedImg);
		
		Image minimizeImg = new Image(MINIMIZE_ICON);
		ImageView minimizeView = new ImageView(minimizeImg);
		
		Image minimizeHoverImg = new Image(MINIMIZE_HOVER_ICON);
		ImageView minimizeHoverView = new ImageView(minimizeHoverImg);
		
		Image minimizePressedImg = new Image(MINIMIZE_PRESSED_ICON);
		ImageView minimizePressedView = new ImageView(minimizePressedImg);
		
		Image restoreImg = new Image(RESTORE_ICON);
		ImageView restoreView = new ImageView(restoreImg);
		
		Image restoreHoverImg = new Image(RESTORE_HOVER_ICON);
		ImageView restoreHoverView = new ImageView(restoreHoverImg);
		
		Image restorePressedImg = new Image(RESTORE_PRESSED_ICON);
		ImageView restorePressedView = new ImageView(restorePressedImg);
		
		// Create title label
		titleLabel = new Label("Arti's Lab");
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
		
		// Create maximize button
		maximizeButton = new Button();
		maximizeButton.setGraphic(maximizeView);
		maximizeButton.setPadding(new Insets(0, 0, 0, 0));
		maximizeButton.setBackground(Background.EMPTY);
		maximizeButton.setPrefWidth(48.0);
		maximizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (maximized) {
					maximized = false;
					sendEvent(Action.RESTORE);
				}
				else {
					maximized = true;
					sendEvent(Action.MAXIMIZE);
				}
			}
		});
		maximizeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (maximized) {
					if (arg0.isPrimaryButtonDown())
						maximizeButton.setGraphic(restorePressedView);
					else
						maximizeButton.setGraphic(restoreHoverView);
				}
				else {
					if (arg0.isPrimaryButtonDown())
						maximizeButton.setGraphic(maximizePressedView);
					else
						maximizeButton.setGraphic(maximizeHoverView);
				}
			}
		});
		maximizeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (maximized) {
					if (arg0.isPrimaryButtonDown())
						maximizeButton.setGraphic(restorePressedView);
					else
						maximizeButton.setGraphic(restoreView);
				}
				else {
					if (arg0.isPrimaryButtonDown())
						maximizeButton.setGraphic(maximizePressedView);
					else
						maximizeButton.setGraphic(maximizeView);
				}
			}
		});
		maximizeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (maximized) {
					maximizeButton.setGraphic(restorePressedView);
				}
				else {
					maximizeButton.setGraphic(maximizePressedView);
				}
			}
		});
		maximizeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = maximizeButton.getBoundsInLocal();
				
				if (maximized) {
					if (bounds.contains(arg0.getX(), arg0.getY()))
						maximizeButton.setGraphic(restoreHoverView);
					else
						maximizeButton.setGraphic(restoreView);
				}
				else {
					if (bounds.contains(arg0.getX(), arg0.getY()))
						maximizeButton.setGraphic(maximizeHoverView);
					else
						maximizeButton.setGraphic(maximizeView);
				}
			}
		});
		
		// Create minimize button
		minimizeButton = new Button();
		minimizeButton.setGraphic(minimizeView);
		minimizeButton.setPadding(new Insets(0, 0, 0, 0));
		minimizeButton.setBackground(Background.EMPTY);
		minimizeButton.setPrefWidth(48.0);
		minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Action.MINIMIZE);
			}
		});
		minimizeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					minimizeButton.setGraphic(minimizePressedView);
				else
					minimizeButton.setGraphic(minimizeHoverView);
			}
		});
		minimizeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					minimizeButton.setGraphic(minimizePressedView);
				else
					minimizeButton.setGraphic(minimizeView);
			}
		});
		minimizeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				minimizeButton.setGraphic(minimizePressedView);
			}
		});
		minimizeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = minimizeButton.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					minimizeButton.setGraphic(minimizeHoverView);
				else
					minimizeButton.setGraphic(minimizeView);
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
		
		buttonBox = new HBox(minimizeButton, maximizeButton, closeButton);
		
		getChildren().addAll(titleBox, buttonBox);
	}
	
	/**
	 * Adds the specified AppTitleBarListener to the list of listeners.
	 * @param listener - The AppTitleBarListener to add.
	 */
	public void addListener(AppTitleBarListener listener) {
		if (listener == null)
			throw new NullPointerException("Error: Cannot add AppTitleBarListener. listener == null.");
		
		this.listener.add(listener);
	}
	
	/**
	 * Returns the current title of the Arti's Lab app window.
	 * @return The title.
	 */
	public String getTitle() {
		return titleLabel.getText();
	}
	
	/**
	 * Sends an AppTitleBarEvent to all AppTitleBarListeners with the specified action.
	 * @param action - The triggered action.
	 */
	private void sendEvent(Action action) {
		AppTitleBarEvent e = new AppTitleBarEvent(this, action);
		
		for (AppTitleBarListener l : listener)
			l.actionTriggered(e);
	}
	
	/**
	 * Sets the title of the Arti's Lab app title bar to the specified title.
	 * @param title - The new title.
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	@Override
	public void sizeChanged(AppSizeEvent e) {                      
		setWidth(e.getWidth());
	}
}
