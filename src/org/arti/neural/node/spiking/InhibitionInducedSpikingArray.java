package org.arti.neural.node.spiking;

import static jcuda.driver.JCudaDriver.cuCtxSynchronize;
import static jcuda.driver.JCudaDriver.cuLaunchKernel;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyDtoH;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.arti.neural.node.NeuralNodeArray;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * <p>public class <b>InhibitionInducedSpikingArray</b><br>
 * extends {@link NeuralNodeArray}</p>
 * 
 * <p>InhibitionInducedSpikingArray class represents an array of neural nodes that use the inhibition-induced spiking Izhikevich spiking 
 * model.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class InhibitionInducedSpikingArray extends NeuralNodeArray {
	// Initial membrane potential.
	private static final float V_INIT = -63.8f;
	
	/**
	 * Default constructor. Creates an empty InhibitionInducedSpikingArray.
	 */
	public InhibitionInducedSpikingArray() {
		// Call parent constructor.
		super();
	}
	
	@Override
	public float a() {
		return -0.02f;
	}

	@Override
	public float b() {
		return -1.0f;
	}
	
	@Override
	public float b2() {
		return 0.0f;
	}
	
	@Override
	public float ba() {
		return b();
	}
	
	@Override
	public float bv() {
		return 0.0f;
	}

	@Override
	public float c() {
		return -60.0f;
	}

	@Override
	public float C() {
		return 0.0f;
	}

	@Override
	public float cu() {
		return 0.0f;
	}

	@Override
	public float d() {
		return 8.0f;
	}
	
	@Override
	public float gc() {
		return 1.0f;
	}

	@Override
	public float gp() {
		return 1.0f;
	}

	@Override
	public float k() {
		return 0.0f;
	}

	@Override
	public void process() {
		for (int i = 0; i < nodes; ++i) {
			v[i] += 0.5f * (0.04f * v[i] * v[i] + 5.0f * v[i] + 140.0f - u[i] + I[i]);
			v[i] += 0.5f * (0.04f * v[i] * v[i] + 5.0f * v[i] + 140.0f - u[i] + I[i]);
			u[i] += a * (b * v[i] - u[i]);
			
			if (v[i] >= vp) {
				spike[i] = 1.0f;
				v[i] = c;
				u[i] += d;
			}
			else {
				spike[i] = 0.0f;
			}
		}
	}
	
	@Override
	public void processGPU() {
		// Load kernel function
		CUfunction function = new CUfunction();
		cuModuleGetFunction(function, neuralSystem.getCudaModule(), "i2003");
		
		// Allocate device memory
		CUdeviceptr dI = new CUdeviceptr();
		cuMemAlloc(dI, nodes * Sizeof.FLOAT);
		cuMemcpyHtoD(dI, Pointer.to(I), nodes * Sizeof.FLOAT);
		
		CUdeviceptr du = new CUdeviceptr();
		cuMemAlloc(du, nodes * Sizeof.FLOAT);
		cuMemcpyHtoD(du, Pointer.to(u), nodes * Sizeof.FLOAT);
		
		CUdeviceptr dv = new CUdeviceptr();
		cuMemAlloc(dv, nodes * Sizeof.FLOAT);
		cuMemcpyHtoD(dv, Pointer.to(v), nodes * Sizeof.FLOAT);
		
		CUdeviceptr dspike = new CUdeviceptr();
		cuMemAlloc(dspike, nodes * Sizeof.FLOAT);
		cuMemcpyHtoD(dspike, Pointer.to(spike), nodes * Sizeof.FLOAT);
		
		// Collect kernel parameters
		Pointer kernelParameters = Pointer.to(
				Pointer.to(new int[] {nodes}),
				Pointer.to(new float[] {a}),
				Pointer.to(new float[] {b}),
				Pointer.to(new float[] {c}),
				Pointer.to(new float[] {d}),
				Pointer.to(dI),
				Pointer.to(du),
				Pointer.to(dv),
				Pointer.to(new float[] {vp}),
				Pointer.to(dspike));
		
		// Launch kernel
		int blockSizeX = 256;
		int gridSizeX = (int)Math.ceil((double)nodes / blockSizeX);
		
		cuLaunchKernel(function,
				gridSizeX, 1, 1,
				blockSizeX, 1, 1,
				0, null,
				kernelParameters, null);
		
		// Wait for all threads to complete
		cuCtxSynchronize();
		
		// Copy device outputs to host memory
		cuMemcpyDtoH(Pointer.to(u), du, nodes * Sizeof.FLOAT);
		cuMemcpyDtoH(Pointer.to(v), dv, nodes * Sizeof.FLOAT);
		cuMemcpyDtoH(Pointer.to(spike), dspike, nodes * Sizeof.FLOAT);
		
		// Free device memory
		cuMemFree(dI);
		cuMemFree(du);
		cuMemFree(dv);
		cuMemFree(dspike);
	}
	
	@Override
	public void processMT() {
		int threads = Math.min(neuralSystem.getMaxThreads(), (int)(nodes / neuralSystem.getI2003NodesPerThread() + 1));
		int batchSize = nodes / threads;
		int remainder = nodes - (threads * batchSize);
		ExecutorService executorService = Executors.newFixedThreadPool(threads);
		Runnable[] task = new Runnable[threads];
		
		// Process threads
		for (int i = 0; i < threads; ++i) {
			int index = i;
			
			task[i] = () -> {
				for (int j = (index * batchSize); j < (index * batchSize) + batchSize; ++j) {
					v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
					v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
					u[j] += a * (b * v[j] - u[j]);
					
					if (v[j] >= vp) {
						spike[j] = 1.0f;
						v[j] = c;
						u[j] += d;
					}
					else {
						spike[j] = 0.0f;
					}
				}
			};
			
			executorService.execute(task[i]);
		}
		
		// Process remainder of nodes
		for (int j = nodes - remainder; j < nodes; ++j) {
			v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
			v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
			u[j] += a * (b * v[j] - u[j]);
			
			if (v[j] >= vp) {
				spike[j] = 1.0f;
				v[j] = c;
				u[j] += d;
			}
			else {
				spike[j] = 0.0f;
			}
		}
		
		// Shutdown executor service
		executorService.shutdown();
		try {
		    if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
		        executorService.shutdownNow();
		    } 
		} catch (InterruptedException e) {
		    executorService.shutdownNow();
		}
	}
	
	@Override
	public void processSIMD() {
		int threads = Math.min(neuralSystem.getMaxThreads(), (int)(nodes / neuralSystem.getI2003NodesPerThread() + 1));
		int batchSize = nodes / threads;
		int remainder = nodes - (threads * batchSize);
		ExecutorService executorService = Executors.newFixedThreadPool(threads);
		Runnable[] task = new Runnable[threads];
		
		// Process threads
		for (int i = 0; i < threads; ++i) {
			int index = i;
			
			task[i] = () -> {
				VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;
				int j = 0;
				int upperBound = species.loopBound(index * batchSize + batchSize);
				
				// Process SIMD loop
				for (; j < upperBound; j += species.length()) {
					var indexMask = species.indexInRange(index * batchSize + j, nodes);
					var vI = FloatVector.fromArray(species, I, index * batchSize + j, indexMask);
					var vv = FloatVector.fromArray(species, v, index * batchSize + j, indexMask);
					var vu = FloatVector.fromArray(species, u, index * batchSize + j, indexMask);
					var vspike = FloatVector.fromArray(species, spike, index * batchSize + j, indexMask);
					vv = vv.add(vI.sub(vu).add(140.0f).add(vv.mul(5.0f)).add(vv.pow(2.0f).mul(0.04f)).mul(0.5f));
					vv = vv.add(vI.sub(vu).add(140.0f).add(vv.mul(5.0f)).add(vv.pow(2.0f).mul(0.04f)).mul(0.5f));
					vu = vu.add(vv.mul(b).sub(vu).mul(a));
					var spikeMask = vv.lt(vp).not();
					vv.blend(c, spikeMask);
					vu.blend(vu.add(d), spikeMask);
					vspike.blend(1.0f, spikeMask);
					vspike.blend(0.0f, spikeMask.not());
					vv.intoArray(v, index * batchSize + j, indexMask);
					vu.intoArray(u, index * batchSize + j, indexMask);
					vspike.intoArray(spike, index * batchSize + j, indexMask);
				}
				
				// Process loop tail
				for (; j < upperBound; ++j) {
					v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
					v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
					u[j] += a * (b * v[j] - u[j]);
					
					if (v[j] >= vp) {
						spike[j] = 1.0f;
						v[j] = c;
						u[j] += d;
					}
					else {
						spike[j] = 0.0f;
					}
				}
			};
			
			executorService.execute(task[i]);
		}
		
		// Process remainder of nodes
		for (int j = nodes - remainder; j < nodes; ++j) {
			v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
			v[j] += 0.5f * (0.04f * v[j] * v[j] + 5.0f * v[j] + 140.0f - u[j] + I[j]);
			u[j] += a * (b * v[j] - u[j]);
			
			if (v[j] >= vp) {
				spike[j] = 1.0f;
				v[j] = c;
				u[j] += d;
			}
			else {
				spike[j] = 0.0f;
			}
		}
		
		// Shutdown executor service
		executorService.shutdown();
		try {
		    if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
		        executorService.shutdownNow();
		    } 
		} catch (InterruptedException e) {
		    executorService.shutdownNow();
		}
	}

	@Override
	public void reset() {
		for (int i = 0; i < nodes; ++i) {
			I[i] = 0.0f;
			u[i] = b() * vp();
			v[i] = V_INIT;
		}
	}
	
	@Override
	public float uInit() {
		return b() * V_INIT;
	}
	
	@Override
	public float umax() {
		return 0.0f;
	}
	
	@Override
	public float upow() {
		return 0.0f;
	}
	
	@Override
	public float uv() {
		return 0.0f;
	}
	
	@Override
	public float uvmin() {
		return 0.0f;
	}
	
	@Override
	public float vInit() {
		return V_INIT;
	}
	
	@Override
	public float vp() {
		return 30.0f;
	}
	
	@Override
	public float vpu() {
		return 0.0f;
	}
	
	@Override
	public float vr() {
		return 0.0f;
	}

	@Override
	public float vt() {
		return 0.0f;
	}
}
