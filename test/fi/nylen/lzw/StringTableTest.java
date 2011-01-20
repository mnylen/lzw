package fi.nylen.lzw;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringTableTest {
    private StringTable table;

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
}
