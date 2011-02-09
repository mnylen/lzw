package fi.nylen.lzw;

import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class LzwTest {
    private Lzw lzw;

    @After
    public void tearDown() {
        File file = new File("eng_com.dic.lzw");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testCompress() throws IOException {
        lzw = new Lzw(new CliOptions(Lzw.Action.COMPRESS, 12, 12, "test/fi/nylen/lzw/eng_com.dic"));
        lzw.run();

        File file = new File("eng_com.dic.lzw");
        assertTrue(file.exists());
        assertArrayEquals(compressFile("test/fi/nylen/lzw/eng_com.dic", 12, 12), readInFile(file));
    }

    private byte[] readInFile(File file) throws IOException {
        byte[] data = new byte[(int)file.length()];
        FileInputStream is = new FileInputStream(file);

        assertTrue(is.read(data) == file.length());
        return data;
    }

    private byte[] compressFile(String fileName, int codeWidth, int maxCodeWidth) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LzwOutputStream os = new LzwOutputStream(codeWidth, maxCodeWidth, baos);

        File file = new File(fileName);
        byte[] data = readInFile(file);

        os.write(data);
        os.finish();
        os.close();
        return baos.toByteArray();
    }
}
