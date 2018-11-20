#include <math.h>
#include <iostream>
#include <tbb/parallel_for.h>
#include <tbb/parallel_reduce.h>




using namespace std;
using namespace tbb;

const int GRAIN = 100000;



const int N = 10;
const int DISPLAY = 100;
const int MAX_VALUE = 10000;

class Timer {
private:
    timeval startTime;
    bool 	started;

public:
    Timer() :started(false) {}

    void start(){
    	started = true;
        gettimeofday(&startTime, NULL);
    }

    double stop(){
        timeval endTime;
        long seconds, useconds;
        double duration = -1;

        if (started) {
			gettimeofday(&endTime, NULL);

			seconds  = endTime.tv_sec  - startTime.tv_sec;
			useconds = endTime.tv_usec - startTime.tv_usec;

			duration = (seconds * 1000.0) + (useconds / 1000.0);
			started = false;
        }
		return duration;
    }
};

/*
State Pattern for Initialization


class ParallelSieve {

public:
	int *myArray;
	int c;
	int state;
	ParallelSieve(int *array, int cm, int s) : myArray(array), c(cm), state(s) {}

	ParallelSieve(ParallelSieve &x, split)
		: myArray(x.myArray), c(x.c) , state(x.state) {}
	void operator() (const blocked_range<int> &r) {
		if (state==0){
			for (int i = r.begin(); i < r.end(); i++) {
						myArray[i] = 0;
		}
	}	else{
			for (int i = r.begin()+1; i != r.end(); i++) {
				myArray[c] = 0;
				if(i%c == 0) {
						myArray[i] = 1;

				}
		}
	}
}




	void join(const ParallelSieve &x) {
		myArray = x.myArray;
	}
};


*/


class ParallelSieve {

public:
	int *myArray;
	int c;
	ParallelSieve(int *array, int cm) : myArray(array), c(cm)  {}

	ParallelSieve(ParallelSieve &x, split)
		: myArray(x.myArray), c(x.c) {}
	void operator() (const blocked_range<int> &r) {

			for (int i = r.begin()+1; i != r.end(); i++) {
				myArray[c] = 0;
				if(i%c == 0) {
						myArray[i] = 1;

				}
		}

}

	void join(const ParallelSieve &x) {
		myArray = x.myArray;
	}
};



void  printprimes(int limit, int *arr) {
  int c;

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

	Timer t;
	double ms;
	double result;
	ms = 0;
	int *arr ;

	for (int i = 0; i < N; i++) {
	t.start();




//

int sqroot = (int)sqrt(limit);
arr = (int*)malloc(limit * sizeof(int));
if(arr == NULL) {
		fprintf(stderr, "Error: Failed to allocate memory for arr.\n");
		return -1;
}

//initarr(limit, arr);

int c=2;
int m;

for(c = 2; c <= sqroot; c++) {
		if(arr[c] == 0) {

      ParallelSieve  init(arr, c);
			parallel_reduce( blocked_range<int>(0, limit, GRAIN),init );
			arr = init.myArray;

		}
}


//





		ms += t.stop();
		}

		if (argc==2){
			cout << "times "<< N <<" - avg time = " << (ms/N) << " ms" << endl;
    }

    printprimes(limit, arr);



    free(arr);

    return 0;
}
