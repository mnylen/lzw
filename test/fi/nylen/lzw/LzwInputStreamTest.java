package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static fi.nylen.lzw.TestUtils.ord;
import static org.junit.Assert.*;

public class LzwInputStreamTest {
    private LzwInputStream lzw;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LzwOutputStream out = new LzwOutputStream(12, 12, baos);
        out.write("/WED/WE/WEE/WEB/WET".getBytes());

        InputStream in = new ByteArrayInputStream(baos.toByteArray());
        lzw = new LzwInputStream(12, 12, in);
    }

    @Test
    public void testRead() {
        assertNextReadEquals('/', false);
        assertNextReadEquals('W', false);
        assertNextReadEquals('E', false);
        assertNextReadEquals('D', false);
        assertNextReadEquals('/', false);
        assertNextReadEquals('W', false);
        assertNextReadEquals('E', false);
        assertNextReadEquals('/', false);
        assertNextReadEquals('W', false);
        assertNextReadEquals('E', false);
        assertNextReadEquals('E', false);
        assertNextReadEquals('/', false);
        assertNextReadEquals('W', false);
        assertNextReadEquals('E', false);
        assertNextReadEquals('B', false);
        assertNextReadEquals('/', false);
        assertNextReadEquals('W', false);
        assertNextReadEquals('E', false);
        assertNextReadEquals('T', true);
    }

    private void assertNextReadEquals(char c, boolean emptyAfter) {
        assertEquals(ord(c), lzw.read());
        assertEquals( (emptyAfter ? 0 : 1 ), lzw.available());
    }
}
