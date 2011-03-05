package fi.nylen.lzw;

public class TranslationTable {
    private int nextCode = StringTable.CLEAR_CODE+1;
    private int maxCode;
    private byte[][] translated;
    private int tableSize;

    public TranslationTable(int codeWidth) {
        tableSize = (int)Math.pow(2, codeWidth);
        maxCode   = tableSize-1;
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

    public byte[] translate(int code) {
        if (code > StringTable.CLEAR_CODE) {
            return translated[code];
        } else {
            return new byte[] { (byte)code };
        }
    }
}
