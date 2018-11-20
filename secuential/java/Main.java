
/*----------------------------------------------------------------

* Autor: A01700318 Jose Ramon Romero Chavez
*--------------------------------------------------------------*/

public class Main {


	public static void  printprimes(int limit, int []arr) {
	  int c;

	  for(c = 2; c <limit; c++) {
	      if(arr[c] == 0) {
					System.out.print(""+c+" ");
	      }
	  }
		System.out.print("\n");
	}
	private static final int MAXTHREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {


				 //System.out.println("IO: "+args[0]+" "+ args.length);

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


				//initarr(limit, arr);

				//
				long startTime, stopTime;
				double acum = 0;
				acum = 0;
				int [] arr = new int[limit];

				for (int j = 1; j <= N; j++) {
				startTime = System.currentTimeMillis();
				int sqroot = (int)Math.sqrt(limit);
				arr = new int[limit];
        int c;
        int m;
      for(c = 2; c <= sqroot; c++) {
          if(arr[c] == 0) {
            //#pragma omp parallel for shared(arr, limit, c) private(m)
              for(m = c+1; m < limit; m++) {
                  if(m%c == 0) {
                      arr[m] = 1;
                  }
              }

          }
      }


					stopTime = System.currentTimeMillis();
					acum +=  (stopTime - startTime);
				}
				if (args.length==1){
					System.out.printf("times %d - avg time = %.5f ms\n", N, (acum / N));
				}
				//

			printprimes(limit, arr);







        System.exit(0);
    }
}
