package org.arti.artilab.gui;

import java.util.ArrayList;

import org.arti.artilab.gui.events.AppMainMenuBarEvent;
import org.arti.artilab.gui.events.AppMainMenuBarListener;
import org.arti.artilab.gui.events.AppSizeEvent;
import org.arti.artilab.gui.events.AppSizeListener;
import org.arti.artilab.gui.events.AppTitleBarEvent;
import org.arti.artilab.gui.events.AppTitleBarListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <p>public class <b>App</b><br>
 * extends {@link Application}<br>
 * implements {@link AppMainMenuBarListener}</p>
 * 
 * <p>App class creates and controls the Artilab JavaFX application.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class App extends Application implements AppMainMenuBarListener, AppTitleBarListener {
	/**
	 * Default window height.
	 */
	public static final int DEF_HEIGHT = 1080;
	/**
	 * Default window width.
	 */
	public static final int DEF_WIDTH = 1920;
	
	/**
	 * Default workspace height.
	 */
	public static final int DEF_WORKSPACE_HEIGHT = DEF_HEIGHT - AppTitleBar.MAX_HEIGHT - AppMainMenuBar.MAX_HEIGHT - AppToolBar.MAX_HEIGHT - 
			AppStatusBar.MAX_HEIGHT;
	
	/**
	 * Default app background color.
	 */
	public static final Color DEF_BG_COLOR = Color.rgb(28,  53,  73);
	/**
	 * Default border color.
	 */
	public static final Color DEF_BORDER_COLOR = Color.rgb(71,  132,  183);
	
	/**
	 * Axis CSS file location.
	 */
	public static final String AXIS_CSS = "css/axis.css";
	/**
	 * Button CSS file location.
	 */
	public static final String BUTTON_CSS = "css/button.css";
	/**
	 * Chart CSS file location.
	 */
	public static final String CHART_CSS = "css/chart.css";
	/**
	 * ListView CSS file location.
	 */
	public static final String LIST_CSS = "css/list.css";
	/**
	 * Menu CSS file location.
	 */
	public static final String MENU_CSS = "css/menu.css";
	/**
	 * Separator CSS file location.
	 */
	public static final String SEPARATOR_CSS = "css/separator.css";
	/**
	 * Textarea CSS file location.
	 */
	public static final String TEXTAREA_CSS = "css/textarea.css";
	/**
	 * Textfield CSS file location.
	 */
	public static final String TEXTFIELD_CSS = "css/textfield.css";
	/**
	 * Track CSS file location.
	 */
	public static final String TRACK_CSS = "css/track.css";
	
	// Izhikevich lab.
	private IzhikevichLab iLab;
	// Main layout.
	private VBox layout;
	// Main menu bar.
	private AppMainMenuBar mainMenuBar;
	// Neural Network lab.
	private NetworkLab netLab;
	// The main scene.
	private Scene scene;
	// The primary stage.
	private Stage stage;
	// Status bar.
	private AppStatusBar statusBar;
	// Title bar.
	private AppTitleBar titleBar;
	// Tool bar.
	private AppToolBar toolBar;

	// App size listeners.
	private ArrayList<AppSizeListener> sizeListener;
	// Previous mouse x coordinate.
	private double prevMouseX;
	// Previous mouse y coordinate.
	private double prevMouseY;
	// Previous window height.
	private double prevWndHeight;
	// Previous window width.
	private double prevWndWidth;
	
	@Override
	public void actionTriggered(AppTitleBarEvent e) {
		Robot robot = new Robot();
		
		switch (e.getAction()) {
		case AppTitleBar.Action.CLOSE:
			shutdown();
			break;
		case AppTitleBar.Action.DRAGGING:
			if (!stage.isFullScreen()) {
				scene.getWindow().setX(scene.getWindow().getX() - (prevMouseX - robot.getMouseX()));
				scene.getWindow().setY(scene.getWindow().getY() - (prevMouseY - robot.getMouseY()));
				prevMouseX = robot.getMouseX();
				prevMouseY = robot.getMouseY();
			}
			break;
		case AppTitleBar.Action.MAXIMIZE:
			prevWndHeight = scene.getWindow().getHeight();
			prevWndWidth = scene.getWindow().getWidth();
			stage.setFullScreen(true);
			
			for (AppSizeListener sl : sizeListener)
				sl.sizeChanged(new AppSizeEvent(this, scene.getWidth(), scene.getHeight(), workspaceHeight()));
			break;
		case AppTitleBar.Action.MINIMIZE:
			stage.setIconified(true);
			break;
		case AppTitleBar.Action.PRESSED:
			prevMouseX = robot.getMouseX();
			prevMouseY = robot.getMouseY();
			break;
		case AppTitleBar.Action.RESTORE:
			stage.setFullScreen(false);
			scene.getWindow().setHeight(prevWndHeight);
			scene.getWindow().setWidth(prevWndWidth);
			
			for (AppSizeListener sl : sizeListener)
				sl.sizeChanged(new AppSizeEvent(this, scene.getWidth(), scene.getHeight(), workspaceHeight()));
			break;
		default:
			break;
		}
	}
	
	/**
	 * Displays an error alert message box with the specified header title and content message.
	 * @param header - The header title.
	 * @param msg - The content message.
	 */
	public static void alertError(String header, String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	/**
	 * Displays an information alert message box with the specified header title and content message.
	 * @param header - The header title.
	 * @param msg - The content message.
	 */
	public static void alertInfo(String header, String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	/**
	 * Program entry point method.
	 * @param args - Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void menuItemSelected(AppMainMenuBarEvent e) {
		switch (e.getMenuItem()) {
		case AppMainMenuBar.Item.FILE_EXIT:
			shutdown();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Shuts down Artilab and exits the program.
	 */
	public void shutdown() {
		iLab.shutdown();
		Platform.exit();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Create undecorated window
		stage = primaryStage;
		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		// Create title bar
		titleBar = new AppTitleBar();
		titleBar.addListener(this);
		
		// Create main menu bar
		mainMenuBar = new AppMainMenuBar();
		mainMenuBar.addListener(this);
		
		// Create tool bar
		toolBar = new AppToolBar();
		
		// Create Izhikevich lab
		iLab = new IzhikevichLab();
		
		// Create Neural Network lab
		netLab = new NetworkLab();
		toolBar.displayNetworkLabToolBar(netLab);
		
		// Create status bar
		statusBar = new AppStatusBar();
		
		// Create window layout
		layout = new VBox();
		layout.setBackground(Background.fill(DEF_BG_COLOR));
		layout.getChildren().addAll(titleBar, mainMenuBar, toolBar, netLab, statusBar);
		
		// Create app size listeners
		sizeListener = new ArrayList<AppSizeListener>();
		sizeListener.add(titleBar);
		sizeListener.add(toolBar);
		sizeListener.add(iLab);
		sizeListener.add(statusBar);
		
		// Create scene
		scene = new Scene(layout, DEF_WIDTH, DEF_HEIGHT);
		scene.getStylesheets().addAll(
				AXIS_CSS, 
				BUTTON_CSS,
				CHART_CSS, 
				LIST_CSS, 
				MENU_CSS, 
				SEPARATOR_CSS,
				TEXTAREA_CSS, 
				TEXTFIELD_CSS,
				TRACK_CSS);
		
		// Set scene and show window
		primaryStage.setScene(scene);
		primaryStage.show();
		
		for (AppSizeListener sl : sizeListener)
			sl.sizeChanged(new AppSizeEvent(this, scene.getWidth(), scene.getHeight(), workspaceHeight()));
	}
	
	/**
	 * Returns the current workspace height of the app.
	 * @return The workspace height.
	 */
	private double workspaceHeight() {
		return scene.getHeight() - AppTitleBar.MAX_HEIGHT - AppMainMenuBar.MAX_HEIGHT - AppToolBar.MAX_HEIGHT - AppStatusBar.MAX_HEIGHT;
	}
}
