package org.arti.artislab.gui.events;

/**
 * <p>public interface <b>AppSizeListener</b></p>
 * 
 * <p>AppSizeListener interface provides methods for handling AppSizeEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface AppSizeListener {
	/**
	 * Handles when the Artilab application window changes size.
	 * @param e - The AppSizeEvent containing the new window size.
	 */
	public void sizeChanged(AppSizeEvent e);
}
