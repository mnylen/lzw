package fi.nylen.lzw;

import java.io.FilterInputStream;
import java.io.InputStream;

public class LzwInputStream extends FilterInputStream {
    private int maxCodeWidth;
    private int codeWidth;
    private CodeReader reader;

    public LzwInputStream(int initialCodeWidth, int maxCodeWidth, InputStream in) {
        super(in);

        this.codeWidth    = initialCodeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.reader       = new CodeReader(in, codeWidth);
    }

    /**
     * @return <code>1</code> if there are more bytes to be read; <code>0</code> otherwise
     */
    @Override
    public int available() {
        return 0;
    }

    @Override
    public int read() {
        return 0;
    }
}
