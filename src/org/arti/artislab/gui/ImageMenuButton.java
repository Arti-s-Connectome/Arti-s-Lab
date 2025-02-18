package org.arti.artislab.gui;

import javafx.geometry.Insets;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Background;

/**
 * <p>public class <b>ImageMenuButton</b><br>
 * extends {@link MenuButton}</p>
 * 
 * <p>ImageButton class represents a JavaFX menu button used by Arti's Lab's TopToolBar. A tool bar button is an icon only menu button located 
 * on the tool bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class ImageMenuButton extends MenuButton {
	/**
	 * The size of the icon.
	 */
	public static final int ICON_SIZE = 32;
	
	// Icon name ending.
	private static final String ENDING_NAME = "-" + ICON_SIZE + ".png";
	
	// Normal icon.
	private Icon icon;
	
	/**
	 * Creates a tool bar menu button using the specified icon name. The icon name is not the full filename of the icon image. The icon name 
	 * is used to produce the full name of the icon image. For example, if the icon is name "icon-test", its full name must be 
	 * "icon-test-24.png", or this constructor will fail.
	 * @param iconName - The name of the icon image.
	 * @throws IllegalArgumentException - Thrown if iconName is null.
	 * @throws NullPointerException - Thrown if iconName is invalid or unsupported.
	 */
	public ImageMenuButton(String iconName) throws IllegalArgumentException, NullPointerException {
		// Call parent constructor
		super();
		
		// Initialize variables
		try {
			icon = new Icon(iconName + ENDING_NAME);
		}
		catch (Exception e) {
			throw e;
		}
		
		// Setup button
		setGraphic(icon);
		setPadding(new Insets(0, 0, 0, 0));
		setBackground(Background.EMPTY);
	}
}
