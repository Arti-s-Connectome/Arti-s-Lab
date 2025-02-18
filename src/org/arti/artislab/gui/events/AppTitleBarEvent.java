package org.arti.artislab.gui.events;

import java.util.EventObject;

import org.arti.artislab.gui.AppTitleBar;

/**
 * <p>public class <b>AppTitleBarEvent</b><br>
 * extends {@link EventObject}</p>
 * 
 * <p>AppTitleBarEvent class represents an event sent from the AppTitleBar to its listeners.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppTitleBarEvent extends EventObject {
	// Serial version unique identifier for AppTitleBarEvent.
	private static final long serialVersionUID = -3602914493437535320L;
	
	// The title bar action that is triggering the event.
	private AppTitleBar.Action action;

	/**
	 * Creates an AppTitleBarEvent with the specified AppTitleBar source and the specified AppTitleBar action.
	 * @param source - The AppTitleBar creating this.
	 * @param action - The action triggering this.
	 */
	public AppTitleBarEvent(AppTitleBar source, AppTitleBar.Action action) {
		// Call parent constructor
		super(source);
		
		this.action = action;
	}

	/**
	 * Returns the action triggering this event.
	 * @return The title bar action.
	 */
	public AppTitleBar.Action getAction() {
		return action;
	}
}
