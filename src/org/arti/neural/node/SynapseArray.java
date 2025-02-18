package org.arti.neural.node;

import java.util.ArrayList;

import org.arti.neural.NeuralSystem;

/**
 * <p>public abstract class <b>SynapseArray</b><br>
 * extends {@link Object}<br>
 * implements {@link Runnable}</p>
 * 
 * <p>SynapseArray class represents an array of a type of synapse connected to neural nodes. Subclasses will define the specific synapse 
 * type.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public abstract class SynapseArray implements Runnable {
	/**
	 * The NeuralSystem controlling the processing of neural nodes and networks.
	 */
	protected NeuralSystem neuralSystem;
	/**
	 * The list of neural node data for the postsynaptic neural nodes.
	 */
	protected ArrayList<NodeData> post;
	/** 
	 * The list of neural node data for the presynaptic neural nodes.
	 */
	protected ArrayList<NodeData> pre;
	/**
	 * Time constant of x recovery.
	 */
	protected float[] td;
	/**
	 * Time constant of u decay.
	 */
	protected float[] tf;
	/**
	 * Fraction of available neurotransmitters ready for use.
	 */
	protected float[] u;
	/**
	 * The increment of u produced by a spike.
	 */
	protected float[] U;
	/**
	 * Fraction of resources that remain available after neurotransmitter depletion.
	 */
	private float[] x;
	
	
	
	/**
	 * Processes the array of synapses on a single thread and core without SIMD.
	 */
	public abstract void process();
	
	/**
	 * Processes the array of synapses on a GPU.
	 */
	public abstract void processGPU();
	
	/**
	 * Processes the array of synapses on multiple threads and cores without SIMD.
	 */
	public abstract void processMT();
	
	/**
	 * Processes the array of synapses on multiple threads and cores with SIMD.
	 */
	public abstract void processSIMD();
	
	@Override
	public void run() {
		switch (neuralSystem.getProcessingMode()) {
		case GPU:
			processGPU();
			break;
		case MULTI_THREADED:
			processMT();
			break;
		case SINGLE_THREAD:
			process();
			break;
		case SIMD:
			processSIMD();
			break;
		default:
			process();
			break;
		}
	}
}
