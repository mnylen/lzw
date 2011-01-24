package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.Assert.*;

public class TranslationTablePerformanceTest {
    private TranslationTable table;
    
    @Before
    public void setUp() {
        table = new TranslationTable(23);
    }
    
    @Test
    public void benchmarkEnglishDictionary() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("test/fi/nylen/lzw/eng_com.dic"));
        String word;

        long totalTimeMillis = 0;
        while ((word = reader.readLine()) != null) {
            word = word.trim();
            byte[] characters = word.getBytes();
            int code = (int)(characters[0]);
            
            long startTime = System.currentTimeMillis();
            for (int i = 1; i < characters.length; i++) {
                code = table.add(code, characters[i]);
            }

            byte[] translated = table.translate(code);
            totalTimeMillis += (System.currentTimeMillis() - startTime);
            assertArrayEquals(characters, translated);
        }

        System.out.println("Adding and translating english dictionary took in total " +
            totalTimeMillis + " ms");

        reader.close();
    }
}
