package org.arti.artislab.gui.events;

/**
 * <p>public interface <b>DialogWindowListener</b></p>
 * 
 * <p>DialogWindowListener interface provides methods for handling DialogWindowEvents passed to the implementing listeners.</p> 
 */
public interface DialogWindowListener {
	/**
	 * This method is used to handle when a dialog window is closed, but not completed (such as when the close button is clicked).
	 * @param e - The DialogWindowEvent calling this.
	 */
	public void dialogClosed(DialogWindowEvent e);
	/**
	 * This method is used to handle when a dialog window is completed and closed.
	 * @param e - The DialogWindowEvent calling this.
	 */
	public void dialogCompleted(DialogWindowEvent e);
}
