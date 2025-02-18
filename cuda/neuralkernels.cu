extern "C"
__global__ void i2003(int n, float *a, float *b, float *c, float *d, float *I, float *u, float *v, float *vp, float *spike) {
	int i = blockIdx.x * blockDim.x + threadIdx.x;
	
	if (i < n) {
		v[i] += 0.5f * (0.04f * v[i] * v[i] + 5.0f * v[i] + 140.0f - u[i] + I[i]);
		v[i] += 0.5f * (0.04f * v[i] * v[i] + 5.0f * v[i] + 140.0f - u[i] + I[i]);
		u[i] += *a * (*b * v[i] - u[i]);
		spike[i] = (v[i] >= *vp);
		u[i] = (v[i] >= *vp) * (u[i] + *d) + (v[i] < *vp) * u[i];
		v[i] = (v[i] >= *vp) * *c + (v[i] < *vp) * v[i];
	}
}