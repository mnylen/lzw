package fi.nylen.lzw;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes codes to <code>OutputStream</code> using a specified
 * code width (in bits).
 */
public class CodeWriter {
    private int codeWidth;
    private int outputBitBuffer = 0;
    private int outputBitCount = 0;
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

    public void increaseCodeWidth() {
        codeWidth++;
    }

    /**
     * Writes code.
     * @param code the code
     * @throws IOException if anything goes wrong
     */
    public void write(int code) throws IOException {
        outputBitBuffer |= code << (32 - codeWidth - outputBitCount);
        outputBitCount  += codeWidth;

        while (outputBitCount >= 8) {
            out.write(outputBitBuffer >>> 24);
            outputBitBuffer <<= 8;
            outputBitCount -= 8;
        }
    }

    /**
     * Writes out anything that's left in the buffer.
     * @throws IOException if anything goes wrong
     */
    public void flush() throws IOException {
        write(0);
    }
}
