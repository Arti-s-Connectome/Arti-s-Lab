package org.arti.artislab.gui.events;

/**
 * <p>public interface <b>DialogTitleBarListener</b></p>
 * 
 * <p>DialogTitleBarListener interface provides methods for handling DialogTitleBarEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface DialogTitleBarListener {
	/**
	 * Handles when an action is triggered by the DialogTitleBar.
	 * @param e - The DialogTitleBarEvent containing which action was triggered.
	 */
	public void actionTriggered(DialogTitleBarEvent e);
}
