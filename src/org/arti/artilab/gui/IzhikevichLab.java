package org.arti.artilab.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.arti.artilab.gui.events.AppSizeEvent;
import org.arti.artilab.gui.events.AppSizeListener;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

/**
 * <p>public class <b>IzhikevichLab</b><br>
 * extends {@link HBox}<br>
 * implements {@link AppSizeListener}, {@link Runnable}</p>
 * 
 * <p>IzhikevichLab class creates and controls the Izhikevich Lab app in the Artilab application.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class IzhikevichLab extends BorderPane implements AppSizeListener, Runnable {
	// Default height.
	private static final double DEF_HEIGHT = App.DEF_WORKSPACE_HEIGHT;
	
	// Izhikevich model file location.
	private static final String IZHIKEVICH_MDL = "models/izhikevich.mdl";
	// Number of standard Izhikevich models.
	private static final int STD_MODELS = 20;
	
	// Default value for a.
	private static final float DEF_A = 0.02f;
	// Default value for b.
	private static final float DEF_B = 0.2f;
	// Default value for c.
	private static final float DEF_C = -65.0f;
	// Default value for d.
	private static final float DEF_D = 2.0f;
	// Graph timespan.
	private static final int TIMESPAN = 1000;
	// Membrane potential peak.
	private static final float V_PEAK = 30.0f;
	
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
	// After-spike reset value of v.
	private float c;
	// List of c values.
	private ArrayList<Float> cList;
	// Default c value.
	private float cDef;
	// After-spike reset value of u.
	private float d;
	// List of d values.
	private ArrayList<Float> dList;
	// Default d value.
	private float dDef;
	// Has history flag.
	private boolean hasHistory;
	// Input current.
	private float I;
	// Input current array.
	private float[] IArr;
	// Input current history array.
	private float[] IHistArr;
	// List of I values.
	private ArrayList<Float> IList;
	// Default I value.
	private float IDef;
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
	// Membrane potential.
	private float v;
	// Membrane potential array.
	private float[] vArr;
	// Membrane potential history array.
	private float[] vHistArr;
	
	// Spike model list label.
	private Label modelLabel;
	// Spike model list.
	private ListView<String> modelList;
	// List of spike model names.
	private ObservableList<String> modelNames;
	// Index of the currently selected model.
	private int selectedIndex;
	
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
	
	// Value a label.
	private Label aLabel;
	// Value a slider.
	private Slider aSlider;
	// Value a value text field.
	private TextField aValText;
	// Value a default button.
	private Button aDefButton;
	// Value a reset button.
	private Button aResetButton;
	// Value b label.
	private Label bLabel;
	// Value b slider.
	private Slider bSlider;
	// Value b value text field.
	private TextField bValText;
	// Value b default button.
	private Button bDefButton;
	// Value b reset button.
	private Button bResetButton;
	// Value c label.
	private Label cLabel;
	// Value c slider.
	private Slider cSlider;
	// Value c value text field.
	private TextField cValText;
	// Value c default button.
	private Button cDefButton;
	// Value c reset button.
	private Button cResetButton;
	// Value d label.
	private Label dLabel;
	// Value d slider.
	private Slider dSlider;
	// Value d value text field.
	private TextField dValText;
	// Value d default button.
	private Button dDefButton;
	// Value d reset button.
	private Button dResetButton;
	// Value I label.
	private Label ILabel;
	// Value I slider.
	private Slider ISlider;
	// Value I value text field.
	private TextField IValText;
	// Value I default button.
	private Button IDefButton;
	// Value I reset button.
	private Button IResetButton;
	
	// Ramp input start label.
	private Label rampXStartLabel;
	// Ramp input start text field.
	private TextField rampXStartText;
	// Ramp input end label.
	private Label rampXEndLabel;
	// Ramp input end text field.
	private TextField rampXEndText;
	// Ramp time length label.
	private Label rampTimeLabel;
	// Ramp time length text field.
	private TextField rampTimeText;
	// Ramp input button.
	private Button rampButton;
	// Ramp input start value.
	private float rampXStart;
	// Ramp input end value.
	private float rampXEnd;
	// Ramp input time length.
	private float rampTimeLength;
	// Current ramp time.
	private float rampTime;
	// Ramping input flag.
	private boolean ramping;
	// Previous I value before ramping.
	private float prevI;
	// Ramp input box.
	private VBox rampBox;
	
	// Time label.
	private Label timeLabel;
	// Time value label.
	private Label timeValLabel;
	// Default button.
	private Button defButton;
	// Reset button.
	private Button resetButton;
	// Step button.
	private Button stepButton;
	// Start/stop button
	private Button startButton;
	// Process box.
	private VBox processBox;
	
	// New model button.
	private Button newModelButton;
	// Save model button.
	private Button saveModelButton;
	// Save history button.
	private Button saveHistoryButton;
	// Save log button.
	private Button saveLogButton;
	// Save box.
	private VBox saveBox;
	
	// Output log label.
	private Label outputLabel;
	// Output log text area.
	private TextArea outputArea;
	// Output log text.
	private String outputLog;
	
	/**
	 * Default constructor. Creates the Izhikevich Lab app.
	 */
	public IzhikevichLab() {
		// Call parent constructor.
		super();
		
		// Initialize variables
		a = DEF_A;
		aList = new ArrayList<Float>();
		aDef = DEF_A;
		b = DEF_B;
		bList = new ArrayList<Float>();
		bDef = DEF_B;
		c = DEF_C;
		cList = new ArrayList<Float>();
		cDef = DEF_C;
		d = DEF_D;
		dList = new ArrayList<Float>();
		dDef = DEF_D;
		hasHistory = false;
		I = 0.0f;
		IArr = new float[TIMESPAN];
		IHistArr = new float[TIMESPAN];
		IList = new ArrayList<Float>();
		IDef = 0.0f;
		spike = false;
		spikes = 0.0f;
		started = false;
		t = 0.0f;
		u = DEF_B * c;
		uArr = new float[TIMESPAN];
		uHistArr = new float[TIMESPAN];
		v = c;
		vArr = new float[TIMESPAN];
		vHistArr = new float[TIMESPAN];
		
		for (int i = 0; i < TIMESPAN; ++i) {
			IArr[i] = 0.0f;
			IHistArr[i] = 0.0f;
			uArr[i] = u;
			uHistArr[i] = 0.0f;
			vArr[i] = c;
			vHistArr[i] = 0.0f;
		}
		
		rampXStart = 0.0f;
		rampXEnd = 0.0f;
		rampTimeLength = 0.0f;
		rampTime = 0.0f;
		ramping = false;
		prevI = I;
		
		// Load model file
		modelNames = FXCollections.observableArrayList();
		
		try {
			Scanner scanner = new Scanner(new File(IZHIKEVICH_MDL));
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] part = line.split(",");
				
				modelNames.add(part[0]);
				aList.add(Float.parseFloat(part[1]));
				bList.add(Float.parseFloat(part[2]));
				cList.add(Float.parseFloat(part[3]));
				dList.add(Float.parseFloat(part[4]));
				IList.add(Float.parseFloat(part[5]));
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			App.alertError("File not found!", "Could not find file: " + IZHIKEVICH_MDL);
		}
		
		// Create spike model list
		modelLabel = new Label("Spike Models");
		modelLabel.setTextFill(Color.WHITE);
		modelLabel.setFont(new Font(modelLabel.getFont().getName(), 16.0));
		modelLabel.setBackground(Background.EMPTY);
		modelLabel.setMinHeight(40.0);
		modelLabel.setAlignment(Pos.CENTER);
		
		modelList = new ListView<String>(modelNames);
		modelList.setMinHeight(DEF_HEIGHT - modelLabel.getMinHeight());
		modelList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// Set selected index
				selectedIndex = arg0.getValue().intValue();
				
				// Update values
				a = aList.get(arg0.getValue().intValue());
				b = bList.get(arg0.getValue().intValue());
				c = cList.get(arg0.getValue().intValue());
				d = dList.get(arg0.getValue().intValue());
				I = IList.get(arg0.getValue().intValue());
				aDef = a;
				bDef = b;
				cDef = c;
				dDef = d;
				IDef = I;
				
				// Update controls
				aSlider.setValue(a);
				bSlider.setValue(b);
				cSlider.setValue(c);
				dSlider.setValue(d);
				ISlider.setValue(I);
				
				// Disable save model button for standard models
				if (selectedIndex < STD_MODELS)
					saveModelButton.setDisable(true);
				else
					saveModelButton.setDisable(false);
			}
		});
		modelList.setCellFactory(listView -> {
			// Get the list item
			ListCell<String> cell = new ListCell<String>();

			// Create rename menu item
            MenuItem renameItem = new MenuItem();
    		Label renameLabel = new Label("Rename");
    		renameLabel.setStyle("-fx-text-fill: white;");
    		renameItem.setGraphic(renameLabel);
            renameItem.setOnAction(event -> {
            	// Get list item
                String item = cell.getItem();
                
                // Request new name from user
                TextInputDialog renameDialog = new TextInputDialog(item);
                renameDialog.setTitle("Rename");
                renameDialog.setHeaderText("Please enter a name for the model.");
                renameDialog.setContentText("Name: ");
                Button okButton = (Button)renameDialog.getDialogPane().lookupButton(ButtonType.OK);
                TextField nameField = renameDialog.getEditor();
                BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> {
                	return nameField.getText().isBlank() || nameField.getText().isEmpty();
                }, nameField.textProperty());
                okButton.disableProperty().bind(isInvalid);
                Optional<String> name = renameDialog.showAndWait();
                
                // If a new name is provided, update list item
                name.ifPresent(n -> {
                	cell.setItem(n);
                	modelNames.set(cell.getIndex(), n);
                });
            });
            
            // Create remove menu item
            MenuItem removeItem = new MenuItem();
    		Label removeLabel = new Label("Remove");
    		removeLabel.setStyle("-fx-text-fill: white;");
    		removeItem.setGraphic(removeLabel);
            removeItem.setOnAction(event -> {
            	// Confirm removal of model
            	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            	ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            	Alert alert = new Alert(AlertType.CONFIRMATION,
            	        null,
            	        yesButton,
            	        noButton);
            	alert.setTitle("Remove");
            	alert.setHeaderText("Are you sure you want to remove " + cell.getItem() + "?");
            	Optional<ButtonType> result = alert.showAndWait();

            	// Remove model if yes is clicked
            	if (result.orElse(yesButton) == yesButton) {
            		listView.getItems().remove(cell.getItem());
            	}
            });
            
            // Create context menu
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(renameItem, removeItem);

            cell.textProperty().bind(cell.itemProperty());

            // Check if cell is empty or not
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
            	// Ignore empty cells
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                }
                // Add context menu to non-empty cells
                else {
                    cell.setContextMenu(contextMenu);
                    
                    // Disable context menu items for standard models
                    if (cell.getIndex() < STD_MODELS) {
                    	ObservableList<MenuItem> items = cell.getContextMenu().getItems();
                    	
                    	for (int i = 0; i < items.size(); ++i)
                    		((MenuItem)items.get(i)).disableProperty().set(true);
                    }
                }
            });
			
            // Return the list item
			return cell;
		});
		
		// Create phase portrait graph
		pxAxis = new NumberAxis(-100.0f, 30.0f, 10.0f);
		pxAxis.setLabel("Membrane Potential (v)");
		pyAxis = new NumberAxis(-40.0f, 60.0f, 10.0f);
		pyAxis.setLabel("Membrane Recovery (u)");
		pGraph = new LineChart<Number, Number>(pxAxis, pyAxis);
		pcSeries = new XYChart.Series<Number, Number>();
		pcSeries.setName("membrane reset");
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
		
		// Create membrane recovery graph
		uxAxis = new NumberAxis(0.0, 1000.0, 100.0);
		uxAxis.setLabel("Time");
		uyAxis = new NumberAxis(-40.0f, 40.0f, 10.0f);
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
		
		// Update graphs
		updatePGraph();
		updateUGraph();
		updateVGraph();
		
		// Create slider and controls for value a
		aLabel = new Label("a:");
		aLabel.setTextFill(Color.WHITE);
		aLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		aLabel.setMinWidth(15.0);
		
		aSlider = new Slider(-2.0, 2.0, a);
		aSlider.setMajorTickUnit(0.2);
		aSlider.setMinorTickCount(5);
		aSlider.setShowTickLabels(true);
		aSlider.setShowTickMarks(true);
        aSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		a = (float)aSlider.getValue();
        		aValText.setText(String.format("%2.3f", a));
        	}
        });
		
        aValText = new TextField(String.format("%2.3f", a));
        aValText.setBackground(Background.fill(Color.BLACK));
        aValText.setFont(new Font(aLabel.getFont().getName(), 16.0));
		aValText.setMinWidth(100.0);
		aValText.setAlignment(Pos.CENTER);
		aValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					a = (float)Math.max(-2.0, Math.min(2.0, Float.parseFloat(aValText.getText())));
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
		
		aDefButton = new Button("Default");
		aDefButton.setId("pinkbutton");
		aDefButton.setMinWidth(100.0);
		aDefButton.setMaxHeight(30.0);
		aDefButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				a = DEF_A;
				aSlider.setValue(a);
			}
		});
		
		aResetButton = new Button("Reset");
		aResetButton.setId("pinkbutton");
		aResetButton.setMinWidth(100.0);
		aResetButton.setMaxHeight(30.0);
		aResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				a = aDef;
				aSlider.setValue(a);
			}
		});
		
		HBox aBox = new HBox(aLabel, aSlider, aValText, aDefButton, aResetButton);
		aBox.setSpacing(10.0);
		aBox.setAlignment(Pos.CENTER);
		aBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
		
		// Create slider and controls for value b
		bLabel = new Label("b:");
		bLabel.setTextFill(Color.WHITE);
		bLabel.setFont(new Font(bLabel.getFont().getName(), 16.0));
		bLabel.setMinWidth(15.0);
		
		bSlider = new Slider(-2.0, 2.0, b);
		bSlider.setMajorTickUnit(0.2);
		bSlider.setMinorTickCount(5);
		bSlider.setShowTickLabels(true);
		bSlider.setShowTickMarks(true);
        bSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		b = (float)bSlider.getValue();
        		bValText.setText(String.format("%2.3f", b));
        	}
        });
		
		bValText = new TextField(String.format("%2.3f", b));
		bValText.setBackground(Background.fill(Color.BLACK));
		bValText.setFont(new Font(aLabel.getFont().getName(), 16.0));
		bValText.setMinWidth(100.0);
		bValText.setAlignment(Pos.CENTER);
		bValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					b = (float)Math.max(-2.0, Math.min(2.0, Float.parseFloat(bValText.getText())));
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
		
		bDefButton = new Button("Default");
		bDefButton.setId("pinkbutton");
		bDefButton.setMinWidth(100.0);
		bDefButton.setMaxHeight(30.0);
		bDefButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				b = DEF_B;
				bSlider.setValue(b);
			}
		});
		
		bResetButton = new Button("Reset");
		bResetButton.setId("pinkbutton");
		bResetButton.setMinWidth(100.0);
		bResetButton.setMaxHeight(30.0);
		bResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				b = bDef;
				bSlider.setValue(b);
			}
		});
		
		HBox bBox = new HBox(bLabel, bSlider, bValText, bDefButton, bResetButton);
		bBox.setSpacing(10.0);
		bBox.setAlignment(Pos.CENTER);
		bBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
		
		// Create slider and controls for value c
		cLabel = new Label("c:");
		cLabel.setTextFill(Color.WHITE);
		cLabel.setFont(new Font(cLabel.getFont().getName(), 16.0));
		cLabel.setMinWidth(15.0);
		
		cSlider = new Slider(-90.0, -40.0, c);
		cSlider.setMajorTickUnit(10.0);
		cSlider.setMinorTickCount(5);
		cSlider.setShowTickLabels(true);
		cSlider.setShowTickMarks(true);
        cSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		c = (float)cSlider.getValue();
        		cValText.setText(String.format("%3.3f", c));
        	}
        });
		
        cValText = new TextField(String.format("%3.3f", c));
        cValText.setBackground(Background.fill(Color.BLACK));
        cValText.setFont(new Font(aLabel.getFont().getName(), 16.0));
        cValText.setMinWidth(100.0);
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
		
		cDefButton = new Button("Default");
		cDefButton.setId("pinkbutton");
		cDefButton.setMinWidth(100.0);
		cDefButton.setMaxHeight(30.0);
		cDefButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				c = DEF_C;
				cSlider.setValue(c);
			}
		});
		
		cResetButton = new Button("Reset");
		cResetButton.setId("pinkbutton");
		cResetButton.setMinWidth(100.0);
		cResetButton.setMaxHeight(30.0);
		cResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				c = cDef;
				cSlider.setValue(c);
			}
		});
		
		HBox cBox = new HBox(cLabel, cSlider, cValText, cDefButton, cResetButton);
		cBox.setSpacing(10.0);
		cBox.setAlignment(Pos.CENTER);
		cBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
		
		// Create slider and controls for value d
		dLabel = new Label("d:");
		dLabel.setTextFill(Color.WHITE);
		dLabel.setFont(new Font(dLabel.getFont().getName(), 16.0));
		dLabel.setMinWidth(15.0);
		
		dSlider = new Slider(-25.0, 25.0, d);
		dSlider.setMajorTickUnit(10.0);
		dSlider.setMinorTickCount(5);
		dSlider.setShowTickLabels(true);
		dSlider.setShowTickMarks(true);
        dSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		d = (float)dSlider.getValue();
        		dValText.setText(String.format("%3.3f", d));
        	}
        });
		
        dValText = new TextField(String.format("%3.3f", d));
        dValText.setBackground(Background.fill(Color.BLACK));
        dValText.setFont(new Font(aLabel.getFont().getName(), 16.0));
        dValText.setMinWidth(100.0);
        dValText.setAlignment(Pos.CENTER);
		dValText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					d = (float)Math.max(-25.0, Math.min(25.0, Float.parseFloat(dValText.getText())));
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
		
		dDefButton = new Button("Default");
		dDefButton.setId("pinkbutton");
		dDefButton.setMinWidth(100.0);
		dDefButton.setMaxHeight(30.0);
		dDefButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				d = DEF_D;
				dSlider.setValue(d);
			}
		});
		
		dResetButton = new Button("Reset");
		dResetButton.setId("pinkbutton");
		dResetButton.setMinWidth(100.0);
		dResetButton.setMaxHeight(30.0);
		dResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				d = dDef;
				dSlider.setValue(d);
			}
		});
		
		HBox dBox = new HBox(dLabel, dSlider, dValText, dDefButton, dResetButton);
		dBox.setSpacing(10.0);
		dBox.setAlignment(Pos.CENTER);
		dBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
		
		// Create slider and controls for value I
		ILabel = new Label("I:");
		ILabel.setTextFill(Color.WHITE);
		ILabel.setFont(new Font(ILabel.getFont().getName(), 16.0));
		ILabel.setMinWidth(15.0);
		
		ISlider = new Slider(-100.0, 100.0, I);
		ISlider.setMajorTickUnit(40.0);
		ISlider.setMinorTickCount(5);
		ISlider.setShowTickLabels(true);
		ISlider.setShowTickMarks(true);
        ISlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue <? extends Number> observable, Number oldValue, Number newValue) {
        		I = (float)ISlider.getValue();
        		IValText.setText(String.format("%4.3f", I));
        	}
        });
		
        IValText = new TextField(String.format("%4.3f", I));
        IValText.setBackground(Background.fill(Color.BLACK));
        IValText.setFont(new Font(aLabel.getFont().getName(), 16.0));
		IValText.setMinWidth(100.0);
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
		
		IDefButton = new Button("Default");
		IDefButton.setId("pinkbutton");
		IDefButton.setMinWidth(100.0);
		IDefButton.setMaxHeight(30.0);
		IDefButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				I = 0.0f;
				ISlider.setValue(I);
			}
		});
		
		IResetButton = new Button("Reset");
		IResetButton.setId("pinkbutton");
		IResetButton.setMinWidth(100.0);
		IResetButton.setMaxHeight(30.0);
		IResetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				I = IDef;
				ISlider.setValue(I);
			}
		});
		
		HBox IBox = new HBox(ILabel, ISlider, IValText, IDefButton, IResetButton);
		IBox.setSpacing(10.0);
		IBox.setAlignment(Pos.CENTER);
		IBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
		
		VBox sliderBox = new VBox(aBox, bBox, cBox, dBox, IBox);
		sliderBox.setMinWidth(App.DEF_WIDTH * 0.3);
		
		// Create ramp input controls
		Label rampLabel = new Label("Ramp Input");
		rampLabel.setTextFill(Color.WHITE);
		rampLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampLabel.setMinWidth(100.0);
		rampLabel.setAlignment(Pos.CENTER);
		
		rampXStartLabel = new Label("Start: ");
		rampXStartLabel.setTextFill(Color.WHITE);
		rampXStartLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampXStartLabel.setMinWidth(50.0);
		
		rampXStartText = new TextField(String.format("%4.3f", rampXStart));
		rampXStartText.setBackground(Background.fill(Color.BLACK));
		rampXStartText.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampXStartText.setMinWidth(100.0);
		rampXStartText.setAlignment(Pos.CENTER);
		rampXStartText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					rampXStart = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(rampXStartText.getText())));
					rampXStartText.setText(String.format("%4.3f", rampXStart));
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					rampXStartText.setText(String.format("%4.3f", rampXStart));
				}
			}
		});
		
		HBox rampStartBox = new HBox(rampXStartLabel, rampXStartText);
		rampStartBox.setAlignment(Pos.CENTER);
		
		rampXEndLabel = new Label("End: ");
		rampXEndLabel.setTextFill(Color.WHITE);
		rampXEndLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampXEndLabel.setMinWidth(50.0);
		
		rampXEndText = new TextField(String.format("%4.3f", rampXEnd));
		rampXEndText.setBackground(Background.fill(Color.BLACK));
		rampXEndText.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampXEndText.setMinWidth(100.0);
		rampXEndText.setAlignment(Pos.CENTER);
		rampXEndText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					rampXEnd = (float)Math.max(-100.0, Math.min(100.0, Float.parseFloat(rampXEndText.getText())));
					rampXEndText.setText(String.format("%4.3f", rampXEnd));
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					rampXEndText.setText(String.format("%4.3f", rampXEnd));
				}
			}
		});
		
		HBox rampEndBox = new HBox(rampXEndLabel, rampXEndText);
		rampEndBox.setAlignment(Pos.CENTER);
		
		rampTimeLabel = new Label("Time: ");
		rampTimeLabel.setTextFill(Color.WHITE);
		rampTimeLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampTimeLabel.setMinWidth(50.0);
		
		rampTimeText = new TextField(String.format("%4.1f", rampTime));
		rampTimeText.setBackground(Background.fill(Color.BLACK));
		rampTimeText.setFont(new Font(aLabel.getFont().getName(), 16.0));
		rampTimeText.setMinWidth(100.0);
		rampTimeText.setAlignment(Pos.CENTER);
		rampTimeText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					rampTimeLength = (float)Math.max(1.0, Math.min(1000.0 - t, Float.parseFloat(rampTimeText.getText())));
					rampTimeText.setText(String.format("%4.1f", rampTimeLength));
				}
				catch (Exception e) {
					App.alertError("Input error!", "Unable to parse text into value.");
					rampTimeText.setText(String.format("%4.1f", rampTimeLength));
				}
			}
		});
		
		HBox rampTimeBox = new HBox(rampTimeLabel, rampTimeText);
		rampTimeBox.setAlignment(Pos.CENTER);
		
		rampButton = new Button("Ramp Input");
		rampButton.setId("pinkbutton");
		rampButton.setMinWidth(150.0);
		rampButton.setMaxHeight(30.0);
		rampButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Start ramping the input
				if (!ramping) {
					prevI = I;
					ramping = true;
					rampButton.setDisable(true);
				}
			}
		});
		
		rampBox = new VBox(rampLabel, rampStartBox, rampEndBox, rampTimeBox, rampButton);
		rampBox.setSpacing(10.0);
		rampBox.setMinWidth(App.DEF_WIDTH * 0.1);
		rampBox.setAlignment(Pos.CENTER);
		rampBox.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));
		
		// Create processing controls
		timeLabel = new Label("Time: ");
		timeLabel.setTextFill(Color.WHITE);
		timeLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		
		timeValLabel = new Label(String.format("%4.1f", t));
		timeValLabel.setBackground(Background.fill(Color.BLACK));
		timeValLabel.setTextFill(Color.WHITE);
		timeValLabel.setFont(new Font(aLabel.getFont().getName(), 16.0));
		timeValLabel.setMinWidth(100.0);
		timeValLabel.setAlignment(Pos.CENTER);
		
		HBox timeBox = new HBox(timeLabel, timeValLabel);
		timeBox.setAlignment(Pos.CENTER);
		
		defButton = new Button("Default");
		defButton.setId("pinkbutton");
		defButton.setMinWidth(150.0);
		defButton.setMaxHeight(30.0);
		defButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Stop thread
				started = false;
				
				// Stop ramping
				ramping = false;
				
				// Reset and default data
				setDefault();
				
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
			}
		});
		
		resetButton = new Button("Reset");
		resetButton.setId("pinkbutton");
		resetButton.setMinWidth(150.0);
		resetButton.setMaxHeight(30.0);
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Stop thread
				started = false;
				
				// Stop ramping
				ramping = false;
				
				// Reset data
				reset();
				
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
			}
		});
		
		stepButton = new Button("Step");
		stepButton.setId("pinkbutton");
		stepButton.setMinWidth(150.0);
		stepButton.setMaxHeight(30.0);
		stepButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Start new log and spike count if t = 0
				if (t == 0.0f) {
					outputLog = "Log Start: " + LocalDateTime.now().toString() + "\n";
					outputArea.setText(outputLog);
					spikes = 0.0f;
				}
				
				// Update input if ramping
				if (ramping && rampTime < rampTimeLength) {
					I = rampXStart + ((rampXEnd - rampXStart) * (rampTime / rampTimeLength));
					ISlider.setValue(I);
					
					rampTime += 1.0f;
					
					// If ramp time expires, stop ramping and return input to previous value
					if (rampTime >= rampTimeLength) {
						ramping = false;
						rampButton.setDisable(false);
						I = prevI;
						ISlider.setValue(I);
					}
				}
				
				// Process neuron
				process();
				
				// Update spike count
				if (spike) spikes += 1.0f;
				
				// Update graphs
				updatePGraph();
				updateUGraph();
				updateVGraph();
				
				// Update controls
				timeValLabel.setText(String.format("%6.1f", t));
				outputLog += String.format("t: %6s - v: %7s; u: %7s; I: %8s", 
						String.format("%6.1f", t), 
						String.format("%7.3f", vArr[(int)(t - 1.0f)]), 
						String.format("%7.3f", uArr[(int)(t - 1.0f)]),
						String.format("%8.3f", IArr[(int)(t - 1.0f)])) +
						((vArr[(int)(t - 1.0f)] >= V_PEAK) ? "*\n" : "\n");
				outputArea.setText(outputLog);
				outputArea.setScrollTop(Double.MAX_VALUE);
				
				// Disable buttons when timespan is reached
				if (t == TIMESPAN) {
					// Log spike count
					outputLog += String.format("Spikes: %6.1f\n", spikes);
					outputArea.setText(outputLog);
					outputArea.setScrollTop(Double.MAX_VALUE);
					
					rampButton.setDisable(true);
					stepButton.setDisable(true);
					startButton.setDisable(true);
					startButton.setText("Start");
					saveHistoryButton.setDisable(false);
					hasHistory = false;
					saveLogButton.setDisable(false);
				}
				else {
					saveHistoryButton.setDisable(true);
					saveLogButton.setDisable(true);
				}
			}
		});
		
		startButton = new Button("Start");
		startButton.setId("pinkbutton");
		startButton.setMinWidth(150.0);
		startButton.setMaxHeight(30.0);
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Start thread
				if (!started) {
					// Start new log and spike count if t = 0
					if (t == 0.0f) {
						outputLog = "Log Start: " + LocalDateTime.now().toString() + "\n";
						outputArea.setText(outputLog);
						outputArea.setScrollTop(Double.MAX_VALUE);
						spikes = 0.0f;
					}
					
					started = true;
					runThread();
					startButton.setText("Stop");
					rampButton.setDisable(true);
					newModelButton.setDisable(true);
					saveModelButton.setDisable(true);
					saveHistoryButton.setDisable(true);
				}
				// Stop thread
				else {
					started = false;
					startButton.setText("Start");
					rampButton.setDisable(false);
					newModelButton.setDisable(false);
					if (selectedIndex >= STD_MODELS)
						saveModelButton.setDisable(false);
				}
			}
		});
		
		processBox = new VBox(timeBox, defButton, resetButton, stepButton, startButton);
		processBox.setSpacing(10.0);
		processBox.setMinWidth(App.DEF_WIDTH * 0.1);
		processBox.setAlignment(Pos.CENTER);
		
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
		
		saveHistoryButton = new Button("Save History");
		saveHistoryButton.setId("pinkbutton");
		saveHistoryButton.setMinWidth(150.0);
		saveHistoryButton.setMaxHeight(30.0);
		saveHistoryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Set history flag
				hasHistory = true;
				
				// Store past values
				for (int i = 0; i < TIMESPAN; ++i) {
					IHistArr[i] = IArr[i];
					uHistArr[i] = uArr[i];
					vHistArr[i] = vArr[i];
				}
				
				// Update controls
				saveHistoryButton.setDisable(true);
			}
		});
		saveHistoryButton.setDisable(true);
		
		saveLogButton = new Button("Save Log");
		saveLogButton.setId("pinkbutton");
		saveLogButton.setMinWidth(150.0);
		saveLogButton.setMaxHeight(30.0);
		saveLogButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
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
		});
		saveLogButton.setDisable(true);
		
		saveBox = new VBox(newModelButton, saveModelButton, saveHistoryButton, saveLogButton);
		saveBox.setSpacing(10.0);
		saveBox.setMinWidth(App.DEF_WIDTH * 0.1);
		saveBox.setAlignment(Pos.CENTER);
		
		// Create output log
		outputLabel = new Label("Output Log");
		outputLabel.setTextFill(Color.WHITE);
		outputLabel.setFont(new Font(outputLabel.getFont().getName(), 16.0));
		outputLabel.setBackground(Background.EMPTY);
		outputLabel.setMinHeight(40.0);
		outputLabel.setAlignment(Pos.CENTER);
		
		outputArea = new TextArea();
		outputArea.setFont(new Font("Courier New", 10.0));
		outputArea.setEditable(false);
		outputArea.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				outputArea.setScrollTop(Double.MAX_VALUE);
			}
		});
		
		outputLog = "";
		
		// Add components
		VBox modelBox = new VBox();
		modelBox.getChildren().addAll(modelLabel, modelList);
		VBox.setVgrow(modelList, Priority.ALWAYS);
		modelBox.setPadding(new Insets(0.0, 5.0, 0.0, 5.0));
		
		VBox vuGraphBox = new VBox();
		vuGraphBox.getChildren().addAll(vGraph, uGraph);
		
		HBox graphBox = new HBox();
		graphBox.getChildren().addAll(vuGraphBox, pGraph);
		
		HBox controlBox = new HBox();
		controlBox.getChildren().addAll(sliderBox, rampBox, processBox, saveBox);
		
		VBox centerBox = new VBox();
		centerBox.getChildren().addAll(graphBox, controlBox);
		
		VBox outputBox = new VBox();
		outputBox.getChildren().addAll(outputLabel, outputArea);
		VBox.setVgrow(outputArea, Priority.ALWAYS);
		outputBox.setPadding(new Insets(0.0, 5.0, 0.0, 5.0));
		
		setLeft(modelBox);
		setCenter(centerBox);
		setRight(outputBox);
		
		// Select the first model
		modelList.getSelectionModel().select(selectedIndex);
	}
	
	/**
	 * Returns the value of a.
	 * @return a.
	 */
	public float a() { return a; }
	
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
	
	/**
	 * Processes a single processing cycle of the Izhikevich neuron.
	 */
	public void process() {
		v += 0.5f * (0.04f * v * v + 5.0f * v + 140.0f - u + I);
		v += 0.5f * (0.04f * v * v + 5.0f * v + 140.0f - u + I);
		u += a * (b * v - u);
		
		IArr[(int)t] = I;
		uArr[(int)t] = u;
		vArr[(int)t] = (v >= V_PEAK) ? V_PEAK : v;
		
		if (v >= V_PEAK) {
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
	 * Resets the time and neuron model variables to their original starting values.
	 */
	public void reset() {
		a = aDef;
		b = bDef;
		c = cDef;
		d = dDef;
		I = IDef;
		t = 0.0f;
		u = b * c;
		uArr = new float[TIMESPAN];
		v = c;
		vArr = new float[TIMESPAN];
		
		for (int i = 0; i < TIMESPAN; ++i) {
			IArr[i] = I;
			uArr[i] = u;
			vArr[i] = c;
		}
		
		prevI = I;
	}
	
	@Override
	public void run() {
		// While started
		while (started && t < TIMESPAN) {
			// Update input if ramping
			if (ramping && rampTime < rampTimeLength) {
				I = rampXStart + ((rampXEnd - rampXStart) * (rampTime / rampTimeLength));
				ISlider.setValue(I);
				
				rampTime += 1.0f;
				
				// If ramp time expires, stop ramping and return input to previous value
				if (rampTime >= rampTimeLength) {
					ramping = false;
					I = prevI;
				}
			}
			
			// Process neuron
			process();
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Update spike count
					if (spike) spikes += 1.0f;
					
					// If ramp time expires, update controls
					if (rampTime >= rampTimeLength) {
						rampButton.setDisable(false);
						ISlider.setValue(I);
					}
					
					// Update graphs
					updatePGraph();
					updateUGraph();
					updateVGraph();
					
					// Update controls
					timeValLabel.setText(String.format("%6.1f", t));
					outputLog += String.format("t: %6s - v: %7s; u: %7s; I: %8s", 
							String.format("%6.1f", t), 
							String.format("%7.3f", vArr[(int)(t - 1.0f)]), 
							String.format("%7.3f", uArr[(int)(t - 1.0f)]),
							String.format("%8.3f", IArr[(int)(t - 1.0f)])) +
							((vArr[(int)(t - 1.0f)] >= V_PEAK) ? "*\n" : "\n");
					outputArea.setText(outputLog);
					outputArea.setScrollTop(Double.MAX_VALUE);
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
					
					// Update controls
					rampButton.setDisable(true);
					stepButton.setDisable(true);
					startButton.setDisable(true);
					startButton.setText("Start");
					newModelButton.setDisable(false);
					if (selectedIndex >= STD_MODELS)
						saveModelButton.setDisable(false);
					saveHistoryButton.setDisable(false);
					hasHistory = false;
					saveLogButton.setDisable(false);
				}
			});
		}
	}
	
	/**
	 * Starts a thread that processes the neuron.
	 */
	private void runThread() {
		(new Thread(this)).start();
	}
	
	/**
	 * Sets the value of a.
	 * @param a - The new value for a.
	 */
	public void setA(float a) { this.a = a; }
	
	/**
	 * Sets the value of b.
	 * @param b - The new value for b.
	 */
	public void setB(float b) { this.b = b; }
	
	/**
	 * Sets the value of c.
	 * @param c - The new value for c.
	 */
	public void setC(float c) { this.c = c; }
	
	/**
	 * Sets the value of d.
	 * @param d - The new value for d.
	 */
	public void setD(float d) { this.d = d; }
	
	/**
	 * Sets the value of I.
	 * @param I - The new value for I.
	 */
	public void setI(float I) { this.I = I; }
	
	/**
	 * Resets the time and sets all neuron variables to their default starting values.
	 */
	public void setDefault() {
		a = DEF_A;
		b = DEF_B;
		c = DEF_C;
		d = DEF_D;
		I = 0.0f;
		t = 0.0f;
		u = bDef * c;
		uArr = new float[TIMESPAN];
		v = c;
		vArr = new float[TIMESPAN];
		
		for (int i = 0; i < TIMESPAN; ++i) {
			IArr[i] = I;
			uArr[i] = u;
			vArr[i] = c;
		}
		
		prevI = I;
	}
	
	/**
	 * Shuts down the Izhikevich lab. This saves the model file so that all new models are saved.
	 */
	public void shutdown() {
		try {
			FileWriter fileWriter = new FileWriter("models\\izhikevich.mdl", false);
			
			for (int i = 0; i < modelNames.size(); ++i) {
				fileWriter.write(modelNames.get(i) + ", " + aList.get(i) + ", " + bList.get(i) + ", " + cList.get(i) + ", " + dList.get(i) +
						", " + IList.get(i) + "\n");
			}
			
			fileWriter.close();
		} 
		catch (IOException e) {
			App.alertError("I/O Error", "Failed to write to file " + IZHIKEVICH_MDL + "!");
		}
	}
	
	@Override
	public void sizeChanged(AppSizeEvent e) {
		resize(e.getWidth(), e.getWorkspaceHeight());
		modelLabel.setMinWidth(e.getWidth() * 0.125);
		modelLabel.setMaxWidth(e.getWidth() * 0.125);
		modelList.setMinWidth(e.getWidth() * 0.125);
		modelList.setMaxWidth(e.getWidth() * 0.125);
		modelList.setMinHeight(e.getWorkspaceHeight() - modelLabel.getHeight());
		modelList.setMaxHeight(e.getWorkspaceHeight() - modelLabel.getHeight());
		pGraph.setMinHeight(e.getWorkspaceHeight() * 0.7);
		pGraph.setMaxHeight(e.getWorkspaceHeight() * 0.7);
		pGraph.setMinWidth(e.getWidth() * 0.34);
		pGraph.setMaxWidth(e.getWidth() * 0.34);
		uGraph.setMinHeight(e.getWorkspaceHeight() * 0.35);
		uGraph.setMaxHeight(e.getWorkspaceHeight() * 0.35);
		uGraph.setMinWidth(e.getWidth() * 0.34);
		uGraph.setMaxWidth(e.getWidth() * 0.34);
		vGraph.setMinHeight(e.getWorkspaceHeight() * 0.35);
		vGraph.setMaxHeight(e.getWorkspaceHeight() * 0.35);
		vGraph.setMinWidth(e.getWidth() * 0.34);
		vGraph.setMaxWidth(e.getWidth() * 0.34);
		aSlider.setPrefWidth(e.getWidth() * 0.3 - 15.0);
		bSlider.setPrefWidth(e.getWidth() * 0.3 - 15.0);
		cSlider.setPrefWidth(e.getWidth() * 0.3 - 15.0);
		dSlider.setPrefWidth(e.getWidth() * 0.3 - 15.0);
		ISlider.setPrefWidth(e.getWidth() * 0.3 - 15.0);
		rampBox.setMinWidth(e.getWidth()* 0.1);
		rampBox.setMaxWidth(e.getWidth() * 0.1);
		processBox.setMinWidth(e.getWidth()* 0.1);
		processBox.setMaxWidth(e.getWidth() * 0.1);
		saveBox.setMinWidth(e.getWidth() * 0.1);
		saveBox.setMaxWidth(e.getWidth() * 0.1);
		outputLabel.setMinWidth(e.getWidth() * 0.175);
		outputLabel.setMaxWidth(e.getWidth() * 0.175);
		outputArea.setMinHeight(e.getWorkspaceHeight() - outputLabel.getHeight());
		outputArea.setMaxHeight(e.getWorkspaceHeight() - outputLabel.getHeight());
		outputArea.setMinWidth(e.getWidth() * 0.175);
		outputArea.setMaxWidth(e.getWidth() * 0.175);
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
	 * Updates the phase portrait graph.
	 */
	public void updatePGraph() {
		// Clear graph and series
		pcSeries.getData().clear();
		puNullSeries.getData().clear();
		pvNullSeries.getData().clear();
		pvuSeries.getData().clear();
		pGraph.getData().clear();
		
		// Create series
		pcSeries.getData().add(new XYChart.Data<Number, Number>(c, -40.0f));
		pcSeries.getData().add(new XYChart.Data<Number, Number>(c, 60.0f));
		for (int i = 0; i < 130; ++i) {
			puNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, b * (-100.0f + (float)i)));
			pvNullSeries.getData().add(new XYChart.Data<Number, Number>(-100.0f + (float)i, 
					0.04f * (-100.0f + (float)i) * (-100.0f + (float)i) + 5.0f * (-100.0f + (float)i) + 140.0f + I));
		}
		for (int i = 0; i < TIMESPAN; ++i)
			pvuSeries.getData().add(new XYChart.Data<Number, Number>(vArr[i], uArr[i]));
		
		// Create p graph
		pGraph.getData().add(pcSeries);
		pGraph.getData().add(puNullSeries);
		pGraph.getData().add(pvNullSeries);
		pGraph.getData().add(pvuSeries);
	}
	
	/**
	 * Updates the membrane recovery graph.
	 */
	public void updateUGraph() {
		// Clear graph and series
		uSeries.getData().clear();
		uHistSeries.getData().clear();
		uGraph.getData().clear();
		
		// Create u series
		for (int i = 0; i < TIMESPAN; ++i) {
			uSeries.getData().add(new XYChart.Data<Number, Number>((float)i, uArr[i]));
			
			if (hasHistory)
				uHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, uHistArr[i]));
		}
		
		// Create u graph
		uGraph.getData().add(uSeries);
		
		if (hasHistory)
			uGraph.getData().add(uHistSeries);
	}
	
	/**
	 * Updates the membrane potential graph.
	 */
	public void updateVGraph() {
		// Clear graph and series
		vSeries.getData().clear();
		vHistSeries.getData().clear();
		vGraph.getData().clear();
		
		// Create v series
		for (int i = 0; i < TIMESPAN; ++i) {
			vSeries.getData().add(new XYChart.Data<Number, Number>((float)i, vArr[i]));
			
			if (hasHistory)
				vHistSeries.getData().add(new XYChart.Data<Number, Number>((float)i, vHistArr[i]));
		}
		
		// Create v graph
		vGraph.getData().add(vSeries);
		
		if (hasHistory)
			vGraph.getData().add(vHistSeries);
	}
	
	/**
	 * Returns the value of v.
	 * @return v.
	 */
	public float v() { return v; }
}
