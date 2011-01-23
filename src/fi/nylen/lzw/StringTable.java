package fi.nylen.lzw;

import java.util.Arrays;
import java.util.HashMap;

import static fi.nylen.lzw.MathUtil.nextPrime;

public class StringTable {
    public static final int STOP_CODE = 256;
    public static final int CLEAR_CODE = 257;
    private int nextCode = CLEAR_CODE+1;
    private int bits;
    private int tableSize;
    private int[] prefixCodes;
    private byte[] appendCharacters;
    private int[] codeValues;
    
    public StringTable(int codeWidth) {
        tableSize = nextPrime((int)Math.pow(2, codeWidth));
        bits = codeWidth;

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
     * Adds an entry to the table.
     * @param prefixCode the prefix code
     * @param appendCharacter the append character
     */
    public void add(int prefixCode, byte appendCharacter) {
        int index = findIndex(prefixCode, appendCharacter);
        prefixCodes[index] = prefixCode;
        appendCharacters[index] = appendCharacter;
        codeValues[index] = nextCode++;
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
        int probes = 1;
        int index;

        while (true) {
            index = calculateHash(prefixCode, appendCharacter, probes);

            if (indexEmpty(index) || entryMatchesAt(index, prefixCode, appendCharacter)) {
                return index;
            } else {
                probes += 1;
            }
        }
    }

    private boolean indexEmpty(int index) {
        return prefixCodes[index] == -1;
    }

    private boolean entryMatchesAt(int index, int prefixCode, byte appendCharacter) {
        return prefixCodes[index] == prefixCode && appendCharacters[index] == appendCharacter;
    }

    private int calculateHash(int prefixCode, byte appendCharacter, int probes) {
        int hash = (prefixCode << (bits-8)) ^ appendCharacter + probes;
        return Math.abs(hash % tableSize);
    }
}
