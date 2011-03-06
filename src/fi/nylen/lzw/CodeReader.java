package fi.nylen.lzw;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads codes from an <code>InputStream</code>.
 */
public class CodeReader {
    private int inputBitBuffer = 0;
    private int inputBitCount = 0;
    private int codeWidth;
    private InputStream in;
    private int lastCode;

    /**
     * Creates a new <code>CodeReader</code> for reading codes from the given
     * input stream with the specified initial code width.
     * @param in the input stream to read from
     * @param codeWidth the initial code width
     */
    public CodeReader(InputStream in, int codeWidth) {
        this.codeWidth = codeWidth;
        this.in        = in;
    }

    /**
     * Increases the current code width by one.
     */
    public void increaseCodeWidth() {
        codeWidth++;
    }

    /**
     * Checks whether there is a next code to be read.
     * @return <code>true</code> if the last code read was not the stop code; <code>false</code> otherwise
     * @throws IOException
     */
    public boolean hasNext() {
        return lastCode != StringTable.STOP_CODE;
    }

    /**
     * Reads next code from the input stream using the current code width.
     * @return the code read
     * @throws IOException if anything goes wrong
     */
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