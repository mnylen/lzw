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
}

class MockOutputStream extends OutputStream {
    private byte[] bytes     = new byte[1024];
    private int bytesWritten = 0;
    
    @Override
    public void write(int i) throws IOException {
        bytes[bytesWritten] = (byte)i;
        bytesWritten++;
    }

    public int count() {
        return bytesWritten;
    }

    public byte[] bytes() {
        return bytes;
    }
}
