package org.arti.artilab.gui.events;

import java.util.EventObject;

import org.arti.artilab.gui.App;

/**
 * <p>public class <b>AppSizeEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>AppSizeEvent class represents an event sent from the App to its listeners when the Artilab application window changes size.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppSizeEvent extends EventObject {
	// Serial version unique identifier for AppSizeEvent.
	private static final long serialVersionUID = -4797208232671915563L;
	
	// Current height of the app window.
	private double height;
	// Current width of the app window.
	private double width;
	// Current workspace height of the app window.
	private double workspaceHeight;

	/**
	 * Creates an AppSizeEvent with the specified App source and the specified width, height, and workspace height of the Artilab application 
	 * window.
	 * @param source - The Artilab App creating this.
	 * @param width - The current width of the Artilab application window.
	 * @param height - The current height of the Artilab application window.
	 * @param workspaceHeight - The current workspace height of the Artilab application window.
	 */
	public AppSizeEvent(App source, double width, double height, double workspaceHeight) {
		// Call parent constructor
		super(source);
		
		this.height = height;
		this.width = width;
		this.workspaceHeight = workspaceHeight;
	}
	
	/**
	 * Returns the height of the window.
	 * @return The window height.
	 */
	public double getHeight() { return height; }

	/**
	 * Returns the width of the window.
	 * @return The window width.
	 */
	public double getWidth() { return width; }
	
	/**
	 * Returns the height of the workspace.
	 * @return The workspace height.
	 */
	public double getWorkspaceHeight() { return workspaceHeight; }
}
