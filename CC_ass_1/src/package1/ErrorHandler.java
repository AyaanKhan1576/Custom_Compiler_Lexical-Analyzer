package package1;

public class ErrorHandler {
    public static void reportError(String message, int lineNumber) {
        System.err.println("Error (line " + lineNumber + "): " + message);
    }
    
    private static final String FILE_NOT_FOUND = "Forsooth! The file cannot be found - ";
    private static final String FILE_PATH_HINT = "Prithee ensure the file exists and the path is correct.";
    private static final String FILE_READ_ERROR = "Alas! Error in reading thy file: ";
    private static final String SYNTAX_ERROR = "Forsooth! Thine syntax is amiss at line ";
    private static final String TYPE_ERROR = "Alas! The types do not match at line ";
    private static final String UNDEFINED_ERROR = "Hark! This symbol is unknown: ";
    private static final String SCOPE_ERROR = "Thou cannot access this variable from this realm at line ";

    // File-related errors
    public static void handleFileNotFound(String filePath) {
        System.err.println(FILE_NOT_FOUND + filePath);
        System.err.println(FILE_PATH_HINT);
    }

    public static void handleFileReadError(String message) {
        System.err.println(FILE_READ_ERROR + message);
    }

    // Syntax and parsing errors
    public static void handleSyntaxError(int lineNumber, String details) {
        System.err.println(SYNTAX_ERROR + lineNumber + ": " + details);
    }

    // Type checking errors
    public static void handleTypeError(int lineNumber, String expected, String found) {
        System.err.println(TYPE_ERROR + lineNumber);
        System.err.println("Expected: " + expected + ", Found: " + found);
    }

    // Symbol-related errors
    public static void handleUndefinedSymbol(String symbol) {
        System.err.println(UNDEFINED_ERROR + symbol);
    }

    // Scope-related errors
    public static void handleScopeError(int lineNumber, String symbol) {
        System.err.println(SCOPE_ERROR + lineNumber);
        System.err.println("Symbol: " + symbol);
    }

    // Generic error handler for unexpected errors
    public static void handleGenericError(String message) {
        System.err.println("Verily, an error hath occurred: " + message);
    }

    // Warning messages (less severe than errors)
    public static void issueWarning(String message) {
        System.out.println("Hearken! A warning doth appear: " + message);
    }
}
