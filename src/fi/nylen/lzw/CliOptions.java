package fi.nylen.lzw;

/**
 * Utility class for parsing command line options passed to the <code>Lzw</code> utility
 * program.
 */
class CliOptions {
    public static final int DEFAULT_CODE_WIDTH = 9;

    private Lzw.Action action;
    private int codeWidth;
    private int maxCodeWidth;
    private String fileName;

    /**
     * Creates new options.
     * @param action the action to perform
     * @param codeWidth initial code width
     * @param maxCodeWidth maximum code width
     * @param fileName file to compress/decompress
     */
    public CliOptions(Lzw.Action action, int codeWidth, int maxCodeWidth, String fileName) {
        this.action       = action;
        this.codeWidth    = codeWidth;
        this.maxCodeWidth = maxCodeWidth;
        this.fileName     = fileName;
    }

    /**
     * Gets action.
     * @return action
     */
    public Lzw.Action getAction() {
        return action;
    }

    /**
     * Gets initial code width.
     * @return initial code width
     */
    public int getCodeWidth() {
        return codeWidth;
    }

    /**
     * Gets maximum code width.
     * @return maximum code width
     */
    public int getMaxCodeWidth() {
        return maxCodeWidth;
    }

    /**
     * Gets file name to compress/decompress.
     * @return file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Parses a <code>CliOptions</code> from the given arguments array.
     * @param args the arguments
     * @return the <code>CliOptions</code> parsed
     * @throws IllegalOptionsException if the options could not be parsed
     */
    public static CliOptions fromArgs(String[] args) throws IllegalOptionsException {
        int codeWidth = DEFAULT_CODE_WIDTH;
        int maxCodeWidth = -1;

        Lzw.Action action = parseAction(args);
        int off = 1;

        while (off < args.length) {
            String arg = args[off];
            String optionName = parseOptionName(arg);
            String optionValue = parseOptionValue(arg);

            if (optionName.equals("--code-width")) {
                codeWidth = parseNumericOption("--code-width", optionValue);
                if (codeWidth < 9) {
                    throw new IllegalOptionsException("Invalid value: --code-width must be at least 9");
                } else if (maxCodeWidth != -1 && maxCodeWidth < codeWidth) {
                    throw new IllegalOptionsException("Invalid value: --code-width must be less than or equal to --max-code-width");
                } else if (codeWidth > 32) {
                    throw new IllegalOptionsException("Invalid value: --code-width must not exceed 32");
                }
                off++;
            } else if (optionName.equals("--max-code-width")) {
                maxCodeWidth = parseNumericOption("--max-code-width", optionValue);
                if (maxCodeWidth < 9) {
                    throw new IllegalOptionsException("Invalid value: --max-code-width must be at least 9");
                } else if (codeWidth > maxCodeWidth) {
                    throw new IllegalOptionsException("Invalid value: --max-code-width must be greater than or equal to --code-width");
                } else if (maxCodeWidth > 32) {
                    throw new IllegalOptionsException("Invalid value: --max-code-width must not exceed 32");
                }
                off++;
            } else if (optionName.startsWith("--")) {
                throw new IllegalOptionsException("Unknown option: " + optionName);
            } else {
                break;
            }
        }

        String fileName = parseFileName(args, off);
        maxCodeWidth = (maxCodeWidth != -1) ? maxCodeWidth : codeWidth;
        
        return new CliOptions(action, codeWidth, maxCodeWidth, fileName);
    }

    private static String parseOptionName(String arg) {
        if (arg.indexOf('=') != -1) {
            return arg.substring(0, arg.indexOf('='));
        } else {
            return arg;
        }
    }

    private static String parseOptionValue(String arg) {
        if (arg.indexOf('=') != -1) {
            return arg.substring(arg.indexOf('=')+1);
        } else {
            return "";
        }
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

    private static int parseNumericOption(String optionName, String optionValue) {
        if (optionValue.isEmpty()) {
            throw new IllegalOptionsException("Invalid value: " + optionName + " must be numeric value");
        }

        try {
            return Integer.parseInt(optionValue);
        } catch (NumberFormatException e) {
            throw new IllegalOptionsException("Invalid value: " + optionName + " must be numeric value");
        }
    }

    private static String parseFileName(String[] args, int off) {
        if (off < args.length) {
            return args[off];
        } else {
            throw new IllegalOptionsException("No file given");
        }
    }
}