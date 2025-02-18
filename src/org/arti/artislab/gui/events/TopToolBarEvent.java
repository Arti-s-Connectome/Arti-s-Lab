package org.arti.artislab.gui.events;

import java.util.EventObject;

import org.arti.artislab.gui.TopToolBar;

/**
 * <p>public class <b>TopToolBarEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>TopToolBarEvent class represents an event sent from the TopToolBar to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class TopToolBarEvent extends EventObject {
	// Serial version unique identifier for TopToolBarEvent.
	private static final long serialVersionUID = -3602914493437535320L;
	
	// The tool bar item that was selected to cause the event.
	private TopToolBar.Item item;
	// The tool bar item detail attached to the selected tool bar item.
	private TopToolBar.Detail detail;

	/**
	 * Creates an TopToolBarEvent with the specified TopToolBar source, the specified TopToolBar item, and the specified TopToolBar detail that
	 * applies to the item, if any.
	 * @param source - The TopToolBar creating this.
	 * @param item - The selected item.
	 * @param detail - The selected item's detail.
	 */
	public TopToolBarEvent(TopToolBar source, TopToolBar.Item item, TopToolBar.Detail detail) {
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
	public TopToolBar.Detail getDetail() {
		return detail;
	}

	/**
	 * Returns the selected item causing this event.
	 * @return The tool bar item.
	 */
	public TopToolBar.Item getItem() {
		return item;
	}
}
