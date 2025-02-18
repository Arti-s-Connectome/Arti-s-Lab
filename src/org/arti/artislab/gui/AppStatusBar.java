package org.arti.artislab.gui;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;

import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>AppStatusBar</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>AppStatusBar class creates and controls the Arti's Lab application's status bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppStatusBar extends HBox implements AppSizeListener {
	/**
	 * Height of the status bar.
	 */
	public static final int HEIGHT = 32;
	
	/**
	 * Default constructor. Creates the status bar of the Arti's Lab application.
	 */
	public AppStatusBar() {
		// Call parent constructor.
		super();
		
		setMaxHeight(HEIGHT);
		setMinHeight(HEIGHT);
		setBackground(Background.fill(Color.TRANSPARENT));
	}
	
	@Override
	public void sizeChanged(AppSizeEvent e) {                      
		setWidth(e.getWidth());
	}
}
