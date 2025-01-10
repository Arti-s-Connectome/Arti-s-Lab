package org.arti.artilab.gui.events;

/**
 * <p>public interface <b>DisplayNeuronListener</b></p>
 * 
 * <p>DisplayNeuronListener interface provides methods for handling DisplayNeuronEvents passed to the implementing listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public interface DisplayNeuronListener {
	/**
	 * Handles when an action is triggered by a DisplayNeuron.
	 * @param e - The DisplayNeuronEvent containing which action was triggered.
	 */
	public void actionTriggered(DisplayNeuronEvent e);
}
