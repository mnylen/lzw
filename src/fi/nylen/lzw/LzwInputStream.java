package fi.nylen.lzw;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class LzwInputStream extends InputStream {
    private int maxCodeWidth;
    private int codeWidth;
    private ByteBuffer buffer;
    private CodeReader reader;
    private int oldCode = -1;
    private byte character;
    private TranslationTable table;

    public LzwInputStream(int initialCodeWidth, int maxCodeWidth, InputStream in) {
        this.codeWidth    = initialCodeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.reader       = new CodeReader(in, codeWidth);
        this.table        = new TranslationTable(maxCodeWidth);

        this.buffer = ByteBuffer.allocate(4096);
        this.buffer.flip();
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
            return buffer.get() & 0xFF;
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
        int newCode = reader.read();
        if (newCode != StringTable.STOP_CODE) {
            byte[] string = translate(newCode, oldCode, character);
            character = string[0];

            addToBuffer(string);
            table.add(oldCode, character);
            oldCode = newCode;

            return character & 0xFF;
        } else {
            return -1;
        }
    }

    private void addToBuffer(byte[] string) {
        if (string.length > 1) {
            buffer.clear();
            buffer.put(string, 1, string.length-1);
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
}
