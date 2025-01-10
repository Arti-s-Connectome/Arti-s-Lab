package org.arti.artilab.gui.events;

/**
 * <p>public interface <b>AppToolBarListener</b></p>
 * 
 * <p>AppToolBarListener interface provides methods for handling AppToolBarEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface AppToolBarListener {
	/**
	 * Handles when an item on the AppToolBar is selected.
	 * @param e - The AppToolBarEvent containing which item was selected.
	 */
	public void itemSelected(AppToolBarEvent e);
}
