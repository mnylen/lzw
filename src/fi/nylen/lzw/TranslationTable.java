package fi.nylen.lzw;

import java.util.Arrays;

public class TranslationTable {
    private int nextCode = StringTable.CLEAR_CODE+1;
    private int[] prefixCodes;
    private byte[] appendCharacters;
    private byte[] decodeStack;
    private int tableSize;

    public TranslationTable(int codeWidth) {
        tableSize = (int)Math.pow(2, codeWidth);
        prefixCodes = new int[tableSize];
        appendCharacters = new byte[tableSize];
        decodeStack = new byte[tableSize];
    }

    public boolean contains(int code) {
        return code < nextCode;
    }

    public int add(int prefixCode, byte appendCharacter) {
        int code = nextCode;
        prefixCodes[code] = prefixCode;
        appendCharacters[code] = appendCharacter;
        nextCode++;
        
        return code;
    }

    public byte[] translate(int code) {
        int length = 0;
        while (code > StringTable.CLEAR_CODE) {
            decodeStack[length] = appendCharacters[code];
            code = prefixCodes[code];
            length++;
        }
        
        decodeStack[length++] = (byte)code;
        return ArrayUtils.reverse(decodeStack, 0, length);
    }
}
