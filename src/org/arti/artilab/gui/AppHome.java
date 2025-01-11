package org.arti.artilab.gui;

import org.arti.artilab.gui.events.AppSizeEvent;
import org.arti.artilab.gui.events.AppSizeListener;

import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>AppHome</b><br>
 * extends {@link Pane}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>AppHome class creates and controls the Artilab's home workspace.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppHome extends Pane implements AppSizeListener {
	/**
	 * Default window height.
	 */
	public static final int DEF_HEIGHT = App.DEF_WORKSPACE_HEIGHT;
	/**
	 * Default window width.
	 */
	public static final int DEF_WIDTH = App.DEF_WIDTH;
	
	/**
	 * Default background color.
	 */
	public static final Color DEF_BG_COLOR = App.DEF_BG_COLOR;
	
	/**
	 * Default constructor. Creates the Artilab home workspace.
	 */
	public AppHome() {
		// Call parent constructor
		super();
		
		// Setup layout
		setPrefHeight(DEF_HEIGHT);
		setPrefWidth(DEF_WIDTH);
		setBackground(Background.fill(DEF_BG_COLOR));
	}

	@Override
	public void sizeChanged(AppSizeEvent e) {
		setHeight(e.getHeight());
		setWidth(e.getWidth());
	}
}
