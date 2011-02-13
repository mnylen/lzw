package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static fi.nylen.lzw.TestUtils.*;

public class StringTableTest {
    private StringTable table;

    @Before
    public void setUp() {
        table = new StringTable(9);
    }

    @Test
    public void testNextCode() {
        assertEquals(StringTable.CLEAR_CODE+1, table.nextCode());
        
        int nextCode = table.nextCode();
        table.add(ord('L'), (byte)'Z');
        assertEquals(nextCode, table.codeValue(ord('L'), (byte)'Z'));
    }

    @Test
    public void testAddNextCode() {
        int codeValueForLZ = table.nextCode();
        assertTrue(table.add(ord('L'), (byte)'Z'));

        int codeValueForHI = table.nextCode();
        assertTrue(table.add(ord('H'), (byte)'I'));

        assertEquals(codeValueForLZ, table.codeValue(ord('L'), (byte)'Z'));
        assertEquals(codeValueForHI, table.codeValue(ord('H'), (byte)'I'));
    }

    @Test
    public void testAddWhenTableIsFull() {
        table = new StringTable(3);
        assertFalse(table.add(ord('L'), (byte)'Z'));
        assertEquals(-1, table.codeValue(ord('L'), (byte)'Z'));
    }
}
