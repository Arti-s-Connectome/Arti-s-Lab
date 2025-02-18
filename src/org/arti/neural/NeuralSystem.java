package org.arti.neural;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.cuModuleLoad;
import static jcuda.runtime.JCuda.cudaGetDeviceCount;
import static jcuda.runtime.cudaError.cudaSuccess;

import java.util.HashMap;

import org.arti.neural.node.NeuralNodeArray;
import org.arti.neural.node.NodeData;

import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;
import jdk.incubator.vector.VectorShape;

/**
 * <p>public class <b>NeuralSystem</b><br>
 * extends {@link Object}</p>
 * 
 * <p>NeuralSystem class represents the system controlling the processing of neural nodes and networks. This class controls settings for the
 * number of neural nodes processed per thread, which processing mode to use, and optimizing for near real-time processing.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class NeuralSystem {
	// The single runtime instance of NeuralSystem.
	private static NeuralSystem INSTANCE = null;
	
	// The neuralkernels.ptx file location.
	private static final String NEURALKERNELS_PTX = "cuda/neuralkernels.ptx";
	
	/**
	 * <p>private enum <b>ProcessingMode</b></p>
	 * 
	 * <p>ProcessingMode enum lists the possible ways the NeuralSystem can process neural nodes and networks, based on hardware availability.
	 * 
	 * @author Monroe Gordon
	 * @version 1.0.0
	 * @since JDK 22
	 */
	public enum ProcessingMode {
		/** 
		 * Process in GPU mode.
		 */
		GPU,
		/**
		 * Process in multi-threaded mode.
		 */
		MULTI_THREADED,
		/**
		 * Process in single thread mode.
		 */
		SINGLE_THREAD,
		/**
		 * Process in multi-threaded SIMD mode.
		 */
		SIMD
	}
	
	// The number of available CPU cores at runtime.
	private int cpuCores;
	// A Cuda context.
	private CUcontext cuContext;
	// A Cuda device.
	private CUdevice cuDevice;
	// A Cuda module for loading the PTX file.
	private CUmodule cuModule;
	// The number of Izhikevich 2003 spiking model neural nodes to run per thread.
	private long i2003NodesPerThread;
	// The next neural ID value.
	private long nextID;
	// Hash map of neural node id values and data.
	private HashMap<Long, NodeData> node;
	// The processing mode.
	private ProcessingMode processMode;
	
	/**
	 * Default constructor. Creates a NeuralSystem.
	 */
	private NeuralSystem() {
		// Initialize variables
		cpuCores = Runtime.getRuntime().availableProcessors();
		cuDevice = null;
		cuContext = null;
		cuModule = null;
		i2003NodesPerThread = 100000;
		nextID = 0;
		node = new HashMap<Long, NodeData>();
		processMode = ProcessingMode.SINGLE_THREAD;
		
		// Determine processing mode based on hardware availability
		JCudaDriver.setExceptionsEnabled(true);
		int[] deviceCount = { 0 };
		cudaGetDeviceCount(deviceCount);
		
		if (deviceCount[0] > 0 && cuInit(0) == cudaSuccess) {
			processMode = ProcessingMode.GPU;
			
			// Set Cuda device and context
			cuDevice = new CUdevice();
			cuDeviceGet(cuDevice, 0);
			cuContext = new CUcontext();
			cuCtxCreate(cuContext, 0, cuDevice);
			
			// Load PTX file
			cuModule = new CUmodule();
			cuModuleLoad(cuModule, NEURALKERNELS_PTX);
		}
		else if (cpuCores > 1) {
			try {
				VectorShape.preferredShape();
				processMode = ProcessingMode.SIMD;
			}
			catch (Exception e) {
				processMode = ProcessingMode.MULTI_THREADED;
			}
		}
		else {
			processMode = ProcessingMode.SINGLE_THREAD;
		}
	}
	
	/**
	 * Adds a new neural node with the specified name to the hash map of neural nodes. A new ID value is created for the new neural node and
	 * returned from this method.
	 * @param array - The NeuralNodeArray the new neural node belongs to.
	 * @param index - The index in the NeuralNodeArray that the new neural node belongs to.
	 * @param name - The name of the new neural node.
	 * @return The ID value of the new neural node.
	 */
	public long addNode(NeuralNodeArray array, int index, String name) {
		nextID++;
		node.put(nextID - 1, new NodeData(array, index, name));
		return nextID - 1;
	}
	
	/**
	 * Returns the number of CPU cores available at runtime.
	 * @return the number of CPU cores available at runtime.
	 */
	public int getCpuCores() {
		return cpuCores;
	}
	
	/**
	 * Returns the Cuda context currently in use, if any.
	 * @return The Cuda context.
	 */
	public CUcontext getCudaContext() {
		return cuContext;
	}
	
	/**
	 * Returns the Cuda device currently in use, if any.
	 * @return The Cuda device.
	 */
	public CUdevice getCudaDevice() {
		return cuDevice;
	}
	
	/**
	 * Returns the Cuda module currently in use, if any.
	 * @return The Cuda module.
	 */
	public CUmodule getCudaModule() {
		return cuModule;
	}
	
	/**
	 * Returns the number of Izhikevich 2003 spiking model neural nodes that can be processed per thread.
	 * @return The number of Izhikevich 2003 spiking model neural nodes that can be processed per thread.
	 */
	public long getI2003NodesPerThread() {
		return i2003NodesPerThread;
	}
	
	/**
	 * Returns the single runtime instance of NeuralSystem. If an instance does not exist, one is created and returned.
	 * @return The single runtime instance of NeuralSystem.
	 */
	public static NeuralSystem getInstance() {
		if (INSTANCE == null)
			INSTANCE = new NeuralSystem();
		
		return INSTANCE;
	}
	
	/**
	 * Returns the next ID value for a neural ID.
	 * @return The next ID value.
	 */
	public long getNextID() {
		return nextID;
	}
	
	/**
	 * Returns the node data of the neural node with the specified ID value.
	 * @param id - The ID value of the neural node.
	 * @return The node data of the neural node with the id value.
	 */
	public NodeData getNodeData(long id) {
		return node.get(id);
	}
	
	/**
	 * Returns the index of the neural node with the specified ID value.
	 * @param id - The ID of the neural node.
	 * @return The index of the neural node with the id value.
	 */
	public int getNodeIndex(long id) {
		return node.get(id).getIndex();
	}
	
	/**
	 * Returns the name of the neural node with the specified ID value.
	 * @param id - The ID of the neural node.
	 * @return The name of the neural node with the id value.
	 */
	public String getNodeName(long id) {
		return node.get(id).getName();
	}
	
	/**
	 * Returns the NeuralNodeArray of the neural node with the specified ID value.
	 * @param id - The ID of the neural node.
	 * @return The NeuralNodeArray of the neural node with the id value.
	 */
	public NeuralNodeArray getNodeNeuralNodeArray(long id) {
		return node.get(id).getNeuralNodeArray();
	}
	
	/**
	 * Returns the processing mode that the NeuralSystem is using to process neural nodes and networks.
	 * @return The processing mode.
	 */
	public ProcessingMode getProcessingMode() {
		return processMode;
	}
	
	/**
	 * Returns the maximum number of threads that can be created at a time.
	 * @return The maximum number of threads.
	 */
	public int getMaxThreads() {
		return Math.max(1, cpuCores / 2);
	}
	
	/**
	 * Removes the neural node from the hash map that has the specified ID value.
	 * @param id - The ID value of the neural node to remove.
	 */
	public void removeNode(long id) {
		node.remove(id);
	}
	
	/**
	 * Renames the neural node with the specified ID value to the specified name.
	 * @param id - The ID value of the neural node to rename.
	 * @param name - The new name of the neural node.
	 */
	public void renameNode(long id, String name) {
		node.get(id).rename(name);;
	}
	
	/**
	 * Sets the index of the neural node with the specified ID value.
	 * @param id - The ID value of the neural node.
	 * @param index - The new index.
	 */
	public void setNodeIndex(long id, int index) {
		node.get(id).setIndex(index);
	}
}
