package org.arti.artislab.gui;

import java.util.ArrayList;

import org.arti.artislab.gui.events.DialogTitleBarEvent;
import org.arti.artislab.gui.events.DialogTitleBarListener;
import org.arti.artislab.gui.events.DialogWindowEvent;
import org.arti.artislab.gui.events.DialogWindowListener;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <p>public class <b>DialogWindow</b><br>
 * extends {@link Stage}</p>
 * 
 * <p>DialogWindow class is the base class for any dialog window in the Arti's Lab app. Dialog windows are application modal windows that block other windows
 * until they are closed.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class DialogWindow extends Stage implements DialogTitleBarListener {
	// The dialog window's content pane.
	private Pane content;
	// The main layout for the dialog window.
	private VBox layout;
	// The layout's shape.
	private Rectangle layoutShape;
	// Dialog window listeners.
	protected ArrayList<DialogWindowListener> listener;
	// Previous mouse x coordinate.
	private double prevMouseX;
	// Previous mouse y coordinate.
	private double prevMouseY;
	// The dialog window's scene.
	private Scene scene;
	// The dialog windows' title bar.
	private DialogTitleBar titleBar;
	
	/**
	 * Creates a dialog window with the specified title and the specified width and height.
	 * @param title - The title of the window.
	 * @param width - The window's width.
	 * @param height - The window's height.
	 */
	public DialogWindow(String title, double width, double height) {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = new ArrayList<DialogWindowListener>();
		
		// Setup stage
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.APPLICATION_MODAL);
		setWidth(width);
		setHeight(height);
		
		// Create titleBar
		titleBar = new DialogTitleBar(title);
		titleBar.addListener(this);
		
		// Create content pane
		content = new Pane();
		content.setBackground(Background.fill(App.DEF_BG_COLOR));
		content.setMaxWidth(width - 5.0);
		content.setMinHeight(height - DialogTitleBar.HEIGHT - 10.0);
		
		// Create window layout
		layout = new VBox();
		layout.setBackground(Background.fill(App.DEF_BG_COLOR));
		layoutShape = new Rectangle();
		layoutShape.setX(0.0);
		layoutShape.setY(0.0);
		layoutShape.setWidth(width);
		layoutShape.setHeight(height);
		layoutShape.setArcWidth(20.0);
		layoutShape.setArcHeight(20.0);
		layout.setShape(layoutShape);
		layout.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
		layout.setBorder(new Border(new BorderStroke(Color.rgb(22, 200, 249), BorderStrokeStyle.SOLID, new CornerRadii(20.0), new BorderWidths(2))));
		
		layout.getChildren().addAll(titleBar, content);
		
		// Create scene
		scene = new Scene(layout);
		scene.getStylesheets().addAll(
				App.AXIS_CSS,
				App.BUTTON_CSS,
				App.CHART_CSS, 
				App.COMBOBOX_CSS,
				App.MENU_CSS, 
				App.PROGRESS_BAR_CSS,
				App.SCROLLBAR_CSS,
				App.SEPARATOR_CSS,
				App.TEXT_CSS,
				App.TEXTAREA_CSS);
		scene.setFill(Color.TRANSPARENT);
		
		// Show stage
		setScene(scene);
		show();
	}
	
	/**
	 * Creates a dialog window with the specified title and image and the specified width and height.
	 * @param title - The title of the window.
	 * @param image - The title image of the window.
	 * @param width - The window's width.
	 * @param height - The window's height.
	 */
	public DialogWindow(String title, ImageView image, double width, double height) {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = new ArrayList<DialogWindowListener>();
		
		// Setup stage
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.APPLICATION_MODAL);
		setWidth(width);
		setHeight(height);
		
		// Create titleBar
		titleBar = new DialogTitleBar(title, image);
		titleBar.addListener(this);
		
		// Create content pane
		content = new Pane();
		content.setBackground(Background.fill(App.DEF_BG_COLOR));
		content.setMaxWidth(width - 5.0);
		content.setMinHeight(height - DialogTitleBar.HEIGHT - 10.0);
		
		// Create window layout
		layout = new VBox();
		layout.setBackground(Background.fill(App.DEF_BG_COLOR));
		layoutShape = new Rectangle();
		layoutShape.setX(0.0);
		layoutShape.setY(0.0);
		layoutShape.setWidth(width);
		layoutShape.setHeight(height);
		layoutShape.setArcWidth(20.0);
		layoutShape.setArcHeight(20.0);
		layout.setShape(layoutShape);
		layout.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
		layout.setBorder(new Border(new BorderStroke(App.INFO_BORDER_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(20.0), BorderWidths.DEFAULT)));
		
		layout.getChildren().addAll(titleBar, content);
		
		// Create scene
		scene = new Scene(layout);
		scene.getStylesheets().addAll(
				App.AXIS_CSS,
				App.BUTTON_CSS,
				App.CHART_CSS, 
				App.COMBOBOX_CSS,
				App.MENU_CSS, 
				App.PROGRESS_BAR_CSS,
				App.SCROLLBAR_CSS,
				App.SEPARATOR_CSS,
				App.TEXT_CSS,
				App.TEXTAREA_CSS);
		scene.setFill(Color.TRANSPARENT);
		
		// Show stage
		setScene(scene);
		show();
	}
	
	@Override
	public void actionTriggered(DialogTitleBarEvent e) {
		Robot robot = new Robot();
		
		switch (e.getAction()) {
		case DialogTitleBar.Action.CLOSE:
			sendDialogClosedEvent();
			close();
			break;
		case DialogTitleBar.Action.DRAGGING:
			scene.getWindow().setX(scene.getWindow().getX() - (prevMouseX - robot.getMouseX()));
			scene.getWindow().setY(scene.getWindow().getY() - (prevMouseY - robot.getMouseY()));
			prevMouseX = robot.getMouseX();
			prevMouseY = robot.getMouseY();
			break;
		case DialogTitleBar.Action.PRESSED:
			prevMouseX = robot.getMouseX();
			prevMouseY = robot.getMouseY();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Adds the specified DialogWindowListener to the list of DialogWindowListeners. If the specified DialogWindowListener is null, it is ignored.
	 * @param listener - The DialogWindowListener to add.
	 */
	public void addDialogWindowListener(DialogWindowListener listener) {
		if (listener != null)
			this.listener.add(listener);
	}
	
	/**
	 * Returns the content pane of this dialog window. This is used for adding components to the dialog window's content.
	 * @return The content pane.
	 */
	public Pane getContentPane() {
		return content;
	}
	
	/**
	 * Returns the current title of this dialog window.
	 * @return The title.
	 */
	public String getDialogTitle() {
		return titleBar.getTitle();
	}
	
	/**
	 * Removes the specified DialogWindowListener from the list of DialogWindowListeners.
	 * @param listener - The DialogWindowListener to remove.
	 * @return True if listener is removed, otherwise false.
	 */
	public boolean removeDialogWindowListener(DialogWindowListener listener) {
		return this.listener.remove(listener);
	}
	
	// Informs all DialogWindowEventListeners that this dialog window has closed.
	protected void sendDialogClosedEvent() {
		for (DialogWindowListener l : listener)
			l.dialogClosed(new DialogWindowEvent(this));
	}
	
	// Informs all DialogWindowEventListeners that this dialog window has completed and closed.
	protected void sendDialogCompletedEvent() {
		for (DialogWindowListener l : listener)
			l.dialogCompleted(new DialogWindowEvent(this));
	}
	
	/**
	 * Sets the color of the border to the specified color.
	 * @param color - The new border color.
	 */
	public void setDialogBorderColor(Color color) {
		layout.setBorder(new Border(new BorderStroke((color != null) ? color : App.INFO_BORDER_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(20.0), 
				BorderWidths.DEFAULT)));
	}
	
	/**
	 * Sets the title of this dialog window to the specified title.
	 * @param title - The new title.
	 */
	public void setDialogTitle(String title) {
		titleBar.setTitle(title);
	}
}
