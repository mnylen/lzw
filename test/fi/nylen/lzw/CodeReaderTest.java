package fi.nylen.lzw;

import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class CodeReaderTest {
    private CodeReader reader;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        CodeWriter writer = new CodeWriter(bos, 12);
        writer.write(1);
        writer.write(490);
        writer.write(2048);
        writer.write(StringTable.STOP_CODE);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        reader = new CodeReader(bis, 12);
    }

    @Test
    public void testRead()
      throws IOException {
        assertEquals(1, reader.read());
        assertEquals(490, reader.read());
        assertEquals(2048, reader.read());
        assertEquals(StringTable.STOP_CODE, reader.read());
    }

    @Test
    public void testHasNext()
      throws IOException {
        assertTrue(reader.hasNext());
        for (int i = 0; i < 4; i++) {
            reader.read();
        }

        assertFalse(reader.hasNext());
    }
}
