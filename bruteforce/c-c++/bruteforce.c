
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
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








void initarr(int limit,int *a) {
  int c;
  //#pragma omp parallel for shared(a, limit) private(c)
  for(c = 2; c < limit; c++) {
      a[c] = 0;
  }
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



int main(int argc, char **argv) {
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
    for (i = 0; i < N; i++) {
      start_timer();
      //->
      arr = (int*)malloc(limit * sizeof(int));
      memset(arr,0,limit*sizeof(int));
      int c;

      for(c = 2; c < limit; c++) {
          int m;
          for ( m = 1; m < c; m++) {
            if (c%m==0 && m!=1 && m!=c ){
              arr[c]=1;
              break;
            }else{
              arr[c]=0;
            }
          }

      }









      //->
      ms += stop_timer();
    }

    if (argc==2){
      printf("times %i - avg time = %.5lf ms\n",N,(ms / N));
    }





    //



    printprimes(limit, arr);



    free(arr);

    return 0;
}
