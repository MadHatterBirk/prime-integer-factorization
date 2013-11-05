import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrimeFactors {
	public static List<BigInteger> primeFactors(BigInteger number) {

		List<BigInteger> factors = new ArrayList<BigInteger>();

		for (BigInteger i = BigInteger.valueOf(2); i.compareTo(number) <= 0; i = i.add(BigInteger.ONE)) {
			

			while (number.remainder(i).compareTo(BigInteger.ZERO) == 0) {
				factors.add(i);
				

				number = number.divide(i);
			}

		}
		return factors;
	}
	
	

	public static void main(String[] args) {
		
		String RSA_100 = "1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139";
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Number: ");
		
		BigInteger bigInteger = new BigInteger(in.next());	//new BigInteger(RSA_100);
		System.out.println("Prime Factors of " + bigInteger);

		for (BigInteger integer : primeFactors(bigInteger)) {
			System.out.println(integer);
		}
		
		System.out.println("DONE!!!");
		

	}
	
}