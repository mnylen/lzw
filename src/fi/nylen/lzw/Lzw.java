package fi.nylen.lzw;

import java.io.*;

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
        } else if (options.getAction() == Action.DECOMPRESS) {
            decompress();
        }
    }    

    private void compress() throws IOException {
        File inputFile = new File(options.getFileName());
        String outputFileName = inputFile.getName() + ".lzw";
        File outputFile = new File(outputFileName);
        if (outputFile.exists()) {
            System.out.println("lzw: output file already exists: " + outputFile.getName());
            return;
        }
        
        createOutputFile(outputFile);
        
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
        byte[] input = new byte[8192];
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

    private File createOutputFile(File outputFile) {
        try {
            if (outputFile.createNewFile()) {
                return outputFile;
            }
        } catch (IOException e) {
        }

        return null;
    }

    private void decompress() throws IOException {
        File inputFile  = new File(options.getFileName());

        File outputFile = new File(decompressedOutputFileName(inputFile.getName()));
        if (outputFile.exists()) {
            System.out.println("lzw: output file already exists: " + outputFile.getName());
            return;
        }

        createOutputFile(outputFile);
        LzwInputStream lzw = null;
        OutputStream to    = null;

        try {
            lzw = createLzwInputStream(inputFile);
            to = new BufferedOutputStream(new FileOutputStream(outputFile));
            byte[] buffer = new byte[8192];

            int bytesRead;
            while ( (bytesRead = lzw.read(buffer)) > 0) {
                to.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Remove the output file
            if (outputFile.exists()) {
                outputFile.delete();
            }
        } finally {
            if (lzw != null) {
                try {
                    lzw.close();
                } catch(IOException e) {

                }
            }

            if (to != null) {
                try {
                    to.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private String decompressedOutputFileName(String inputFileName) {
        int pos;
        if ((pos = inputFileName.lastIndexOf('.')) != -1) {
            return inputFileName.substring(0, pos);
        } else {
            return inputFileName;
        }
    }

    private LzwInputStream createLzwInputStream(File inputFile) throws IOException {
        return new LzwInputStream(
                options.getCodeWidth(),
                options.getMaxCodeWidth(),
                new BufferedInputStream(new FileInputStream(inputFile)));
    }
}
