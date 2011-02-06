package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

public class LzwOutputStreamTest {
    private LzwOutputStream out;
    private MockCodeWriter writer;
    private int initialWidth = 9;
    private int maxWidth     = 10;
    
    @Before
    public void setUp() {
        writer = new MockCodeWriter();
        out    = new LzwOutputStream(initialWidth, maxWidth, writer);
    }

    @Test
    public void testWrite() throws IOException {
        out.write((int)'H'); // writes 72
        out.write((int)'e'); // writes 101: He      = 258
        out.write((int)'l'); // writes 108: Hel     = 259
        out.write((int)'l'); // writes 108: Hell    = 260
        out.write((int)'o'); // writes 111: Hello   = 261
        out.write(new byte[] { 'H', 'e', 'l', 'l', 'o' });
        out.finish(); // writes stop code and flushes anything

        assertEquals(8, writer.count());
        int[] expectedWrites = new int[] { 72, 101, 108, 108, 111, 261, StringTable.STOP_CODE, 0 };
        for (int i = 0; i < 8; i++) {
            assertEquals(expectedWrites[i], writer.codes()[i]);
        }
    }
}
