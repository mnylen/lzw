package fi.nylen.lzw;

public class MathUtil {
    public static boolean isPrime(int n) {
        int maxDivisor = (int)Math.sqrt(n);
        for (int divisor = 2; divisor <= maxDivisor; divisor++) {
            if (n % divisor == 0) {
                return false; // not a prime
            }
        }

        return true;
    }

    public static int nextPrime(int n) {
        n++;
        while (!(isPrime(n))) {
            n++;
        }

        return n;
    }
}
