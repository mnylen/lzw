package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import static fi.nylen.lzw.TestUtils.ord;
import static org.junit.Assert.*;

public class TranslationTableTest {
    private TranslationTable table;

    @Before
    public void setUp() {
        table = new TranslationTable(3);
    }
    
    @Test
    public void shouldNotContainCodeWhenNotAdded() {
        assertFalse(table.contains(StringTable.CLEAR_CODE+1));
    }

    @Test
    public void shouldContainCodesUpToClearCode() {
        assertTrue(table.contains(1));
    }

    @Test
    public void shouldContainAfterAdding() {
        table.add(ord('H'), (byte)'E');
        assertTrue(table.contains(StringTable.CLEAR_CODE+1));

        table.add(StringTable.CLEAR_CODE+1, (byte)'L');
        assertTrue(table.contains(StringTable.CLEAR_CODE+2));
    }

    @Test
    public void shouldReturnAssignedCodeWhenAdding() {
        assertEquals(StringTable.CLEAR_CODE+1, table.add(ord('H'), (byte)'E'));
    }
}
