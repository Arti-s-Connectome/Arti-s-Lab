package org.arti.artislab.gui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;

/**
 * <p>public class <b>ImageButton</b><br>
 * extends {@link Button}</p>
 * 
 * <p>ImageButton class represents a JavaFX button used by Arti's Lab's TopToolBar. A tool bar button is an icon only button located on the 
 * tool bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class ImageButton extends Button {
	/**
	 * The size of the icon.
	 */
	public static final int ICON_SIZE = 32;
	
	// Icon name ending.
	private static final String ENDING_NAME = "-" + ICON_SIZE + ".png";
	// Hover icon name extension.
	private static final String HOVER_NAME = "-hover";
	// Pressed icon name extension.
	private static final String PRESSED_NAME = "-pressed";
	
	// Normal icon.
	private Icon icon;
	// Hover icon.
	private Icon iconHover;
	// Pressed icon.
	private Icon iconPressed;
	
	/**
	 * Creates a tool bar button using the specified icon name. The icon name is not the full filename of the icon image. The icon name is
	 * used to produce the full name of the icon image, however. The specified icon name must also have a hover and pressed version. For
	 * example, if the icon is name "icon-test", its full name must be "icon-test-24.png", and it must have a file named 
	 * "icon-test-hover-24.png" and a file name "icon-test-pressed-24.png", or this constructor will fail.
	 * @param iconName - The name of the icon image.
	 * @throws IllegalArgumentException - Thrown if iconName is null.
	 * @throws NullPointerException - Thrown if iconName is invalid or unsupported.
	 */
	public ImageButton(String iconName) throws IllegalArgumentException, NullPointerException {
		// Call parent constructor
		super();
		
		// Initialize variables
		try {
			icon = new Icon(iconName + ENDING_NAME);
			iconHover = new Icon(iconName + HOVER_NAME + ENDING_NAME);
			iconPressed = new Icon(iconName + PRESSED_NAME + ENDING_NAME);
		}
		catch (Exception e) {
			throw e;
		}
		
		// Setup button
		setGraphic(icon);
		setPadding(new Insets(0, 0, 0, 0));
		setBackground(Background.EMPTY);
		
		// Handle mouse events
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					setGraphic(iconPressed);
				else
					setGraphic(iconHover);
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					setGraphic(iconPressed);
				else
					setGraphic(icon);
			}
		});
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				setGraphic(iconPressed);
			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					setGraphic(iconHover);
				else
					setGraphic(icon);
			}
		});
	}
}
