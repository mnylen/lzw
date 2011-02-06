package fi.nylen.lzw;

import java.io.IOException;
import java.io.OutputStream;

class MockOutputStream extends OutputStream {
    private byte[] bytes     = new byte[1024];
    private int bytesWritten = 0;

    @Override
    public void write(int i) throws IOException {
        bytes[bytesWritten] = (byte)i;
        bytesWritten++;
    }

    public int count() {
        return bytesWritten;
    }

    public byte[] bytes() {
        return bytes;
    }
}