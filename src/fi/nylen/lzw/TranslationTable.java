package fi.nylen.lzw;

public class TranslationTable {
    private int nextCode = StringTable.CLEAR_CODE+1;

    public TranslationTable(int codeWidth) {
        
    }

    public boolean contains(int code) {
        return code < nextCode;
    }
}
