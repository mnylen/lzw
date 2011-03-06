package fi.nylen.lzw;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <code>OutputStream</code> for outputting uncompressed data to the underlying
 * output stream. Compression is done using Lempel-Ziv-Welch data compression
 * algorithm with variable width codes.
 */
public class LzwOutputStream extends OutputStream {
    private static final int BYTE_TO_INT_MASK = 0xFF;
    private CodeWriter writer;
    private int codeWidth;
    private int maxCodeWidth;
    private StringTable table;
    private int prefixCode = -1;

    protected LzwOutputStream(int initialCodeWidth, int maxCodeWidth, CodeWriter writer) {
        this.codeWidth    = initialCodeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.writer       = writer;
        this.table        = new StringTable(maxCodeWidth);
    }

    /**
     * Creates a new <code>LzwOutputStream</code> for writing compressed data to <code>out</code>.
     * @param initialCodeWidth initial code width for compression
     * @param maxCodeWidth maximum code width for compression
     * @param out stream to write to
     */
    public LzwOutputStream(int initialCodeWidth, int maxCodeWidth, OutputStream out) {
        this(initialCodeWidth, maxCodeWidth, new CodeWriter(new BufferedOutputStream(out), initialCodeWidth));
    }

    /**
     * Writes and compresses a single byte to the underlying output stream.
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
                boolean added = table.add(prefixCode, (byte)b);

                if (added && codeWidthNeedsToBeIncreased()) {
                    writer.increaseCodeWidth();
                    codeWidth++;
                }

                prefixCode = b;
            }
        }
    }

    /**
     * Writes and compresses bytes between <code>off</code> and
     * <code>off+len-1</code> in the <code>input</code> array to the underlying
     * output stream.
     * @param input input array
     * @param off offset
     * @param len number of bytes to write
     * @throws IOException if anything goes wrong
     */
    @Override
    public void write(byte[] input, int off, int len) throws IOException {
        for (int i = off; i < len; i++) {
            int b = input[i] & BYTE_TO_INT_MASK;
            write(b);
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

    private boolean codeWidthNeedsToBeIncreased() {
        int maxCodeForCurrentWidth = (int)Math.pow(2, codeWidth);
        return (codeWidth < maxCodeWidth) && (maxCodeForCurrentWidth+1) == table.nextCode();
    }
}
