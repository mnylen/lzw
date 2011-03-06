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

    @Test
    public void testFromArgsWithEmptyValueForCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width=", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --code-width must be numeric value", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithNoValueForCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --code-width must be numeric value", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithNonNumericValueForCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width=lorem", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --code-width must be numeric value", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithTooSmallValueForCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width=8", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --code-width must be at least 9", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithTooSmallValueForMaxCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--max-code-width=8", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --max-code-width must be at least 9", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithTooLargeValueForCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width=33", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --code-width must not exceed 32", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithTooLargeValueForMaxCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--max-code-width=33", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --max-code-width must not exceed 32", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithMaxCodeWidthSmallerThanCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--code-width=10", "--max-code-width=9", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --max-code-width must be greater than or equal to --code-width", e.getMessage());
        }
    }

    @Test
    public void testFromArgsWithMaxCodeWidthSmallerThanCodeWidthWhenCodeWidthGivenAfterMaxCodeWidth() {
        try {
            CliOptions.fromArgs(new String[] { "compress", "--max-code-width=9", "--code-width=10", "file.txt" });
            fail();
        } catch (IllegalOptionsException e) {
            assertEquals("Invalid value: --code-width must be less than or equal to --max-code-width", e.getMessage());
        }
    }
}
