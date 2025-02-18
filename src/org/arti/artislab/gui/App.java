package org.arti.artislab.gui;

import java.util.ArrayList;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;
import org.arti.artislab.gui.events.AppTitleBarEvent;
import org.arti.artislab.gui.events.AppTitleBarListener;
import org.arti.artislab.gui.events.TopToolBarEvent;
import org.arti.artislab.gui.events.TopToolBarListener;
import org.arti.artislab.gui.events.SideToolBarEvent;
import org.arti.artislab.gui.events.SideToolBarListener;
import org.arti.neural.NeuralSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <p>public class <b>App</b><br>
 * extends {@link Application}<br>
 * implements {@link TopToolBarListener}, {@link SideToolBarListener}, {@link AppTitleBarListener}</p>
 * 
 * <p>App class creates and controls the Arti's Lab JavaFX application.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class App extends Application implements TopToolBarListener, SideToolBarListener, AppTitleBarListener {
	/**
	 * Default window height.
	 */
	public static final int DEF_HEIGHT = 1080;
	/**
	 * Default window width.
	 */
	public static final int DEF_WIDTH = 1920;
	/**
	 * Window resize space.
	 */
	public static final double RESIZE_SPACE = 10.0;
	
	/**
	 * Default workspace height.
	 */
	public static final double DEF_WORKSPACE_HEIGHT = DEF_HEIGHT - AppTitleBar.HEIGHT - TopToolBar.HEIGHT - AppStatusBar.HEIGHT;
	/**
	 * Default workspace width.
	 */
	public static final double DEF_WORKSPACE_WIDTH = DEF_WIDTH - SideToolBar.WIDTH;
	
	/**
	 * Default app background color.
	 */
	public static final Color DEF_BG_COLOR = Color.rgb(0,  0,  0);
	/**
	 * Default border color.
	 */
	public static final Color DEF_BORDER_COLOR = Color.rgb(22, 200, 249);
	/**
	 * Error border color.
	 */
	public static final Color ERROR_BORDER_COLOR = Color.rgb(255, 0, 0);
	/**
	 * Help border color.
	 */
	public static final Color HELP_BORDER_COLOR = Color.rgb(255, 144, 224);
	/**
	 * Information border color.
	 */
	public static final Color INFO_BORDER_COLOR = DEF_BORDER_COLOR;
	/**
	 * Warning border color.
	 */
	public static final Color WARN_BORDER_COLOR = Color.rgb(255, 224, 0);
	
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
	 * Combobox CSS file location.
	 */
	public static final String COMBOBOX_CSS = "css/combobox.css";
	/**
	 * Menu CSS file location.
	 */
	public static final String MENU_CSS = "css/menu.css";
	/**
	 * Progress bar CSS file location.
	 */
	public static final String PROGRESS_BAR_CSS = "css/progressbar.css";
	/**
	 * Scrollbar CSS file location.
	 */
	public static final String SCROLLBAR_CSS = "css/scrollbar.css";
	/**
	 * Separator CSS file location.
	 */
	public static final String SEPARATOR_CSS = "css/separator.css";
	/**
	 * Text CSS file location.
	 */
	public static final String TEXT_CSS = "css/text.css";
	/**
	 * Textarea CSS file location.
	 */
	public static final String TEXTAREA_CSS = "css/textarea.css";
	
	// Pulsating delta.
	private double pulsatingDelta;
	// Pulsating thread.
	private Thread pulsatingThread;
	// Shutting down flag.
	private boolean shuttingDown;
	
	// Center layout.
	private HBox center;
	// Border color transition.
	private double colorTrans;
	// Home workspace.
	private AppHome homeWorkspace;
	// Izhikevich lab.
	private NeuralModelLab modelLab;
	// Main layout.
	private VBox layout;
	// Main layout shape.
	private Rectangle layoutShape;
	// Neural Network lab.
	private NetworkLab netLab;
	// The NeuralSystem controlling the processing of neural nodes and networks.
	private NeuralSystem neuralSystem;
	// The main scene.
	private Scene scene;
	// Side tool bar.
	private TopToolBar topToolBar;
	// The primary stage.
	private Stage stage;
	// Status bar.
	private AppStatusBar statusBar;
	// Title bar.
	private AppTitleBar titleBar;
	// Top tool bar.
	private SideToolBar sideToolBar;
	// Workspace.
	private Node workspace;

	// Mouse pressed scene x coordinate.
	private double mpSceneX;
	// Mouse pressed scene y coordinate.
	private double mpSceneY;
	// Mouse pressed screen x coordinate.
	private double mpScreenX;
	// Mouse pressed screen y coordinate.
	private double mpScreenY;
	// Mouse pressed stage width.
	private double mpStageW;
	// Mouse pressed stage height.
	private double mpStageH;
	// Previous mouse x coordinate.
	private double prevMouseX;
	// Previous mouse y coordinate.
	private double prevMouseY;
	// Previous window height.
	private double prevWndHeight;
	// Previous window width.
	private double prevWndWidth;
	// App size listeners.
	private ArrayList<AppSizeListener> sizeListener;
	
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
			
			layout.setPrefWidth(scene.getWidth());
			layout.setPrefHeight(scene.getHeight());
			layout.resize(scene.getWidth(), scene.getHeight());
			layout.setShape(new Rectangle(0, 0, scene.getWidth(), scene.getHeight()));
			
			sendSizeChangedEvent();
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
			
			layout.setShape(layoutShape);
			
			sendSizeChangedEvent();
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
	
	// Calculates and returns the next border color in the linear gradient animation.
	private Color nextBorderColor(double delta) {
		Color start = Color.rgb(1, 229, 255);
		Color end = Color.rgb(255, 1, 217);
		Color ret = start.interpolate(end, colorTrans);
		
		colorTrans = Math.min(1.0, Math.max(0.0, colorTrans + delta));
		
		return ret;
	}
	
	/**
	 * Returns the current instance of NeuralSystem.
	 * @return the NeuralSystem.
	 */
	public NeuralSystem getNeuralSystem() {
		return neuralSystem;
	}
	
	/**
	 * Program entry point method.
	 * @param args - Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void itemSelected(TopToolBarEvent e) {
		
	}
	
	@Override
	public void itemSelected(SideToolBarEvent e) {
		switch (e.getItem()) {
		case GAMES_LAB:
			break;
		case HOME_SCREEN:
			workspace = homeWorkspace;
			center.getChildren().set(1, workspace);
			VBox.setVgrow(workspace, Priority.NEVER);
			topToolBar.displayEmptyToolBar();
			modelLab.removeListener(topToolBar);
			sideToolBar.displayHomeToolBar();
			break;
		case NEURAL_MODEL_LAB:
			workspace = modelLab;
			center.getChildren().set(1, workspace);
			VBox.setVgrow(workspace, Priority.NEVER);
			topToolBar.displayNeuralModelToolBar(modelLab);
			modelLab.addListener(topToolBar);
			sideToolBar.displayNeuralModelToolBar();
			break;
		case NEURAL_NET_LAB:
			workspace = netLab;
			center.getChildren().set(1, workspace);
			VBox.setVgrow(workspace, Priority.ALWAYS);
			topToolBar.displayNetworkLabToolBar(netLab);
			modelLab.removeListener(topToolBar);
			sideToolBar.displayNeuralNetToolBar();
			break;
		case SYNAPSE_LAB:
			break;
		default:
			break;
		}
	}
	
	/**
	 * Shuts down Arti's Lab and exits the program.
	 */
	public void shutdown() {
		// Stop pulsating thread
		shuttingDown = true;
		
		try {
			pulsatingThread.join();
		} catch (InterruptedException e) {
			// TODO Catch error.
		}
		
		// Shut down program and exit
		homeWorkspace.shutDown();
		modelLab.shutdown();
		Platform.exit();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Create undecorated window
		stage = primaryStage;
		stage.initStyle(StageStyle.TRANSPARENT);
		
		// Initialize variables
		colorTrans = 0.0;
		pulsatingDelta = 0.02;
		pulsatingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int cycle = 0;
				
				while (!shuttingDown) {
					layout.setBorder(new Border(new BorderStroke(nextBorderColor(pulsatingDelta), BorderStrokeStyle.SOLID, new CornerRadii(20.0), 
							new BorderWidths(2.0))));
					homeWorkspace.nextArtiFade(pulsatingDelta);
					
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException e) {
						// TODO: Catch error.
					}
					
					cycle++;
					
					if (cycle == 50) {
						pulsatingDelta = (pulsatingDelta > 0.0) ? -0.02 : 0.02;
						cycle = 0;
					}
				}
			}
		});
		
		// Create title bar
		titleBar = new AppTitleBar();
		titleBar.addListener(this);
		
		// Create top tool bar
		topToolBar = new TopToolBar();
		
		// Create home workspace
		homeWorkspace = new AppHome();
		
		// Create the neural system
		neuralSystem = NeuralSystem.getInstance();
		
		// Create Izhikevich lab
		modelLab = new NeuralModelLab();
		
		// Create Neural Network lab
		netLab = new NetworkLab();
		
		// Create status bar
		statusBar = new AppStatusBar();
		
		// Create side tool bar
		sideToolBar = new SideToolBar();
		sideToolBar.setListener(this);
		sideToolBar.displayHomeToolBar();
		
		// Set workspace
		workspace = homeWorkspace;
		
		// Create center layout
		center = new HBox(sideToolBar, workspace);
		center.setMaxHeight(DEF_WORKSPACE_HEIGHT);
		
		// Create window layout
		layout = new VBox();
		layout.setBackground(Background.fill(DEF_BG_COLOR));
		layoutShape = new Rectangle();
		layoutShape.setX(0.0);
		layoutShape.setY(0.0);
		layoutShape.setWidth(DEF_WIDTH);
		layoutShape.setHeight(DEF_HEIGHT);
		layoutShape.setArcWidth(20.0);
		layoutShape.setArcHeight(20.0);
		layout.setShape(layoutShape);
		layout.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
		layout.setBorder(new Border(new BorderStroke(nextBorderColor(0.0), BorderStrokeStyle.SOLID, new CornerRadii(20.0), new BorderWidths(2.0))));
		layout.getChildren().addAll(titleBar, topToolBar, center, statusBar);
		
		// Create app size listeners
		sizeListener = new ArrayList<AppSizeListener>();
		sizeListener.add(titleBar);
		sizeListener.add(sideToolBar);
		sizeListener.add(topToolBar);
		sizeListener.add(homeWorkspace);
		sizeListener.add(modelLab);
		sizeListener.add(netLab);
		sizeListener.add(statusBar);
		
		// Create scene
		scene = new Scene(layout, DEF_WIDTH, DEF_HEIGHT);
		scene.getStylesheets().addAll(
				AXIS_CSS,
				BUTTON_CSS,
				CHART_CSS, 
				COMBOBOX_CSS,
				MENU_CSS, 
				PROGRESS_BAR_CSS,
				SCROLLBAR_CSS,
				SEPARATOR_CSS,
				TEXT_CSS,
				TEXTAREA_CSS);
		scene.setFill(Color.TRANSPARENT);
		
		// Handle mouse pressed events on the scene
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mpSceneX = arg0.getSceneX();
				mpSceneY = arg0.getSceneY();
				mpScreenX = arg0.getScreenX();
				mpScreenY = arg0.getScreenY();
				mpStageW = stage.getWidth();
				mpStageH = stage.getHeight();
			}
		});
		
		// Handle mouse moved events on the scene to allow for resizing the window
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				double sx = arg0.getSceneX();
				double sy = arg0.getSceneY();
				
				boolean left = sx > 0.0 && sx < RESIZE_SPACE;
				boolean right = sx < scene.getWidth() && sx > scene.getWidth() - RESIZE_SPACE;
				boolean up = sy < scene.getHeight() && sy > scene.getHeight() - RESIZE_SPACE;
				boolean down = sy > 0.0 && sy < RESIZE_SPACE;
				
				// Resize from northwest
				if (left && down) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newWidth = mpStageW - (arg0.getScreenX() - mpScreenX);
						    double newHeight = mpStageH - (arg0.getScreenY() - mpScreenY);
						    
						    if (newHeight > stage.getMinHeight()) {
						    	stage.setY(arg0.getScreenY() - mpSceneY);
						        stage.setHeight(newHeight);
						    }
						    if (newWidth > stage.getMinWidth()) {
						        stage.setX(arg0.getScreenX() - mpSceneX);
						        stage.setWidth(newWidth);
						    }
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.NW_RESIZE);
				}
				// Resize from northeast
				else if (left && up) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newWidth = mpStageW - (arg0.getScreenX() - mpScreenX);
						    double newHeight = mpStageH - (arg0.getScreenY() - mpScreenY);
						    
						    if (newHeight > stage.getMinHeight()) 
						    	stage.setHeight(newHeight);
						    if (newWidth > stage.getMinWidth()) {
						        stage.setX(arg0.getScreenX() - mpSceneX);
						        stage.setWidth(newWidth);
						    }
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.NE_RESIZE);
				}
				// Resize from southwest
				else if (right && down) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newWidth = mpStageW - (arg0.getScreenX() - mpScreenX);
						    double newHeight = mpStageH - (arg0.getScreenY() - mpScreenY);
						    
						    if (newHeight > stage.getMinHeight()) {
						        stage.setHeight(newHeight);
						        stage.setY(arg0.getScreenY() - mpSceneY);
						    }
						    if (newWidth > stage.getMinWidth()) 
						    	stage.setWidth(newWidth);
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.SW_RESIZE);
				}
				// Resize from southeast
				else if (right && up) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newWidth = mpStageW - (arg0.getScreenX() - mpScreenX);
						    double newHeight = mpStageH - (arg0.getScreenY() - mpScreenY);
						    
						    if (newHeight > stage.getMinHeight()) 
						    	stage.setHeight(newHeight);
						    if (newWidth > stage.getMinWidth()) 
						    	stage.setWidth(newWidth);
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.SE_RESIZE);
				}
				// Resize from east
				else if (left) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newWidth = mpStageW - (arg0.getScreenX() - mpScreenX);
						    
						    if (newWidth > stage.getMinWidth()) {
						        stage.setX(arg0.getScreenX() - mpSceneX);
						        stage.setWidth(newWidth);
						    }
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.E_RESIZE);
				}
				// Resize from west
				else if (right) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newWidth = mpStageW + (arg0.getScreenX() - mpScreenX);
						    
						    if (newWidth > stage.getMinWidth()) 
						    	stage.setWidth(newWidth);
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.W_RESIZE);
				}
				// Resize from south
				else if (up) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newHeight = mpStageH + (arg0.getScreenY() - mpScreenY);
						    
						    if (newHeight > stage.getMinHeight()) 
						    	stage.setHeight(newHeight);
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.S_RESIZE);
				}
				// Resize from north
				else if (down) {
					scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
						    double newHeight = mpStageH - (arg0.getScreenY() - mpScreenY);
						    
						    if (newHeight > stage.getMinHeight()) {
						        stage.setY(arg0.getScreenY() - mpSceneY);
						        stage.setHeight(newHeight);
						    }
						    
						    sendSizeChangedEvent();
						}
					});
					
					scene.setCursor(Cursor.N_RESIZE);
				}
				// No resize
				else {
					scene.setCursor(Cursor.DEFAULT);
					scene.setOnMouseDragged(null);
				}
			}
		});
		
		// Set scene and show window
		primaryStage.setScene(scene);
		primaryStage.show();
		
		sendSizeChangedEvent();
		
		// Start pulsating thread
		pulsatingThread.start();
	}
	
	// Sends a size change AppSizeEvent to all AppSizeEventListeners
	private void sendSizeChangedEvent() {
		center.setMaxHeight(workspaceHeight());
		
		for (AppSizeListener sl : sizeListener)
			sl.sizeChanged(new AppSizeEvent(this, scene.getWidth(), scene.getHeight(), workspaceHeight(), workspaceWidth()));
	}
	
	/**
	 * Returns the current workspace height of the app.
	 * @return The workspace height.
	 */
	private double workspaceHeight() {
		return scene.getHeight() - AppTitleBar.HEIGHT - TopToolBar.HEIGHT - AppStatusBar.HEIGHT;
	}
	
	/**
	 * Returns the current workspace width of the app.
	 * @return The workspace width.
	 */
	private double workspaceWidth() {
		return scene.getWidth() - SideToolBar.WIDTH;
	}
}
