package fi.nylen.lzw;

/**
 * Translation table management.
 */
public class TranslationTable {
    private int nextCode = StringTable.CLEAR_CODE+1;
    private int maxCode;
    private byte[][] translated;
    private int tableSize;

    /**
     * Creates a new translation table for holding 2^codeWidth entries.
     * @param codeWidth the code width
     */
    public TranslationTable(int codeWidth) {
        tableSize = (int)Math.pow(2, codeWidth);
        maxCode   = tableSize-1;
        translated = new byte[tableSize][];
    }

    /**
     * Gets the code to be assigned for the next entry.
     * @return the code to be assigned for the next entry
     */
    public int nextCode() {
        return nextCode;
    }

    /**
     * Checks whether the table contains the code.
     * @param code the code
     * @return <code>true</code> if the table contains the code; <code>false</code> otherwise
     */
    public boolean contains(int code) {
        return code < nextCode;
    }

    /**
     * Adds a string to the translation table.
     * @param prefixCode the prefix string
     * @param appendCharacter the append character
     * @return the code assigned for the string
     */
    public int add(int prefixCode, byte appendCharacter) {
        int code = nextCode;
        
        if (code <= maxCode) {
            byte[] prefix = translated[prefixCode];
            byte[] string;

            if (prefix != null) {
                string = new byte[prefix.length+1];
                System.arraycopy(prefix, 0, string, 0, prefix.length);
                string[prefix.length] = appendCharacter;
            } else {
                string = new byte[] { (byte)prefixCode, appendCharacter };
            }
            
            translated[code] = string;
            nextCode++;
    
            return code;
        } else {
            return -1;
        }
    }

    /**
     * Translates the given code as a string.
     * @param code the code
     * @return the string translated from code
     */
    public byte[] translate(int code) {
        if (code > StringTable.CLEAR_CODE) {
            return translated[code];
        } else {
            return new byte[] { (byte)code };
        }
    }
}
