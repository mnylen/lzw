package fi.nylen.lzw;

import org.junit.Test;

import static org.junit.Assert.*;

public class CliOptionsTest {
    @Test
    public void testFromArgs() {
        CliOptions opts = CliOptions.fromArgs(
                new String[] { "compress", "--code-width=18", "--max-code-width=30", "file.txt" } );

        assertEquals(Lzw.Action.COMPRESS, opts.getAction());
        assertEquals(18, opts.getCodeWidth());
        assertEquals(30, opts.getMaxCodeWidth());
        assertEquals("file.txt", opts.getFileName());
    }
}
