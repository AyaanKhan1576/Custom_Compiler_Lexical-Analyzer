package package1;

public class ErrorHandler {
    public static void reportError(String message, int lineNumber) {
        System.err.println("Error (line " + lineNumber + "): " + message);
    }
}
