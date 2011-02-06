package fi.nylen.lzw;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <code>OutputStream</code> for outputting bytes as LZW.
 */
public class LzwOutputStream extends OutputStream {
    private CodeWriter writer;
    private int codeWidth;
    private int maxCodeWidth;
    private StringTable table;
    private int prefixCode = -1;

    /**
     * Creates an output stream that writes LZW compressed bytes to the underlying
     * output stream.
     * @param initialCodeWidth initial code width for compression
     * @param maxCodeWidth maximum code width for compression
     * @param out stream to write to
     */
    public LzwOutputStream(int initialCodeWidth, int maxCodeWidth, OutputStream out) {
        this(initialCodeWidth, maxCodeWidth, new CodeWriter(out, initialCodeWidth));
    }

    /**
     * Creates an output stream that writes LZW compressed bytes using the
     * writer.
     * @param initialCodeWidth initial code width for compression
     * @param maxCodeWidth maximum code width for compression
     * @param writer writer to use
     */
    protected LzwOutputStream(int initialCodeWidth, int maxCodeWidth, CodeWriter writer) {
        this.codeWidth    = initialCodeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.writer       = writer;
        this.table        = new StringTable(maxCodeWidth);
    }

    /**
     * Writes single byte to the underlying output stream.
     * @param b byte to write
     * @throws IOException if anything goes wrong
     */
    @Override
    public void write(int b) throws IOException {
        if (prefixCode == -1) {
            prefixCode = b;
        } else {
            int newPrefixCode = table.codeValue(prefixCode, (byte)b);
            if (newPrefixCode != -1) {
                prefixCode = newPrefixCode;
            } else {
                writer.write(prefixCode);
                table.add(prefixCode, (byte)b);
                prefixCode = b;
            }
        }
    }

    /**
     * Finishes the compression by writing out stop code and by flushing
     * anything that haven't been written yet.
     * @throws IOException if anything goes wrong
     */
    public void finish() throws IOException {
        writer.write(prefixCode);
        writer.write(StringTable.STOP_CODE);
        writer.flush();
    }
}
