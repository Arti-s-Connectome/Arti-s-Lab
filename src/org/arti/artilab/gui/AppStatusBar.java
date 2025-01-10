package org.arti.artilab.gui;

import org.arti.artilab.gui.events.AppSizeEvent;
import org.arti.artilab.gui.events.AppSizeListener;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>AppStatusBar</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>AppStatusBar class creates and controls the Artilab application's status bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppStatusBar extends HBox implements AppSizeListener {
	/**
	 * Maximum height of the status bar.
	 */
	public static final int MAX_HEIGHT = 32;
	
	/**
	 * Default window background color.
	 */
	public static final Color DEF_BG_COLOR = Color.rgb(42,  79,  110);
	
	/**
	 * Default constructor. Creates the Atrilabn
	 */
	public AppStatusBar() {
		// Call parent constructor.
		super();
		
		setBorder(new Border(new BorderStroke(App.DEF_BORDER_COLOR, null, null, null, 
				BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
				CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(0.0, 0.0, 0.0, 0.0))));
		setBackground(Background.fill(DEF_BG_COLOR));
		setMaxHeight(MAX_HEIGHT);
		setPrefHeight(MAX_HEIGHT);
	}
	
	@Override
	public void sizeChanged(AppSizeEvent e) {                      
		setWidth(e.getWidth());
	}
}
