package fi.nylen.lzw;

import org.junit.Test;
import static org.junit.Assert.*;
import static fi.nylen.lzw.MathUtil.*;

public class MathUtilTest {
    @Test
    public void nonPrimesShouldNotBePrimes() {
        assertFalse(isPrime(6));
        assertFalse(isPrime(1050));
        assertFalse(isPrime(49000));
    }

    @Test
    public void primesShouldBePrimes() {
        assertTrue(isPrime(3));
        assertTrue(isPrime(62401));
        assertTrue(isPrime(104729));
    }

    @Test
    public void nextPrimeShouldBeLargerThanGivenValue() {
        assertTrue(nextPrime(100) > 100);
    }

    @Test
    public void nextPrimeShouldBePrime() {
        assertTrue(isPrime(nextPrime(100)));
        assertTrue(isPrime(nextPrime(549)));
    }
}
