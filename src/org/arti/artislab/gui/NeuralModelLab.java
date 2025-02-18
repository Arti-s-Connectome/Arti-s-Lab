package org.arti.artislab.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.arti.artislab.gui.events.AppSizeEvent;
import org.arti.artislab.gui.events.AppSizeListener;
import org.arti.artislab.gui.events.NeuralModelLabEvent;
import org.arti.artislab.gui.events.NeuralModelLabListener;
import org.arti.artislab.gui.events.TopToolBarEvent;
import org.arti.artislab.gui.events.TopToolBarListener;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

/**
 * <p>public class <b>NeuralModelLab</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}, {@link Runnable}</p>
 * 
 * <p>NeuralModelLab class creates and controls the Izhikevich Lab app in the Arti's Lab application.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class NeuralModelLab extends BorderPane implements TopToolBarListener, AppSizeListener, Runnable {
	// Default height.
	private static final double DEF_HEIGHT = App.DEF_WORKSPACE_HEIGHT;
	// Default width.
	private static final double DEF_WIDTH = App.DEF_WORKSPACE_WIDTH;
	
	// Minus icon location.
	private static final String MINUS_ICON = "icons8-minus";
	// Plus icon location.
	private static final String PLUS_ICON = "icons8-plus";
	// Pulse icon location.
	private static final String PULSE_ICON = "icons8-spike";
	// Reset icon location.
	private static final String RESET_ICON = "icons8-reset";
	
	// Soma model file location.
	private static final String SOMA_MDL = "models/soma.mdl";
	// Spiking model file location.
	private static final String SPIKING_MDL = "models/spiking.mdl";
	
	// Dendrite neural model type.
	private static final int DENDRITE_TYPE = 0;
	// Soma neural model type.
	private static final int SOMA_TYPE = 1;
	// Spiking neural model type.
	private static final int SPIKING_TYPE = 2;
	
	// Number of soma models.
	private static final int SOMA_MODELS = 25;
	// Number of spiking models.
	private static final int SPIKING_MODELS = 25;
	
	// Graph timespan.
	private static final int TIMESPAN = 1000;
	
	// Default increment/decrement value.
	private static final float DEF_INC_DEC = 10.0f;
	// Default pulse value.
	private static final float DEF_PULSE = 100.0f;
	
	/**
	 * <p>public enum <b>Action</b></p>
	 * 
	 * <p>Action enum contains the list of actions that can trigger an NeuralModelLabEvent.</p>
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 23
	 */
	public enum Action {
		/**
		 * A custom neural model is selected.
		 */
		CUSTOM_MODEL,
		/**
		 * A standard neural model is selected.
		 */
		STANDARD_MODEL,
		/**
		 * Neural model has processed its full timespan.
		 */
		TIMESPAN_REACHED
	}
	
	// List of NeuralModelLabEvent listeners
	private ArrayList<NeuralModelLabListener> listener;
	// The type of model.
	private int modelType;
	// The number of preset models
	private int presetModels;
	// The processing thread.
	private Thread processThread;
	
	// Time scale of recovery variable u.
	private float a;
	// List of a values.
	private ArrayList<Float> aList;
	// Default a value.
	private float aDef;
	// Sensitivity of u to the fluctuations in v.
	private float b;
	// List of b values.
	private ArrayList<Float> bList;
	// Default b value.
	private float bDef;
	// Second, voltage-dependent value of b.
	private float b2;
	// List of b2 values.
	private ArrayList<Float> b2List;
	// Default b2 value.
	private float b2Def;
	// Alternate, voltage-dependent value of b.
	private float ba;
	// List of ba values.
	private ArrayList<Float> baList;
	// Default ba value.
	private float baDef;
	// Voltage threshold for alternate, voltage-dependent value of b.
	private float bv;
	// List of bv values.
	private ArrayList<Float> bvList;
	// Default bv value.
	private float bvDef;
	// After-spike reset value of v.
	private float c;
	// List of c values.
	private ArrayList<Float> cList;
	// Default c value.
	private float cDef;
	// Membrane capacitance.
	private float C;
	// List of membrane capacitance values.
	private ArrayList<Float> CList;
	// Default membrane capacitance value.
	private float CDef;
	// The coefficient for u in the membrean reset equation.
	private float cu;
	// List of u coefficient values.
	private ArrayList<Float> cuList;
	// Default u coefficient value.
	private float cuDef;
	// After-spike reset value of u.
	private float d;
	// List of d values.
	private ArrayList<Float> dList;
	// Default d value.
	private float dDef;
	// Child conductance.
	private float gc;
	// List of child conductance values.
	private ArrayList<Float> gcList;
	// Default child conductance value.
	private float gcDef;
	// Parent conductance.
	private float gp;
	// List of parent conductance values.
	private ArrayList<Float> gpList;
	// Default parent conductance value.
	private float gpDef;
	// Has history flag.
	private boolean hasHistory;
	// Input current.
	private float I;
	// Input current array.
	private float[] IArr;
	// Input current history array.
	private float[] IHistArr;
	// Child neural node input current.
	private float vgc;
	// Child neural node membrane potential array.
	private float[] vgcArr;
	// Child neural node membrane potential history array.
	private float[] vgcHistArr;
	// Parent neural node membrane potential.
	private float vgp;
	// Parent neural node membrane potential array.
	private float[] vgpArr;
	// Parent neural node membrane potential history array.
	private float[] vgpHistArr;
	// Square polynomial coefficient value.
	private float k;
	// List of square polynomial coefficient values.
	private ArrayList<Float> kList;
	// Default square polynomial coefficient value.
	private float kDef;
	// Spike flag.
	private boolean spike;
	// Spike count.
	private float spikes;
	// Started processing flag.
	private boolean started;
	// Time
	private float t;
	// Recovery variable.
	private float u;
	// Recovery variable array.
	private float[] uArr;
	// Recovery variable history array.
	private float[] uHistArr;
	// Initial membrane recovery
	private float uinit;
	// List of initial membrane recovery values.
	private ArrayList<Float> uinitList;
	// Maximal u value.
	private float umax;
	// List of maximal u values.
	private ArrayList<Float> umaxList;
	// Default maximal u value.
	private float umaxDef;
	// The exponential power used in the u equation.
	private float upow;
	// List of u power values.
	private ArrayList<Float> upowList;
	// Default u power value.
	private float upowDef;
	// The membrane potential added to the current membrane potential in the u equation.
	private float uv;
	// List of u equation membrane potential values.
	private ArrayList<Float> uvList;
	// Default u equation membrane potential value.
	private float uvDef;
	// The minimum membrane potential difference value in the u equation.
	private float uvmin;
	// List of minimum u equation membrane potential values.
	private ArrayList<Float> uvminList;
	// Default minimum u equation membrane potential value.
	private float uvminDef;
	// Membrane potential.
	private float v;
	// Membrane potential array.
	private float[] vArr;
	// Membrane potential history array.
	private float[] vHistArr;
	// Initial membrane potential
	private float vinit;
	// List of initial membrane potential values.
	private ArrayList<Float> vinitList;
	// Spike peak membrane potential.
	private float vp;
	// List of spike peak membrane potential values.
	private ArrayList<Float> vpList;
	// Default spike peak membrane potential value.
	private float vpDef;
	// The coeffiecient for u in the spike cutoff equation.
	private float vpu;
	// List of u spike cutoff coefficient values.
	private ArrayList<Float> vpuList;
	// Default u spike cutoff coefficient value.
	private float vpuDef;
	// Resting membrane potential.
	private float vr;
	// List of resting membrane potential values.
	private ArrayList<Float> vrList;
	// Default resting membrane potential value.
	private float vrDef;
	// Instantaneous threshold potential.
	private float vt;
	// List of instantaneous threshold potential values.
	private ArrayList<Float> vtList;
	// Default instantaneous threshold potential value.
	private float vtDef;
	
	// Neural model list label.
	private Label modelLabel;
	// Neural model type label.
	private Label modelTypeLabel;
	// Neural model type list.
	private ComboBox<String> modelTypeList;
	// Neural model name label.
	private Label modelNameLabel;
	// Neural model name list.
	private ComboBox<String> modelNameList;
	// List of neural model names.
	private ObservableList<String> modelNames;
	// Index of the currently selected model.
	private int selectedIndex;
	// Value a label.
	private Label aLabel;
	// Value a slider.
	private SliderBar aSlider;
	// Value a value text field.
	private TextField aValText;
	// Value a reset button.
	private ImageButton aResetButton;
	// Value b label.
	private Label bLabel;
	// Value b slider.
	private SliderBar bSlider;
	// Value b value text field.
	private TextField bValText;
	// Value b reset button.
	private ImageButton bResetButton;
	// Value b2 label.
	private Label b2Label;
	// Value b2 slider.
	private SliderBar b2Slider;
	// Value b value text field.
	private TextField b2ValText;
	// Value b2 reset button.
	private ImageButton b2ResetButton;
	// Value ba label.
	private Label baLabel;
	// Value ba slider.
	private SliderBar baSlider;
	// Value ba value text field.
	private TextField baValText;
	// Value ba reset button.
	private ImageButton baResetButton;
	// Value bv label.
	private Label bvLabel;
	// Value bv slider.
	private SliderBar bvSlider;
	// Value bv value text field.
	private TextField bvValText;
	// Value bv reset button.
	private ImageButton bvResetButton;
	// Value c label.
	private Label cLabel;
	// Value c slider.
	private SliderBar cSlider;
	// Value c value text field.
	private TextField cValText;
	// Value c reset button.
	private ImageButton cResetButton;
	// Value d label.
	private Label dLabel;
	// Value d slider.
	private SliderBar dSlider;
	// Value d value text field.
	private TextField dValText;
	// Value d reset button.
	private ImageButton dResetButton;
	// Value C label.
	private Label CLabel;
	// Value C slider.
	private SliderBar CSlider;
	// Value C value text field.
	private TextField CValText;
	// Value C reset button.
	private ImageButton CResetButton;
	// Value k label.
	private Label kLabel;
	// Value k slider.
	private SliderBar kSlider;
	// Value k value text field.
	private TextField kValText;
	// Value k reset button.
	private ImageButton kResetButton;
	// Value vp label.
	private Label vpLabel;
	// Value vp slider.
	private SliderBar vpSlider;
	// Value vp value text field.
	private TextField vpValText;
	// Value vp reset button.
	private ImageButton vpResetButton;
	// Value vr label.
	private Label vrLabel;
	// Value vr slider.
	private SliderBar vrSlider;
	// Value vr value text field.
	private TextField vrValText;
	// Value vr reset button.
	private ImageButton vrResetButton;
	// Value vt label.
	private Label vtLabel;
	// Value vt slider.
	private SliderBar vtSlider;
	// Value vt value text field.
	private TextField vtValText;
	// Value vt reset button.
	private ImageButton vtResetButton;
	// Value cu label.
	private Label cuLabel;
	// Value cu slider.
	private SliderBar cuSlider;
	// Value cu value text field.
	private TextField cuValText;
	// Value cu reset button.
	private ImageButton cuResetButton;
	// Value vpu label.
	private Label vpuLabel;
	// Value vpu slider.
	private SliderBar vpuSlider;
	// Value vpu value text field.
	private TextField vpuValText;
	// Value vpu reset button.
	private ImageButton vpuResetButton;
	// Value umax label.
	private Label umaxLabel;
	// Value umax slider.
	private SliderBar umaxSlider;
	// Value umax value text field.
	private TextField umaxValText;
	// Value umax reset button.
	private ImageButton umaxResetButton;
	// Value upow label.
	private Label upowLabel;
	// Value upow slider.
	private SliderBar upowSlider;
	// Value upow value text field.
	private TextField upowValText;
	// Value upow reset button.
	private ImageButton upowResetButton;
	// Value uv label.
	private Label uvLabel;
	// Value uv slider.
	private SliderBar uvSlider;
	// Value uv value text field.
	private TextField uvValText;
	// Value uv reset button.
	private ImageButton uvResetButton;
	// Value uvmin label.
	private Label uvminLabel;
	// Value uvmin slider.
	private SliderBar uvminSlider;
	// Value uvmin value text field.
	private TextField uvminValText;
	// Value uvmin reset button.
	private ImageButton uvminResetButton;
	// Value bgclabel.
	private Label gcLabel;
	// Value gc slider.
	private SliderBar gcSlider;
	// Value gc value text field.
	private TextField gcValText;
	// Value gc reset button.
	private ImageButton gcResetButton;
	// Value gp label.
	private Label gpLabel;
	// Value gp slider.
	private SliderBar gpSlider;
	// Value gp value text field.
	private TextField gpValText;
	// Value gp reset button.
	private ImageButton gpResetButton;
	// Model vertical layout box.
	private VBox modelBox;
	
	// Input current label.
	private Label inputLabel;
	// Value I label.
	private Label ILabel;
	// Value I slider.
	private SliderBar ISlider;
	// Value I value text field.
	private TextField IValText;
	// Value I increment button.
	private ImageButton IIncButton;
	// Value I decrement button.
	private ImageButton IDecButton;
	// Value I pulse button.
	private ImageButton IPulseButton;
	// Value I reset button.
	private ImageButton IResetButton;
	// Value vgc label.
	private Label vgcLabel;
	// Value vgc slider.
	private SliderBar vgcSlider;
	// Value vgc value text field.
	private TextField vgcValText;
	// Value vgc increment button.
	private ImageButton vgcIncButton;
	// Value vgc decrement button.
	private ImageButton vgcDecButton;
	// Value vgc pulse button.
	private ImageButton vgcPulseButton;
	// Value vgc reset button.
	private ImageButton vgcResetButton;
	// Value vgp label.
	private Label vgpLabel;
	// Value vgp slider.
	private SliderBar vgpSlider;
	// Value vgp value text field.
	private TextField vgpValText;
	// Value vgp increment button.
	private ImageButton vgpIncButton;
	// Value vgp decrement button.
	private ImageButton vgpDecButton;
	// Value vgp pulse button.
	private ImageButton vgpPulseButton;
	// Value vgp reset button.
	private ImageButton vgpResetButton;
	// Increment/decrement label.
	private Label incDecLabel;
	// Increment/decrement value text field.
	private TextField incDecValText;
	// Increment/decrement value.
	private float incDec;
	// Pulse label.
	private Label pulseLabel;
	// Pulse value text field.
	private TextField pulseValText;
	// Pulse value.
	private float pulse;
	
	// Phase portrait graph.
	private LineChart<Number, Number> pGraph;
	// Phase portrait c series.
	private XYChart.Series<Number, Number> pcSeries;
	// Phase portrait u nullcline series.
	private XYChart.Series<Number, Number> puNullSeries;
	// Phase portrait v nullcline series.
	private XYChart.Series<Number, Number> pvNullSeries;
	// Phase portrait v, u series.
	private XYChart.Series<Number, Number> pvuSeries;
	// Phase portrait graph x axis.
	private NumberAxis pxAxis;
	// Phase portrait graph y axis.
	private NumberAxis pyAxis;
	// Membrane recovery graph.
	private LineChart<Number, Number> uGraph;
	// Membrane recovery series.
	private XYChart.Series<Number, Number> uSeries;
	// Membrane recovery history series.
	private XYChart.Series<Number, Number> uHistSeries;
	// Membrane recovery graph x axis.
	private NumberAxis uxAxis;
	// Membrane recovery graph y axis.
	private NumberAxis uyAxis;
	// Membrane potential graph.
	private LineChart<Number, Number> vGraph;
	// Membrane potential series.
	private XYChart.Series<Number, Number> vSeries;
	// Membrane potential history series.
	private XYChart.Series<Number, Number> vHistSeries;
	// Membrane potential graph x axis.
	private NumberAxis vxAxis;
	// Membrane potential graph y axis.
	private NumberAxis vyAxis;
	// Input current graph.
	private LineChart<Number, Number> IGraph;
	// Input current series.
	private XYChart.Series<Number, Number> ISeries;
	// Input current history series.
	private XYChart.Series<Number, Number> IHistSeries;
	// Input current graph x axis.
	private NumberAxis IxAxis;
	// Input current graph y axis.
	private NumberAxis IyAxis;
	// Connected neural node input current graph.
	private LineChart<Number, Number> gGraph;
	// Connected child neural node input current series.
	private XYChart.Series<Number, Number> gcSeries;
	// Connected parent neural node input current series.
	private XYChart.Series<Number, Number> gpSeries;
	// Connected child neural node input current history series.
	private XYChart.Series<Number, Number> gcHistSeries;
	// Connected parent neural node input current history series.
	private XYChart.Series<Number, Number> gpHistSeries;
	// Connected neural node input current graph x axis.
	private NumberAxis gxAxis;
	// Connected neural node input current graph y axis.
	private NumberAxis gyAxis;
	
	// Output log label.
	private Label outputLabel;
	// Output log text area.
	private TextArea outputArea;
	// Output log text.
	private String outputLog;
	// Output vertical layout box.
	private VBox outputBox;
	
	/**
	 * Default constructor. Creates the Izhikevich Lab app.
	 */
	public NeuralModelLab() {
		// Call parent constructor.
		super();
		
		// Initialize variables
		listener = new ArrayList<NeuralModelLabListener>();
		modelType = DENDRITE_TYPE;
		presetModels = 0;
		processThread = null;
		
		a = 0.0f;
		aList = new ArrayList<Float>();
		aDef = 0.0f;
		b = 0.0f;
		bList = new ArrayList<Float>();
		bDef = 0.0f;
		b2 = 0.0f;
		b2List = new ArrayList<Float>();
		b2Def = 0.0f;
		ba = 0.0f;
		baList = new ArrayList<Float>();
		baDef = 0.0f;
		bv = 0.0f;
		bvList = new ArrayList<Float>();
		bvDef = 0.0f;
		c = 0.0f;
		cList = new ArrayList<Float>();
		cDef = 0.0f;
		C = 0.0f;
		CList = new ArrayList<Float>();
		CDef = 0.0f;
		cu = 0.0f;
		cuList = new ArrayList<Float>();
		cuDef = 0.0f;
		d = 0.0f;
		dList = new ArrayList<Float>();
		dDef = 0.0f;
		gc = 0.0f;
		gcList = new ArrayList<Float>();
		gcDef = 0.0f;
		gp = 0.0f;
		gpList = new ArrayList<Float>();
		gpDef = 0.0f;
		hasHistory = false;
		I = 0.0f;
		IArr = new float[TIMESPAN];
		IHistArr = new float[TIMESPAN];
		incDec = DEF_INC_DEC;
		k = 0.0f;
		kList = new ArrayList<Float>();
		kDef = 0.0f;
		pulse = DEF_PULSE;
		spike = false;
		spikes = 0.0f;
		started = false;
		t = 0.0f;
		u = 0.0f;
		uArr = new float[TIMESPAN];
		uHistArr = new float[TIMESPAN];
		uinit = 0.0f;
		uinitList = new ArrayList<Float>();
		umax = 0.0f;
		umaxList = new ArrayList<Float>();
		umaxDef = 0.0f;
		upow = 0.0f;
		upowList = new ArrayList<Float>();
		upowDef = 0.0f;
		uv = 0.0f;
		uvList = new ArrayList<Float>();
		uvDef = 0.0f;
		uvmin = 0.0f;
		uvminList = new ArrayList<Float>();
		uvminDef = 0.0f;
		v = vr;
		vArr = new float[TIMESPAN];
		vHistArr = new float[TIMESPAN];
		vgc = 0.0f;
		vgcArr = new float[TIMESPAN];
		vgcHistArr = new float[TIMESPAN];
		vgp = 0.0f;
		vgpArr = new float[TIMESPAN];
		vgpHistArr = new float[TIMESPAN];
		vinit = 0.0f;
		vinitList = new ArrayList<Float>();
		vp = 0.0f;
		vpList = new ArrayList<Float>();
		vpDef = 0.0f;
		vpu = 0.0f;
		vpuList = new ArrayList<Float>();
		vpuDef = 0.0f;
		vr = 0.0f;
		vrList = new ArrayList<Float>();
		vrDef = 0.0f;
		vt = 0.0f;
		vtList = new ArrayList<Float>();
		vtDef = 0.0f;
		
		for (int i = 0; i < TIMESPAN; ++i) {
			IArr[i] = 0.0f;
			IHistArr[i] = 0.0f;
			vgcArr[i] = 0.0f;
			vgcHistArr[i] = 0.0f;
			vgpArr[i] = 0.0f;
			vgpHistArr[i] = 0.0f;
			uArr[i] = u;
			uHistArr[i] = 0.0f;
			vArr[i] = c;
			vHistArr[i] = 0.0f;
		}
		
		// Setup layout
		setMinHeight(DEF_HEIGHT);
		setMaxHeight(DEF_HEIGHT);
		setMinWidth(DEF_WIDTH);
		setMaxWidth(DEF_WIDTH);
		
		// Create neural model list
		modelLabel = new Label("Neural Models");
		modelLabel.setTextFill(Color.WHITE);
		modelLabel.setFont(new Font(modelLabel.getFont().getName(), 16.0));
		modelLabel.setBackground(Background.EMPTY);
		modelLabel.setMinHeight(25.0);
		modelLabel.setMaxHeight(25.0);
		modelLabel.setAlignment(Pos.CENTER);
		
		modelTypeLabel = new Label("Model Type:");
		modelTypeLabel.setTextFill(Color.WHITE);
		modelTypeLabel.setFont(new Font(modelTypeLabel.getFont().getName(), 12.0));
		modelTypeLabel.setBackground(Background.EMPTY);
		modelTypeLabel.setMinHeight(35.0);
		modelTypeLabel.setMaxHeight(35.0);
		modelTypeLabel.setMinWidth(75.0);
		modelTypeLabel.setMaxWidth(75.0);
		
		ObservableList<String> typeNames = FXCollections.observableArrayList("Dendrite Models", "Soma Models", "Spiking Models");
		
		modelTypeList = new ComboBox<String>(typeNames);
		modelTypeList.setMinHeight(35.0);
		modelTypeList.setMaxHeight(35.0);
		modelTypeList.setMinWidth(App.DEF_WORKSPACE_WIDTH * 0.2 - 90.0);
		modelTypeList.setMaxWidth(App.DEF_WORKSPACE_WIDTH * 0.2 - 90.0);
		modelTypeList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// If Dendrite Models is selected
				if (arg0.getValue().intValue() == 0) {
					// Load dendrite models file
					modelNames = FXCollections.observableArrayList();
					
					modelNameList.setItems(modelNames);
					modelNameList.getSelectionModel().select(0);
					
					modelType = DENDRITE_TYPE;
					presetModels = 0;
					sendEvent(Action.STANDARD_MODEL);
					
					pvNullSeries.getData().clear();
					puNullSeries.getData().clear();
					
					for (int i = 0; i <= 160; ++i) {
						puNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, b * ((-100.0f + (float)i) - vr)));
						pvNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, 
								k * ((-100.0f + (float)i) - vr) * ((-100.0f + (float)i) - vt) + I));
					}
				}
				// If Soma Models is selected
				else if (arg0.getValue().intValue() == 1) {
					// Load soma models file
					modelNames = FXCollections.observableArrayList();
					
					try {
						Scanner scanner = new Scanner(new File(SOMA_MDL));
						
						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							String[] part = line.split(",");
							
							modelNames.add(part[0]);
							aList.add(Float.parseFloat(part[1]));
							bList.add(Float.parseFloat(part[2]));
							b2List.add(Float.parseFloat(part[3]));
							baList.add(Float.parseFloat(part[4]));
							bvList.add(Float.parseFloat(part[5]));
							cList.add(Float.parseFloat(part[6]));
							CList.add(Float.parseFloat(part[7]));
							cuList.add(Float.parseFloat(part[8]));
							dList.add(Float.parseFloat(part[9]));
							gcList.add(Float.parseFloat(part[10]));
							gpList.add(Float.parseFloat(part[11]));
							kList.add(Float.parseFloat(part[12]));
							uinitList.add(Float.parseFloat(part[13]));
							umaxList.add(Float.parseFloat(part[14]));
							upowList.add(Float.parseFloat(part[15]));
							uvList.add(Float.parseFloat(part[16]));
							uvminList.add(Float.parseFloat(part[17]));
							vinitList.add(Float.parseFloat(part[18]));
							vpList.add(Float.parseFloat(part[19]));
							vpuList.add(Float.parseFloat(part[20]));
							vrList.add(Float.parseFloat(part[21]));
							vtList.add(Float.parseFloat(part[22]));
						}
						
						scanner.close();
					} catch (FileNotFoundException e) {
						App.alertError("File not found!", "Could not find file: " + SOMA_MDL);
					}
					
					modelNameList.setItems(modelNames);
					modelNameList.getSelectionModel().select(0);
					
					modelType = SOMA_TYPE;
					presetModels = SOMA_MODELS;
					sendEvent(Action.STANDARD_MODEL);
					
					pvNullSeries.getData().clear();
					puNullSeries.getData().clear();
					
					for (int i = 0; i <= 160; ++i) {
						puNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, b * ((-100.0f + (float)i) - vr)));
						pvNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, 
								k * ((-100.0f + (float)i) - vr) * ((-100.0f + (float)i) - vt) + I));
					}
				}
				// If Spiking Models is selected
				else if (arg0.getValue().intValue() == 2) {
					// Load spiking models file
					modelNames = FXCollections.observableArrayList();
					
					try {
						Scanner scanner = new Scanner(new File(SPIKING_MDL));
						
						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							String[] part = line.split(",");
							
							modelNames.add(part[0]);
							aList.add(Float.parseFloat(part[1]));
							bList.add(Float.parseFloat(part[2]));
							b2List.add(Float.parseFloat(part[3]));
							baList.add(Float.parseFloat(part[4]));
							bvList.add(Float.parseFloat(part[5]));
							cList.add(Float.parseFloat(part[6]));
							CList.add(Float.parseFloat(part[7]));
							cuList.add(Float.parseFloat(part[8]));
							dList.add(Float.parseFloat(part[9]));
							gcList.add(Float.parseFloat(part[10]));
							gpList.add(Float.parseFloat(part[11]));
							kList.add(Float.parseFloat(part[12]));
							uinitList.add(Float.parseFloat(part[13]));
							umaxList.add(Float.parseFloat(part[14]));
							upowList.add(Float.parseFloat(part[15]));
							uvList.add(Float.parseFloat(part[16]));
							uvminList.add(Float.parseFloat(part[17]));
							vinitList.add(Float.parseFloat(part[18]));
							vpList.add(Float.parseFloat(part[19]));
							vpuList.add(Float.parseFloat(part[20]));
							vrList.add(Float.parseFloat(part[21]));
							vtList.add(Float.parseFloat(part[22]));
						}
						
						scanner.close();
					} catch (FileNotFoundException e) {
						App.alertError("File not found!", "Could not find file: " + SPIKING_MDL);
					}
					
					modelNameList.setItems(modelNames);
					modelNameList.getSelectionModel().select(0);
					
					modelType = SPIKING_TYPE;
					presetModels = SPIKING_MODELS;
					sendEvent(Action.STANDARD_MODEL);
					
					pvNullSeries.getData().clear();
					puNullSeries.getData().clear();
					
					for (int i = 0; i <= 160; ++i) {
						puNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, b * (-100.0f + (float)i)));
						pvNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, 
								0.04 * ((-100.0f + (float)i) * (-100.0f + (float)i)) + 5.0f * (-100.0f + (float)i) + 140.0 + I));
					}
				}
			}
		});
		
		HBox modelTypeBox = new HBox(modelTypeLabel, modelTypeList);
		modelTypeBox.setSpacing(5.0);
		modelTypeBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		modelNameLabel = new Label("Model Name:");
		modelNameLabel.setTextFill(Color.WHITE);
		modelNameLabel.setFont(new Font(modelNameLabel.getFont().getName(), 12.0));
		modelNameLabel.setBackground(Background.EMPTY);
		modelNameLabel.setMinHeight(35.0);
		modelNameLabel.setMaxHeight(35.0);
		modelNameLabel.setMinWidth(75.0);
		modelNameLabel.setMaxWidth(75.0);
		
		modelNameList = new ComboBox<String>(modelNames);
		modelNameList.setMinHeight(35.0);
		modelNameList.setMaxHeight(35.0);
		modelNameList.setMinWidth(App.DEF_WORKSPACE_WIDTH * 0.2 - 90.0);
		modelNameList.setMaxWidth(App.DEF_WORKSPACE_WIDTH * 0.2 - 90.0);
		modelNameList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// Set selected index
				selectedIndex = arg0.getValue().intValue();
				
				// Update values
				a = aList.get(selectedIndex);
				b = bList.get(selectedIndex);
				b2 = b2List.get(selectedIndex);
				ba = baList.get(selectedIndex);
				bv = bvList.get(selectedIndex);
				c = cList.get(selectedIndex);
				d = dList.get(selectedIndex);
				C = CList.get(selectedIndex);
				k = kList.get(selectedIndex);
				vinit = vinitList.get(selectedIndex);
				vr = vrList.get(selectedIndex);
				vt = vtList.get(selectedIndex);
				vp = vpList.get(selectedIndex);
				cu = cuList.get(selectedIndex);
				vpu = vpuList.get(selectedIndex);
				uinit = uinitList.get(selectedIndex);
				umax = umaxList.get(selectedIndex);
				upow = upowList.get(selectedIndex);
				uv = uvList.get(selectedIndex);
				uvmin = uvminList.get(selectedIndex);
				aDef = a;
				bDef = b;
				baDef = ba;
				bvDef = bv;
				cDef = c;
				dDef = d;
				kDef = k;
				CDef = C;
				vpDef = vp;
				vrDef = vr;
				vtDef = vt;
				cuDef = cu;
				vpuDef = vpu;
				umaxDef = umax;
				upowDef = upow;
				uvDef = uv;
				uvminDef = uvmin;
				
				// Update controls
				aSlider.setValue(a);
				bSlider.setValue(b);
				baSlider.setValue(ba);
				bvSlider.setValue(bv);
				cSlider.setValue(c);
				dSlider.setValue(d);
				ISlider.setValue(I);
				kSlider.setValue(k);
				CSlider.setValue(C);
				vpSlider.setValue(vp);
				vrSlider.setValue(vr);
				vtSlider.setValue(vt);
				cuSlider.setValue(cu);
				vpuSlider.setValue(vpu);
				umaxSlider.setValue(umax);
				upowSlider.setValue(upow);
				uvSlider.setValue(uv);
				uvminSlider.setValue(uvmin);
				
				if (selectedIndex < presetModels) 
					sendEvent(Action.STANDARD_MODEL);
				else 
					sendEvent(Action.CUSTOM_MODEL);
			}
		});
		
		HBox modelNameBox = new HBox(modelNameLabel, modelNameList);
		modelNameBox.setSpacing(5.0);
		modelNameBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value a
		aLabel = new Label("a:");
		aLabel.setTextFill(Color.WHITE);
		aLabel.setFont(new Font(aLabel.getFont().getName(), 12.0));
		aLabel.setMinWidth(50.0);
		aLabel.setMaxWidth(50.0);
		aLabel.setAlignment(Pos.CENTER);
		
		aSlider = new SliderBar(-0.1, 0.1, a);
		aSlider.setMaxWidth(Double.MAX_VALUE);
        aSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		a = (float)aSlider.getValue();
        		aValText.setText(String.format("%2.3f", a));
        	}
        });
		
        aValText = new TextField(String.format("%2.3f", a));
        aValText.setBackground(Background.fill(Color.BLACK));
        aValText.setFont(new Font(aValText.getFont().getName(), 12.0));
		aValText.setMinWidth(75.0);
		aValText.setMaxWidth(75.0);
		aValText.setAlignment(Pos.CENTER);
		aValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					a = (float)Math.max(-0.1, Math.min(0.1, Float.parseFloat(aValText.getText())));
					aValText.setText(String.format("%2.3f", a));
					aSlider.setValue(a);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					aValText.setText(String.format("%2.3f", a));
					aSlider.setValue(a);
				}
			}
		});
		
		aResetButton = new ImageButton(RESET_ICON);
		aResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		aResetButton.setMinWidth(25.0);
		aResetButton.setMaxWidth(25.0);
		aResetButton.setMinHeight(25.0);
		aResetButton.setMaxHeight(25.0);
		aResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				a = aDef;
				aSlider.setValue(a);
			}
		});
		
		HBox aBox = new HBox(aLabel, aSlider, aValText, aResetButton);
		HBox.setHgrow(aSlider, Priority.ALWAYS);
		aBox.setSpacing(5.0);
		aBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value b
		bLabel = new Label("b:");
		bLabel.setTextFill(Color.WHITE);
		bLabel.setFont(new Font(bLabel.getFont().getName(), 12.0));
		bLabel.setMinWidth(50.0);
		bLabel.setMaxWidth(50.0);
		bLabel.setAlignment(Pos.CENTER);
		
		bSlider = new SliderBar(-5.0, 20.0, b);
		bSlider.setMaxWidth(Double.MAX_VALUE);
        bSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		b = (float)bSlider.getValue();
        		bValText.setText(String.format("%2.3f", b));
        	}
        });
		
		bValText = new TextField(String.format("%2.3f", b));
		bValText.setBackground(Background.fill(Color.BLACK));
		bValText.setFont(new Font(bValText.getFont().getName(), 12.0));
		bValText.setMinWidth(75.0);
		bValText.setMaxWidth(75.0);
		bValText.setAlignment(Pos.CENTER);
		bValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					b = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(bValText.getText())));
					bValText.setText(String.format("%2.3f", b));
					bSlider.setValue(b);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					bValText.setText(String.format("%2.3f", b));
					bSlider.setValue(b);
				}
			}
		});
		
		bResetButton = new ImageButton(RESET_ICON);
		bResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		bResetButton.setMinWidth(25.0);
		bResetButton.setMaxWidth(25.0);
		bResetButton.setMinHeight(25.0);
		bResetButton.setMaxHeight(25.0);
		bResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				b = bDef;
				bSlider.setValue(b);
			}
		});
		
		HBox bBox = new HBox(bLabel, bSlider, bValText, bResetButton);
		HBox.setHgrow(bSlider, Priority.ALWAYS);
		bBox.setSpacing(5.0);
		bBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value b2
		b2Label = new Label("b2:");
		b2Label.setTextFill(Color.WHITE);
		b2Label.setFont(new Font(bLabel.getFont().getName(), 12.0));
		b2Label.setMinWidth(50.0);
		b2Label.setMaxWidth(50.0);
		b2Label.setAlignment(Pos.CENTER);
		
		b2Slider = new SliderBar(-5.0, 20.0, b2);
		b2Slider.setMaxWidth(Double.MAX_VALUE);
        b2Slider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		b2 = (float)b2Slider.getValue();
        		b2ValText.setText(String.format("%2.3f", b2));
        	}
        });
		
		b2ValText = new TextField(String.format("%2.3f", b2));
		b2ValText.setBackground(Background.fill(Color.BLACK));
		b2ValText.setFont(new Font(b2ValText.getFont().getName(), 12.0));
		b2ValText.setMinWidth(75.0);
		b2ValText.setMaxWidth(75.0);
		b2ValText.setAlignment(Pos.CENTER);
		b2ValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					b2 = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(b2ValText.getText())));
					b2ValText.setText(String.format("%2.3f", b2));
					b2Slider.setValue(b2);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					b2ValText.setText(String.format("%2.3f", b2));
					b2Slider.setValue(b2);
				}
			}
		});
		
		b2ResetButton = new ImageButton(RESET_ICON);
		b2ResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		b2ResetButton.setMinWidth(25.0);
		b2ResetButton.setMaxWidth(25.0);
		b2ResetButton.setMinHeight(25.0);
		b2ResetButton.setMaxHeight(25.0);
		b2ResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				b2 = b2Def;
				b2Slider.setValue(b2);
			}
		});
		
		HBox b2Box = new HBox(b2Label, b2Slider, b2ValText, b2ResetButton);
		HBox.setHgrow(b2Slider, Priority.ALWAYS);
		b2Box.setSpacing(5.0);
		b2Box.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value ba
		baLabel = new Label("ba:");
		baLabel.setTextFill(Color.WHITE);
		baLabel.setFont(new Font(baLabel.getFont().getName(), 12.0));
		baLabel.setMinWidth(50.0);
		baLabel.setMaxWidth(50.0);
		baLabel.setAlignment(Pos.CENTER);
		
		baSlider = new SliderBar(-5.0, 20.0, ba);
		baSlider.setMaxWidth(Double.MAX_VALUE);
        baSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		ba = (float)baSlider.getValue();
        		baValText.setText(String.format("%2.3f", ba));
        	}
        });
		
		baValText = new TextField(String.format("%2.3f", ba));
		baValText.setBackground(Background.fill(Color.BLACK));
		baValText.setFont(new Font(baValText.getFont().getName(), 12.0));
		baValText.setMinWidth(75.0);
		baValText.setMaxWidth(75.0);
		baValText.setAlignment(Pos.CENTER);
		baValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					ba = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(baValText.getText())));
					baValText.setText(String.format("%2.3f", ba));
					baSlider.setValue(ba);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					baValText.setText(String.format("%2.3f", ba));
					baSlider.setValue(ba);
				}
			}
		});
		
		baResetButton = new ImageButton(RESET_ICON);
		baResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		baResetButton.setMinWidth(25.0);
		baResetButton.setMaxWidth(25.0);
		baResetButton.setMinHeight(25.0);
		baResetButton.setMaxHeight(25.0);
		baResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ba = baDef;
				baSlider.setValue(ba);
			}
		});
		
		HBox baBox = new HBox(baLabel, baSlider, baValText, baResetButton);
		HBox.setHgrow(baSlider, Priority.ALWAYS);
		baBox.setSpacing(5.0);
		baBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value bv
		bvLabel = new Label("bv:");
		bvLabel.setTextFill(Color.WHITE);
		bvLabel.setFont(new Font(bvLabel.getFont().getName(), 12.0));
		bvLabel.setMinWidth(50.0);
		bvLabel.setMaxWidth(50.0);
		bvLabel.setAlignment(Pos.CENTER);
		
		bvSlider = new SliderBar(-5.0, 20.0, bv);
		bvSlider.setMaxWidth(Double.MAX_VALUE);
        bvSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		bv = (float)bvSlider.getValue();
        		bvValText.setText(String.format("%2.3f", bv));
        	}
        });
		
		bvValText = new TextField(String.format("%2.3f", bv));
		bvValText.setBackground(Background.fill(Color.BLACK));
		bvValText.setFont(new Font(bvValText.getFont().getName(), 12.0));
		bvValText.setMinWidth(75.0);
		bvValText.setMaxWidth(75.0);
		bvValText.setAlignment(Pos.CENTER);
		bvValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					bv = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(bvValText.getText())));
					bvValText.setText(String.format("%2.3f", bv));
					bvSlider.setValue(bv);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					bvValText.setText(String.format("%2.3f", bv));
					bvSlider.setValue(bv);
				}
			}
		});
		
		bvResetButton = new ImageButton(RESET_ICON);
		bvResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		bvResetButton.setMinWidth(25.0);
		bvResetButton.setMaxWidth(25.0);
		bvResetButton.setMinHeight(25.0);
		bvResetButton.setMaxHeight(25.0);
		bvResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bv = bvDef;
				bvSlider.setValue(bv);
			}
		});
		
		HBox bvBox = new HBox(bvLabel, bvSlider, bvValText, bvResetButton);
		HBox.setHgrow(bvSlider, Priority.ALWAYS);
		bvBox.setSpacing(5.0);
		bvBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value c
		cLabel = new Label("c:");
		cLabel.setTextFill(Color.WHITE);
		cLabel.setFont(new Font(cLabel.getFont().getName(), 12.0));
		cLabel.setMinWidth(50.0);
		cLabel.setMaxWidth(50.0);
		cLabel.setAlignment(Pos.CENTER);
		
		cSlider = new SliderBar(-90.0, -40.0, c);
		cSlider.setMaxWidth(Double.MAX_VALUE);
        cSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		c = (float)cSlider.getValue();
        		cValText.setText(String.format("%3.3f", c));
        		
        		pcSeries.getData().clear();
        		pcSeries.getData().add(new XYChart.Data<Number, Number>(c, 200.0f));
        		pcSeries.getData().add(new XYChart.Data<Number, Number>(c, -200.0f));
        	}
        });
		
        cValText = new TextField(String.format("%3.3f", c));
        cValText.setBackground(Background.fill(Color.BLACK));
        cValText.setFont(new Font(cValText.getFont().getName(), 12.0));
        cValText.setMinWidth(75.0);
        cValText.setMaxWidth(75.0);
        cValText.setAlignment(Pos.CENTER);
		cValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					c = (float)Math.max(-90.0, Math.min(-40.0, Float.parseFloat(cValText.getText())));
					cValText.setText(String.format("%3.3f", c));
					cSlider.setValue(c);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					cValText.setText(String.format("%3.3f", c));
					cSlider.setValue(c);
				}
			}
		});
		
		cResetButton = new ImageButton(RESET_ICON);
		cResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		cResetButton.setMinWidth(25.0);
		cResetButton.setMaxWidth(25.0);
		cResetButton.setMinHeight(25.0);
		cResetButton.setMaxHeight(25.0);
		cResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				c = cDef;
				cSlider.setValue(c);
			}
		});
		
		HBox cBox = new HBox(cLabel, cSlider, cValText, cResetButton);
		HBox.setHgrow(cSlider, Priority.ALWAYS);
		cBox.setSpacing(5.0);
		cBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value d
		dLabel = new Label("d:");
		dLabel.setTextFill(Color.WHITE);
		dLabel.setFont(new Font(dLabel.getFont().getName(), 12.0));
		dLabel.setMinWidth(50.0);
		dLabel.setMaxWidth(50.0);
		dLabel.setAlignment(Pos.CENTER);
		
		dSlider = new SliderBar(0.0, 1000.0, d);
		dSlider.setMaxWidth(Double.MAX_VALUE);
        dSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		d = (float)dSlider.getValue();
        		dValText.setText(String.format("%3.3f", d));
        	}
        });
		
        dValText = new TextField(String.format("%3.3f", d));
        dValText.setBackground(Background.fill(Color.BLACK));
        dValText.setFont(new Font(dValText.getFont().getName(), 12.0));
        dValText.setMinWidth(75.0);
        dValText.setMaxWidth(75.0);
        dValText.setAlignment(Pos.CENTER);
		dValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					d = (float)Math.max(0.0, Math.min(1000.0, Float.parseFloat(dValText.getText())));
					dValText.setText(String.format("%3.3f", d));
					dSlider.setValue(d);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					dValText.setText(String.format("%3.3f", d));
					dSlider.setValue(d);
				}
			}
		});
		
		dResetButton = new ImageButton(RESET_ICON);
		dResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		dResetButton.setMinWidth(25.0);
		dResetButton.setMaxWidth(25.0);
		dResetButton.setMinHeight(25.0);
		dResetButton.setMaxHeight(25.0);
		dResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				d = dDef;
				dSlider.setValue(d);
			}
		});
		
		HBox dBox = new HBox(dLabel, dSlider, dValText, dResetButton);
		HBox.setHgrow(dSlider, Priority.ALWAYS);
		dBox.setSpacing(5.0);
		dBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value C
		CLabel = new Label("C:");
		CLabel.setTextFill(Color.WHITE);
		CLabel.setFont(new Font(CLabel.getFont().getName(), 12.0));
		CLabel.setMinWidth(50.0);
		CLabel.setMaxWidth(50.0);
		CLabel.setAlignment(Pos.CENTER);
		
		CSlider = new SliderBar(0.0, 200.0, C);
		CSlider.setMaxWidth(Double.MAX_VALUE);
        CSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		C = (float)CSlider.getValue();
        		CValText.setText(String.format("%3.3f", C));
        	}
        });
		
        CValText = new TextField(String.format("%3.3f", C));
        CValText.setBackground(Background.fill(Color.BLACK));
        CValText.setFont(new Font(CValText.getFont().getName(), 12.0));
        CValText.setMinWidth(75.0);
        CValText.setMaxWidth(75.0);
        CValText.setAlignment(Pos.CENTER);
		CValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					C = (float)Math.max(0.0, Math.min(200.0, Float.parseFloat(CValText.getText())));
					CValText.setText(String.format("%3.3f", C));
					CSlider.setValue(C);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					CValText.setText(String.format("%3.3f", C));
					CSlider.setValue(C);
				}
			}
		});
		
		CResetButton = new ImageButton(RESET_ICON);
		CResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		CResetButton.setMinWidth(25.0);
		CResetButton.setMaxWidth(25.0);
		CResetButton.setMinHeight(25.0);
		CResetButton.setMaxHeight(25.0);
		CResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				C = CDef;
				CSlider.setValue(C);
			}
		});
		
		HBox CBox = new HBox(CLabel, CSlider, CValText, CResetButton);
		HBox.setHgrow(CSlider, Priority.ALWAYS);
		CBox.setSpacing(5.0);
		CBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value k
		kLabel = new Label("k:");
		kLabel.setTextFill(Color.WHITE);
		kLabel.setFont(new Font(kLabel.getFont().getName(), 12.0));
		kLabel.setMinWidth(50.0);
		kLabel.setMaxWidth(50.0);
		kLabel.setAlignment(Pos.CENTER);
		
		kSlider = new SliderBar(0.0, 10.0, k);
		kSlider.setMaxWidth(Double.MAX_VALUE);
        kSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		k = (float)kSlider.getValue();
        		kValText.setText(String.format("%3.3f", k));
        	}
        });
		
        kValText = new TextField(String.format("%3.3f", k));
        kValText.setBackground(Background.fill(Color.BLACK));
        kValText.setFont(new Font(kValText.getFont().getName(), 12.0));
        kValText.setMinWidth(75.0);
        kValText.setMaxWidth(75.0);
        kValText.setAlignment(Pos.CENTER);
		kValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					k = (float)Math.max(0.0, Math.min(10.0, Float.parseFloat(kValText.getText())));
					kValText.setText(String.format("%3.3f", k));
					kSlider.setValue(k);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					kValText.setText(String.format("%3.3f", k));
					kSlider.setValue(k);
				}
			}
		});
		
		kResetButton = new ImageButton(RESET_ICON);
		kResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		kResetButton.setMinWidth(25.0);
		kResetButton.setMaxWidth(25.0);
		kResetButton.setMinHeight(25.0);
		kResetButton.setMaxHeight(25.0);
		kResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				k = kDef;
				kSlider.setValue(k);
			}
		});
		
		HBox kBox = new HBox(kLabel, kSlider, kValText, kResetButton);
		HBox.setHgrow(kSlider, Priority.ALWAYS);
		kBox.setSpacing(5.0);
		kBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value vr
		vrLabel = new Label("vr:");
		vrLabel.setTextFill(Color.WHITE);
		vrLabel.setFont(new Font(vrLabel.getFont().getName(), 12.0));
		vrLabel.setMinWidth(50.0);
		vrLabel.setMaxWidth(50.0);
		vrLabel.setAlignment(Pos.CENTER);
		
		vrSlider = new SliderBar(-100.0, 0.0, vr);
		vrSlider.setMaxWidth(Double.MAX_VALUE);
        vrSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		vr = (float)vrSlider.getValue();
        		vrValText.setText(String.format("%3.3f", vr));
        	}
        });
		
        vrValText = new TextField(String.format("%3.3f", vr));
        vrValText.setBackground(Background.fill(Color.BLACK));
        vrValText.setFont(new Font(vrValText.getFont().getName(), 12.0));
        vrValText.setMinWidth(75.0);
        vrValText.setMaxWidth(75.0);
        vrValText.setAlignment(Pos.CENTER);
		vrValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					vr = (float)Math.max(-100.0, Math.min(0.0, Float.parseFloat(vrValText.getText())));
					vrValText.setText(String.format("%3.3f", vr));
					vrSlider.setValue(vr);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					vrValText.setText(String.format("%3.3f", vr));
					vrSlider.setValue(vr);
				}
			}
		});
		
		vrResetButton = new ImageButton(RESET_ICON);
		vrResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		vrResetButton.setMinWidth(25.0);
		vrResetButton.setMaxWidth(25.0);
		vrResetButton.setMinHeight(25.0);
		vrResetButton.setMaxHeight(25.0);
		vrResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vr = vrDef;
				vrSlider.setValue(vr);
			}
		});
		
		HBox vrBox = new HBox(vrLabel, vrSlider, vrValText, vrResetButton);
		HBox.setHgrow(vrSlider, Priority.ALWAYS);
		vrBox.setSpacing(5.0);
		vrBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value vt
		vtLabel = new Label("vt:");
		vtLabel.setTextFill(Color.WHITE);
		vtLabel.setFont(new Font(vtLabel.getFont().getName(), 12.0));
		vtLabel.setMinWidth(50.0);
		vtLabel.setMaxWidth(50.0);
		vtLabel.setAlignment(Pos.CENTER);
		
		vtSlider = new SliderBar(-100.0, 0.0, vt);
		vtSlider.setMaxWidth(Double.MAX_VALUE);
        vtSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		vt = (float)vtSlider.getValue();
        		vtValText.setText(String.format("%3.3f", vt));
        	}
        });
		
        vtValText = new TextField(String.format("%3.3f", vt));
        vtValText.setBackground(Background.fill(Color.BLACK));
        vtValText.setFont(new Font(vtValText.getFont().getName(), 12.0));
        vtValText.setMinWidth(75.0);
        vtValText.setMaxWidth(75.0);
        vtValText.setAlignment(Pos.CENTER);
		vtValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					vt = (float)Math.max(-100.0, Math.min(0.0, Float.parseFloat(vtValText.getText())));
					vtValText.setText(String.format("%3.3f", vt));
					vtSlider.setValue(vt);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					vtValText.setText(String.format("%3.3f", vt));
					vtSlider.setValue(vt);
				}
			}
		});
		
		vtResetButton = new ImageButton(RESET_ICON);
		vtResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		vtResetButton.setMinWidth(25.0);
		vtResetButton.setMaxWidth(25.0);
		vtResetButton.setMinHeight(25.0);
		vtResetButton.setMaxHeight(25.0);
		vtResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vt = vtDef;
				vtSlider.setValue(vt);
			}
		});
		
		HBox vtBox = new HBox(vtLabel, vtSlider, vtValText, vtResetButton);
		HBox.setHgrow(vtSlider, Priority.ALWAYS);
		vtBox.setSpacing(5.0);
		vtBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value vp
		vpLabel = new Label("vp:");
		vpLabel.setTextFill(Color.WHITE);
		vpLabel.setFont(new Font(vpLabel.getFont().getName(), 12.0));
		vpLabel.setMinWidth(50.0);
		vpLabel.setMaxWidth(50.0);
		vpLabel.setAlignment(Pos.CENTER);
		
		vpSlider = new SliderBar(0.0, 60.0, vp);
		vpSlider.setMaxWidth(Double.MAX_VALUE);
        vpSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		vp = (float)vpSlider.getValue();
        		vpValText.setText(String.format("%3.3f", vp));
        	}
        });
		
        vpValText = new TextField(String.format("%3.3f", vp));
        vpValText.setBackground(Background.fill(Color.BLACK));
        vpValText.setFont(new Font(vpValText.getFont().getName(), 12.0));
        vpValText.setMinWidth(75.0);
        vpValText.setMaxWidth(75.0);
        vpValText.setAlignment(Pos.CENTER);
		vpValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					vp = (float)Math.max(0.0, Math.min(60.0, Float.parseFloat(vpValText.getText())));
					vpValText.setText(String.format("%3.3f", vp));
					vpSlider.setValue(vp);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					vpValText.setText(String.format("%3.3f", vp));
					vpSlider.setValue(vp);
				}
			}
		});
		
		vpResetButton = new ImageButton(RESET_ICON);
		vpResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		vpResetButton.setMinWidth(25.0);
		vpResetButton.setMaxWidth(25.0);
		vpResetButton.setMinHeight(25.0);
		vpResetButton.setMaxHeight(25.0);
		vpResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vp = vpDef;
				vpSlider.setValue(vp);
			}
		});
		
		HBox vpBox = new HBox(vpLabel, vpSlider, vpValText, vpResetButton);
		HBox.setHgrow(vpSlider, Priority.ALWAYS);
		vpBox.setSpacing(5.0);
		vpBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value cu
		cuLabel = new Label("cu:");
		cuLabel.setTextFill(Color.WHITE);
		cuLabel.setFont(new Font(cuLabel.getFont().getName(), 12.0));
		cuLabel.setMinWidth(50.0);
		cuLabel.setMaxWidth(50.0);
		cuLabel.setAlignment(Pos.CENTER);
		
		cuSlider = new SliderBar(-5.0, 20.0, b);
		cuSlider.setMaxWidth(Double.MAX_VALUE);
        cuSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		cu = (float)cuSlider.getValue();
        		cuValText.setText(String.format("%2.3f", cu));
        	}
        });
		
		cuValText = new TextField(String.format("%2.3f", cu));
		cuValText.setBackground(Background.fill(Color.BLACK));
		cuValText.setFont(new Font(cuValText.getFont().getName(), 12.0));
		cuValText.setMinWidth(75.0);
		cuValText.setMaxWidth(75.0);
		cuValText.setAlignment(Pos.CENTER);
		cuValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					cu = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(cuValText.getText())));
					cuValText.setText(String.format("%2.3f", cu));
					cuSlider.setValue(cu);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					cuValText.setText(String.format("%2.3f", cu));
					cuSlider.setValue(cu);
				}
			}
		});
		
		cuResetButton = new ImageButton(RESET_ICON);
		cuResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		cuResetButton.setMinWidth(25.0);
		cuResetButton.setMaxWidth(25.0);
		cuResetButton.setMinHeight(25.0);
		cuResetButton.setMaxHeight(25.0);
		cuResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cu = cuDef;
				cuSlider.setValue(cu);
			}
		});
		
		HBox cuBox = new HBox(cuLabel, cuSlider, cuValText, cuResetButton);
		HBox.setHgrow(cuSlider, Priority.ALWAYS);
		cuBox.setSpacing(5.0);
		cuBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value vpu
		vpuLabel = new Label("vpu:");
		vpuLabel.setTextFill(Color.WHITE);
		vpuLabel.setFont(new Font(vpuLabel.getFont().getName(), 12.0));
		vpuLabel.setMinWidth(50.0);
		vpuLabel.setMaxWidth(50.0);
		vpuLabel.setAlignment(Pos.CENTER);
		
		vpuSlider = new SliderBar(-5.0, 20.0, b);
		vpuSlider.setMaxWidth(Double.MAX_VALUE);
        vpuSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		vpu = (float)vpuSlider.getValue();
        		vpuValText.setText(String.format("%2.3f", vpu));
        	}
        });
		
		vpuValText = new TextField(String.format("%2.3f", vpu));
		vpuValText.setBackground(Background.fill(Color.BLACK));
		vpuValText.setFont(new Font(vpuValText.getFont().getName(), 12.0));
		vpuValText.setMinWidth(75.0);
		vpuValText.setMaxWidth(75.0);
		vpuValText.setAlignment(Pos.CENTER);
		vpuValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					vpu = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(vpuValText.getText())));
					vpuValText.setText(String.format("%2.3f", vpu));
					vpuSlider.setValue(vpu);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					vpuValText.setText(String.format("%2.3f", vpu));
					vpuSlider.setValue(vpu);
				}
			}
		});
		
		vpuResetButton = new ImageButton(RESET_ICON);
		vpuResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		vpuResetButton.setMinWidth(25.0);
		vpuResetButton.setMaxWidth(25.0);
		vpuResetButton.setMinHeight(25.0);
		vpuResetButton.setMaxHeight(25.0);
		vpuResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vpu = vpuDef;
				vpuSlider.setValue(vpu);
			}
		});
		
		HBox vpuBox = new HBox(vpuLabel, vpuSlider, vpuValText, vpuResetButton);
		HBox.setHgrow(vpuSlider, Priority.ALWAYS);
		vpuBox.setSpacing(5.0);
		vpuBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value umax
		umaxLabel = new Label("umax:");
		umaxLabel.setTextFill(Color.WHITE);
		umaxLabel.setFont(new Font(umaxLabel.getFont().getName(), 12.0));
		umaxLabel.setMinWidth(50.0);
		umaxLabel.setMaxWidth(50.0);
		umaxLabel.setAlignment(Pos.CENTER);
		
		umaxSlider = new SliderBar(-5.0, 20.0, b);
		umaxSlider.setMaxWidth(Double.MAX_VALUE);
        umaxSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		umax = (float)umaxSlider.getValue();
        		umaxValText.setText(String.format("%2.3f", umax));
        	}
        });
		
		umaxValText = new TextField(String.format("%2.3f", umax));
		umaxValText.setBackground(Background.fill(Color.BLACK));
		umaxValText.setFont(new Font(umaxValText.getFont().getName(), 12.0));
		umaxValText.setMinWidth(75.0);
		umaxValText.setMaxWidth(75.0);
		umaxValText.setAlignment(Pos.CENTER);
		umaxValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					umax = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(umaxValText.getText())));
					umaxValText.setText(String.format("%2.3f", umax));
					umaxSlider.setValue(umax);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					umaxValText.setText(String.format("%2.3f", umax));
					umaxSlider.setValue(umax);
				}
			}
		});
		
		umaxResetButton = new ImageButton(RESET_ICON);
		umaxResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		umaxResetButton.setMinWidth(25.0);
		umaxResetButton.setMaxWidth(25.0);
		umaxResetButton.setMinHeight(25.0);
		umaxResetButton.setMaxHeight(25.0);
		umaxResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				umax = umaxDef;
				umaxSlider.setValue(umax);
			}
		});
		
		HBox umaxBox = new HBox(umaxLabel, umaxSlider, umaxValText, umaxResetButton);
		HBox.setHgrow(umaxSlider, Priority.ALWAYS);
		umaxBox.setSpacing(5.0);
		umaxBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value upow
		upowLabel = new Label("upow:");
		upowLabel.setTextFill(Color.WHITE);
		upowLabel.setFont(new Font(upowLabel.getFont().getName(), 12.0));
		upowLabel.setMinWidth(50.0);
		upowLabel.setMaxWidth(50.0);
		upowLabel.setAlignment(Pos.CENTER);
		
		upowSlider = new SliderBar(-5.0, 20.0, b);
		upowSlider.setMaxWidth(Double.MAX_VALUE);
        upowSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		upow = (float)upowSlider.getValue();
        		upowValText.setText(String.format("%2.3f", upow));
        	}
        });
		
		upowValText = new TextField(String.format("%2.3f", upow));
		upowValText.setBackground(Background.fill(Color.BLACK));
		upowValText.setFont(new Font(upowValText.getFont().getName(), 12.0));
		upowValText.setMinWidth(75.0);
		upowValText.setMaxWidth(75.0);
		upowValText.setAlignment(Pos.CENTER);
		upowValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					upow = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(upowValText.getText())));
					upowValText.setText(String.format("%2.3f", upow));
					upowSlider.setValue(upow);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					upowValText.setText(String.format("%2.3f", upow));
					upowSlider.setValue(upow);
				}
			}
		});
		
		upowResetButton = new ImageButton(RESET_ICON);
		upowResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		upowResetButton.setMinWidth(25.0);
		upowResetButton.setMaxWidth(25.0);
		upowResetButton.setMinHeight(25.0);
		upowResetButton.setMaxHeight(25.0);
		upowResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				upow = upowDef;
				upowSlider.setValue(upow);
			}
		});
		
		HBox upowBox = new HBox(upowLabel, upowSlider, upowValText, upowResetButton);
		HBox.setHgrow(upowSlider, Priority.ALWAYS);
		upowBox.setSpacing(5.0);
		upowBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value uv
		uvLabel = new Label("uv:");
		uvLabel.setTextFill(Color.WHITE);
		uvLabel.setFont(new Font(uvLabel.getFont().getName(), 12.0));
		uvLabel.setMinWidth(50.0);
		uvLabel.setMaxWidth(50.0);
		uvLabel.setAlignment(Pos.CENTER);
		
		uvSlider = new SliderBar(-5.0, 20.0, b);
		uvSlider.setMaxWidth(Double.MAX_VALUE);
        uvSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		uv = (float)uvSlider.getValue();
        		uvValText.setText(String.format("%2.3f", uv));
        	}
        });
		
		uvValText = new TextField(String.format("%2.3f", uv));
		uvValText.setBackground(Background.fill(Color.BLACK));
		uvValText.setFont(new Font(uvValText.getFont().getName(), 12.0));
		uvValText.setMinWidth(75.0);
		uvValText.setMaxWidth(75.0);
		uvValText.setAlignment(Pos.CENTER);
		uvValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					uv = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(uvValText.getText())));
					uvValText.setText(String.format("%2.3f", uv));
					uvSlider.setValue(uv);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					uvValText.setText(String.format("%2.3f", uv));
					uvSlider.setValue(uv);
				}
			}
		});
		
		uvResetButton = new ImageButton(RESET_ICON);
		uvResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		uvResetButton.setMinWidth(25.0);
		uvResetButton.setMaxWidth(25.0);
		uvResetButton.setMinHeight(25.0);
		uvResetButton.setMaxHeight(25.0);
		uvResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				uv = uvDef;
				uvSlider.setValue(uv);
			}
		});
		
		HBox uvBox = new HBox(uvLabel, uvSlider, uvValText, uvResetButton);
		HBox.setHgrow(uvSlider, Priority.ALWAYS);
		uvBox.setSpacing(5.0);
		uvBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value uvmin
		uvminLabel = new Label("uvmin:");
		uvminLabel.setTextFill(Color.WHITE);
		uvminLabel.setFont(new Font(uvminLabel.getFont().getName(), 12.0));
		uvminLabel.setMinWidth(50.0);
		uvminLabel.setMaxWidth(50.0);
		uvminLabel.setAlignment(Pos.CENTER);
		
		uvminSlider = new SliderBar(-5.0, 20.0, uvmin);
		uvminSlider.setMaxWidth(Double.MAX_VALUE);
        uvminSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		uvmin = (float)uvminSlider.getValue();
        		uvminValText.setText(String.format("%2.3f", uvmin));
        	}
        });
		
		uvminValText = new TextField(String.format("%2.3f", uvmin));
		uvminValText.setBackground(Background.fill(Color.BLACK));
		uvminValText.setFont(new Font(uvminValText.getFont().getName(), 12.0));
		uvminValText.setMinWidth(75.0);
		uvminValText.setMaxWidth(75.0);
		uvminValText.setAlignment(Pos.CENTER);
		uvminValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					uvmin = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(uvminValText.getText())));
					uvminValText.setText(String.format("%2.3f", uvmin));
					uvminSlider.setValue(uvmin);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					uvminValText.setText(String.format("%2.3f", uvmin));
					uvminSlider.setValue(uvmin);
				}
			}
		});
		
		uvminResetButton = new ImageButton(RESET_ICON);
		uvminResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		uvminResetButton.setMinWidth(25.0);
		uvminResetButton.setMaxWidth(25.0);
		uvminResetButton.setMinHeight(25.0);
		uvminResetButton.setMaxHeight(25.0);
		uvminResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				uvmin = uvminDef;
				uvminSlider.setValue(uvmin);
			}
		});
		
		HBox uvminBox = new HBox(uvminLabel, uvminSlider, uvminValText, uvminResetButton);
		HBox.setHgrow(uvminSlider, Priority.ALWAYS);
		uvminBox.setSpacing(5.0);
		uvminBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value gc
		gcLabel = new Label("gc:");
		gcLabel.setTextFill(Color.WHITE);
		gcLabel.setFont(new Font(gcLabel.getFont().getName(), 12.0));
		gcLabel.setMinWidth(50.0);
		gcLabel.setMaxWidth(50.0);
		gcLabel.setAlignment(Pos.CENTER);
		
		gcSlider = new SliderBar(-5.0, 20.0, gc);
		gcSlider.setMaxWidth(Double.MAX_VALUE);
        gcSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		gc = (float)gcSlider.getValue();
        		gcValText.setText(String.format("%2.3f", gc));
        	}
        });
		
		gcValText = new TextField(String.format("%2.3f", gc));
		gcValText.setBackground(Background.fill(Color.BLACK));
		gcValText.setFont(new Font(gcValText.getFont().getName(), 12.0));
		gcValText.setMinWidth(75.0);
		gcValText.setMaxWidth(75.0);
		gcValText.setAlignment(Pos.CENTER);
		gcValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					gc = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(gcValText.getText())));
					gcValText.setText(String.format("%2.3f", gc));
					gcSlider.setValue(gc);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					gcValText.setText(String.format("%2.3f", gc));
					gcSlider.setValue(gc);
				}
			}
		});
		
		gcResetButton = new ImageButton(RESET_ICON);
		gcResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		gcResetButton.setMinWidth(25.0);
		gcResetButton.setMaxWidth(25.0);
		gcResetButton.setMinHeight(25.0);
		gcResetButton.setMaxHeight(25.0);
		gcResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gc = gcDef;
				gcSlider.setValue(gc);
			}
		});
		
		HBox gcBox = new HBox(gcLabel, gcSlider, gcValText, gcResetButton);
		HBox.setHgrow(gcSlider, Priority.ALWAYS);
		gcBox.setSpacing(5.0);
		gcBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value gp
		gpLabel = new Label("gp:");
		gpLabel.setTextFill(Color.WHITE);
		gpLabel.setFont(new Font(gpLabel.getFont().getName(), 12.0));
		gpLabel.setMinWidth(50.0);
		gpLabel.setMaxWidth(50.0);
		gpLabel.setAlignment(Pos.CENTER);
		
		gpSlider = new SliderBar(-5.0, 20.0, gp);
		gpSlider.setMaxWidth(Double.MAX_VALUE);
        gpSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		gp = (float)gpSlider.getValue();
        		gpValText.setText(String.format("%2.3f", gp));
        	}
        });
		
		gpValText = new TextField(String.format("%2.3f", gp));
		gpValText.setBackground(Background.fill(Color.BLACK));
		gpValText.setFont(new Font(gpValText.getFont().getName(), 12.0));
		gpValText.setMinWidth(75.0);
		gpValText.setMaxWidth(75.0);
		gpValText.setAlignment(Pos.CENTER);
		gpValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					gp = (float)Math.max(-5.0, Math.min(20.0, Float.parseFloat(gpValText.getText())));
					gpValText.setText(String.format("%2.3f", gp));
					gpSlider.setValue(gp);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					gpValText.setText(String.format("%2.3f", gp));
					gpSlider.setValue(gp);
				}
			}
		});
		
		gpResetButton = new ImageButton(RESET_ICON);
		gpResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		gpResetButton.setMinWidth(25.0);
		gpResetButton.setMaxWidth(25.0);
		gpResetButton.setMinHeight(25.0);
		gpResetButton.setMaxHeight(25.0);
		gpResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gp = gpDef;
				gpSlider.setValue(gp);
			}
		});
		
		HBox gpBox = new HBox(gpLabel, gpSlider, gpValText, gpResetButton);
		HBox.setHgrow(gpSlider, Priority.ALWAYS);
		gpBox.setSpacing(5.0);
		gpBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create input current label
		inputLabel = new Label("Input Currents");
		inputLabel.setTextFill(Color.WHITE);
		inputLabel.setFont(new Font(inputLabel.getFont().getName(), 16.0));
		inputLabel.setBackground(Background.EMPTY);
		inputLabel.setMinHeight(30.0);
		inputLabel.setMaxHeight(30.0);
		inputLabel.setAlignment(Pos.CENTER);
		
		// Create slider and controls for value I
		ILabel = new Label("I:");
		ILabel.setTextFill(Color.WHITE);
		ILabel.setFont(new Font(ILabel.getFont().getName(), 12.0));
		ILabel.setMinWidth(50.0);
		ILabel.setMaxWidth(50.0);
		ILabel.setAlignment(Pos.CENTER);
		
		ISlider = new SliderBar(-100.0, 100.0, I);
		ISlider.setMaxWidth(Double.MAX_VALUE);
        ISlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		I = (float)ISlider.getValue();
        		IValText.setText(String.format("%4.3f", I));
        	}
        });
		
        IValText = new TextField(String.format("%4.3f", I));
        IValText.setBackground(Background.fill(Color.BLACK));
        IValText.setFont(new Font(IValText.getFont().getName(), 12.0));
		IValText.setMinWidth(75.0);
		IValText.setMaxWidth(75.0);
		IValText.setAlignment(Pos.CENTER);
		IValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					I = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(IValText.getText())));
					IValText.setText(String.format("%4.3f", I));
					ISlider.setValue(I);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					IValText.setText(String.format("%4.3f", I));
					ISlider.setValue(I);
				}
			}
		});
		
		IIncButton = new ImageButton(PLUS_ICON);
		IIncButton.setTooltip(new Tooltip("Increment Value"));
		IIncButton.setMinWidth(25.0);
		IIncButton.setMaxWidth(25.0);
		IIncButton.setMinHeight(25.0);
		IIncButton.setMaxHeight(25.0);
		IIncButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				I += incDec;
				ISlider.setValue(I);
			}
		});
		
		IDecButton = new ImageButton(MINUS_ICON);
		IDecButton.setTooltip(new Tooltip("Decrement Value"));
		IDecButton.setMinWidth(25.0);
		IDecButton.setMaxWidth(25.0);
		IDecButton.setMinHeight(25.0);
		IDecButton.setMaxHeight(25.0);
		IDecButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				I -= incDec;
				ISlider.setValue(I);
			}
		});
		
		IPulseButton = new ImageButton(PULSE_ICON);
		IPulseButton.setTooltip(new Tooltip("Single Pulse Value"));
		IPulseButton.setMinWidth(25.0);
		IPulseButton.setMaxWidth(25.0);
		IPulseButton.setMinHeight(25.0);
		IPulseButton.setMaxHeight(25.0);
		IPulseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				I = pulse;
				ISlider.setValue(I);
			}
		});
		
		IResetButton = new ImageButton(RESET_ICON);
		IResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		IResetButton.setMinWidth(25.0);
		IResetButton.setMaxWidth(25.0);
		IResetButton.setMinHeight(25.0);
		IResetButton.setMaxHeight(25.0);
		IResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				I = 0.0f;
				ISlider.setValue(I);
			}
		});
		
		HBox IBox = new HBox(ILabel, ISlider, IValText, IIncButton, IDecButton, IPulseButton, IResetButton);
		HBox.setHgrow(ISlider, Priority.ALWAYS);
		IBox.setSpacing(5.0);
		IBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value vgc
		vgcLabel = new Label("vgc:");
		vgcLabel.setTextFill(Color.WHITE);
		vgcLabel.setFont(new Font(vgcLabel.getFont().getName(), 12.0));
		vgcLabel.setMinWidth(50.0);
		vgcLabel.setMaxWidth(50.0);
		vgcLabel.setAlignment(Pos.CENTER);
		
		vgcSlider = new SliderBar(-100.0, 100.0, vgc);
		vgcSlider.setMaxWidth(Double.MAX_VALUE);
        vgcSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		vgc = (float)vgcSlider.getValue();
        		vgcValText.setText(String.format("%4.3f", vgc));
        	}
        });
		
        vgcValText = new TextField(String.format("%4.3f", vgc));
        vgcValText.setBackground(Background.fill(Color.BLACK));
        vgcValText.setFont(new Font(vgcValText.getFont().getName(), 12.0));
		vgcValText.setMinWidth(75.0);
		vgcValText.setMaxWidth(75.0);
		vgcValText.setAlignment(Pos.CENTER);
		vgcValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					vgc = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(vgcValText.getText())));
					vgcValText.setText(String.format("%4.3f", vgc));
					vgcSlider.setValue(I);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					vgcValText.setText(String.format("%4.3f", vgc));
					vgcSlider.setValue(I);
				}
			}
		});
		
		vgcIncButton = new ImageButton(PLUS_ICON);
		vgcIncButton.setTooltip(new Tooltip("Increment Value"));
		vgcIncButton.setMinWidth(25.0);
		vgcIncButton.setMaxWidth(25.0);
		vgcIncButton.setMinHeight(25.0);
		vgcIncButton.setMaxHeight(25.0);
		vgcIncButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgc += incDec;
				vgcSlider.setValue(vgc);
			}
		});
		
		vgcDecButton = new ImageButton(MINUS_ICON);
		vgcDecButton.setTooltip(new Tooltip("Decrement Value"));
		vgcDecButton.setMinWidth(25.0);
		vgcDecButton.setMaxWidth(25.0);
		vgcDecButton.setMinHeight(25.0);
		vgcDecButton.setMaxHeight(25.0);
		vgcDecButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgc -= incDec;
				vgcSlider.setValue(vgc);
			}
		});
		
		vgcPulseButton = new ImageButton(PULSE_ICON);
		vgcPulseButton.setTooltip(new Tooltip("Single Pulse Value"));
		vgcPulseButton.setMinWidth(25.0);
		vgcPulseButton.setMaxWidth(25.0);
		vgcPulseButton.setMinHeight(25.0);
		vgcPulseButton.setMaxHeight(25.0);
		vgcPulseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgc = pulse;
				vgcSlider.setValue(vgc);
			}
		});
		
		vgcResetButton = new ImageButton(RESET_ICON);
		vgcResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		vgcResetButton.setMinWidth(25.0);
		vgcResetButton.setMaxWidth(25.0);
		vgcResetButton.setMinHeight(25.0);
		vgcResetButton.setMaxHeight(25.0);
		vgcResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgc = 0.0f;
				vgcSlider.setValue(vgc);
			}
		});
		
		HBox vgcBox = new HBox(vgcLabel, vgcSlider, vgcValText, vgcIncButton, vgcDecButton, vgcPulseButton, vgcResetButton);
		HBox.setHgrow(vgcSlider, Priority.ALWAYS);
		vgcBox.setSpacing(5.0);
		vgcBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create slider and controls for value vgp
		vgpLabel = new Label("vgp:");
		vgpLabel.setTextFill(Color.WHITE);
		vgpLabel.setFont(new Font(vgpLabel.getFont().getName(), 12.0));
		vgpLabel.setMinWidth(50.0);
		vgpLabel.setMaxWidth(50.0);
		vgpLabel.setAlignment(Pos.CENTER);
		
		vgpSlider = new SliderBar(-100.0, 100.0, vgp);
		vgpSlider.setMaxWidth(Double.MAX_VALUE);
        vgpSlider.progressProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		vgp = (float)vgpSlider.getValue();
        		vgpValText.setText(String.format("%4.3f", vgp));
        	}
        });
		
        vgpValText = new TextField(String.format("%4.3f", vgp));
        vgpValText.setBackground(Background.fill(Color.BLACK));
        vgpValText.setFont(new Font(vgpValText.getFont().getName(), 12.0));
		vgpValText.setMinWidth(75.0);
		vgpValText.setMaxWidth(75.0);
		vgpValText.setAlignment(Pos.CENTER);
		vgpValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					vgp = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(vgpValText.getText())));
					vgpValText.setText(String.format("%4.3f", vgp));
					vgpSlider.setValue(I);
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					vgpValText.setText(String.format("%4.3f", vgp));
					vgpSlider.setValue(I);
				}
			}
		});
		
		vgpIncButton = new ImageButton(PLUS_ICON);
		vgpIncButton.setTooltip(new Tooltip("Increment Value"));
		vgpIncButton.setMinWidth(25.0);
		vgpIncButton.setMaxWidth(25.0);
		vgpIncButton.setMinHeight(25.0);
		vgpIncButton.setMaxHeight(25.0);
		vgpIncButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgp += incDec;
				vgpSlider.setValue(vgp);
			}
		});
		
		vgpDecButton = new ImageButton(MINUS_ICON);
		vgpDecButton.setTooltip(new Tooltip("Decrement Value"));
		vgpDecButton.setMinWidth(25.0);
		vgpDecButton.setMaxWidth(25.0);
		vgpDecButton.setMinHeight(25.0);
		vgpDecButton.setMaxHeight(25.0);
		vgpDecButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgp -= incDec;
				vgpSlider.setValue(vgp);
			}
		});
		
		vgpPulseButton = new ImageButton(PULSE_ICON);
		vgpPulseButton.setTooltip(new Tooltip("Single Pulse Value"));
		vgpPulseButton.setMinWidth(25.0);
		vgpPulseButton.setMaxWidth(25.0);
		vgpPulseButton.setMinHeight(25.0);
		vgpPulseButton.setMaxHeight(25.0);
		vgpPulseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgp = pulse;
				vgpSlider.setValue(vgp);
			}
		});
		
		vgpResetButton = new ImageButton(RESET_ICON);
		vgpResetButton.setTooltip(new Tooltip("Reset To Default Value"));
		vgpResetButton.setMinWidth(25.0);
		vgpResetButton.setMaxWidth(25.0);
		vgpResetButton.setMinHeight(25.0);
		vgpResetButton.setMaxHeight(25.0);
		vgpResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vgp = 0.0f;
				vgpSlider.setValue(vgp);
			}
		});
		
		HBox vgpBox = new HBox(vgpLabel, vgpSlider, vgpValText, vgpIncButton, vgpDecButton, vgpPulseButton, vgpResetButton);
		HBox.setHgrow(vgpSlider, Priority.ALWAYS);
		vgpBox.setSpacing(5.0);
		vgpBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
		
		// Create increment/decrement and pulse value controls
		incDecLabel = new Label("Increment/Decrement:");
		incDecLabel.setTextFill(Color.WHITE);
		incDecLabel.setFont(new Font(incDecLabel.getFont().getName(), 12.0));
		incDecLabel.setAlignment(Pos.CENTER);
		
        incDecValText = new TextField(String.format("%4.3f", incDec));
        incDecValText.setBackground(Background.fill(Color.BLACK));
        incDecValText.setFont(new Font(incDecValText.getFont().getName(), 12.0));
        incDecValText.setMinWidth(75.0);
        incDecValText.setMaxWidth(75.0);
        incDecValText.setAlignment(Pos.CENTER);
        incDecValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					incDec = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(incDecValText.getText())));
					incDecValText.setText(String.format("%4.3f", incDec));
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					incDecValText.setText(String.format("%4.3f", incDec));
				}
			}
		});
        
		pulseLabel = new Label("Pulse:");
		pulseLabel.setTextFill(Color.WHITE);
		pulseLabel.setFont(new Font(pulseLabel.getFont().getName(), 12.0));
		pulseLabel.setAlignment(Pos.CENTER);
		
        pulseValText = new TextField(String.format("%4.3f", pulse));
        pulseValText.setBackground(Background.fill(Color.BLACK));
        pulseValText.setFont(new Font(pulseValText.getFont().getName(), 12.0));
        pulseValText.setMinWidth(75.0);
        pulseValText.setMaxWidth(75.0);
        pulseValText.setAlignment(Pos.CENTER);
        pulseValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					pulse = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(pulseValText.getText())));
					pulseValText.setText(String.format("%4.3f", pulse));
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					pulseValText.setText(String.format("%4.3f", pulse));
				}
			}
		});
        
        HBox idpBox = new HBox(incDecLabel, incDecValText, pulseLabel, pulseValText);
        idpBox.setSpacing(5.0);
        idpBox.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
        idpBox.setAlignment(Pos.CENTER);
		
		// Create phase portrait graph
		pxAxis = new NumberAxis(-100.0f, 60.0f, 10.0f);
		pxAxis.setLabel("Membrane Potential (v)");
		pyAxis = new NumberAxis(-50.0f, 100.0f, 10.0f);
		pyAxis.setLabel("Membrane Recovery (u)");
		pGraph = new LineChart<Number, Number>(pxAxis, pyAxis);
		pcSeries = new XYChart.Series<Number, Number>();
		pcSeries.setName("membrane reset");
		pcSeries.getData().add(new XYChart.Data<Number, Number>(c, 200.0f));
		pcSeries.getData().add(new XYChart.Data<Number, Number>(c, -200.0f));
		puNullSeries = new XYChart.Series<Number, Number>();
		puNullSeries.setName("u nullcline");
		pvNullSeries = new XYChart.Series<Number, Number>();
		pvNullSeries.setName("v nullcline");
		pvuSeries = new XYChart.Series<Number, Number>();
		pvuSeries.setName("v/u trace");
		pGraph.setTitle("Phase Portrait");
		pGraph.setHorizontalGridLinesVisible(true);
		pGraph.setVerticalGridLinesVisible(true);
		pGraph.setCreateSymbols(false);
		pGraph.setAnimated(false);
		pGraph.getData().add(pcSeries);
		pGraph.getData().add(pvNullSeries);
		pGraph.getData().add(puNullSeries);
		pGraph.getData().add(pvuSeries);
		
		// Create membrane recovery graph
		uxAxis = new NumberAxis(0.0, 1000.0, 100.0);
		uxAxis.setLabel("Time");
		uyAxis = new NumberAxis(-50.0f, 100.0f, 10.0f);
		uyAxis.setLabel("Membrane Recovery (u)");
		uGraph = new LineChart<Number, Number>(uxAxis, uyAxis);
		uSeries = new XYChart.Series<Number, Number>();
		uSeries.setName("Membrane Recovery (u)");
		uHistSeries = new XYChart.Series<Number, Number>();
		uHistSeries.setName("Membrane Recovery History (u)");
		uGraph.setTitle("Membrane Recovery");
		uGraph.setId("ugraph");
		uGraph.setHorizontalGridLinesVisible(true);
		uGraph.setVerticalGridLinesVisible(true);
		uGraph.setCreateSymbols(false);
		uGraph.setAnimated(false);
		uGraph.getData().add(uSeries);
		uGraph.getData().add(uHistSeries);
		
		// Create membrane potential graph
		vxAxis = new NumberAxis(0.0, 1000.0, 100.0);
		vxAxis.setLabel("Time");
		vyAxis = new NumberAxis(-100.0f, 40.0f, 10.0f);
		vyAxis.setLabel("Membrane Potential (v)");
		vGraph = new LineChart<Number, Number>(vxAxis, vyAxis);
		vSeries = new XYChart.Series<Number, Number>();
		vSeries.setName("Membrane Potential (v)");
		vHistSeries = new XYChart.Series<Number, Number>();
		vHistSeries.setName("Membrane Potential History (v)");
		vGraph.setTitle("Membrane Potential");
		vGraph.setId("vgraph");
		vGraph.setHorizontalGridLinesVisible(true);
		vGraph.setVerticalGridLinesVisible(true);
		vGraph.setCreateSymbols(false);
		vGraph.setAnimated(false);
		vGraph.getData().add(vSeries);
		vGraph.getData().add(vHistSeries);
		
		// Create input current graph
		IxAxis = new NumberAxis(0.0, 1000.0, 100.0);
		IxAxis.setLabel("Time");
		IyAxis = new NumberAxis(-40.0f, 40.0f, 10.0f);
		IyAxis.setLabel("Input Current (I)");
		IGraph = new LineChart<Number, Number>(IxAxis, IyAxis);
		ISeries = new XYChart.Series<Number, Number>();
		ISeries.setName("Input Current (I)");
		IHistSeries = new XYChart.Series<Number, Number>();
		IHistSeries.setName("Input Current History (I)");
		IGraph.setTitle("Input Current");
		IGraph.setId("Igraph");
		IGraph.setHorizontalGridLinesVisible(true);
		IGraph.setVerticalGridLinesVisible(true);
		IGraph.setCreateSymbols(false);
		IGraph.setAnimated(false);
		IGraph.getData().add(ISeries);
		IGraph.getData().add(IHistSeries);
		
		// Create neural node input current graph
		gxAxis = new NumberAxis(0.0, 1000.0, 100.0);
		gxAxis.setLabel("Time");
		gyAxis = new NumberAxis(-40.0f, 40.0f, 10.0f);
		gyAxis.setLabel("Input Current (Igc/Igp)");
		gGraph = new LineChart<Number, Number>(gxAxis, gyAxis);
		gcSeries = new XYChart.Series<Number, Number>();
		gcSeries.setName("Child Input Current (Igc)");
		gpSeries = new XYChart.Series<Number, Number>();
		gpSeries.setName("Parent Input Current (Igc)");
		gcHistSeries = new XYChart.Series<Number, Number>();
		gcHistSeries.setName("Child Input Current History (Igc)");
		gpHistSeries = new XYChart.Series<Number, Number>();
		gpHistSeries.setName("Parent Input Current History (Igp)");
		gGraph.setTitle("Neural Node Input Current");
		gGraph.setId("ggraph");
		gGraph.setHorizontalGridLinesVisible(true);
		gGraph.setVerticalGridLinesVisible(true);
		gGraph.setCreateSymbols(false);
		gGraph.setAnimated(false);
		gGraph.getData().add(gcSeries);
		gGraph.getData().add(gpSeries);
		gGraph.getData().add(gcHistSeries);
		gGraph.getData().add(gpHistSeries);
		
		/*
		// Create new/save controls
		newModelButton = new Button("New Model");
		newModelButton.setId("pinkbutton");
		newModelButton.setMinWidth(150.0);
		newModelButton.setMaxHeight(30.0);
		newModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
                // Request new model name from user
                TextInputDialog newDialog = new TextInputDialog("New Model");
                newDialog.setTitle("New Model");
                newDialog.setHeaderText("Please enter a name for the new model.");
                newDialog.setContentText("Name: ");
                Button okButton = (Button)newDialog.getDialogPane().lookupButton(ButtonType.OK);
                TextField nameField = newDialog.getEditor();
                BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> {
                	return nameField.getText().isBlank() || nameField.getText().isEmpty();
                }, nameField.textProperty());
                okButton.disableProperty().bind(isInvalid);
                Optional<String> name = newDialog.showAndWait();
                
                // If a new name is provided, add it to the list and select it
                name.ifPresent(n -> {
                	modelNames.add(n);
                	modelList.setItems(modelNames);
                	
                	// Set new model's value to default values
                	aList.add(DEF_A);
                	bList.add(DEF_B);
                	cList.add(DEF_C);
                	dList.add(DEF_D);
                	IList.add(0.0f);
                	aDef = DEF_A;
                	bDef = DEF_B;
                	cDef = DEF_C;
                	dDef = DEF_D;
                	IDef = 0.0f;
                	a = aDef;
                	b = bDef;
                	c = cDef;
                	d = dDef;
                	I = IDef;
                	
                	// Select new model
                	modelList.getSelectionModel().selectLast();
                	
    				// Stop thread
    				started = false;
    				
    				// Stop ramping
    				ramping = false;
    				
    				// Update graphs
    				updatePGraph();
    				updateUGraph();
    				updateVGraph();
    				
    				// Enable buttons
    				rampButton.setDisable(false);
    				stepButton.setDisable(false);
    				startButton.setDisable(false);
    				startButton.setText("Start");
    				
    				// Update controls
    				aSlider.setValue(a);
    				bSlider.setValue(b);
    				cSlider.setValue(c);
    				dSlider.setValue(d);
    				ISlider.setValue(I);
    				saveHistoryButton.setDisable(true);
    				saveLogButton.setDisable(true);
    				timeValLabel.setText(String.format("%4.1f", t));
    				outputLog = "";
    				outputArea.setText(outputLog);
                });
			}
		});
		
		saveModelButton = new Button("Save Model");
		saveModelButton.setId("pinkbutton");
		saveModelButton.setMinWidth(150.0);
		saveModelButton.setMaxHeight(30.0);
		saveModelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Update values for model at selected index
				aList.set(selectedIndex, a);
				bList.set(selectedIndex, b);
				cList.set(selectedIndex, c);
				dList.set(selectedIndex, d);
				IList.set(selectedIndex, I);
				aDef = a;
				bDef = b;
				cDef = c;
				dDef = d;
				IDef = I;
				
				// Inform user that model is saved
				App.alertInfo("Saved successfully.", "Successfully saved " + modelNames.get(selectedIndex) + " data!");
			}
		});
		*/
		
		// Create output log
		outputLabel = new Label("Output Log");
		outputLabel.setTextFill(Color.WHITE);
		outputLabel.setFont(new Font(outputLabel.getFont().getName(), 16.0));
		outputLabel.setBackground(Background.EMPTY);
		outputLabel.setMinHeight(30.0);
		outputLabel.setMaxHeight(30.0);
		outputLabel.setAlignment(Pos.CENTER);
		
		outputArea = new TextArea();
		outputArea.setFont(new Font("Courier New", 11.0));
		outputArea.setEditable(false);
		outputArea.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				outputArea.setScrollTop(Double.MAX_VALUE);
			}
		});
		
		outputLog = "";
		
		// Add components
		modelBox = new VBox();
		modelBox.getChildren().addAll(
				modelLabel,
				modelTypeBox,
				modelNameBox,
				aBox,
				bBox,
				b2Box,
				baBox,
				bvBox,
				cBox,
				dBox,
				CBox,
				kBox,
				vpBox,
				vrBox,
				vtBox,
				cuBox,
				vpuBox,
				umaxBox,
				upowBox,
				uvBox,
				uvminBox,
				gcBox,
				gpBox,
				inputLabel,
				IBox,
				vgcBox,
				vgpBox,
				idpBox);
		modelBox.setPadding(new Insets(0.0, 5.0, 0.0, 5.0));
		modelBox.setMinWidth(App.DEF_WORKSPACE_WIDTH * 0.2);
		modelBox.setMaxWidth(App.DEF_WORKSPACE_WIDTH * 0.2);
		
		VBox vuGraphBox = new VBox();
		vuGraphBox.getChildren().addAll(vGraph, uGraph);
		
		HBox graphBox = new HBox();
		graphBox.getChildren().addAll(vuGraphBox, pGraph);
		
		HBox igGraphBox = new HBox();
		igGraphBox.getChildren().addAll(IGraph, gGraph);
		
		VBox centerBox = new VBox();
		centerBox.getChildren().addAll(graphBox, igGraphBox);
		
		outputBox = new VBox();
		outputBox.getChildren().addAll(outputLabel, outputArea);
		outputBox.setPadding(new Insets(0.0, 5.0, 0.0, 5.0));
		VBox.setVgrow(outputArea, Priority.ALWAYS);
		
		setLeft(modelBox);
		setCenter(centerBox);
		setRight(outputBox);
		
		// Select the first model
		modelTypeList.getSelectionModel().select(0);
		modelNameList.getSelectionModel().select(0);
	}
	
	/**
	 * Returns the value of a.
	 * @return a.
	 */
	public float a() { return a; }
	
	/**
	 * Adds the specified NeuralModelLabListener to the list of listeners. If the specified listener is null, it is not added.
	 * @param listener - The NeuralModelLabListener to add.
	 */
	public void addListener(NeuralModelLabListener listener) {
		if (listener != null)
			this.listener.add(listener);
	}
	
	/**
	 * Returns the value of b.
	 * @return b.
	 */
	public float b() { return b; }
	
	/**
	 * Returns the value of c.
	 * @return c.
	 */
	public float c() { return c; }
	
	/**
	 * Returns the value of d.
	 * @return d.
	 */
	public float d() { return d; }
	
	/**
	 * Returns the value of I.
	 * @return I.
	 */
	public float I() { return I; }

	@Override
	public void itemSelected(TopToolBarEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Returns the value of k.
	 * @return k.
	 */
	public float k() { return k; }
	
	/**
	 * Returns the value of C.
	 * @return C.
	 */
	public float C() { return C; }
	
	/**
	 * Launches the new dendrite model wizard to create a new dendrite neural model.
	 */
	public void newDendriteModel() {
		NeuralModelCreator nmcWnd = new NeuralModelCreator(DENDRITE_TYPE);
	}
	
	/**
	 * Launches the new soma model wizard to create a new soma neural model.
	 */
	public void newSomaModel() {
		//NeuralModelCreator nmcWnd = new NeuralModelCreator(SOMA_TYPE);
		new ErrorDialog("Test Error", "Testing the error dialog!");
	}
	
	/**
	 * Launches the new spiking model wizard to create a new spiking neural model.
	 */
	public void newSpikingModel() {
		//NeuralModelCreator nmcWnd = new NeuralModelCreator(SPIKING_TYPE);
		new WarningDialog("Test Warning", "Testing the warning dialog!");
	}
	
	/**
	 * Pauses the execution of the processing thread.
	 */
	public void pause() {
		// Stop the processing thread
		started = false;
		
		try {
			processThread.join();
			processThread = null;
		} catch (InterruptedException e) {
			App.alertError("Thread Interrupted", "Neural model processing thread was interrupted.");
		}
	}
	
	/**
	 * Processes a single processing cycle of the Izhikevich neuron.
	 */
	public void process() {
		if (modelType == SPIKING_TYPE) {
			v += 0.5f * (0.04f * (v * v) + 5.0f * v + 140.0 - u + I);
			v += 0.5f * (0.04f * (v * v) + 5.0f * v + 140.0 - u + I);
			u += a * (b * v - u);
		}
		else {
			v += (k * (v - vr) * (v - vt) - u + I) / C;
			u += a * (b * (v - vr) - u);
		}
		
		IArr[(int)t] = I;
		vgcArr[(int)t] = vgc;
		vgpArr[(int)t] = vgp;
		uArr[(int)t] = (v >= vp) ? u + d : u;
		vArr[(int)t] = (v >= vp) ? vp : v;
		
		if (v >= vp) {
			v = c;
			u += d;
			spike = true;
		}
		else {
			spike = false;
		}
		
		t += 1.0f;
	}
	
	/**
	 * Removes the specified NeuralModelLabListener from the list of listeners.
	 * @param listener - The NeuralModelLabListener to remove.
	 */
	public void removeListener(NeuralModelLabListener listener) {
		this.listener.remove(listener);
	}
	
	/**
	 * Removes the current neural model from the list of neural models.
	 */
	public void removeModel() {
		
	}
	
	/**
	 * Resets the time and neuron model variables to their original starting values and stops the processing thread.
	 */
	public void reset() {
		// Stop thread
		started = false;
		
		// Reset values
		a = aDef;
		b = bDef;
		c = cDef;
		d = dDef;
		I = 0.0f;
		C = CDef;
		k = kDef;
		vp = vpDef;
		vr = vrDef;
		vt = vtDef;
		cu = cuDef;
		vpu = vpuDef;
		umax = umaxDef;
		upow = upowDef;
		uv = uvDef;
		uvmin = uvminDef;
		gc = gcDef;
		gp = gpDef;
		t = 0.0f;
		u = uinit;
		v = vinit;
		vgc = 0.0f;
		vgp = 0.0f;
		
		for (int i = 0; i < TIMESPAN; ++i) {
			IArr[i] = I;
			vgcArr[i] = vgc;
			vgpArr[i] = vgp;
			uArr[i] = u;
			vArr[i] = c;
		}
		
		// Reset graphs
		vSeries.getData().clear();
		uSeries.getData().clear();
		pvuSeries.getData().clear();
		ISeries.getData().clear();
		gcSeries.getData().clear();
		gpSeries.getData().clear();
		
		if (!hasHistory) {
			vHistSeries.getData().clear();
			uHistSeries.getData().clear();
			IHistSeries.getData().clear();
			gcHistSeries.getData().clear();
			gpHistSeries.getData().clear();
		}
		else {
			for (int i = 0; i < TIMESPAN; ++i) {
				vHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, vHistArr[i]));
				uHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, uHistArr[i]));
				IHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, IHistArr[i]));
				gcHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, vgcHistArr[i]));
				gpHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, vgpHistArr[i]));
			}
		}
		
		vGraph.getData().set(1, vHistSeries);
		uGraph.getData().set(1, uHistSeries);
		IGraph.getData().set(1, IHistSeries);
		gGraph.getData().set(2, gcHistSeries);
		gGraph.getData().set(3, gpHistSeries);
		
		// Update controls
		aSlider.setValue(a);
		bSlider.setValue(b);
		cSlider.setValue(c);
		dSlider.setValue(d);
		ISlider.setValue(I);
		CSlider.setValue(C);
		kSlider.setValue(k);
		vpSlider.setValue(vp);
		vrSlider.setValue(vr);
		vtSlider.setValue(vt);
		cuSlider.setValue(cu);
		vpuSlider.setValue(vpu);
		umaxSlider.setValue(umax);
		upowSlider.setValue(upow);
		uvSlider.setValue(uv);
		uvminSlider.setValue(uvmin);
		gcSlider.setValue(gc);
		gpSlider.setValue(gp);
		outputLog = "";
		outputArea.setText(outputLog);
	}
	
	@Override
	public void run() {
		// While started
		while (started && t < TIMESPAN) {
			// Process neuron
			process();
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (t > 0.0) {
						// Update spike count
						if (spike) spikes += 1.0f;
						
						// Update graphs
						updateGraphs();
						
						// Update controls
						outputLog += String.format("t: %6s - v: %7s; u: %7s; I: %8s", 
								String.format("%6.1f", t), 
								String.format("%7.3f", vArr[(int)(t - 1.0f)]), 
								String.format("%7.3f", uArr[(int)(t - 1.0f)]),
								String.format("%8.3f", IArr[(int)(t - 1.0f)])) +
								((vArr[(int)(t - 1.0f)] >= vp) ? "*\n" : "\n");
						outputArea.setText(outputLog);
						outputArea.setScrollTop(Double.MAX_VALUE);
					}
				}
			});
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Reset started
		started = false;
		
		// Disable buttons when timespan is reached
		if (t == TIMESPAN) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Log spike count
					outputLog += String.format("Spikes: %6.1f\n", spikes);
					outputArea.setText(outputLog);
					outputArea.setScrollTop(Double.MAX_VALUE);
					
					hasHistory = false;
					sendEvent(Action.TIMESPAN_REACHED);
				}
			});
		}
	}
	
	/**
	 * Saves the history of the current neural model.
	 */
	public void saveHistory() {
		// Set history flag
		hasHistory = true;
		
		// Store past values
		for (int i = 0; i < TIMESPAN; ++i) {
			IHistArr[i] = IArr[i];
			vgcHistArr[i] = vgcArr[i];
			vgpHistArr[i] = vgpArr[i];
			uHistArr[i] = uArr[i];
			vHistArr[i] = vArr[i];
		}
	}
	
	/**
	 * Saves the output log of the current neural model.
	 */
	public void saveOutputLog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("models"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		File file = fileChooser.showSaveDialog(null);
		
		if (file != null) {
			try {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(outputLog);
				fileWriter.close();
			} catch (IOException e) {
				App.alertError("Failed to save file.", "Could not save " + file.getName() + "!");
			}
		}
	}
	
	/**
	 * Sends an NeuralModelLabEvent to all IzhikevichLabListeners with the specified action(s).
	 * @param action - The action(s).
	 */
	private void sendEvent(Action ... action) {
		NeuralModelLabEvent event = new NeuralModelLabEvent(this, action);
		
		for (int i = 0; i < listener.size(); ++i)
			listener.get(i).actionTriggered(event);
	}
	
	/**
	 * Shuts down the Izhikevich lab. This saves the model file so that all new models are saved.
	 */
	public void shutdown() {
		/*
		try {
			FileWriter fileWriter = new FileWriter("models\\izhikevich.mdl", false);
			
			for (int i = 0; i < modelNames.size(); ++i) {
				fileWriter.write(modelNames.get(i) + ", " + aList.get(i) + ", " + bList.get(i) + ", " + cList.get(i) + ", " + dList.get(i) +
						", " + IList.get(i) + ", " + CList.get(i) + ", " + kList.get(i) + ", " + vpList.get(i) + ", " + vrList.get(i) + 
						", " + vtList.get(i) + "\n");
			}
			
			fileWriter.close();
		} 
		catch (IOException e) {
			App.alertError("I/O Error", "Failed to write to file " + IZHIKEVICH_MDL + "!");
		}
		*/
	}
	
	/**
	 * Starts a thread that runs the current neural model.
	 */
	public void start() {
		started = true;
		processThread = new Thread(this);
		processThread.start();
	}
	
	/**
	 * Processes a single cycle of the neural model.
	 */
	public void step() {
		// Process neuron
		process();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update spike count
				if (spike) spikes += 1.0f;
				
				// Update graphs
				updateGraphs();
				
				// Update controls
				outputLog += String.format("t: %6s - v: %7s; u: %7s; I: %8s", 
						String.format("%6.1f", t), 
						String.format("%7.3f", vArr[(int)(t - 1.0f)]), 
						String.format("%7.3f", uArr[(int)(t - 1.0f)]),
						String.format("%8.3f", IArr[(int)(t - 1.0f)])) +
						((vArr[(int)(t - 1.0f)] >= vp) ? "*\n" : "\n");
				outputArea.setText(outputLog);
				outputArea.setScrollTop(Double.MAX_VALUE);
			}
		});
		
		// Disable buttons when timespan is reached
		if (t == TIMESPAN) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Log spike count
					outputLog += String.format("Spikes: %6.1f\n", spikes);
					outputArea.setText(outputLog);
					outputArea.setScrollTop(Double.MAX_VALUE);
					
					hasHistory = false;
					sendEvent(Action.TIMESPAN_REACHED);
				}
			});
		}
	}
	
	@Override
	public void sizeChanged(AppSizeEvent e) {
		setMinHeight(e.getWorkspaceHeight());
		setMaxHeight(e.getWorkspaceHeight());
		setMinWidth(e.getWorkspaceWidth());
		setMaxWidth(e.getWorkspaceWidth());
		modelLabel.setMinWidth(e.getWorkspaceWidth() * 0.2);
		modelLabel.setMaxWidth(e.getWorkspaceWidth() * 0.2);
		modelTypeList.setMinWidth(e.getWorkspaceWidth() * 0.2 - 90.0);
		modelTypeList.setMaxWidth(e.getWorkspaceWidth() * 0.2 - 90.0);
		modelNameList.setMinWidth(e.getWorkspaceWidth() * 0.2 - 90.0);
		modelNameList.setMaxWidth(e.getWorkspaceWidth() * 0.2 - 90.0);
		inputLabel.setMinWidth(e.getWorkspaceWidth() * 0.2);
		inputLabel.setMaxWidth(e.getWorkspaceWidth() * 0.2);
		modelBox.setMinWidth(e.getWorkspaceWidth() * 0.2);
		modelBox.setMaxWidth(e.getWorkspaceWidth() * 0.2);
		pGraph.setMinHeight(e.getWorkspaceHeight() * 0.66);
		pGraph.setMaxHeight(e.getWorkspaceHeight() * 0.66);
		pGraph.setMinWidth(e.getWorkspaceWidth() * 0.3);
		pGraph.setMaxWidth(e.getWorkspaceWidth() * 0.3);
		uGraph.setMinHeight(e.getWorkspaceHeight() * 0.33);
		uGraph.setMaxHeight(e.getWorkspaceHeight() * 0.33);
		uGraph.setMinWidth(e.getWorkspaceWidth() * 0.3);
		uGraph.setMaxWidth(e.getWorkspaceWidth() * 0.3);
		vGraph.setMinHeight(e.getWorkspaceHeight() * 0.33);
		vGraph.setMaxHeight(e.getWorkspaceHeight() * 0.33);
		vGraph.setMinWidth(e.getWorkspaceWidth() * 0.3);
		vGraph.setMaxWidth(e.getWorkspaceWidth() * 0.3);
		IGraph.setMinHeight(e.getWorkspaceHeight() * 0.33);
		IGraph.setMaxHeight(e.getWorkspaceHeight() * 0.33);
		IGraph.setMinWidth(e.getWorkspaceWidth() * 0.3);
		IGraph.setMaxWidth(e.getWorkspaceWidth() * 0.3);
		gGraph.setMinHeight(e.getWorkspaceHeight() * 0.33);
		gGraph.setMaxHeight(e.getWorkspaceHeight() * 0.33);
		gGraph.setMinWidth(e.getWorkspaceWidth() * 0.3);
		gGraph.setMaxWidth(e.getWorkspaceWidth() * 0.3);
		outputLabel.setMinWidth(e.getWorkspaceWidth() * 0.2);
		outputLabel.setMaxWidth(e.getWorkspaceWidth() * 0.2);
		outputArea.setMinWidth(e.getWorkspaceWidth() * 0.19);
		outputArea.setMaxWidth(e.getWorkspaceWidth() * 0.19);
		outputBox.setMinWidth(e.getWorkspaceWidth() * 0.2);
		outputBox.setMaxWidth(e.getWorkspaceWidth() * 0.2);
	}
	
	/**
	 * Returns if the neuron spiked on the previous processing cycle.
	 * @return True if the neuron spiked, otherwise false.
	 */
	public boolean spike() { return spike; }
	
	/**
	 * Returns the value of t.
	 * @return t.
	 */
	public float t() { return t; }
	
	/**
	 * Returns the value of u.
	 * @return u.
	 */
	public float u() { return u; }
	
	/**
	 * Updates the input current graph.
	 */
	public void updateGraphs() {
		vSeries.getData().add(new XYChart.Data<Number, Number>((float)t - 1, vArr[(int)t - 1]));
		uSeries.getData().add(new XYChart.Data<Number, Number>((float)t - 1, uArr[(int)t - 1]));
		pvuSeries.getData().add(new XYChart.Data<Number, Number>(vArr[(int)t - 1], uArr[(int)t - 1]));
		ISeries.getData().add(new XYChart.Data<Number, Number>((float)t - 1, IArr[(int)t - 1]));
		gcSeries.getData().add(new XYChart.Data<Number, Number>((float)t - 1, vgcArr[(int)t - 1]));
		gpSeries.getData().add(new XYChart.Data<Number, Number>((float)t - 1, vgpArr[(int)t - 1]));
		
		vGraph.getData().set(0, vSeries);
		uGraph.getData().set(0, uSeries);
		pGraph.getData().set(3, pvuSeries);
		IGraph.getData().set(0, ISeries);
		gGraph.getData().set(0, gcSeries);
		gGraph.getData().set(1, gpSeries);
	}
	
	/**
	 * Returns the value of v.
	 * @return v.
	 */
	public float v() { return v; }
	
	/**
	 * Returns the value of vr.
	 * @return vr.
	 */
	public float vr() { return vr; }
	
	/**
	 * Returns the value of vt.
	 * @return vt.
	 */
	public float vt() { return vt; }
}
