package fi.nylen.lzw;

import java.io.IOException;
import java.io.InputStream;

class CodeReader {
    private int inputBitBuffer = 0;
    private int inputBitCount = 0;
    private int codeWidth;
    private InputStream in;
    private int lastCode;

    public CodeReader(InputStream in, int codeWidth) {
        this.codeWidth = codeWidth;
        this.in        = in;
    }

    public void increaseCodeWidth() {
        codeWidth++;
    }
    
    public boolean hasNext() throws IOException {
        return lastCode != StringTable.STOP_CODE;
    }

    public int read() throws IOException {
        if (!(hasNext())) {
            return StringTable.STOP_CODE;
        }
        
        while (inputBitCount < codeWidth) {
            int b = in.read();
            if (b == -1) {
                return -1;
            }

            inputBitBuffer |= b << (24 - inputBitCount);
            inputBitCount  += 8;
        }

        int returnValue = Math.abs(inputBitBuffer >>> (32 - codeWidth));
        inputBitBuffer = inputBitBuffer << codeWidth;
        inputBitCount -= codeWidth;

        lastCode = returnValue;
        return returnValue;
    }
}