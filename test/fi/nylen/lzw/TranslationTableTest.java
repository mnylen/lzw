package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import static fi.nylen.lzw.TestUtils.ord;
import static org.junit.Assert.*;

public class TranslationTableTest {
    private TranslationTable table;

    @Before
    public void setUp() {
        table = new TranslationTable(10);
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

    @Test
    public void shouldTranslate() {
        int code;
        code = table.add(ord('H'), (byte)'E'); // 258
        code = table.add(code, (byte)'L');     // 259
        code = table.add(code, (byte)'L');     // 260
        code = table.add(code, (byte)'O');     // 261

        byte[] expected   = "HELLO".getBytes();
        byte[] translated = table.translate(code);
        assertArrayEquals(expected, translated);

        // should cache results for faster lookup
        byte[] translateSecondTime = table.translate(code);
        assertEquals(translated, translateSecondTime);

        byte[] translatePartial = table.translate(260);
        assertArrayEquals("HELL".getBytes(), translatePartial);
    }
}
