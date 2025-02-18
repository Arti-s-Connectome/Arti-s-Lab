package org.arti.artislab.gui.events;

/**
 * <p>public interface <b>SideToolBarListener</b></p>
 * 
 * <p>SideToolBarListener interface provides methods for handling AppToolBarEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface SideToolBarListener {
	/**
	 * Handles when an item on the SideToolBar is selected.
	 * @param e - The SideToolBarEvent containing which item was selected.
	 */
	public void itemSelected(SideToolBarEvent e);
}
