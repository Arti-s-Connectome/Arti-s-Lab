package org.arti.neural.node;

import java.util.Arrays;
import java.util.HashMap;

import org.arti.neural.NeuralSystem;

/**
 * <p>public abstract class <b>NeuralNode</b><br>
 * extends {@link Object}<br>
 * implements {@link Runnable}</p>
 * 
 * <p>NeuralNodeArray class represents an array of a type of node in a neural network. A neural node may be a soma, dendrite, or axon 
 * compartment. All compartments are represented using the Izhikevich spiking model. This class is the base class for all subclasses. 
 * Subclasses define more specialized forms that model specific neuron soma and dendrite morphologies. NeuralNodeArray allows for processing 
 * multiple neural nodes of the same type in parallel by storing their values in arrays.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public abstract class NeuralNodeArray implements Runnable {
	/**
	 * The default coefficient for u in the membrane potential reset equation.
	 */
	public static final float DEF_C_U = 0.0f;
	/**
	 * The default maximum recovery value.
	 */
	public static final float DEF_U_MAX = Float.MAX_VALUE;
	/**
	 * The default minimum membrane potential difference in the u equation.
	 */
	public static final float DEF_UV_MIN = -Float.MAX_VALUE;
	/**
	 * The default exponential power used in the u equation.
	 */
	public static final float DEF_U_POW = 1.0f;
	/**
	 * The default coefficient for u in the spike cutoff equation.
	 */
	public static final float DEF_VP_U = 0.0f;
	
	/** 
	 * The recovery time constant.
	 */
	protected float a;
	/**
	 * The amplification/resonance control variable.
	 */
	protected float b;
	/**
	 * The second voltage-dependent b value.
	 */
	protected float b2;
	/**
	 * Alternate, voltage-dependent b value.
	 */
	protected float ba;
	/**
	 * Voltage threshold for alternate b value.
	 */
	protected float bv;
	/**
	 * The membrane potential reset value.
	 */
	protected float c;
	/**
	 * The membrane capacitance.
	 */
	protected float C;
	/**
	 * The left child neural node data.
	 */
	protected NodeData childLeft;
	/**
	 * The right child neural node data.
	 */
	protected NodeData childRight;
	/**
	 * The coefficient for u in the membrane potential reset equation.
	 */
	protected float cu;
	/**
	 * The total outward minus inward current during a spike.
	 */
	protected float d;
	/**
	 * The conductance from child neural nodes.
	 */
	protected float gc;
	/**
	 * The conductance from parent neural nodes.
	 */
	protected float gp;
	/**
	 * The total input current.
	 */
	protected float[] I;
	/**
	 * Neural nodes' neural IDs.
	 */
	protected HashMap<Integer, Long> id;
	/**
	 * The NeuralSystem controlling the processing of neural nodes and networks.
	 */
	protected NeuralSystem neuralSystem;
	/**
	 * The number of neural nodes.
	 */
	protected int nodes;
	/**
	 * The parent neural node data.
	 */
	protected NodeData parent;
	/**
	 * The coefficient of the square polynomial.
	 */
	protected float k;
	/**
	 * Spike fired flag.
	 */
	protected float[] spike;
	/**
	 * The membrane resting potential value.
	 */
	protected float vr;
	/**
	 * The instantaneous threshold potential value.
	 */
	protected float vt;
	/**
	 * The spike cutoff value.
	 */
	protected float vp;
	/**
	 * The coefficient for u in the spike cutoff equation.
	 */
	protected float vpu;
	/**
	 * The recovery current value.
	 */
	protected float[] u;
	/**
	 * The maximum recovery value.
	 */
	protected float umax;
	/**
	 * The exponential power used in the u equation.
	 */
	protected float upow;
	/**
	 * The membrane potential value added to the current membrane potential in the u equation.
	 */
	protected float uv;
	/**
	 * The minimum membrane potential difference value in the u equation.
	 */
	protected float uvmin;
	/**
	 * The membrane potential value.
	 */
	protected float[] v;
	
	/**
	 * Default constructor. Creates an empty NeuralNodeArray.
	 */
	protected NeuralNodeArray() {
		// Initialize variables
		neuralSystem = NeuralSystem.getInstance();
		nodes = 0;
		a = a();
		b = b();
		b2 = b2();
		ba = ba();
		bv = bv();
		c = c();
		d = d();
		I = new float[nodes];
		C = C();
		k = k();
		vr = vr();
		vt = vt();
		vp = vp();
		u = new float[nodes];
		v = new float[nodes];
		spike = new float[nodes];
		cu = cu();
		vpu = vpu();
		umax = umax();
		upow = upow();
		uv = uv();
		uvmin = uvmin();
		gc = gc();
		gp = gp();
		childLeft = null;
		childRight = null;
		parent = null;
		id = new HashMap<Integer, Long>();
	}
	
	/**
	 * Returns the value of a that this NeuralNodeArray uses.
	 * @return The value of a.
	 */
	public abstract float a();
	
	/**
	 * Adds the specified input current to the total input current of the neural node at the specified index.
	 * @param index - The index of the neural node.
	 * @param I - The input current to add.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public void addI(int index, float I) {
		if (index < 0 || index >= nodes)
			throw new IndexOutOfBoundsException("Error: Cannot add to I at " + index + ". Index out of bounds.");
		
		this.I[index] += I;
	}
	
	/**
	 * Adds a neural node to this NeuralNodeArray.
	 * @param name - The new neural node's name.
	 */
	public void addNode(String name) {
		float[] ITemp = new float[nodes];
		float[] uTemp = new float[nodes];
		float[] vTemp = new float[nodes];
		float[] spikeTemp = new float[nodes];
		
		for (int i = 0; i < nodes; ++ i) {
			ITemp[i] = I[i];
			uTemp[i] = u[i];
			vTemp[i] = v[i];
			spikeTemp[i] = spike[i];
		}
		
		I = new float[nodes + 1];
		u = new float[nodes + 1];
		v = new float[nodes + 1];
		spike = new float[nodes + 1];
		
		for (int i = 0; i < nodes; ++i) {
			I[i] = ITemp[i];
			u[i] = uTemp[i];
			v[i] = vTemp[i];
			spike[i] = spikeTemp[i];
		}
		
		I[nodes] = 0.0f;
		u[nodes] = uInit();
		v[nodes] = vInit();
		spike[nodes] = 0.0f;
		
		id.put(nodes, neuralSystem.addNode(this, nodes, name));
		
		nodes++;
	}
	
	/**
	 * Returns the value of b that this NeuralNodeArray uses.
	 * @return The value of b.
	 */
	public abstract float b();
	
	/**
	 * Returns the second voltage-dependent value of b that this NeuralNodeArray uses.
	 * @return The value of b2.
	 */
	public abstract float b2();
	
	/**
	 * Returns the alternate, voltage-dependent value of b that this NeuralNodeArray uses.
	 * @return The value of ba.
	 */
	public abstract float ba();
	
	/**
	 * Returns the voltage threshold for the alternate, voltage-dependent value of b that this NeuralNodeArray uses.
	 * @return The value of bv.
	 */
	public abstract float bv();
	
	/**
	 * Returns the value of c that this NeuralNodeArray uses.
	 * @return The value of c.
	 */
	public abstract float c();
	
	/**
	 * Returns the value of C that this NeuralNodeArray uses.
	 * @return The value of C.
	 */
	public abstract float C();
	
	/**
	 * Returns the value of cu that this NeuralNodeArray uses.
	 * @return The value of cu.
	 */
	public abstract float cu();
	
	/**
	 * Returns the value of d that this NeuralNodeArray uses.
	 * @return The value of d.
	 */
	public abstract float d();
	
	/**
	 * Returns the value of gc that this NeuralNodeArray uses.
	 * @return The value of gc.
	 */
	public abstract float gc();
	
	/**
	 * Returns the value of gp that this NeuralNodeArray uses.
	 * @return The value of gp.
	 */
	public abstract float gp();
	
	/**
	 * Returns the value of I at the specified index.
	 * @param index - The index of the neural node.
	 * @return The value of I at index.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public float I(int index) {
		if (index < 0 || index >= nodes)
			throw new IndexOutOfBoundsException("Error: Cannot get I at " + index + ". Index out of bounds.");
		
		return I[index];
	}
	
	/**
	 * Returns the value of k that this NeuralNodeArray uses.
	 * @return The value of k.
	 */
	public abstract float k();
	
	/**
	 * Returns the node data for the left child neural node.
	 * @return The left child node data.
	 */
	public NodeData leftChild() {
		return childLeft;
	}
	
	/**
	 * Returns the number of neural nodes in this neural node array.
	 * @return The number of neural nodes.
	 */
	public int nodes() {
		return nodes;
	}
	
	/**
	 * Returns the node data for the parent neural node.
	 * @return The parent node data.
	 */
	public NodeData parent() {
		return childLeft;
	}
	
	/**
	 * Processes the array of neural nodes on a single thread and core without SIMD.
	 */
	public abstract void process();
	
	/**
	 * Processes the array of neural nodes on a GPU.
	 */
	public abstract void processGPU();
	
	/**
	 * Processes the array of neural nodes on multiple threads and cores without SIMD.
	 */
	public abstract void processMT();
	
	/**
	 * Processes the array of neural nodes on multiple threads and cores with SIMD.
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
	
	/**
	 * Removes the neural node at the specified index.
	 * @param index - The index of the neural node.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public void removeNode(int index) {
		if (index < 0 || index >= nodes)
			throw new IndexOutOfBoundsException("Error: Cannot remove neural node at " + index + ". Index out of bounds.");
		
		float[] ITemp = new float[nodes];
		float[] uTemp = new float[nodes];
		float[] vTemp = new float[nodes];
		float[] spikeTemp = new float[nodes];
		
		for (int i = 0; i < nodes; ++ i) {
			ITemp[i] = I[i];
			uTemp[i] = u[i];
			vTemp[i] = v[i];
			spikeTemp[i] = spike[i];
		}
		
		I = new float[nodes - 1];
		u = new float[nodes - 1];
		v = new float[nodes - 1];
		spike = new float[nodes - 1];
		
		neuralSystem.removeNode(id.get(index));
		
		for (int i = 0; i < nodes; ++i) {
			if (i < index) {
				I[i] = ITemp[i];
				u[i] = uTemp[i];
				v[i] = vTemp[i];
				spike[i] = spikeTemp[i];
			}
			else if (i > index) {
				I[i - 1] = ITemp[i];
				u[i - 1] = uTemp[i];
				v[i - 1] = vTemp[i];
				spike[i - 1] = spikeTemp[i];
				neuralSystem.setNodeIndex(id.get(i), i - 1);
				id.replace(i - 1, id.get(i));
			}
		}
		
		id.remove(nodes);
		
		nodes--;
	}
	
	/**
	 * Renames the neural node with the specified ID value to the specified name.
	 * @param id - The ID value of the neural node to rename.
	 * @param name - The new name of the neural node.
	 */
	public void rename(long id, String name) {
		neuralSystem.renameNode(id, name);
	}
	
	/**
	 * Renames the neural node at the specified index to the specified name.
	 * @param index - The index of the neural node to rename.
	 * @param name - The new name of the neural node.
	 */
	public void rename(int index, String name) {
		neuralSystem.renameNode(id.get(index), name);
	}
	
	/**
	 * Resets all values of all neural nodes to their initial values.
	 */
	public abstract void reset();
	
	/**
	 * Resets the I value of all neural nodes to 0.
	 */
	public void resetI() {
		Arrays.fill(I, 0.0f);
	}
	
	/**
	 * Returns the node data for the right child neural node.
	 * @return The right child node data.
	 */
	public NodeData rightChild() {
		return childRight;
	}
	
	/**
	 * Sets the left child neural node to the specified node data.
	 * @param node - The node data for the left child neural node.
	 */
	public void setLeftChild(NodeData node) {
		childLeft = node;
	}
	
	/**
	 * Sets the left child neural node to the node data of the specified neural node ID value.
	 * @param id - The neural node ID value.
	 */
	public void setLeftChild(Long id) {
		childLeft = (id == null) ? null : neuralSystem.getNodeData(id);
	}
	
	/**
	 * Sets the parent neural node to the specified node data.
	 * @param node - The node data for the parent neural node.
	 */
	public void setParent(NodeData node) {
		childLeft = node;
	}
	
	/**
	 * Sets the parent neural node to the node data of the specified neural node ID value.
	 * @param id - The neural node ID value.
	 */
	public void setParent(Long id) {
		parent = (id == null) ? null : neuralSystem.getNodeData(id);
	}
	
	/**
	 * Sets the right child neural node to the specified node data.
	 * @param node - The node data for the right child neural node.
	 */
	public void setRightChild(NodeData node) {
		childRight = node;
	}
	
	/**
	 * Sets the right child neural node to the node data of the specified neural node ID value.
	 * @param id - The neural node ID value.
	 */
	public void setRightChild(Long id) {
		childRight = (id == null) ? null : neuralSystem.getNodeData(id);
	}
	
	/**
	 * Returns the value of spike at the specified index.
	 * @param index - The index of the neural node.
	 * @return The value of spike at index.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public float spike(int index) {
		if (index < 0 || index >= nodes)
			throw new IndexOutOfBoundsException("Error: Cannot get spike at " + index + ". Index out of bounds.");
		
		return spike[index];
	}
	
	/**
	 * Returns the value of u at the specified index.
	 * @param index - The index of the neural node.
	 * @return The value of u at index.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public float u(int index) {
		if (index < 0 || index >= nodes)
			throw new IndexOutOfBoundsException("Error: Cannot get u at " + index + ". Index out of bounds.");
		
		return u[index];
	}
	
	/**
	 * Returns the value of u for a new neural node.
	 * @return A new neural node's u value.
	 */
	public abstract float uInit();
	
	/**
	 * Returns the value of umax that this NeuralNodeArray uses.
	 * @return The value of umax.
	 */
	public abstract float umax();
	
	/**
	 * Returns the value of upow that this NeuralNodeArray uses.
	 * @return The value of upow.
	 */
	public abstract float upow();
	
	/**
	 * Returns the value of uv that this NeuralNodeArray uses.
	 * @return The value of uv.
	 */
	public abstract float uv();
	
	/**
	 * Returns the value of uvmin that this NeuralNodeArray uses.
	 * @return The value of uvmin.
	 */
	public abstract float uvmin();
	
	/**
	 * Returns the value of v at the specified index.
	 * @param index - The index of the neural node.
	 * @return The value of v at index.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public float v(int index) {
		if (index < 0 || index >= nodes)
			throw new IndexOutOfBoundsException("Error: Cannot get v at " + index + ". Index out of bounds.");
		
		return v[index];
	}
	
	/**
	 * Returns the value of v for a new neural node.
	 * @return A new neural node's v value.
	 */
	public abstract float vInit();
	
	/**
	 * Returns the value of vp that this NeuralNodeArray uses.
	 * @return The value of vp.
	 */
	public abstract float vp();
	
	/**
	 * Returns the value of vpu that this NeuralNodeArray uses.
	 * @return The value of vpu.
	 */
	public abstract float vpu();
	
	/**
	 * Returns the value of vr that this NeuralNodeArray uses.
	 * @return The value of vr.
	 */
	public abstract float vr();
	
	/**
	 * Returns the value of vt that this NeuralNodeArray uses.
	 * @return The value of vt.
	 */
	public abstract float vt();
}
