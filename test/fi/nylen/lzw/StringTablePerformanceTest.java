package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;
import static fi.nylen.lzw.TestUtils.*;

public class StringTablePerformanceTest {
    private StringTable table;

    @Before
    public void setUp() {
        table = new StringTable(24);
    }

    @Test
    public void addingAndRetrievingOneCode() {
        System.out.println("Adding and retrieving one code took: " +
            benchmark(1) + " ms");
    }

    @Test
    public void addingAndRetrievingTenCodes() {
        System.out.println("Adding and retrieving ten codes took: " +
            benchmark(10) + " ms");
    }

    @Test
    public void addingAndRetrievingHundredCodes() {
        System.out.println("Adding and retrieving hundred codes took: " +
            benchmark(100) + " ms");
    }

    @Test
    public void addingAndRetrievingThousandCodes() {
        System.out.println("Adding and retrieving thousand codes took: " +
            benchmark(1000) + " ms");
    }
    
    @Test
    public void addingAndRetrievingHundredThousandCodes() {
        System.out.println("Adding and retrieving hundred thousand codes took: " +
            benchmark(100000) + " ms");
    }

    @Test
    public void addingAndRetrievingMillionCodes() {
        System.out.println("Adding and retrieving million codes took: " +
            benchmark(1000000) + " ms");
    }

    @Test
    public void addingAndRetrievingFiveMillionCodes() {
        System.out.println("Adding and retrieving five million codes took: " +
            benchmark(5000000) + " ms");
    }

    private long benchmark(int numberOfCodes) {
        long startTime = System.currentTimeMillis();

        for (int prefixCode = 0; prefixCode < numberOfCodes; prefixCode++) {
            int prefix = randomPrefix();
            byte append = randomAppend();
            table.add(prefix, append);
            table.codeValue(prefix, append);
        }

        return (System.currentTimeMillis() - startTime);
    }

    private int randomPrefix() {
        return (int)(Math.random() * 10000000) + 1;
    }

    private byte randomAppend() {
        return (byte)((int)(Math.random() * 255) + 1);
    }
}
