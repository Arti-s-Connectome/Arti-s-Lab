package org.arti.artislab.gui.events;

/**
 * <p>public interface <b>NeuralModelLabListener</b></p>
 * 
 * <p>NeuralModelLabListener interface provides methods for handling IzhikevichLabEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface NeuralModelLabListener {
	/**
	 * Handles when an action is triggered by the Izhikevich lab.
	 * @param e - The NeuralModelLabEvent containing which action was triggered.
	 */
	public void actionTriggered(NeuralModelLabEvent e);
}
