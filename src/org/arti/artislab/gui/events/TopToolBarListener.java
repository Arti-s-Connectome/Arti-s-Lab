package org.arti.artislab.gui.events;

/**
 * <p>public interface <b>TopToolBarListener</b></p>
 * 
 * <p>TopToolBarListener interface provides methods for handling AppToolBarEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface TopToolBarListener {
	/**
	 * Handles when an item on the TopToolBar is selected.
	 * @param e - The TopToolBarEvent containing which item was selected.
	 */
	public void itemSelected(TopToolBarEvent e);
}
