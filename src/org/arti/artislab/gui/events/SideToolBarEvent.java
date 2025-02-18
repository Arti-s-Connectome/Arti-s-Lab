package org.arti.artislab.gui.events;

import java.util.EventObject;

import org.arti.artislab.gui.SideToolBar;

/**
 * <p>public class <b>SideToolBarEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>SideToolBarEvent class represents an event sent from the SideToolBar to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class SideToolBarEvent extends EventObject {
	// Serial version unique identifier for SideToolBarEvent.
	private static final long serialVersionUID = -3602914493437535320L;
	
	// The tool bar item that was selected to cause the event.
	private SideToolBar.Item item;

	/**
	 * Creates an SideToolBarEvent with the specified SideToolBar source and the specified SideToolBar item.
	 * @param source - The SideToolBar creating this.
	 * @param item - The selected item.
	 */
	public SideToolBarEvent(SideToolBar source, SideToolBar.Item item) {
		// Call parent constructor
		super(source);
		
		// Initialize variables
		this.item = item;
	}

	/**
	 * Returns the selected item causing this event.
	 * @return The tool bar item.
	 */
	public SideToolBar.Item getItem() {
		return item;
	}
}
