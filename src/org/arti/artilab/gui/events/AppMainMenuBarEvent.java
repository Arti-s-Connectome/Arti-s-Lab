package org.arti.artilab.gui.events;

import java.util.EventObject;

import org.arti.artilab.gui.AppMainMenuBar;

/**
 * <p>public class <b>AppMainMenuBarEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>AppMainMenuBarEvent class represents an event sent from the AppMainMenuBar to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppMainMenuBarEvent extends EventObject {
	// Serial version unique identifier for AppMainMenuBarEvent.
	private static final long serialVersionUID = 8655863964117696050L;
	
	// The menu item that was selected to trigger the event.
	private AppMainMenuBar.Item item;

	/**
	 * Creates an AppMainMenuBarEvent with the specified AppMainMenuBar source and the specified, selected AppMainMenuBar menu item.
	 * @param source - The AppMainMenuBar creating this.
	 * @param item - The menu item triggering this.
	 */
	public AppMainMenuBarEvent(AppMainMenuBar source, AppMainMenuBar.Item item) {
		// Call parent constructor
		super(source);
		
		this.item = item;
	}

	/**
	 * Returns the menu item that was selected to trigger this event.
	 * @return The selected menu item.
	 */
	public AppMainMenuBar.Item getMenuItem() {
		return item;
	}
}
