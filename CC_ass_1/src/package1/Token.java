package package1;

public class Token {
    public String type;
    public String lexeme;
    public int lineNumber;

    public Token(String type, String lexeme, int lineNumber) {
        this.type = type;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "[Line " + lineNumber + "] " + type + " : '" + lexeme + "'";
    }
}
