package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static fi.nylen.lzw.TestUtils.*;

public class StringTableTest {
    private StringTable table;

    @Before
    public void setUp() {
        table = new StringTable(3);
    }

    @Test
    public void nextCodeShouldInitiallyBeClearCodePlusOne() {
        assertEquals(StringTable.CLEAR_CODE+1, table.nextCode());
    }

    @Test
    public void addingStringShouldIncreaseNextCodeByOne() {
        int oldNextCode = table.nextCode();
        table.add(ord('L'), (byte)'Z');

        assertEquals(oldNextCode+1, table.nextCode());
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
