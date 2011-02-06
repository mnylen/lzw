package fi.nylen.lzw;

public class CliOptions {
    public static final int DEFAULT_CODE_WIDTH = 12;
    public static final int DEFAULT_MAX_CODE_WIDTH = 20;

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

    public static CliOptions fromArgs(String[] args) {
        Lzw.Action action = Lzw.Action.valueOf(args[0].toUpperCase());
        int codeWidth = 0;
        int maxCodeWidth = 0;
        String fileName = args[args.length-1];
        
        for (int i = 1; i < (args.length-1); i++) {
            String arg = args[i];

            if (arg.startsWith("--code-width")) {
                codeWidth = Integer.parseInt(arg.substring(arg.indexOf('=')+1));
            } else if (arg.startsWith("--max-code-width")) {
                maxCodeWidth = Integer.parseInt(arg.substring(arg.indexOf('=')+1));
            }
        }

        return new CliOptions(action, codeWidth, maxCodeWidth, fileName);
    }
}