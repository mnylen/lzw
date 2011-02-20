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
        File inputFile = new File("eng_com.dic.lzw");
        if (inputFile.exists()) {
            inputFile.delete();
        }

        File outputFile = new File("eng_com.dic");
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testCompressAndDecompress() throws IOException {
        testCompress();
        testDecompress();
    }

    private void testCompress() throws IOException {
        lzw = new Lzw(new CliOptions(Lzw.Action.COMPRESS, 9, 13, "test/fi/nylen/lzw/eng_com.dic"));
        lzw.run();

        File file = new File("eng_com.dic.lzw");
        assertTrue(file.exists());
        assertArrayEquals(compressFile("test/fi/nylen/lzw/eng_com.dic", 9, 13), readInFile(file));
    }


    private void testDecompress() throws IOException {
        lzw = new Lzw(new CliOptions(Lzw.Action.DECOMPRESS, 9, 13, "eng_com.dic.lzw"));
        lzw.run();

        File originalFile = new File("test/fi/nylen/lzw/eng_com.dic");
        File outputFile   = new File("eng_com.dic");
        assertTrue(outputFile.exists());
        assertArrayEquals(readInFile(originalFile), readInFile(outputFile));
    }

    private byte[] readInFile(File file) throws IOException {
        byte[] data = new byte[(int)file.length()];
        FileInputStream is = new FileInputStream(file);

        try {
            assertTrue(is.read(data) == file.length());
        } finally {
            is.close();
        }
        
        return data;
    }

    private byte[] compressFile(String fileName, int codeWidth, int maxCodeWidth) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LzwOutputStream os = new LzwOutputStream(codeWidth, maxCodeWidth, baos);

        try {
            File file = new File(fileName);
            byte[] data = readInFile(file);

            os.write(data);
            os.finish();
        } finally {
            os.close();    
        }

        return baos.toByteArray();
    }
}
