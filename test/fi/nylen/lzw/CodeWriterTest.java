package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CodeWriterTest {
    private MockOutputStream out;
    private CodeWriter writer;

    @Before
    public void setUp() {
        out = new MockOutputStream();
    }

    @Test
    public void testWriteCodeWhenCodeWidthIsOneByte() throws IOException {
        writer = new CodeWriter(out, 8);
        writer.write(1);

        assertEquals(1, out.count());
        assertEquals(1, out.bytes()[0]);
    }

    @Test
    public void testWriteCodeWhenCodeWidthIsThreeBytes() throws IOException {
        writer = new CodeWriter(out, 24);
        writer.write(1);

        assertEquals(3, out.count());
        assertEquals(0, out.bytes()[0]);
        assertEquals(0, out.bytes()[1]);
        assertEquals(1, out.bytes()[2]);
    }

    @Test
    public void testWriteCodeAndFlushWhenCodeWidthIsNotWholeBytes() throws IOException {
        writer = new CodeWriter(out, 9);
        writer.write(1);
        assertEquals(1, out.count());
        assertEquals(0, out.bytes()[0]);

        writer.flush();
        assertEquals(2, out.count());
        assertEquals((byte)(1<<7), out.bytes()[1]);
    }

    @Test
    public void testWriteCodeAfterCodeWidthIncreased() throws IOException {
        writer = new CodeWriter(out, 8);
        writer.write(1);
        writer.increaseCodeWidth();
        writer.write(1);

        assertEquals(2, out.count());
        assertEquals((byte)1, out.bytes()[0]);
        assertEquals((byte)(1<<8), out.bytes()[2]);
    }
}
