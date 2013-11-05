import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerPrimeFactor {
	static List<BigInteger> primeFactorsList = new ArrayList<BigInteger>(); //list for storing found integers

	
	/**Does BigInteger Prime Factoring over a range
	 * Needs optimization
	 * 
	 * @param start
	 * @param end
	 * @param number
	 * @return
	 */
	public static BigInteger primeFactors(BigInteger start, BigInteger end, BigInteger number) {
		
		for (BigInteger i = start; i.compareTo(end) <= 0; i = i.add(BigInteger.ONE)) {			
			if (number.remainder(i).compareTo(BigInteger.ZERO) == 0) {
				if (isPrime(i)){
					System.out.println(i);
					return i;
				}
			}
		}
		return number;
	}
	
	
	/**Returns the Sqrt of the BigInteger
	 * 
	 * @param n
	 * @return
	 */
	public static BigInteger sqrt(BigInteger n) {
	    BigInteger a = BigInteger.ONE;
	    BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
	    while(b.compareTo(a) >= 0) {
	      BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
	      if(mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
	      else a = mid.add(BigInteger.ONE);
	    }
	    return a.subtract(BigInteger.ONE);
	  }
	
	/**Checks whether a BigIntint is prime or not.
	 * 
	 * @param n
	 * @return
	 */
	public static boolean isPrime(BigInteger n) {
	    //check if n is a multiple of 2
		if (n.remainder(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0)
			return false;
	    //if not, then just check the odds
	    for (BigInteger i = BigInteger.valueOf(3); (i.multiply(i)).compareTo(n) <= 0; i = i.add(BigInteger.valueOf(2))) {
	    
	    	if (n.remainder(i).compareTo(BigInteger.ZERO) == 0)
	            return false;
	    }
	    return true;
	}
	
	public static void printBigInteger(BigInteger number){
		for (BigInteger integer : primeFactorsList) {
			System.out.println(integer);
		}
	}

	public static void main(String[] args) {
		
		//Takes a short amount of time to factor
		//BigInteger number =   new BigInteger("2251797028667587"); // = 33554393 * 67108859
		
		BigInteger number =   new BigInteger("864"); // = 33554393 * 67108859
		
		//Takes several hours to factor
		//BigInteger number = new BigInteger("36893488065814724653"); // = 4294967291 * 8589934583
		
		BigInteger start, end;
		
		System.out.println("Prime Factors of " + number + ": ");
		
		long startTime = System.nanoTime(); //Start Timing
		
		//This will be used for sending jobs (ranges of divisors to check) to the nodes
		for (BigInteger i = BigInteger.valueOf(2); i.compareTo(number) <= 0; i = i.add(BigInteger.valueOf(100))) {
			
			start = i; //Start Range
			end = i.add(BigInteger.valueOf(100)); //End Range

			BigInteger found = primeFactors(start, end, number); //method to send node job
			
			primeFactorsList.add(found);
		}
		long time = System.nanoTime() - startTime; //End Timing

		System.out.println("Time= " + time / 1000000000.0 + " seconds"); //Print time to factor
		
	}
}