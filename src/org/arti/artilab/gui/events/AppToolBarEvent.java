package org.arti.artilab.gui.events;

import java.util.EventObject;

import org.arti.artilab.gui.AppToolBar;

/**
 * <p>public class <b>AppToolBarEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>AppToolBarEvent class represents an event sent from the AppToolBar to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppToolBarEvent extends EventObject {
	// Serial version unique identifier for AppToolBarEvent.
	private static final long serialVersionUID = -3602914493437535320L;
	
	// The tool bar item that was selected to cause the event.
	private AppToolBar.Item item;
	// The tool bar item detail attached to the selected tool bar item.
	private AppToolBar.Detail detail;

	/**
	 * Creates an AppToolBarEvent with the specified AppToolBar source, the specified AppToolBar item, and the specified AppToolBar detail that
	 * applies to the item, if any.
	 * @param source - The AppToolBar creating this.
	 * @param item - The selected item.
	 * @param detail - The selected item's detail.
	 */
	public AppToolBarEvent(AppToolBar source, AppToolBar.Item item, AppToolBar.Detail detail) {
		// Call parent constructor
		super(source);
		
		// Initialize variables
		this.item = item;
		this.detail = detail;
	}
	
	/**
	 * Returns the selected item's detail.
	 * @return The tool bar item's detail.
	 */
	public AppToolBar.Detail getDetail() {
		return detail;
	}

	/**
	 * Returns the selected item causing this event.
	 * @return The tool bar item.
	 */
	public AppToolBar.Item getItem() {
		return item;
	}
}
