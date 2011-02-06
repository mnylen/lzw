package fi.nylen.lzw;

import java.io.IOException;
import java.io.OutputStream;

class MockCodeWriter extends CodeWriter {
    private int[] written = new int[1024];
    private int count = 0;

    public MockCodeWriter(OutputStream out, int initialCodeWidth) {
        super(out, initialCodeWidth);
    }

    public MockCodeWriter() {
        this(null, 11);
    }

    @Override
    public void write(int code) throws IOException {
        written[count] = code;
        count++;
    }

    public int count() {
        return count;
    }

    public int[] codes() {
        return written;
    }
}
