package fi.nylen.lzw;

public class StringTable {
    public static final int STOP_CODE = 256;
    public static final int CLEAR_CODE = 257;
    private int currentCode = -1;
    
    /**
     * @return current code - initially <code>-1</code>
     */
    public int currentCode() {
        return currentCode;
    }

    /**
     * @return next code - initially <code>CLEAR_CODE+1</code>
     */
    public int nextCode() {
        if (currentCode == -1) {
            return CLEAR_CODE+1;
        } else {
            return currentCode+1;
        }
    }

    /**
     * Increases current code by one.
     */
    public void increaseCurrentCode() {
        if (currentCode == -1) {
            currentCode = CLEAR_CODE+1;
        } else {
            currentCode++;
        }
    }
}
