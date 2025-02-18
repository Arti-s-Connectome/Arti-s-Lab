package org.arti.artislab.gui.events;

import java.util.EventObject;

import org.arti.artislab.gui.DialogWindow;

/**
 * <p>public class <b>DialogWindowEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>DialogWindowEvent class represents an event from a dialog window.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class DialogWindowEvent extends EventObject {
	// Serial version unique ID for DialogWindowEvent.
	private static final long serialVersionUID = 4822550346750738278L;

	/**
	 * Creates a DialogWindowEvent with the specified dialog window source.
	 * @param source - The dialog window creating this event.
	 */
	public DialogWindowEvent(DialogWindow source) {
		// Call parent constructor
		super(source);
	}

}
