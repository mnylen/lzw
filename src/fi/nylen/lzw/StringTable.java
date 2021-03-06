package fi.nylen.lzw;

import java.util.Arrays;

/**
 * String table management.
 */
public class StringTable {
    public static final int STOP_CODE = 256;
    public static final int CLEAR_CODE = 257;
    private int nextCode = CLEAR_CODE+1;
    private int maxCode;
    private int bitMask;
    private int bits;
    private int[] prefixCodes;
    private byte[] appendCharacters;
    private int[] codeValues;

    /**
     * Initializes a new string table that can hold 2^codeWidth.
     * 
     * @param codeWidth the code width
     */
    public StringTable(int codeWidth) {
        int tableSize = (int)Math.pow(2, codeWidth+1);
        maxCode = (int)Math.pow(2, codeWidth)-1;
        bits = codeWidth;
        bitMask = tableSize-1;
        prefixCodes = new int[tableSize];
        appendCharacters = new byte[tableSize];
        codeValues = new int[tableSize];

        Arrays.fill(codeValues, -1);
        Arrays.fill(prefixCodes, -1);
    }

    /**
     * Gets the code that will be assigned for the next entry added.
     * @return next code - initially <code>CLEAR_CODE+1</code>
     */
    public int nextCode() {
        return nextCode;
    }

    /**
     * Checks whether the table is full or not.
     * @return <code>true</code> if the table is full, <code>false</code> otherwise
     */
    public boolean isFull() {
        return nextCode > maxCode;
    }

    /**
     * Adds an entry to the table. If the table is full, ignores the add.
     * @param prefixCode the prefix code
     * @param appendCharacter the append character
     * @return <code>true</code> if the entry was added; <code>false</code> otherwise
     */
    public boolean add(int prefixCode, byte appendCharacter) {
        if (!(isFull())) {
            int index = findIndex(prefixCode, appendCharacter);
            prefixCodes[index] = prefixCode;
            appendCharacters[index] = appendCharacter;
            codeValues[index] = nextCode;
            nextCode++;

            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves assigned code value for the entry.
     * @param prefixCode the prefix code
     * @param appendCharacter the append character
     * @return assigned code value for the entry or <code>-1</code> if the entry was not found
     */
    public int codeValue(int prefixCode, byte appendCharacter) {
        return codeValues[findIndex(prefixCode, appendCharacter)];
    }

    private int findIndex(int prefixCode, byte appendCharacter) {
        int index  = calculateHash(prefixCode, appendCharacter) & bitMask;
        int probes = 1;

        while (true) {
            if (indexEmpty(index) || entryMatchesAt(index, prefixCode, appendCharacter)) {
                return index;
            } else {
                index = (index+(int)Math.pow(probes, 2)) & bitMask;
                probes++;
            }
        }
    }

    private boolean indexEmpty(int index) {
        return prefixCodes[index] == -1;
    }

    private boolean entryMatchesAt(int index, int prefixCode, byte appendCharacter) {
        return prefixCodes[index] == prefixCode && appendCharacters[index] == appendCharacter;
    }

    private int calculateHash(int prefixCode, byte appendCharacter) {
        int hash = ((prefixCode >> (bits) * 37) ^ (prefixCode*37 << (bits-8))) ^ (appendCharacter * 37);
        return Math.abs(hash);
    }
}
