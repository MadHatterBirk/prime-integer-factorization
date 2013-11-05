import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PrimeFactorsEffective {
	static List<BigInteger> primeFactors = new ArrayList<BigInteger>();
	static List<Integer> primeFactorsOriginal = new ArrayList<Integer>();
	static List<Integer> listOfPrimesOriginal = new ArrayList<Integer>();
	static List<Integer> listOfPrimes = new ArrayList<Integer>();

	public static Integer primeFactors(int start, int end, int number) {
		for (int i = start; i <= end; i++) {
			if (number % i == 0) {
				if (isPrime(i))
				return i;				
			}
		}
		return number;
	}
	
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
	
	//checks whether an int is prime or not.
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
	
	//checks whether an int is prime or not.
	public static boolean isPrime(int n) {
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}
	
	public static Integer primeFactorsOriginal(int start, int end, int number) {	    
	    for (int i = start; i <= end / i; i++) {
	      while (number % i == 0) {
	    	  primeFactorsOriginal.add(i);
	        number /= i;
	      }
	    }
	    if (number > 1) {
	    	primeFactorsOriginal.add(number);
	    }
	    return number;
	  }	

	public static void main(String[] args) {
		//BigInteger number2 = new BigInteger("99999990");
		//BigInteger number2 =   new BigInteger("2251797028667587");
		BigInteger number2 = new BigInteger("36893488065814724653");
		int number3 = 99999990;
		BigInteger start;
		BigInteger end;
		
		System.out.println("primeFactors: ");
		long startTime = System.nanoTime();
		for (BigInteger i = BigInteger.valueOf(2); i.compareTo(number2) <= 0; i = i.add(BigInteger.valueOf(100))) {
			start = i;
			end = i.add(BigInteger.valueOf(100));
			//number1 = primeFactors(start, end, number1);
			BigInteger found = primeFactors(start, end, number2);
			if ( !found.equals(number2))
			primeFactors.add(found);
		}
		long time = System.nanoTime() - startTime;
		/*for (BigInteger integer : primeFactors) {
			System.out.println(integer);
		}*/
		System.out.println("Time= " + time / 1000000000.0 + " seconds");

		
	}
}