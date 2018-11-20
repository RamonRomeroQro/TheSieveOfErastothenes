/*----------------------------------------------------------------

*


*

*--------------------------------------------------------------*/
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Main {


  	public static void initarr(int limit,int []a) {
  	  int c;
  	  for(c = 2; c < limit; c++) {
  	      a[c] = 0;
  	  }
  	}

  	public static void  printprimes(int limit, int []arr) {
  	  int c;

  	  for(c = 2; c <limit; c++) {
  	      if(arr[c] == 0) {
  					System.out.print(""+c+" ");
  	      }
  	  }
  		System.out.print("\n");
  	  /* code */
  	}

    private static final int MAXTHREADS = Runtime.getRuntime().availableProcessors();

	public static void main(String args[]) throws Exception {

    int N = 10;
    int limit=16;
    if (args.length>2){
      System.err.println("Error: uso: Main [limite_superior_positivo]\n");
      System.exit(-1);
    }else if (args.length==1 || args.length==2) {
      int parsed=Integer.parseInt(args[0]);
      //System.err.println(parsed);

      if (parsed<0){
        System.err.println("Error: uso: %s [limite_superior_positivo]\n");
        System.exit(-1);
      }else{
        limit=parsed;
      }
      if (args.length==2){
        N=1;

      }

    }

    long startTime, stopTime;
    double acum = 0;
    acum = 0;
    int [] arr = new int[limit];


    for (int j = 1; j <= N; j++) {
    startTime = System.currentTimeMillis();
    arr = new int[limit];




    //

    int sqroot = (int)Math.sqrt(limit);
    ForkJoinPool pool = new ForkJoinPool(MAXTHREADS);;
    //

    //
    //initarr(limit, arr);
    int c;
    int m;


    for(c = 2; c <= sqroot; c++) {
        if(arr[c] == 0) {

          //
            pool.invoke(new ForkSieve(0, limit, arr, c));

          //


        }
    }
    //
      stopTime = System.currentTimeMillis();
      acum +=  (stopTime - startTime);
    }
    if (args.length==1){
      System.out.printf("times %d - avg time = %.5f ms\n", N, (acum / N));
    }












  printprimes(limit, arr);







    System.exit(0);



	}
}
/*----------------------------------------------------------------

*

* Actividad de programación: Fork-join framework

* Fecha: 23-Sep-2018

* Autor: A01700318 Ramon Romero

*

*--------------------------------------------------------------*/

class ForkSieve extends RecursiveAction {
	private static final long MIN = 10;
  public int [] arr;
  public int ct=-1;
  public int begin;
  public int end;


  public ForkSieve( int b, int e, int [] a, int c ) {
     this.arr   = a;
     this.begin = b;
     this.end   = e;
     this.ct    = c;
  }


	public void computeDirectly() {

    for (int i = this.begin; i < this.end; i++) {
      //System.err.println(ct+" "+i);

      this.arr[ct] = 0;
      if(i%ct == 0) {
          this.arr[i] = 1;

      }

    }
	}

	@Override
	protected void compute() {
		if ( (this.end - this.begin) <= ForkSieve.MIN ) {
			computeDirectly();

		} else {
			int middle = (end + begin) / 2;

			invokeAll(new ForkSieve( begin, middle, arr, ct),
					  new ForkSieve( middle, end, arr, ct));
		}
	}
}
