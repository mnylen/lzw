package fi.nylen.lzw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility program for compressing files with LZW and decompressing already
 * compressed files.
 */
public class Lzw {
    /**
     * Available actions.
     */
    public enum Action {
        COMPRESS,
        DECOMPRESS
    }

    private CliOptions options;

    /**
     * Creates a new <code>Lzw</code> for the given options.
     * @param options options
     */
    public Lzw(CliOptions options) {
        this.options = options;
    }

    /**
     * The main program.
     * @param args program arguments
     */
    public static void main(String[] args) {
        try {
            new Lzw(CliOptions.fromArgs(args)).run();
        } catch (IOException ioe) {
            System.out.println("lzw: Could not complete your action: " + ioe.getMessage());
        } catch (IllegalOptionsException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println("Usage: lzw compress [option...] FILE");
            System.out.println("       lzw decompress [option...] FILE");
        }
    }

    /**
     * Runs the desired action.
     * @throws IOException if anything goes wrong
     */
    public void run() throws IOException {
        if (options.getAction() == Action.COMPRESS) {
            compress();
        }
    }    

    private void compress() throws IOException {
        File inputFile = new File(options.getFileName());
        File outputFile = createOutputFile();
        LzwOutputStream lzw = null;
        FileInputStream fis = null;
        
        try {
            lzw = createLzwOutputStream(outputFile);
            fis = new FileInputStream(inputFile);
            writeCompressedData(fis, lzw);
        } finally {
            try {
                if (lzw != null) {
                    lzw.close();
                }
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }
        
        System.out.println();
    }

    private LzwOutputStream createLzwOutputStream(File outputFile) throws IOException {
        return new LzwOutputStream(
                options.getCodeWidth(),
                options.getMaxCodeWidth(),
                new FileOutputStream(outputFile));
    }

    private void writeCompressedData(FileInputStream from, LzwOutputStream to) throws IOException {
        byte[] input = new byte[4096];
        int bytesRead;
        int i = 0;

        while ( ( bytesRead = from.read(input) ) != -1 ) {
            if (i % 10 == 0) {
                System.out.print("*");
             }

            to.write(input, 0, bytesRead);
             i++;
        }

        to.finish();
    }

    private File createOutputFile() {
        File inputFile = new File(options.getFileName());
        String outputFileName = inputFile.getName() + ".lzw";
        File outputFile = new File(outputFileName);
        try {
            if (outputFile.createNewFile()) {
                return outputFile;
            }
        } catch (IOException e) {
        }

        return null;
    }
}
