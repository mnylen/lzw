package fi.nylen.lzw;

import java.util.HashMap;

public class StringTable {
    public static final int STOP_CODE = 256;
    public static final int CLEAR_CODE = 257;
    private int currentCode = -1;
    private HashMap<StringEntry, Integer> entries = new HashMap<StringEntry, Integer>();

    private class StringEntry {
        private int prefixCode;
        private int appendCharacter;

        StringEntry(int prefixCode, int appendCharacter) {
            this.prefixCode = prefixCode;
            this.appendCharacter = appendCharacter;
        }

        @Override
        public int hashCode() {
            return prefixCode * appendCharacter;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StringEntry)) {
                return false;
            }
            
            StringEntry other = (StringEntry)obj;
            return other.prefixCode == prefixCode && other.appendCharacter == appendCharacter;
        }
    }

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

    /**
     *
     */
    public void add(int prefixCode, byte appendCharacter) {
        entries.put(new StringEntry(prefixCode, appendCharacter), nextCode());
        increaseCurrentCode();
    }

    public int codeValue(int prefixCode, byte appendCharacter) {
        StringEntry entry = new StringEntry(prefixCode, appendCharacter);

        if (entries.containsKey(entry)) {
            return entries.get(entry);
        } else {
            return -1;
        }
    }
}
