package fi.nylen.lzw;


public abstract class ArrayUtils {
    public static byte[] reverse(byte[] array, int off, int len) {
        byte[] reversed = new byte[len];
        int j = off+len-1;
        for (int i = 0; i < len; i++) {
            reversed[i] = array[j];
            j--;
        }

        return reversed;
    }
}
