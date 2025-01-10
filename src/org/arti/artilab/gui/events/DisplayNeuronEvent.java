package org.arti.artilab.gui.events;

import java.util.EventObject;

import org.arti.artilab.gui.netdisplay.DisplayNeuron;

/**
 * <p>public class <b>DisplayNeuronEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>DisplayNeuronEvent class represents an event sent from a neural network display neuron to its listeners.
 */
public class DisplayNeuronEvent extends EventObject {
	// Serial version unique identifier for DisplayNeuronEvent.
	private static final long serialVersionUID = -3968644784995929521L;
	
	// The display neuron action that is triggering the event.
	private DisplayNeuron.Action action;

	/**
	 * Creates an DisplayNeuronEvent with the specified DisplayNeuron source and the specified DisplayNeuron action.
	 * @param source - The DisplayNeuron creating this.
	 * @param action - The action triggering this.
	 */
	public DisplayNeuronEvent(DisplayNeuron source, DisplayNeuron.Action action) {
		// Call parent constructor
		super(source);
		
		this.action = action;
	}

	/**
	 * Returns the action triggering this event.
	 * @return The display neuron action.
	 */
	public DisplayNeuron.Action getAction() {
		return action;
	}
}
