package org.arti.artilab.gui.events;

/**
 * <p>public interface <b>AppTitleBarListener</b></p>
 * 
 * <p>AppTitleBarListener interface provides methods for handling AppTitleBarEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface AppTitleBarListener {
	/**
	 * Handles when an action is triggered by the AppTitleBar.
	 * @param e - The AppTitleBarEvent containing which action was triggered.
	 */
	public void actionTriggered(AppTitleBarEvent e);
}
