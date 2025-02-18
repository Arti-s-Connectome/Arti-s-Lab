package org.arti.artislab.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * <p>public class <b>WarningDialog</b><br>
 * extends {@link DialogWindow}</p>
 * 
 * <p>WarningDialog class creates and handles a warning message dialog window in the Arti's Lab app.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class WarningDialog extends DialogWindow {
	/**
	 * Error icon location.
	 */
	private static final String WARN_ICON = "icons/icons8-warning-32.png";
	/**
	 * Logo error icon location.
	 */
	private static final String LOGO_WARN_ICON = "icons/warning.png";
	
	// Warning image label.
	private Label imageLabel;
	// Warning message label.
	private Label messageLabel;
	// Ok button.
	private Button okButton;
	
	/**
	 * Creates a warning dialog with the specified title, message and the specified width and height.
	 * @param title - The title of the window.
	 * @param message - The warning message.
	 */
	public WarningDialog(String title, String message) {
		// Call parent constructor
		super(title, new ImageView(new Image(WARN_ICON)), 400.0, 200.0);
		
		// Set border color
		setDialogBorderColor(App.WARN_BORDER_COLOR);
		
		// Create image label
		imageLabel = new Label();
		imageLabel.setGraphic(new ImageView(new Image(LOGO_WARN_ICON)));
		imageLabel.setMinHeight(96.0);
		imageLabel.setMaxHeight(96.0);
		imageLabel.setMinWidth(96.0);
		imageLabel.setMaxWidth(96.0);
		imageLabel.setPadding(new Insets(4.0, 4.0, 4.0, 4.0));
		
		// Create message label
		messageLabel = new Label(message);
		messageLabel.setMinHeight(96.0);
		messageLabel.setMaxHeight(96.0);
		messageLabel.setMinWidth(292.0);
		messageLabel.setMaxWidth(292.0);
		messageLabel.setAlignment(Pos.CENTER);
		messageLabel.setPadding(new Insets(4.0, 4.0, 4.0, 4.0));
		
		HBox labelBox = new HBox(imageLabel, messageLabel);
		labelBox.setPadding(new Insets(0.0, 0.0, 15.0, 0.0));
		
		// Create ok button
		okButton = new Button("OK");
		okButton.setId("glass-grey");
		okButton.setMinHeight(30.0);
		okButton.setMaxHeight(30.0);
		okButton.setMinWidth(90.0);
		okButton.setMaxWidth(90.0);
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendDialogCompletedEvent();
				close();
			}
		});
		
		HBox buttonBox = new HBox(okButton);
		buttonBox.setMinWidth(400.0);
		buttonBox.setMaxWidth(400.0);
		buttonBox.setAlignment(Pos.CENTER);
		
		VBox layout = new VBox(labelBox, buttonBox);
		layout.setPadding(new Insets(0.0, 0.0, 15.0, 0.0));
		
		getContentPane().getChildren().add(layout);
	}
}
