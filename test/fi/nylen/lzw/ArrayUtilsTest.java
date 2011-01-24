package fi.nylen.lzw;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayUtilsTest {
    @Test
    public void shouldReverseArray() {
        byte[] input = new byte[] { 0x20, 0x11 };
        byte[] expected = new byte[] { 0x11, 0x20 };
        byte[] actual = ArrayUtils.reverse(input, 0, 2);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReverseOnlyPortion() {
        byte[] input = new byte[] { 0x20, 0x11, 0x30, 0x40 };
        byte[] expected = new byte[] { 0x30, 0x11 };
        byte[] actual = ArrayUtils.reverse(input, 1, 2);

        assertArrayEquals(expected, actual);
    }
}
