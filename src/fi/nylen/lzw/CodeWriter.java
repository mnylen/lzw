package fi.nylen.lzw;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes codes to <code>OutputStream</code> using a specified
 * code width (in bits).
 */
public class CodeWriter {
    private int codeWidth;
    private OutputStream out;

    /**
     * Initializes a code writer for writing to the output stream with the
     * initial code width.
     *
     * @param out stream to write to
     * @param initialCodeWidth initial code width
     */
    public CodeWriter(OutputStream out, int initialCodeWidth) {
        codeWidth = initialCodeWidth;
        this.out  = out;
    }

    /**
     * Writes code.
     * @param code the code
     * @throws IOException if anything goes wrong
     */
    public void write(int code) throws IOException {
        out.write(code);
    }
}
