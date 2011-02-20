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
        out.finish();

        ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
        lzw = new LzwInputStream(12, 12, in);
    }

    @Test
    public void testRead() throws IOException {
        assertNextReadEquals('/');
        assertNextReadEquals('W');
        assertNextReadEquals('E');
        assertNextReadEquals('D');
        assertNextReadEquals('/');
        assertNextReadEquals('W');
        assertNextReadEquals('E');
        assertNextReadEquals('/');
        assertNextReadEquals('W');
        assertNextReadEquals('E');
        assertNextReadEquals('E');
        assertNextReadEquals('/');
        assertNextReadEquals('W');
        assertNextReadEquals('E');
        assertNextReadEquals('B');
        assertNextReadEquals('/');
        assertNextReadEquals('W');
        assertNextReadEquals('E');
        assertNextReadEquals('T');
        assertEquals(-1, lzw.read());
        assertEquals(0, lzw.available());
    }

    @Test
    public void testReadInChunks() throws IOException {
        byte[] decompressed = new byte[19];
        int bytesRead = lzw.read(decompressed);

        assertEquals(19, bytesRead);
        assertArrayEquals("/WED/WE/WEE/WEB/WET".getBytes(), decompressed);
    }

    private void assertNextReadEquals(char c) throws IOException {
        assertEquals(ord(c), lzw.read());
        assertEquals(1, lzw.available());
    }
}
