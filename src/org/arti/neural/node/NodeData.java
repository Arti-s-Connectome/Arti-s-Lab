package org.arti.neural.node;

/**
 * <p>public class <b>NodeData</b><br>
 * extends {@link Object}</p>
 * 
 * <p>NodeData class contains data for a neural node. It contains the neural node's name, the NeuralNodeArray it belongs to, and its index in
 * that NeuralNodeArray.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class NodeData {
	// The NeuralNodeArray the neural node belongs to.
	private NeuralNodeArray array;
	// The index in the NeuralNodeArray that the neural node belongs to.
	private int index;
	// The name of the neural node.
	private String name;
	
	/**
	 * Creates a new NodeData with the specified NeuralNodeArray, NeuralNodeArray index, and name.
	 * @param array - The NeuralNodeArray the neural node belongs to.
	 * @param index - The index in the NeuralNodeArray that the neural node belongs to.
	 * @param name - The name of the neural node.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 * @throws NullPointerException Thrown if array is null.
	 */
	public NodeData(NeuralNodeArray array, int index, String name) {
		// Check parameters
		if (array == null)
			throw new NullPointerException("Error: NodeData's NeuraNodeArray cannot be null.");
		
		if (index < 0 || index >= array.nodes())
			throw new IndexOutOfBoundsException("Error: NodeData's index is out of bounds.");
		
		// Initialize variables
		this.array = array;
		this.index = index;
		this.name = (name == null) ? "" : name;
	}
	
	/**
	 * Returns the NeuralNodeArray that this neural node belongs to.
	 * @return The NeuralNodeArray.
	 */
	public NeuralNodeArray getNeuralNodeArray() {
		return array;
	}
	
	/**
	 * Returns the index in the NeuralNodeArray that this neural node belongs to.
	 * @return The index in the NeuralNodeArray.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns the name of this neural node.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Renames this neural node to the specified name.
	 * @param name - The new name of the neural node.
	 */
	public void rename(String name) {
		this.name = (name == null) ? "" : name;
	}
	
	/**
	 * Sets the index in the NeuralNodeArray that this neural node belongs to.
	 * @param index - The new index.
	 * @throws IndexOutOfBoundsException Thrown if index is out of bounds.
	 */
	public void setIndex(int index) {
		if (index < 0 || index >= array.nodes())
			throw new IndexOutOfBoundsException("Error: NodeData's index is out of bounds.");
		
		this.index = index;
	}
}
