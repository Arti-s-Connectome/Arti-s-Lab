package org.arti.artilab.gui;

import org.arti.artilab.gui.events.AppSizeEvent;
import org.arti.artilab.gui.events.AppSizeListener;
import org.arti.artilab.gui.events.AppToolBarEvent;
import org.arti.artilab.gui.events.AppToolBarListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>AppToolBar</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>AppToolBar class creates and controls the Artilab application's tool bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class AppToolBar extends ToolBar implements AppSizeListener {
	/**
	 * Maximum height of the tool bar.
	 */
	public static final int MAX_HEIGHT = 24;
	
	/**
	 * Default window background color.
	 */
	public static final Color DEF_BG_COLOR = Color.rgb(42,  79,  110);
	
	/**
	 * Dendrite icon location.
	 */
	private static final String DENDRITE_ICON = "icons/icons8-dendrite-24.png";
	/**
	 * Neuron icon location.
	 */
	private static final String NEURON_ICON = "icons/icons8-neuron-24.png";
	/**
	 * Zoom to 100% icon location.
	 */
	private static final String ZOOM_100_ICON = "icons/icons8-zoom-to-actual-size-24.png";
	/**
	 * Zoom to 100% hover icon location.
	 */
	private static final String ZOOM_100_HOVER_ICON = "icons/icons8-zoom-to-actual-size-hover-24.png";
	/**
	 * Zoom to 100% pressed icon location.
	 */
	private static final String ZOOM_100_PRESSED_ICON = "icons/icons8-zoom-to-actual-size-pressed-24.png";
	/**
	 * Zoom in icon location.
	 */
	private static final String ZOOM_IN_ICON = "icons/icons8-zoom-in-24.png";
	/**
	 * Zoom in hover icon location.
	 */
	private static final String ZOOM_IN_HOVER_ICON = "icons/icons8-zoom-in-hover-24.png";
	/**
	 * Zoom in pressed icon location.
	 */
	private static final String ZOOM_IN_PRESSED_ICON = "icons/icons8-zoom-in-pressed-24.png";
	/**
	 * Zoom out icon location.
	 */
	private static final String ZOOM_OUT_ICON = "icons/icons8-zoom-out-24.png";
	/**
	 * Zoom out hover icon location.
	 */
	private static final String ZOOM_OUT_HOVER_ICON = "icons/icons8-zoom-out-hover-24.png";
	/**
	 * Zoom out pressed icon location.
	 */
	private static final String ZOOM_OUT_PRESSED_ICON = "icons/icons8-zoom-out-pressed-24.png";
	
	/**
	 * <p>public enum <b>Item</b></p>
	 * 
	 * <p>Item enum contains the list of items that can be selected causing an AppToolBarEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum Item {
		/**
		 * Dendrite item.
		 */
		DENDRITE,
		/**
		 * Neuron item.
		 */
		NEURON,
		/**
		 * Zoom to 100% item.
		 */
		ZOOM_100,
		/**
		 * Zoom in item.
		 */
		ZOOM_IN,
		/**
		 * Zoom out item.
		 */
		ZOOM_OUT
	}
	
	/**
	 * <p>public enum <b>Detail</b></p>
	 * 
	 * <p>Detail enum contains the list of details that apply to specific items in an AppToolBarEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum Detail {
		/**
		 * Distal dendrite menu item.
		 */
		DENDRITE_DISTAL,
		/**
		 * Proximal dendrite menu item.
		 */
		DENDRITE_PROXIMAL,
		/**
		 * Accomodation neuron menu item.
		 */
		NEURON_ACCOMODATION,
		/**
		 * Bistability neuron menu item.
		 */
		NEURON_BISTABILITY,
		/**
		 * Class I excitable neuron menu item.
		 */
		NEURON_CLASS_I_EXCITABLE,
		/**
		 * Class II excitable neuron menu item.
		 */
		NEURON_CLASS_II_EXCITABLE,
		/**
		 * Depolarizing after potential neuron menu item.
		 */
		NEURON_DEPOLARIZING_AFTER_POTENTIAL,
		/**
		 * Inhibition-induced bursting neuron menu item.
		 */
		NEURON_INHIBITION_INDUCED_BURSTING,
		/**
		 * Inhibition-induced spiking neuron menu item.
		 */
		NEURON_INHIBITION_INDUCED_SPIKING,
		/**
		 * Integrator neuron menu item.
		 */
		NEURON_INTEGRATOR,
		/**
		 * Mixed mode neuron menu item.
		 */
		NEURON_MIXED_MODE,
		/**
		 * Phasic bursting neuron menu item.
		 */
		NEURON_PHASIC_BURSTING,
		/**
		 * Phasic spiking neuron menu item.
		 */
		NEURON_PHASIC_SPIKING,
		/**
		 * Rebound bursting neuron menu item.
		 */
		NEURON_REBOUND_BURSTING,
		/**
		 * Rebound spiking neuron menu item.
		 */
		NEURON_REBOUND_SPIKING,
		/**
		 * Resonator neuron menu item.
		 */
		NEURON_RESONATOR,
		/**
		 * Spike frequency adaptation neuron menu item.
		 */
		NEURON_SPIKE_FREQUENCY_ADAPTATION,
		/**
		 * Spike latency neuron menu item.
		 */
		NEURON_SPIKE_LATENCY,
		/**
		 * Subthreshold oscillations neuron menu item.
		 */
		NEURON_SUBTHRESHOLD_OSCILLATIONS,
		/**
		 * Threshold variability neuron menu item.
		 */
		NEURON_THRESHOLD_VARIABILITY,
		/**
		 * Tonic bursting neuron menu item.
		 */
		NEURON_TONIC_BURSTING,
		/**
		 * Tonic spiking neuron menu item.
		 */
		NEURON_TONIC_SPIKING,
		/**
		 * Other neuron menu item.
		 */
		NEURON_OTHER
	}
	
	// Center buttons box.
	private HBox centerBox;
	// AppToolBar listener.
	private AppToolBarListener listener;
	// Dendrite menu button.
	private MenuButton dendriteButton;
	// Neuron menu button.
	private MenuButton neuronButton;
	// Zoom 100% button.
	private Button zoom100Button;
	// Zoom in button.
	private Button zoomInButton;
	// Zoom out button.
	private Button zoomOutButton;
	
	/**
	 * Default constructor. Creates the Artilab application tool bar.
	 */
	public AppToolBar() {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = null;
		centerBox = new HBox();
		
		// Initialize tool bar
		setBorder(new Border(new BorderStroke(App.DEF_BORDER_COLOR, null, null, null, 
				BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
				CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(0.0, 0.0, 0.0, 0.0))));
		setBackground(Background.fill(DEF_BG_COLOR));
		setMaxHeight(MAX_HEIGHT);
		setPrefHeight(MAX_HEIGHT);
		
		setPadding(new Insets(0, 0, 0, 0));
	}
	
	/**
	 * Displays and empty tool bar.
	 */
	public void displayEmptyToolBar() {
		getItems().clear();
		centerBox.getChildren().clear();
		
		listener = null;
	}
	
	/**
	 * Displays the tool bar for the Neural Network Lab app.
	 * @param networkLab - The Neural Network Lab app.
	 */
	public void displayNetworkLabToolBar(NetworkLab networkLab) {
		getItems().clear();
		centerBox.getChildren().clear();
		
		// Load icon images
		Image dendriteImg = new Image(DENDRITE_ICON);
		ImageView dendriteView = new ImageView(dendriteImg);
		
		Image neuronImg = new Image(NEURON_ICON);
		ImageView neuronView = new ImageView(neuronImg);
		
		Image zoom100Img = new Image(ZOOM_100_ICON);
		ImageView zoom100View = new ImageView(zoom100Img);
		
		Image zoom100HoverImg = new Image(ZOOM_100_HOVER_ICON);
		ImageView zoom100HoverView = new ImageView(zoom100HoverImg);
		
		Image zoom100PressedImg = new Image(ZOOM_100_PRESSED_ICON);
		ImageView zoom100PressedView = new ImageView(zoom100PressedImg);
		
		Image zoomInImg = new Image(ZOOM_IN_ICON);
		ImageView zoomInView = new ImageView(zoomInImg);
		
		Image zoomInHoverImg = new Image(ZOOM_IN_HOVER_ICON);
		ImageView zoomInHoverView = new ImageView(zoomInHoverImg);
		
		Image zoomInPressedImg = new Image(ZOOM_IN_PRESSED_ICON);
		ImageView zoomInPressedView = new ImageView(zoomInPressedImg);
		
		Image zoomOutImg = new Image(ZOOM_OUT_ICON);
		ImageView zoomOutView = new ImageView(zoomOutImg);
		
		Image zoomOutHoverImg = new Image(ZOOM_OUT_HOVER_ICON);
		ImageView zoomOutHoverView = new ImageView(zoomOutHoverImg);
		
		Image zoomOutPressedImg = new Image(ZOOM_OUT_PRESSED_ICON);
		ImageView zoomOutPressedView = new ImageView(zoomOutPressedImg);
		
		// Create dendrite button
		dendriteButton = new MenuButton();
		dendriteButton.setGraphic(dendriteView);
		dendriteButton.setPadding(new Insets(0, 0, 0, 0));
		dendriteButton.setBackground(Background.EMPTY);
		dendriteButton.setTooltip(new Tooltip("Create New Dendrite"));
		
		// Create dendrite button menu
		MenuItem distalItem = new MenuItem();
		Label distalLabel = new Label("Distal");
		distalLabel.setStyle("-fx-text-fill: white;");
		distalItem.setGraphic(distalLabel);
		distalItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.DENDRITE, Detail.DENDRITE_DISTAL);
			}
		});
		
		MenuItem proximalItem = new MenuItem();
		Label proximalLabel = new Label("Proximal");
		proximalLabel.setStyle("-fx-text-fill: white;");
		proximalItem.setGraphic(proximalLabel);
		proximalItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.DENDRITE, Detail.DENDRITE_PROXIMAL);
			}
		});
		
		dendriteButton.getItems().addAll(
				distalItem,
				proximalItem);
		
		// Create neuron button
		neuronButton = new MenuButton();
		neuronButton.setGraphic(neuronView);
		neuronButton.setPadding(new Insets(0, 0, 0, 0));
		neuronButton.setBackground(Background.EMPTY);
		neuronButton.setTooltip(new Tooltip("Create New Neuron"));
		
		// Create neuron button menu
		MenuItem accItem = new MenuItem();
		Label accLabel = new Label("Accomodation");
		accLabel.setStyle("-fx-text-fill: white;");
		accItem.setGraphic(accLabel);
		accItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_ACCOMODATION);
			}
		});
		
		MenuItem biItem = new MenuItem();
		Label biLabel = new Label("Bistability");
		biLabel.setStyle("-fx-text-fill: white;");
		biItem.setGraphic(biLabel);
		biItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_BISTABILITY);
			}
		});
		
		MenuItem c1Item = new MenuItem();
		Label c1Label = new Label("Class I Excitable");
		c1Label.setStyle("-fx-text-fill: white;");
		c1Item.setGraphic(c1Label);
		c1Item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_CLASS_I_EXCITABLE);
			}
		});
		
		MenuItem c2Item = new MenuItem();
		Label c2Label = new Label("Class II Excitable");
		c2Label.setStyle("-fx-text-fill: white;");
		c2Item.setGraphic(c2Label);
		c2Item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_CLASS_II_EXCITABLE);
			}
		});
		
		MenuItem dapItem = new MenuItem();
		Label dapLabel = new Label("Depolarizing After Potential");
		dapLabel.setStyle("-fx-text-fill: white;");
		dapItem.setGraphic(dapLabel);
		dapItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_DEPOLARIZING_AFTER_POTENTIAL);
			}
		});
		
		MenuItem iibItem = new MenuItem();
		Label iibLabel = new Label("Inhibition-Induced Bursting");
		iibLabel.setStyle("-fx-text-fill: white;");
		iibItem.setGraphic(iibLabel);
		iibItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_INHIBITION_INDUCED_BURSTING);
			}
		});
		
		MenuItem iisItem = new MenuItem();
		Label iisLabel = new Label("Inhibition-Induced Spiking");
		iisLabel.setStyle("-fx-text-fill: white;");
		iisItem.setGraphic(iisLabel);
		iisItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_INHIBITION_INDUCED_SPIKING);
			}
		});
		
		MenuItem intItem = new MenuItem();
		Label intLabel = new Label("Integrator");
		intLabel.setStyle("-fx-text-fill: white;");
		intItem.setGraphic(intLabel);
		intItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_INTEGRATOR);
			}
		});
		
		MenuItem mmItem = new MenuItem();
		Label mmLabel = new Label("Mixed Mode");
		mmLabel.setStyle("-fx-text-fill: white;");
		mmItem.setGraphic(mmLabel);
		mmItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_MIXED_MODE);
			}
		});
		
		MenuItem pbItem = new MenuItem();
		Label pbLabel = new Label("Phasic Bursting");
		pbLabel.setStyle("-fx-text-fill: white;");
		pbItem.setGraphic(pbLabel);
		pbItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_PHASIC_BURSTING);
			}
		});
		
		MenuItem psItem = new MenuItem();
		Label psLabel = new Label("Phasic Spiking");
		psLabel.setStyle("-fx-text-fill: white;");
		psItem.setGraphic(psLabel);
		psItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_PHASIC_SPIKING);
			}
		});
		
		MenuItem rbItem = new MenuItem();
		Label rbLabel = new Label("Rebound Bursting");
		rbLabel.setStyle("-fx-text-fill: white;");
		rbItem.setGraphic(rbLabel);
		rbItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_REBOUND_BURSTING);
			}
		});
		
		MenuItem rsItem = new MenuItem();
		Label rsLabel = new Label("Rebound Spiking");
		rsLabel.setStyle("-fx-text-fill: white;");
		rsItem.setGraphic(rsLabel);
		rsItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_REBOUND_SPIKING);
			}
		});
		
		MenuItem resItem = new MenuItem();
		Label resLabel = new Label("Resonator");
		resLabel.setStyle("-fx-text-fill: white;");
		resItem.setGraphic(resLabel);
		resItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_RESONATOR);
			}
		});
		
		MenuItem sfaItem = new MenuItem();
		Label sfaLabel = new Label("Spike Frequency Adaptation");
		sfaLabel.setStyle("-fx-text-fill: white;");
		sfaItem.setGraphic(sfaLabel);
		sfaItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_SPIKE_FREQUENCY_ADAPTATION);
			}
		});
		
		MenuItem slItem = new MenuItem();
		Label slLabel = new Label("Spike Latency");
		slLabel.setStyle("-fx-text-fill: white;");
		slItem.setGraphic(slLabel);
		slItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_SPIKE_LATENCY);
			}
		});
		
		MenuItem soItem = new MenuItem();
		Label soLabel = new Label("Subthreshold Oscillations");
		soLabel.setStyle("-fx-text-fill: white;");
		soItem.setGraphic(soLabel);
		soItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_SUBTHRESHOLD_OSCILLATIONS);
			}
		});
		
		MenuItem tvItem = new MenuItem();
		Label tvLabel = new Label("Threshold Variability");
		tvLabel.setStyle("-fx-text-fill: white;");
		tvItem.setGraphic(tvLabel);
		tvItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_THRESHOLD_VARIABILITY);
			}
		});
		
		MenuItem tbItem = new MenuItem();
		Label tbLabel = new Label("Tonic Bursting");
		tbLabel.setStyle("-fx-text-fill: white;");
		tbItem.setGraphic(tbLabel);
		tbItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_TONIC_BURSTING);
			}
		});
		
		MenuItem tsItem = new MenuItem();
		Label tsLabel = new Label("Tonic Spiking");
		tsLabel.setStyle("-fx-text-fill: white;");
		tsItem.setGraphic(tsLabel);
		tsItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_TONIC_SPIKING);
			}
		});
		
		MenuItem otherItem = new MenuItem();
		Label otherLabel = new Label("Other");
		otherLabel.setStyle("-fx-text-fill: white;");
		otherItem.setGraphic(otherLabel);
		otherItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.NEURON, Detail.NEURON_OTHER);
			}
		});
		
		neuronButton.getItems().addAll(
				accItem,
				biItem,
				c1Item,
				c2Item,
				dapItem,
				iibItem,
				iisItem,
				intItem,
				mmItem,
				pbItem,
				psItem,
				rbItem,
				rsItem,
				resItem,
				sfaItem,
				slItem,
				soItem,
				tvItem,
				tbItem,
				tsItem,
				otherItem);
		
		// Create zoom 100% button
		zoom100Button = new Button();
		zoom100Button.setGraphic(zoom100View);
		zoom100Button.setPadding(new Insets(0, 0, 0, 0));
		zoom100Button.setBackground(Background.EMPTY);
		zoom100Button.setTooltip(new Tooltip("Zoom To 100%"));
		zoom100Button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					zoom100Button.setGraphic(zoom100PressedView);
				else
					zoom100Button.setGraphic(zoom100HoverView);
			}
		});
		zoom100Button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					zoom100Button.setGraphic(zoom100PressedView);
				else
					zoom100Button.setGraphic(zoom100View);
			}
		});
		zoom100Button.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				zoom100Button.setGraphic(zoom100PressedView);
			}
		});
		zoom100Button.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = zoom100Button.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					zoom100Button.setGraphic(zoom100HoverView);
				else
					zoom100Button.setGraphic(zoom100View);
			}
		});
		zoom100Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.ZOOM_100, null);
			}
		});
		
		// Create zoomIn button
		zoomInButton = new Button();
		zoomInButton.setGraphic(zoomInView);
		zoomInButton.setPadding(new Insets(0, 0, 0, 0));
		zoomInButton.setBackground(Background.EMPTY);
		zoomInButton.setTooltip(new Tooltip("Zoom In"));
		zoomInButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					zoomInButton.setGraphic(zoomInPressedView);
				else
					zoomInButton.setGraphic(zoomInHoverView);
			}
		});
		zoomInButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					zoomInButton.setGraphic(zoomInPressedView);
				else
					zoomInButton.setGraphic(zoomInView);
			}
		});
		zoomInButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				zoomInButton.setGraphic(zoomInPressedView);
			}
		});
		zoomInButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = zoomInButton.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					zoomInButton.setGraphic(zoomInHoverView);
				else
					zoomInButton.setGraphic(zoomInView);
			}
		});
		zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.ZOOM_IN, null);
			}
		});
		
		// Create zoomOut button
		zoomOutButton = new Button();
		zoomOutButton.setGraphic(zoomOutView);
		zoomOutButton.setPadding(new Insets(0, 0, 0, 0));
		zoomOutButton.setBackground(Background.EMPTY);
		zoomOutButton.setTooltip(new Tooltip("Zoom Out"));
		zoomOutButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					zoomOutButton.setGraphic(zoomOutPressedView);
				else
					zoomOutButton.setGraphic(zoomOutHoverView);
			}
		});
		zoomOutButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.isPrimaryButtonDown())
					zoomOutButton.setGraphic(zoomOutPressedView);
				else
					zoomOutButton.setGraphic(zoomOutView);
			}
		});
		zoomOutButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				zoomOutButton.setGraphic(zoomOutPressedView);
			}
		});
		zoomOutButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Bounds bounds = zoomOutButton.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY()))
					zoomOutButton.setGraphic(zoomOutHoverView);
				else
					zoomOutButton.setGraphic(zoomOutView);
			}
		});
		zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.ZOOM_OUT, null);
			}
		});
		
		centerBox = new HBox(neuronButton, dendriteButton, new Separator(Separator.VERTICAL, 22.0), zoom100Button, zoomInButton, 
				zoomOutButton);
		HBox.setHgrow(centerBox, Priority.ALWAYS);
		centerBox.setAlignment(Pos.CENTER);
		
		getItems().addAll(centerBox);
		
		listener = networkLab.getDisplay();
	}
	
	/**
	 * Sends an AppToolBarEvent to all AppToolBarListeners with the specified item and its corresponding detail, if any.
	 * @param item - The selected item.
	 * @param detail - The selected item's detail.
	 */
	private void sendEvent(Item item, Detail detail) {
		AppToolBarEvent event = new AppToolBarEvent(this, item, detail);
		
		if (listener != null)
			listener.itemSelected(event);
	}

	@Override
	public void sizeChanged(AppSizeEvent e) {
		setWidth(e.getWidth());
	}
}
