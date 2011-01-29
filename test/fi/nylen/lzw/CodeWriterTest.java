package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CodeWriterTest {
    private OutputStream out;
    private CodeWriter writer;

    @Before
    public void setUp() {
        out = mock(OutputStream.class);
    }

    @Test
    public void testWritingCodesWhenCodeWidthIsOneByte() throws IOException {
        writer = new CodeWriter(out, 8);
        writer.write(1);
        verify(out).write(1);
    }
}
