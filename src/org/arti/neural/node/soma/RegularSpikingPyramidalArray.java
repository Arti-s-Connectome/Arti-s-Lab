package org.arti.neural.node.soma;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.arti.neural.node.NeuralNodeArray;

/**
 * <p>public class <b>RegularSpikingPyramidalArray</b><br>
 * extends {@link NeuralNodeArray}</p>
 * 
 * <p>RegularSpikingPyramidalArray class represents an array of neural nodes that use the regular spiking pyramidal neuron Izhikevich spiking 
 * model.</p>
 * 
 * @author Monroe Gordon
 * @version 1.0.0
 * @since JDK 22
 */
public class RegularSpikingPyramidalArray extends NeuralNodeArray {
	/**
	 * Default constructor. Creates an empty RegularSpikingPyramidalArray.
	 */
	public RegularSpikingPyramidalArray() {
		// Call parent constructor
		super();
	}
	
	@Override
	public float a() {
		return 0.03f;
	}

	@Override
	public float b() {
		return -2.0f;
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
		return -50.0f;
	}

	@Override
	public float C() {
		return 100.0f;
	}

	@Override
	public float cu() {
		return DEF_C_U;
	}

	@Override
	public float d() {
		return 100.0f;
	}
	
	@Override
	public float gc() {
		return 3.0f;
	}

	@Override
	public float gp() {
		return 5.0f;
	}

	@Override
	public float k() {
		return 0.7f;
	}

	@Override
	public void process() {
		for (int i = 0; i < nodes; ++i) {
			v[i] += (k * (v[i] - vr) * (v[i] - vt) - u[i] + I[i]) / C;
			u[i] += a * ((((v[i] >= bv) ? b : ba) * (float)Math.max(uvmin, Math.pow((v[i] - uv), upow)) + b2 * (float)Math.max(uvmin, v[i] - uv)) - u[i]);
			
			if (v[i] >= vp + vpu * u[i]) {
				spike[i] = 1.0f;
				v[i] = c + cu * u[i];
				u[i] = (float)Math.min(u[i] + d, umax);
			}
			else {
				spike[i] = 0.0f;
			}
		}
	}

	@Override
	public void processGPU() {
		// TODO Auto-generated method stub
		
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
					v[j] += (k * (v[j] - vr) * (v[j] - vt) - u[j] + I[j]) / C;
					u[j] += a * ((((v[j] >= bv) ? b : ba) * (float)Math.max(uvmin, Math.pow((v[j] - uv), upow)) + b2 * (float)Math.max(uvmin, v[j] - uv)) - 
							u[j]);
					
					if (v[j] >= vp + vpu * u[j]) {
						spike[j] = 1.0f;
						v[j] = c + cu * u[j];
						u[j] = (float)Math.min(u[j] + d, umax);
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
			v[j] += (k * (v[j] - vr) * (v[j] - vt) - u[j] + I[j]) / C;
			u[j] += a * ((((v[j] >= bv) ? b : ba) * (float)Math.max(uvmin, Math.pow((v[j] - uv), upow)) + b2 * (float)Math.max(uvmin, v[j] - uv)) - u[j]);
			
			if (v[j] >= vp + vpu * u[j]) {
				spike[j] = 1.0f;
				v[j] = c + cu * u[j];
				u[j] = (float)Math.min(u[j] + d, umax);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		for (int i = 0; i < nodes; ++i) {
			I[i] = 0.0f;
			u[i] = uInit();
			v[i] = vInit();
		}
	}

	@Override
	public float uInit() {
		return 0.0f;
	}

	@Override
	public float umax() {
		return DEF_U_MAX;
	}

	@Override
	public float upow() {
		return DEF_U_POW;
	}

	@Override
	public float uv() {
		return vr();
	}
	
	@Override
	public float uvmin() {
		return DEF_UV_MIN;
	}

	@Override
	public float vInit() {
		return vr();
	}
	
	@Override
	public float vp() {
		return 35.0f;
	}

	@Override
	public float vpu() {
		return DEF_VP_U;
	}

	@Override
	public float vr() {
		return -60.0f;
	}

	@Override
	public float vt() {
		return -40.0f;
	}
}
