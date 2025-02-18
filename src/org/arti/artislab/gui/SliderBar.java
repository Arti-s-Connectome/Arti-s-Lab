package org.arti.artislab.gui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

/**
 * <p>public class <b>SliderBar</b><br>
 * extends {@link ProgressBar}</p>
 * 
 * <p>SliderBar class represents a cross between a Slider and a ProgressBar. This control works like a Slider but looks like a ProgressBar to give it a more
 * updated look.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 23
 */
public class SliderBar extends ProgressBar {
	// Maxaimum value.
	private double max;
	// Minimum value.
	private double min;
	// Percentage value.
	private double percentage;
	// Current value.
	private double value;
	
	/**
	 * Default constructor. Creates a SliderBar with a minimum value of 0 and a maximum value of 1 and the current value set to 0.
	 */
	public SliderBar() {
		// Call parent constructor
		super(0.0);
		
		// Initialize variables
		max = 1.0;
		min = 0.0;
		percentage = 0.0;
		value = 0.0;
		
		// Handle mouse clicks for changing value
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				SliderBar slider = (SliderBar)arg0.getSource();
				Bounds bounds = slider.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY())) {
					percentage = arg0.getX() / bounds.getMaxX();
					slider.setValue(((max - min) * percentage) + min);
				}
			}
		});
	}
	
	/**
	 * Creates a SliderBar with the specified minimum and maximum values and the specified current value.
	 * @param min - The minimum value.
	 * @param max - The maximum value.
	 * @param value - The current value.
	 * @throws IllegalArgumentException Thrown if min is greater than or equal to max.
	 */
	public SliderBar(double min, double max, double value) {
		// Call parent constructor
		super(0.0);
		
		// Check parameters
		if (min >= max)
			throw new IllegalArgumentException("Error: SliderBar's minimum value must be less than its maximum value.");
		
		// Initialize variables
		this.max = max;
		this.min = min;
		percentage = (Math.max(min, Math.min(max, value)) - min) / (max - min);
		this.value = Math.max(min, Math.min(max, value));
		
		// Handle mouse clicks for changing value
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				SliderBar slider = (SliderBar)arg0.getSource();
				Bounds bounds = slider.getBoundsInLocal();
				
				if (bounds.contains(arg0.getX(), arg0.getY())) {
					percentage = arg0.getX() / bounds.getMaxX();
					slider.setValue(((max - min) * percentage) + min);
				}
			}
		});
	}
	
	/**
	 * Returns the maximum value represented by the slider bar.
	 * @return The maximum value.
	 */
	public double getMaximum() {
		return max;
	}
	
	/**
	 * Returns the minimum value represented by the slider bar.
	 * @return The minimum value.
	 */
	public double getMinimum() {
		return min;
	}
	
	/**
	 * Returns the current value represented by the slider bar.
	 * @return The current value.
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * Sets the minimum and maximum value bounds to the specified values.
	 * @param min - The minimum value.
	 * @param max - The maximum value.
	 */
	public void setValueBounds(double min, double max) {
		// Check parameters
		if (min >= max)
			throw new IllegalArgumentException("Error: SliderBar's minimum value must be less than its maximum value.");
		
		this.max = max;
		this.min = min;
		percentage = (Math.max(min, Math.min(max, value)) - min) / (max - min);
		value = Math.max(min, Math.min(max, value));
	}
	
	/**
	 * Sets the current value to the specified value and updates the slider bar.
	 * @param value - The new value.
	 */
	public void setValue(double value) {
		this.value = Math.max(min, Math.min(max, value));
		percentage = (this.value - min) / (max - min);
		setProgress(percentage);
	}
}
