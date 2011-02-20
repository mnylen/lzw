package fi.nylen.lzw;

public class TranslationTable {
    private int nextCode = StringTable.CLEAR_CODE+1;
    private int maxCode;
    private int[] prefixCodes;
    private byte[] appendCharacters;
    private byte[] decodeStack;
    private byte[][] translated;
    private int tableSize;

    public TranslationTable(int codeWidth) {
        tableSize = (int)Math.pow(2, codeWidth);
        maxCode   = tableSize-1;
        prefixCodes = new int[tableSize];
        appendCharacters = new byte[tableSize];
        decodeStack = new byte[tableSize/2];
        translated = new byte[tableSize][];
    }

    public int nextCode() {
        return nextCode;
    }
    
    public boolean contains(int code) {
        return code < nextCode;
    }

    public int add(int prefixCode, byte appendCharacter) {
        int code = nextCode;
        if (code <= maxCode) {
            prefixCodes[code] = prefixCode;
            appendCharacters[code] = appendCharacter;
            nextCode++;
        
            return code;
        } else {
            return -1;
        }
    }

    public byte[] translate(int code) {
        int originalCode = code;
        int length       = 0;
        byte[] prefix    = translated[code];
        
        if (prefix != null) {
            return prefix;
        } else {
            boolean prefixFound = false;
            while (code > StringTable.CLEAR_CODE) {
                if ((prefix = translated[code]) != null) {
                    System.arraycopy(ArrayUtils.reverse(prefix, 0, prefix.length), 0, decodeStack, length, prefix.length);
                    length += prefix.length;
                    prefixFound = true;
                    break;
                }

                decodeStack[length] = appendCharacters[code];
                code = prefixCodes[code];
                length++;
            }

            if (!prefixFound) {
                decodeStack[length++]    = (byte)code;
            }

            translated[originalCode] = ArrayUtils.reverse(decodeStack, 0, length);

            return translated[originalCode];
        }
    }
}
