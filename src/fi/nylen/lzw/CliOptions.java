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
        Lzw.Action action = parseAction(args);
        int codeWidth = DEFAULT_CODE_WIDTH;
        int maxCodeWidth = -1;
        String fileName = args[args.length-1];
        
        for (int i = 1; i < (args.length-1); i++) {
            String arg = args[i];

            if (arg.startsWith("--code-width")) {
                codeWidth = Integer.parseInt(arg.substring(arg.indexOf('=')+1));
            } else if (arg.startsWith("--max-code-width")) {
                maxCodeWidth = Integer.parseInt(arg.substring(arg.indexOf('=')+1));
            }
        }

        if (maxCodeWidth == -1) {
            maxCodeWidth = codeWidth;
        }

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
}