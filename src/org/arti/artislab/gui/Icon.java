package org.arti.artislab.gui;

import javafx.scene.image.ImageView;

/**
 * <p>public class <b>Icon</b><br>
 * extends {@link ImageView}</p>
 * 
 * <p>Icon class represents an icon image.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class Icon extends ImageView {
	// Location of icon image files.
	private static final String PATH = "icons/";
	
	/**
	 * Creates an icon from the specified filename.
	 * @param filename - The name of the icon image file.
	 * @throws IllegalArgumentException - Thrown if filename is null.
	 * @throws NullPointerException - Thrown if filename is invalid or unsupported.
	 */
	public Icon(String filename) throws IllegalArgumentException, NullPointerException {
		// Call parent constructor
		super(PATH + filename);
	}
}
