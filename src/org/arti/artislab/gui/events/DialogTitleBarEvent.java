package org.arti.artislab.gui.events;

import java.util.EventObject;

import org.arti.artislab.gui.DialogTitleBar;

/**
 * <p>public class <b>DialogTitleBarEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>DialogTitleBarEvent class represents an event sent from a DialogTitleBar to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class DialogTitleBarEvent extends EventObject {
	// Serial version unique identifier for AppTitleBarEvent.
	private static final long serialVersionUID = -3602914493437535320L;
	
	// The title bar action that is triggering the event.
	private DialogTitleBar.Action action;

	/**
	 * Creates a DialogTitleBarEvent with the specified DialogTitleBar source and the specified DialogTitleBar action.
	 * @param source - The DialogTitleBar creating this.
	 * @param action - The action triggering this.
	 */
	public DialogTitleBarEvent(DialogTitleBar source, DialogTitleBar.Action action) {
		// Call parent constructor
		super(source);
		
		this.action = action;
	}

	/**
	 * Returns the action triggering this event.
	 * @return The title bar action.
	 */
	public DialogTitleBar.Action getAction() {
		return action;
	}
}
