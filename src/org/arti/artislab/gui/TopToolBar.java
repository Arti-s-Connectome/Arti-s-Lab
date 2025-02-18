package org.arti.artislab.gui;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;
import org.arti.artislab.gui.events.NeuralModelLabEvent;
import org.arti.artislab.gui.events.NeuralModelLabListener;
import org.arti.artislab.gui.events.TopToolBarEvent;
import org.arti.artislab.gui.events.TopToolBarListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * <p>public class <b>TopToolBar</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}</p>
 * 
 * <p>TopToolBar class creates and controls the Arti's Lab application's top tool bar.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class TopToolBar extends ToolBar implements NeuralModelLabListener, AppSizeListener {
	/**
	 * Width of the tool bar.
	 */
	public static final double HEIGHT = 32.0;
	
	/**
	 * Default window background color.
	 */
	public static final Color DEF_BG_COLOR = App.DEF_BG_COLOR;
	
	// Copy icon name.
	private static final String COPY_ICON = "icons8-copy";
	// Dendrite icon name.
	private static final String DENDRITE_ICON = "icons8-dendrite";
	// Export model log icon name.
	private static final String EXPORT_ICON = "icons8-export";
	// Import model log icon name.
	private static final String IMPORT_ICON = "icons8-import";
	// New dendrite model icon name.
	private static final String NEW_DENDRITE_ICON = "icons8-new-dendrite";
	// New soma model icon name.
	private static final String NEW_SOMA_ICON = "icons8-new-neuron";
	// New spiking model icon name.
	private static final String NEW_SPIKE_ICON = "icons8-new-spike";
	// Pause icon name.
	private static final String PAUSE_ICON = "icons8-pause";
	// Play icon name.
	private static final String PLAY_ICON = "icons8-play";
	// Remove model icon name.
	private static final String REMOVE_MODEL_ICON = "icons8-remove-file";
	// Reset icon name.
	private static final String RESET_ICON = "icons8-reset";
	// Save history icon name.
	private static final String SAVE_HISTORY_ICON = "icons8-history";
	// Soma icon name.
	private static final String SOMA_ICON = "icons8-neuron";
	// Spike icon name.
	private static final String SPIKE_ICON = "icons8-spike";
	// Step icon name.
	private static final String STEP_ICON = "icons8-step";
	// Stop icon name.
	private static final String STOP_ICON = "icons8-stop";
	// Update model icon name.
	private static final String UPDATE_MODEL_ICON = "icons8-update-file";
	// Zoom to 100% icon name.
	private static final String ZOOM_100_ICON = "icons8-zoom-to-actual-size";
	// Zoom in icon name.
	private static final String ZOOM_IN_ICON = "icons8-zoom-in";
	// Zoom out icon name.
	private static final String ZOOM_OUT_ICON = "icons8-zoom-out";
	
	/**
	 * <p>public enum <b>Item</b></p>
	 * 
	 * <p>Item enum contains the list of items that can be selected causing an TopToolBarEvent.</p>
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
		 * Soma item.
		 */
		SOMA,
		/**
		 * Spiking item.
		 */
		SPIKING,
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
	 * <p>Detail enum contains the list of details that apply to specific items in an TopToolBarEvent.</p>
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
		 * Accomodation spiking menu item.
		 */
		SPIKING_ACCOMODATION,
		/**
		 * Bistability spiking menu item.
		 */
		SPIKING_BISTABILITY,
		/**
		 * Class I excitable spiking menu item.
		 */
		SPIKING_CLASS_I_EXCITABLE,
		/**
		 * Class II excitable spiking menu item.
		 */
		SPIKING_CLASS_II_EXCITABLE,
		/**
		 * Depolarizing after potential spiking menu item.
		 */
		SPIKING_DEPOLARIZING_AFTER_POTENTIAL,
		/**
		 * Inhibition-induced bursting spiking menu item.
		 */
		SPIKING_INHIBITION_INDUCED_BURSTING,
		/**
		 * Inhibition-induced spiking spiking menu item.
		 */
		SPIKING_INHIBITION_INDUCED_SPIKING,
		/**
		 * Integrator spiking menu item.
		 */
		SPIKING_INTEGRATOR,
		/**
		 * Mixed mode spiking menu item.
		 */
		SPIKING_MIXED_MODE,
		/**
		 * Phasic bursting spiking menu item.
		 */
		SPIKING_PHASIC_BURSTING,
		/**
		 * Phasic spiking spiking menu item.
		 */
		SPIKING_PHASIC_SPIKING,
		/**
		 * Rebound bursting spiking menu item.
		 */
		SPIKING_REBOUND_BURSTING,
		/**
		 * Rebound spiking spiking menu item.
		 */
		SPIKING_REBOUND_SPIKING,
		/**
		 * Resonator spiking menu item.
		 */
		SPIKING_RESONATOR,
		/**
		 * Spike frequency adaptation spiking menu item.
		 */
		SPIKING_SPIKE_FREQUENCY_ADAPTATION,
		/**
		 * Spike latency spiking menu item.
		 */
		SPIKING_SPIKE_LATENCY,
		/**
		 * Subthreshold oscillations spiking menu item.
		 */
		SPIKING_SUBTHRESHOLD_OSCILLATIONS,
		/**
		 * Threshold variability spiking menu item.
		 */
		SPIKING_THRESHOLD_VARIABILITY,
		/**
		 * Tonic bursting spiking menu item.
		 */
		SPIKING_TONIC_BURSTING,
		/**
		 * Tonic spiking spiking menu item.
		 */
		SPIKING_TONIC_SPIKING,
		/**
		 * Other spiking menu item.
		 */
		SPIKING_OTHER
	}
	
	// Tool box layout.
	private HBox toolBox;
	// TopToolBar listener.
	private TopToolBarListener listener;
	// Copy model button.
	private ImageButton copyModelButton;
	// Dendrite menu button.
	private ImageMenuButton dendriteButton;
	// New dendrite model button.
	private ImageButton newDendriteModelButton;
	// New soma model button.
	private ImageButton newSomaModelButton;
	// New spiking model button.
	private ImageButton newSpikeModelButton;
	// Open model log button.
	private ImageButton importModelLogButton;
	// Pause model button.
	private ImageButton pauseModelButton;
	// Remove model button.
	private ImageButton removeModelButton;
	// Reset model menu button.
	private ImageButton resetModelButton;
	// Run model button.
	private ImageButton runModelButton;
	// Save model history button.
	private ImageButton saveModelHistButton;
	// Soma menu button.
	private ImageMenuButton somaButton;
	// Spiking menu button.
	private ImageMenuButton spikingButton;
	// Save model log button.
	private ImageButton exportModelLogButton;
	// Step model butotn.
	private ImageButton stepModelButton;
	// Stop model button.
	private ImageButton stopModelButton;
	// Update model button.
	private ImageButton updateModelButton;
	// Zoom 100% button.
	private ImageButton zoom100Button;
	// Zoom in button.
	private ImageButton zoomInButton;
	// Zoom out button.
	private ImageButton zoomOutButton;
	
	/**
	 * Default constructor. Creates the Arti's Lab application tool bar.
	 */
	public TopToolBar() {
		// Call parent constructor
		super();
		
		// Initialize variables
		listener = null;
		toolBox = new HBox();
		
		// Initialize tool bar
		setBackground(Background.fill(DEF_BG_COLOR));
		setMaxHeight(HEIGHT);
		setMinHeight(HEIGHT);
		setPadding(new Insets(0, 0, 0, 0));
	}
	
	@Override
	public void actionTriggered(NeuralModelLabEvent e) {
		NeuralModelLab.Action action;
		
		for (int i = 0; i < e.actions(); ++i) {
			action = e.getAction(i);
			
			switch (action) {
			case CUSTOM_MODEL:
				removeModelButton.setDisable(false);
				updateModelButton.setDisable(false);
				break;
			case STANDARD_MODEL:
				removeModelButton.setDisable(true);
				updateModelButton.setDisable(true);
				break;
			case TIMESPAN_REACHED:
				saveModelHistButton.setDisable(false);
				exportModelLogButton.setDisable(false);
				stepModelButton.setDisable(true);
				runModelButton.setDisable(true);
				pauseModelButton.setDisable(true);
				stopModelButton.setDisable(true);
				resetModelButton.setDisable(false);
			default:
				break;
			}
		}
	}
	
	/**
	 * Displays and empty tool bar.
	 */
	public void displayEmptyToolBar() {
		getItems().clear();
		toolBox.getChildren().clear();
		
		listener = null;
	}
	
	/**
	 * Displays the tool bar for the Neural Model Lab app.
	 * @param neuralModelLab - The Neural Model Lab app.
	 */
	public void displayNeuralModelToolBar(NeuralModelLab neuralModelLab) {
		// Clear old tool bar
		getItems().clear();
		toolBox.getChildren().clear();
		
		// Create new spiking model button
		newSpikeModelButton = new ImageButton(NEW_SPIKE_ICON);
		newSpikeModelButton.setTooltip(new Tooltip("Create New Spiking Model"));
		newSpikeModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Reset everything
				neuralModelLab.reset();
				
				// Create new spiking model
				neuralModelLab.newSpikingModel();
			}
		});
		
		// Create new soma model button
		newSomaModelButton = new ImageButton(NEW_SOMA_ICON);
		newSomaModelButton.setTooltip(new Tooltip("Create New Soma Model"));
		newSomaModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Reset everything
				neuralModelLab.reset();
				
				// Create new soma model
				neuralModelLab.newSomaModel();
			}
		});
		
		// Create new spiking model button
		newDendriteModelButton = new ImageButton(NEW_DENDRITE_ICON);
		newDendriteModelButton.setTooltip(new Tooltip("Create New Dendrite Model"));
		newDendriteModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Reset everything
				neuralModelLab.reset();
				
				// Create new model
				neuralModelLab.newDendriteModel();
			}
		});
		
		// Create copy model button
		copyModelButton = new ImageButton(COPY_ICON);
		copyModelButton.setTooltip(new Tooltip("Copy Model Parameters"));
		copyModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
		copyModelButton.setDisable(true);
		
		// Create remove model button
		removeModelButton = new ImageButton(REMOVE_MODEL_ICON);
		removeModelButton.setTooltip(new Tooltip("Remove Model"));
		removeModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Reset everything
				neuralModelLab.reset();
			}
		});
		removeModelButton.setDisable(true);
		
		// Create update model button
		updateModelButton = new ImageButton(UPDATE_MODEL_ICON);
		updateModelButton.setTooltip(new Tooltip("Update Model"));
		updateModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
		updateModelButton.setDisable(true);
		
		// Create open model log button
		importModelLogButton = new ImageButton(IMPORT_ICON);
		importModelLogButton.setTooltip(new Tooltip("Import Model Output Log"));
		importModelLogButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
		
		// Create save model log button
		exportModelLogButton = new ImageButton(EXPORT_ICON);
		exportModelLogButton.setTooltip(new Tooltip("Export Model Output Log"));
		exportModelLogButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				neuralModelLab.saveOutputLog();
			}
		});
		exportModelLogButton.setDisable(true);
		
		// Create save model history button
		saveModelHistButton = new ImageButton(SAVE_HISTORY_ICON);
		saveModelHistButton.setTooltip(new Tooltip("Save Model History"));
		saveModelHistButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				neuralModelLab.saveHistory();
			}
		});
		saveModelHistButton.setDisable(true);
		
		// Create step model button
		stepModelButton = new ImageButton(STEP_ICON);
		stepModelButton.setTooltip(new Tooltip("Process Next Step"));
		stepModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				neuralModelLab.step();
			}
		});
		
		// Create run model button
		runModelButton = new ImageButton(PLAY_ICON);
		runModelButton.setTooltip(new Tooltip("Run Model"));
		runModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				stepModelButton.setDisable(true);
				runModelButton.setDisable(true);
				pauseModelButton.setDisable(false);
				stopModelButton.setDisable(false);
				resetModelButton.setDisable(true);
				neuralModelLab.start();
			}
		});
		
		// Create pause model button
		pauseModelButton = new ImageButton(PAUSE_ICON);
		pauseModelButton.setTooltip(new Tooltip("Pause Model"));
		pauseModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				stepModelButton.setDisable(false);
				runModelButton.setDisable(false);
				neuralModelLab.pause();
			}
		});
		pauseModelButton.setDisable(true);
		
		// Create stop model button
		stopModelButton = new ImageButton(STOP_ICON);
		stopModelButton.setTooltip(new Tooltip("Stop Running Model"));
		stopModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				importModelLogButton.setDisable(false);
				saveModelHistButton.setDisable(true);
				exportModelLogButton.setDisable(true);
				stepModelButton.setDisable(false);
				runModelButton.setDisable(false);
				pauseModelButton.setDisable(true);
				stopModelButton.setDisable(true);
				neuralModelLab.reset();
			}
		});
		stopModelButton.setDisable(true);
		
		// Create reset model button
		resetModelButton = new ImageButton(RESET_ICON);
		resetModelButton.setTooltip(new Tooltip("Reset Model"));
		resetModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				importModelLogButton.setDisable(false);
				saveModelHistButton.setDisable(true);
				exportModelLogButton.setDisable(true);
				stepModelButton.setDisable(false);
				runModelButton.setDisable(false);
				pauseModelButton.setDisable(true);
				stopModelButton.setDisable(true);
				neuralModelLab.reset();
			}
		});
		
		// Add buttons to tool bar
		toolBox = new HBox(
				newSpikeModelButton,
				newSomaModelButton,
				newDendriteModelButton,
				copyModelButton,
				removeModelButton, 
				updateModelButton, 
				new Separator(Separator.VERTICAL, 30.0), 
				importModelLogButton, 
				exportModelLogButton, 
				saveModelHistButton, 
				new Separator(Separator.VERTICAL, 30.0), 
				stepModelButton, 
				runModelButton, 
				pauseModelButton, 
				stopModelButton, 
				resetModelButton);
		HBox.setHgrow(toolBox, Priority.ALWAYS);
		toolBox.setAlignment(Pos.CENTER);
		
		getItems().addAll(toolBox);
		
		listener = null;
	}
	
	/**
	 * Displays the tool bar for the Neural Network Lab app.
	 * @param networkLab - The Neural Network Lab app.
	 */
	public void displayNetworkLabToolBar(NetworkLab networkLab) {
		// Clear old tool bar
		getItems().clear();
		toolBox.getChildren().clear();
		
		// Create dendrite button
		dendriteButton = new ImageMenuButton(DENDRITE_ICON);
		dendriteButton.setTooltip(new Tooltip("Create New Dendrite Node"));
		
		// Create soma button
		somaButton = new ImageMenuButton(SOMA_ICON);
		somaButton.setTooltip(new Tooltip("Create New Soma Node"));
		
		// Create spiking button
		spikingButton = new ImageMenuButton(SPIKE_ICON);
		spikingButton.setTooltip(new Tooltip("Create New Spiking Node"));
		
		// Create SPIKING button menu
		MenuItem accItem = new MenuItem();
		Label accLabel = new Label("Accomodation");
		accLabel.setStyle("-fx-text-fill: white;");
		accItem.setGraphic(accLabel);
		accItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_ACCOMODATION);
			}
		});
		
		MenuItem biItem = new MenuItem();
		Label biLabel = new Label("Bistability");
		biLabel.setStyle("-fx-text-fill: white;");
		biItem.setGraphic(biLabel);
		biItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_BISTABILITY);
			}
		});
		
		MenuItem c1Item = new MenuItem();
		Label c1Label = new Label("Class I Excitable");
		c1Label.setStyle("-fx-text-fill: white;");
		c1Item.setGraphic(c1Label);
		c1Item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_CLASS_I_EXCITABLE);
			}
		});
		
		MenuItem c2Item = new MenuItem();
		Label c2Label = new Label("Class II Excitable");
		c2Label.setStyle("-fx-text-fill: white;");
		c2Item.setGraphic(c2Label);
		c2Item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_CLASS_II_EXCITABLE);
			}
		});
		
		MenuItem dapItem = new MenuItem();
		Label dapLabel = new Label("Depolarizing After Potential");
		dapLabel.setStyle("-fx-text-fill: white;");
		dapItem.setGraphic(dapLabel);
		dapItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_DEPOLARIZING_AFTER_POTENTIAL);
			}
		});
		
		MenuItem iibItem = new MenuItem();
		Label iibLabel = new Label("Inhibition-Induced Bursting");
		iibLabel.setStyle("-fx-text-fill: white;");
		iibItem.setGraphic(iibLabel);
		iibItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_INHIBITION_INDUCED_BURSTING);
			}
		});
		
		MenuItem iisItem = new MenuItem();
		Label iisLabel = new Label("Inhibition-Induced Spiking");
		iisLabel.setStyle("-fx-text-fill: white;");
		iisItem.setGraphic(iisLabel);
		iisItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_INHIBITION_INDUCED_SPIKING);
			}
		});
		
		MenuItem intItem = new MenuItem();
		Label intLabel = new Label("Integrator");
		intLabel.setStyle("-fx-text-fill: white;");
		intItem.setGraphic(intLabel);
		intItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_INTEGRATOR);
			}
		});
		
		MenuItem mmItem = new MenuItem();
		Label mmLabel = new Label("Mixed Mode");
		mmLabel.setStyle("-fx-text-fill: white;");
		mmItem.setGraphic(mmLabel);
		mmItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_MIXED_MODE);
			}
		});
		
		MenuItem pbItem = new MenuItem();
		Label pbLabel = new Label("Phasic Bursting");
		pbLabel.setStyle("-fx-text-fill: white;");
		pbItem.setGraphic(pbLabel);
		pbItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_PHASIC_BURSTING);
			}
		});
		
		MenuItem psItem = new MenuItem();
		Label psLabel = new Label("Phasic Spiking");
		psLabel.setStyle("-fx-text-fill: white;");
		psItem.setGraphic(psLabel);
		psItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_PHASIC_SPIKING);
			}
		});
		
		MenuItem rbItem = new MenuItem();
		Label rbLabel = new Label("Rebound Bursting");
		rbLabel.setStyle("-fx-text-fill: white;");
		rbItem.setGraphic(rbLabel);
		rbItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_REBOUND_BURSTING);
			}
		});
		
		MenuItem rsItem = new MenuItem();
		Label rsLabel = new Label("Rebound Spiking");
		rsLabel.setStyle("-fx-text-fill: white;");
		rsItem.setGraphic(rsLabel);
		rsItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_REBOUND_SPIKING);
			}
		});
		
		MenuItem resItem = new MenuItem();
		Label resLabel = new Label("Resonator");
		resLabel.setStyle("-fx-text-fill: white;");
		resItem.setGraphic(resLabel);
		resItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_RESONATOR);
			}
		});
		
		MenuItem sfaItem = new MenuItem();
		Label sfaLabel = new Label("Spike Frequency Adaptation");
		sfaLabel.setStyle("-fx-text-fill: white;");
		sfaItem.setGraphic(sfaLabel);
		sfaItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_SPIKE_FREQUENCY_ADAPTATION);
			}
		});
		
		MenuItem slItem = new MenuItem();
		Label slLabel = new Label("Spike Latency");
		slLabel.setStyle("-fx-text-fill: white;");
		slItem.setGraphic(slLabel);
		slItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_SPIKE_LATENCY);
			}
		});
		
		MenuItem soItem = new MenuItem();
		Label soLabel = new Label("Subthreshold Oscillations");
		soLabel.setStyle("-fx-text-fill: white;");
		soItem.setGraphic(soLabel);
		soItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_SUBTHRESHOLD_OSCILLATIONS);
			}
		});
		
		MenuItem tvItem = new MenuItem();
		Label tvLabel = new Label("Threshold Variability");
		tvLabel.setStyle("-fx-text-fill: white;");
		tvItem.setGraphic(tvLabel);
		tvItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_THRESHOLD_VARIABILITY);
			}
		});
		
		MenuItem tbItem = new MenuItem();
		Label tbLabel = new Label("Tonic Bursting");
		tbLabel.setStyle("-fx-text-fill: white;");
		tbItem.setGraphic(tbLabel);
		tbItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_TONIC_BURSTING);
			}
		});
		
		MenuItem tsItem = new MenuItem();
		Label tsLabel = new Label("Tonic Spiking");
		tsLabel.setStyle("-fx-text-fill: white;");
		tsItem.setGraphic(tsLabel);
		tsItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_TONIC_SPIKING);
			}
		});
		
		MenuItem otherItem = new MenuItem();
		Label otherLabel = new Label("Other");
		otherLabel.setStyle("-fx-text-fill: white;");
		otherItem.setGraphic(otherLabel);
		otherItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.SPIKING, Detail.SPIKING_OTHER);
			}
		});
		
		spikingButton.getItems().addAll(
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
		zoom100Button = new ImageButton(ZOOM_100_ICON);
		zoom100Button.setTooltip(new Tooltip("Zoom To 100%"));
		zoom100Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.ZOOM_100, null);
			}
		});
		
		// Create zoomIn button
		zoomInButton = new ImageButton(ZOOM_IN_ICON);
		zoomInButton.setTooltip(new Tooltip("Zoom In"));
		zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.ZOOM_IN, null);
			}
		});
		
		// Create zoomOut button
		zoomOutButton = new ImageButton(ZOOM_OUT_ICON);
		zoomOutButton.setTooltip(new Tooltip("Zoom Out"));
		zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				sendEvent(Item.ZOOM_OUT, null);
			}
		});
		
		// Add buttons to tool bar
		toolBox = new HBox(
				spikingButton,
				somaButton, 
				dendriteButton, 
				new Separator(Separator.VERTICAL, 30.0), 
				zoom100Button, 
				zoomInButton, 
				zoomOutButton);
		HBox.setHgrow(toolBox, Priority.ALWAYS);
		toolBox.setAlignment(Pos.CENTER);
		
		getItems().addAll(toolBox);
		
		listener = networkLab.getDisplay();
	}
	
	/**
	 * Sends an TopToolBarEvent to all AppToolBarListeners with the specified item and its corresponding detail, if any.
	 * @param item - The selected item.
	 * @param detail - The selected item's detail.
	 */
	private void sendEvent(Item item, Detail detail) {
		TopToolBarEvent event = new TopToolBarEvent(this, item, detail);
		
		if (listener != null)
			listener.itemSelected(event);
	}

	@Override
	public void sizeChanged(AppSizeEvent e) {
		setHeight(e.getWorkspaceHeight());
	}
}
