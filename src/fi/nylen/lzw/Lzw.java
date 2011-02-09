package fi.nylen.lzw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Lzw {
    public enum Action {
        COMPRESS,
        DECOMPRESS
    }

    private CliOptions options;
    
    public Lzw(CliOptions options) {
        this.options = options;
    }

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

    public void run() throws IOException {
        if (options.getAction() == Action.COMPRESS) {
            compress();
        }
    }

    private void compress() throws IOException {
        File inputFile = new File(options.getFileName());
        File outputFile = createOutputFile();
        LzwOutputStream lzw = new LzwOutputStream(
                options.getCodeWidth(),
                options.getMaxCodeWidth(),
                new FileOutputStream(outputFile));

        FileInputStream fis = new FileInputStream(inputFile);
        byte[] input = new byte[4096];
        int bytesRead;
        int i = 0;
        
        while ( ( bytesRead = fis.read(input) ) != -1 ) {
            if (i % 10 == 0) {
                System.out.print("*");
            }
            
            lzw.write(input, 0, bytesRead);
            i++;
        }

        lzw.finish();
        lzw.close();
        
        System.out.println();
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
