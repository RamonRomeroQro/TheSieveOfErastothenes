
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <sys/types.h>


struct timeval startTime, stopTime;
int started = 0;

void start_timer() {
 started = 1;
 gettimeofday(&startTime, NULL);
}

double stop_timer() {
 long seconds, useconds;
 double duration = -1;

 if (started) {
   gettimeofday(&stopTime, NULL);
   seconds  = stopTime.tv_sec  - startTime.tv_sec;
   useconds = stopTime.tv_usec - startTime.tv_usec;
   duration = (seconds * 1000.0) + (useconds / 1000.0);
   started = 0;
 }
 return duration;
}




void  printprimes(int limit, int *arr) {
  int c;
  //#pragma omp parallel for shared(arr, limit) private(c)
  for(c = 2; c <limit; c++) {
      if(arr[c] == 0) {
          fprintf(stdout,"%d ", c);
      }
  }
  fprintf(stdout,"\n");
  /* code */
}



__global__ void init(int *arr, int sqroot, int limit) {
  int c;
  for(c = 2; c <= sqroot; c++) {
      if(arr[c] == 0) {
          /*
          #pragma omp parallel for shared(arr, limit, c) private(m)
          for(m = c+1; m < limit; m++) {
              if(m%c == 0) {
                  arr[m] = 1;
              }
          }
          */
          int tid = c+1+ threadIdx.x + (blockIdx.x * blockDim.x);
          if (tid<limit){
            if (tid % c ==0) {
          		arr[tid] = 1;
          	}
          }


      }
  }
}

double getThreadAndInfo(cudaDeviceProp devProp)
{
  /*
    printf("Major revision number:         %d\n",  devProp.major);
    printf("Minor revision number:         %d\n",  devProp.minor);
    printf("Name:                          %s\n",  devProp.name);
    printf("Total global memory:           %lu\n",  devProp.totalGlobalMem);
    printf("Total shared memory per block: %lu\n",  devProp.sharedMemPerBlock);
    printf("Total registers per block:     %d\n",  devProp.regsPerBlock);
    printf("Warp size:                     %d\n",  devProp.warpSize);
    printf("Maximum memory pitch:          %lu\n",  devProp.memPitch);
    printf("Maximum threads per block:     %i\n",  devProp.maxThreadsPerBlock);
    for (int i = 0; i < 3; ++i)
    printf("Maximum dimension %d of block:  %d\n", i, devProp.maxThreadsDim[i]);
    for (int i = 0; i < 3; ++i)
    printf("Maximum dimension %d of grid:   %d\n", i, devProp.maxGridSize[i]);
    printf("Clock rate:                    %d\n",  devProp.clockRate);
    printf("Total constant memory:         %lu\n",  devProp.totalConstMem);
    printf("Texture alignment:             %lu\n",  devProp.textureAlignment);
    printf("Concurrent copy and execution: %s\n",  (devProp.deviceOverlap ? "Yes" : "No"));
    printf("Number of multiprocessors:     %d\n",  devProp.multiProcessorCount);
    printf("Kernel execution timeout:      %s\n",  (devProp.kernelExecTimeoutEnabled ? "Yes" : "No"));
    */
    return devProp.maxThreadsPerBlock;
}

int main(int argc, char **argv) {
    // Number of CUDA devices
    int threads=1000000;
    int devCount;
    cudaGetDeviceCount(&devCount);
    //printf("CUDA Device Query...\n");
    //printf("There are %d CUDA devices.\n", devCount);

    // Iterate through devices
    for (int i = 0; i < devCount; ++i)
    {
        // Get device properties
        //printf("\nCUDA Device #%d\n", i);
        cudaDeviceProp devProp;
       cudaGetDeviceProperties(&devProp, i);
       if (getThreadAndInfo(devProp)<threads){
         threads=getThreadAndInfo(devProp);
       }

    }

    int N=10;
    int limit ;
    if (argc>3){
      fprintf(stderr, "Error: uso: %s [limite_superior_positivo]\n", argv[0]);
      return -1;

    }else if (argc==2 || argc==3) {
      int parsed=atoi(argv[1]);
      if (parsed<0){
        fprintf(stderr, "Error: uso: %s [limite_superior_positivo]\n", argv[0]);
        return -1;
      }else{
        limit=parsed;
      }
      if (argc==3) {
        N=1;
      }
    }else {
      limit=16;
    }




      int *arr;

      double ms;
      ms = 0;
      int i;
      int *p_array;
      for (i = 0; i < N; i++) {
        start_timer();
        //->
        int sqroot = (int)sqrt(limit);
        arr = (int*)malloc(limit * sizeof(int));
        cudaMalloc((void**) &p_array, limit * sizeof(int));
        cudaMemset(p_array, 0, limit*sizeof(int));
        if (limit<=threads){
		threads=limit;
          init<<<1, threads>>>(p_array, sqroot, limit);
        }else{
          init<<<int(limit/threads)+1, threads>>>(p_array, sqroot, limit);
        }



        cudaMemcpy(arr, p_array, limit * sizeof(int), cudaMemcpyDeviceToHost);

        //->
        ms += stop_timer();
      }


      if (argc==2){
        printf("times %i - avg time = %.5lf ms, %i threads\n",N,(ms / N), threads);
      }






      //




          printprimes(limit, arr);



      free(arr);

  cudaFree(p_array);


    return 0;
}

