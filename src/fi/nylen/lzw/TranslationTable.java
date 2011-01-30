package fi.nylen.lzw;

import java.util.Arrays;

public class TranslationTable {
    private int nextCode = StringTable.CLEAR_CODE+1;
    private int[] prefixCodes;
    private byte[] appendCharacters;
    private byte[] decodeStack;
    private byte[][] translated;
    private int tableSize;

    public TranslationTable(int codeWidth) {
        tableSize = (int)Math.pow(2, codeWidth);
        prefixCodes = new int[tableSize];
        appendCharacters = new byte[tableSize];
        decodeStack = new byte[tableSize/2];
        translated = new byte[tableSize/2][];
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
        if (translated[code] == null) {
            int length = 0;
            int originalCode = code;
            while (code > StringTable.CLEAR_CODE) {
                byte[] prefix = translated[code];
                if (prefix == null) {
                    decodeStack[length] = appendCharacters[code];
                    code = prefixCodes[code];
                    length++;
                } else {
                    System.arraycopy(prefix, 0, decodeStack, length, prefix.length);
                    length += prefix.length;
                }
            }
        
            decodeStack[length++] = (byte)code;
            translated[originalCode] = ArrayUtils.reverse(decodeStack, 0, length);
            return translated[originalCode];
        } else {
            return translated[code];
        }
    }
}
