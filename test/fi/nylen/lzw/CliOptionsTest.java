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

    @Test
    public void testFromArgsWithoutMaxCodeWidth() {
        CliOptions opts = CliOptions.fromArgs(
                new String[] { "compress", "--code-width=18", "file.txt" });

        assertEquals(18, opts.getCodeWidth());
        assertEquals(18, opts.getMaxCodeWidth());
    }

    @Test
    public void testFromArgsWithoutCodeWidth() {
        CliOptions opts = CliOptions.fromArgs(
                new String[] { "compress", "file.txt" });

        assertEquals(CliOptions.DEFAULT_CODE_WIDTH, opts.getCodeWidth());
        assertEquals(CliOptions.DEFAULT_CODE_WIDTH, opts.getMaxCodeWidth());
    }

    @Test
    public void testFromArgsWithoutAction() {
        try {
            CliOptions.fromArgs(new String[] { "--code-width=18", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid action: one of 'compress', 'decompress' expected", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithoutFileName() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width=18" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("No file given", e.getMessage());
        }
    }

    @Test
    public void testFromArgsIgnoresExcessFileNames() {
        CliOptions opts = CliOptions.fromArgs(new String[] { "compress", "fileName1", "fileName2", "fileName3"} );
        assertEquals("fileName1", opts.getFileName());
    }

    @Test
    public void testFromArgsDoesNotAllowUnknownOptions() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--hello-world", "file.txt"} );
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Unknown option: --hello-world", e.getMessage());
        }
    }
}
