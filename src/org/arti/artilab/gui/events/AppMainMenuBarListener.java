package org.arti.artilab.gui.events;

/**
 * <p>public interface <b>AppMainMenuBarListener</b></p>
 * 
 * <p>AppMainMenuBarListener interface provides methods for handling AppMainMenuBarEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface AppMainMenuBarListener {
	/**
	 * Handles when a menu item is selected from the AppMainMenuBar.
	 * @param e - The AppMainMenuBarEvent containing which menu item was selected.
	 */
	public void menuItemSelected(AppMainMenuBarEvent e);
}
