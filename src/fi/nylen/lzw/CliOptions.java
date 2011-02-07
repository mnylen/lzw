package fi.nylen.lzw;

public class CliOptions {
    public static final int DEFAULT_CODE_WIDTH = 12;

    private Lzw.Action action;
    private int codeWidth;
    private int maxCodeWidth;
    private String fileName;

    public CliOptions(Lzw.Action action, int codeWidth, int maxCodeWidth, String fileName) {
        this.action       = action;
        this.codeWidth    = codeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.fileName     = fileName;
    }

    public Lzw.Action getAction() {
        return action;
    }

    public int getCodeWidth() {
        return codeWidth;
    }

    public int getMaxCodeWidth() {
        return maxCodeWidth;
    }

    public String getFileName() {
        return fileName;
    }

    public static CliOptions fromArgs(String[] args) throws IllegalOptionsException {
        int codeWidth = DEFAULT_CODE_WIDTH;
        int maxCodeWidth = -1;

        Lzw.Action action = parseAction(args);
        int off = 1;

        while (off < args.length) {
            String arg = args[off];
            
            if (!(arg.startsWith("--"))) {
                break;
            } else {
                if (arg.startsWith("--code-width")) {
                    codeWidth = parseNumericOption("--code-width", arg);
                } else if (arg.startsWith("--max-code-width")) {
                    maxCodeWidth = parseNumericOption("--max-code-width", arg);
                }
                
                off++;
            }
        }

        String fileName = parseFileName(args, off);
        maxCodeWidth = (maxCodeWidth != -1) ? maxCodeWidth : codeWidth;
        
        return new CliOptions(action, codeWidth, maxCodeWidth, fileName);
    }

    private static Lzw.Action parseAction(String[] args) {
        if (args.length < 0) {
            throw new IllegalOptionsException("Invalid action: one of 'compress', 'decompress' expected");
        } else {
            String actionName = args[0].toUpperCase();

            try {
                return Lzw.Action.valueOf(actionName);
            } catch (IllegalArgumentException e) {
                throw new IllegalOptionsException("Invalid action: one of 'compress', 'decompress' expected");
            }
        }
    }

    private static int parseNumericOption(String optionName, String arg) {
        return Integer.parseInt(arg.substring(arg.indexOf("=")+1));
    }

    private static String parseFileName(String[] args, int off) {
        if (off < args.length) {
            return args[off];
        } else {
            throw new IllegalOptionsException("No file given");
        }
    }
}