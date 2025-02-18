package org.arti.artislab.gui.events;

import java.util.EventObject;

import org.arti.artislab.gui.NeuralModelLab;

/**
 * <p>public class <b>NeuralModelLabEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>NeuralModelLabEvent class represents an event sent from the Izhikevich lab to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class NeuralModelLabEvent extends EventObject {
	// Serial version unique identifier for NeuralModelLabEvent.
	private static final long serialVersionUID = -3602914493437535320L;
	
	// The Izhikevich lab action that is triggering the event.
	private NeuralModelLab.Action[] action;

	/**
	 * Creates an NeuralModelLabEvent with the specified NeuralModelLab source and the specified NeuralModelLab action.
	 * @param source - The NeuralModelLab creating this.
	 * @param action - The action triggering this.
	 */
	public NeuralModelLabEvent(NeuralModelLab source, NeuralModelLab.Action ... action) {
		// Call parent constructor
		super(source);
		
		this.action = action;
	}
	
	/**
	 * Returns the number of actions contained in this event.
	 * @return The number of actions.
	 */
	public int actions() {
		return action.length;
	}

	/**
	 * Returns the action triggering this event at the specified index.
	 * @param index - The index of the action.
	 * @return The neural model lab action at index.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public NeuralModelLab.Action getAction(int index) {
		if (index < 0 || index >= action.length)
			throw new IndexOutOfBoundsException("Error: Cannot get Izhikevich lab event action. Index out of bounds.");
		
		return action[index];
	}
}
