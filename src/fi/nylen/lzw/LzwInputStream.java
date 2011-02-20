package fi.nylen.lzw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

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
        
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.buffer.flip();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * @return <code>1</code> if there are more bytes to be read; <code>0</code> otherwise
     */
    @Override
    public int available() throws IOException {
        return (reader.hasNext() || buffer.hasRemaining()) ? 1 : 0;
    }

    @Override
    public int read() throws IOException {
        if (oldCode == -1) {
            return expandFirst();
        } else if (buffer.hasRemaining()) {
            return buffer.get() & BYTE_TO_INT_MASK;
        } else {
            return expandNext();
        }
    }

    private int expandFirst() throws IOException {
        oldCode = reader.read();
        if (oldCode != StringTable.STOP_CODE) {
            character = (byte)oldCode;
            return character;
        } else {
            return -1;
        }
    }
    
    private int expandNext() throws IOException {
        if (codeWidthNeedsToBeIncreased()) {
            reader.increaseCodeWidth();
        }
        
        int newCode = reader.read();
        if (newCode != StringTable.STOP_CODE) {
            byte[] string = translate(newCode, oldCode, character);
            character = string[0];

            addToBuffer(string, 1, string.length-1);
            table.add(oldCode, character);
            oldCode = newCode;

            return character & BYTE_TO_INT_MASK;
        } else {
            return -1;
        }
    }

    private void addToBuffer(byte[] string, int off, int length) {
        if (string.length > 1) {
            buffer.clear();
            buffer.put(string, off, length);
            buffer.flip();
        }
    }

    private byte[] translate(int newCode, int oldCode, byte character) {
        if (!(table.contains(newCode))) {
            byte[] translated = table.translate(oldCode);
            byte[] string     = new byte[translated.length+1];
            System.arraycopy(translated, 0, string, 0, translated.length);
            string[string.length-1] = character;

            return string;
        } else {
            return table.translate(newCode);
        }
    }

    private boolean codeWidthNeedsToBeIncreased() {
        int maxCodeForCurrentWidth = (int)Math.pow(2, codeWidth);
        return !(codeWidth >= maxCodeWidth) && table.nextCode() == maxCodeForCurrentWidth;
    }
}
