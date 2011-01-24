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
        if (code < StringTable.STOP_CODE) {
           return new byte[] { (byte)code };
        } else {
            byte[] prefix = translate(prefixCodes[code]);
            byte append = appendCharacters[code];
            byte[] string = new byte[prefix.length+1];
            System.arraycopy(prefix, 0, string, 0, prefix.length);
            string[string.length-1] = append;

            return string;
        }
    }
}
