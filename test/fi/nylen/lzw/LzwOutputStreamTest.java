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
        out.write((int)'H');
        out.write((int)'e');
        out.write((int)'l');
        out.write((int)'l');
        out.write((int)'o');
        out.write(new byte[] { 'H', 'e', 'l', 'l', 'o' });
        out.finish();

        /*
          | Character input | Code output | New code value | New string |
          |        He       |      H      |      258       |    He      |
          |        l        |      e      |      259       |    el      |
          |        l        |      l      |      260       |    ll      |
          |        o        |      l      |      261       |    lo      |
          |        H        |      o      |      262       |    oH      |
          |        e        |     ----    |                |            |
          |        l        |     258     |      263       |    Hel     |
          |        l        |     ----    |                |            |
          |        o        |     260     |      264       |    llo     |
          |       EOF       |      o      |      ---       |    ---     |
         */

        assertEquals(10, writer.count()); // 8 writes + stop code + zero for flushing
        int[] expectedWrites = new int[] { 72,  101, 108, 108, 111, 258, 260, 111, StringTable.STOP_CODE, 0};
        for (int i = 0; i < expectedWrites.length; i++) {
            assertEquals(expectedWrites[i], writer.codes()[i]);
        }
    }
}
