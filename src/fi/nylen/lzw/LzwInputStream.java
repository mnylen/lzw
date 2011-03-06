package fi.nylen.lzw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * <code>InputStream</code> for reading compressed data from the underlying
 * input stream stream. Decompression is done using Lempel-Ziv-Welch data
 * decompression algorithm with variable width codes.
 */
public class LzwInputStream extends InputStream {
    private static final int BUFFER_SIZE = 4096;
    private static final int BYTE_TO_INT_MASK = 0xFF;
    
    private int maxCodeWidth;
    private int codeWidth;
    private ByteBuffer buffer;
    private CodeReader reader;
    private InputStream in;
    private int oldCode = -1;
    private byte character;
    private TranslationTable table;

    /**
     * Creates a new <code>LzwInputStream</code> for reading
     * LZW compressed <code>InputStream</code>.
     *
     * @param initialCodeWidth the initial code width that was used in compression
     * @param maxCodeWidth the maximum code width that was used in compression
     * @param in the underlying <code>InputStream</code>
     */
    public LzwInputStream(int initialCodeWidth, int maxCodeWidth, InputStream in) {
        this.codeWidth    = initialCodeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.reader       = new CodeReader(in, codeWidth);
        this.table        = new TranslationTable(maxCodeWidth);
        this.in           = in;

        /*
         Initialize a buffer to store bytes that have been expanded from the
         underlying stream, but have not yet been read out from
         LzwInputStream
         */
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.buffer.flip();
    }

    /**
     * Closes this input stream.
     * @throws IOException if anything goes wrong
     */
    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * Determines if there are more bytes available to read.
     *
     * <p>Note that even though a call to available() would return 1, it does
     * not necessarily mean there are more actual bytes to read. For example,
     * if the next and only byte to be read is the stop code, <code>read()</code>
     * will return -1</p>.
     *
     * @return <code>1</code> if there are more bytes to be read; <code>0</code> otherwise
     */
    @Override
    public int available() throws IOException {
        return (reader.hasNext() || buffer.hasRemaining()) ? 1 : 0;
    }

    /**
     * Reads the next byte from the underlying input stream and decompresses it.
     * @return the decompressed byte as integer or <code>-1</code> if there are no more bytes to read
     * @throws IOException if anything goes wrong
     */
    @Override
    public int read() throws IOException {
        if (buffer.hasRemaining()) {
            return buffer.get() & BYTE_TO_INT_MASK;
        } else if (oldCode == -1) {
            return expandFirst();
        } else {
            return expandNext();
        }
    }

    private int expandFirst() throws IOException {
        oldCode = reader.read();
        if (oldCode != StringTable.STOP_CODE) {
            character = (byte)oldCode;
            return character & BYTE_TO_INT_MASK;
        } else {
            return -1;
        }
    }
    
    private int expandNext() throws IOException {
        int newCode = reader.read();
        if (newCode != StringTable.STOP_CODE) {
            byte[] string = translate(newCode, oldCode, character);
            character = string[0];

            addToBuffer(string, 1, string.length-1);
            table.add(oldCode, character);

            if (codeWidthNeedsToBeIncreased()) {
                reader.increaseCodeWidth();
                codeWidth++;
            }
            
            oldCode = newCode;

            return character & BYTE_TO_INT_MASK;
        } else {
            return -1;
        }
    }

    private void addToBuffer(byte[] string, int off, int length) {
        if ( (off + length) <= string.length) {
            buffer.clear();
            buffer.put(string, off, length);
            buffer.flip();
        }
    }

    private byte[] translate(int newCode, int oldCode, byte character) {
        if (!(table.contains(newCode))) {
            byte[] translated = table.translate(oldCode);

            byte[] string = new byte[translated.length+1];
            string[translated.length] = character;
            System.arraycopy(translated, 0, string, 0, translated.length);
            return string;
        } else {
            return table.translate(newCode);
        }
    }

    private boolean codeWidthNeedsToBeIncreased() {
        int maxCodeForCurrentWidth = (int)Math.pow(2, codeWidth);
        return (codeWidth < maxCodeWidth) && table.nextCode() == maxCodeForCurrentWidth;
    }
}
