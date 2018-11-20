
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
				arr = new int[limit];
	      int c;
				int m;

	      for(c = 2; c < limit; c++) {
	          for ( m = 1; m <  (int)Math.sqrt(c); m++) {
	            if (c%m==0 && m!=1 && m!=c ){
	              arr[c]=1;
	              break;
	            }else{
	              arr[c]=0;
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

/*



Hardy and Littlewood listed as their Conjecture I: "Every large odd number (n > 5) is the sum of a prime and the double of a prime." (Mathematics Magazine, 66.1 (1993): 45–47.) This conjecture is known as Lemoine's conjecture (also called Levy's conjecture).

Every integer greater than 5 can be written as the sum of three primes.
Euler replied in a letter dated 30 June 1742, and reminded Goldbach of an earlier conversation they had ("…so Ew vormals mit mir communicirt haben…"), in which Goldbach remarked his original (and not marginal) conjecture followed from the following statement

Every even integer greater than 2 can be written as the sum of two primes,

paralelized memset library calls


*/
