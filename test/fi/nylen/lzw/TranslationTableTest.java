package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TranslationTableTest {
    private TranslationTable table;

    @Before
    public void setUp() {
        table = new TranslationTable(3);
    }
    
    @Test
    public void shouldNotContainCodeWhenNotAdded() {
        assertFalse(table.contains(300));
    }

    @Test
    public void shouldContainCodesUpToClearCode() {
        assertTrue(table.contains(1));
    }
}
