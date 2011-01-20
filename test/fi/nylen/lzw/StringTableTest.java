package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringTableTest {
    private StringTable table;

    public int ord(char c) {
        return (int)c;
    }

    @Before
    public void setUp() {
        table = new StringTable();
    }

    @Test
    public void currentCodeShouldInitiallyBeNotSpecified() {
        assertEquals(-1, table.currentCode());
    }

    @Test
    public void nextCodeShouldInitiallyBeClearCodePlusOne() {
        assertEquals(StringTable.CLEAR_CODE+1, table.nextCode());
    }

    @Test
    public void increaseCurrentCodeShouldChangeCurrentCodeAsNextCode() {
        int nextCode = table.nextCode();
        table.increaseCurrentCode();

        assertEquals(nextCode, table.currentCode());
    }

    @Test
    public void increaseCurrentCodeShouldIncreaseNextCode() {
        table.increaseCurrentCode();
        assertEquals(table.currentCode()+1, table.nextCode());
    }

    @Test
    public void addingStringShouldIncreaseCurrentCode() {
        int nextCode = table.nextCode();
        table.add(ord('L'), (byte)'Z');

        assertEquals(table.currentCode(), nextCode);
    }

    @Test
    public void addedStringsShouldHaveCodeValue() {
        int codeValueForLZ = table.nextCode();
        table.add(ord('L'), (byte)'Z');

        int codeValueForHI = table.nextCode();
        table.add(ord('H'), (byte)'I');

        assertEquals(codeValueForLZ, table.codeValue(ord('L'), (byte)'Z'));
        assertEquals(codeValueForHI, table.codeValue(ord('H'), (byte)'I'));
    }

    @Test
    public void stringsThatHaventBeenAddedShouldHaveCodeValueMinusOne() {
        assertEquals(-1, table.codeValue(ord('N'), (byte)'O'));
    }
}
