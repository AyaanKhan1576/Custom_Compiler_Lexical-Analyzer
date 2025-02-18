package package1;
import java.util.ArrayList;

public class LexicalAnalyzer {


    public ArrayList<Token> tokenize(String code) {
        ArrayList<Token> tokens = new ArrayList<>();
        // Remove comments first.
        String noComments = removeComments(code);
        String[] lines = splitLines(noComments);
        int lineNumber = 1;
        for (String line : lines) {
            line = trim(line);
            if (line.equals("")) {
                lineNumber++;
                continue;
            }
            int index = 0;
            while (index < line.length()) {
                char c = line.charAt(index);
                if (isWhitespace(c)) {
                    index++;
                    continue;
                }
                // Identifiers:
                if (isLetter(c)) {
                    StringBuilder ident = new StringBuilder();
                    while (index < line.length() && isLetter(line.charAt(index))) {
                        ident.append(line.charAt(index));
                        index++;
                    }
                    tokens.add(new Token("IDENTIFIER", ident.toString(), lineNumber));
                }
                // Numbers: 
                else if (isDigit(c)) {
                    StringBuilder number = new StringBuilder();
                    while (index < line.length() && isDigit(line.charAt(index))) {
                        number.append(line.charAt(index));
                        index++;
                    }
                    // Check for decimal point.
                    if (index < line.length() && line.charAt(index) == '.') {
                        number.append('.');
                        index++;
                        int digitCount = 0;
                        while (index < line.length() && isDigit(line.charAt(index)) && digitCount < 5) {
                            number.append(line.charAt(index));
                            index++;
                            digitCount++;
                        }
                        tokens.add(new Token("DECIMAL", number.toString(), lineNumber));
                    } else {
                        tokens.add(new Token("INTEGER", number.toString(), lineNumber));
                    }
                }
                // Character literal.
                else if (c == '\'') {
                    StringBuilder charLit = new StringBuilder();
                    charLit.append(c);
                    index++;
                    if (index < line.length()) {
                        charLit.append(line.charAt(index));
                        index++;
                    }
                    if (index < line.length() && line.charAt(index) == '\'') {
                        charLit.append('\'');
                        index++;
                    }
                    tokens.add(new Token("CHARACTER", charLit.toString(), lineNumber));
                }
                // Operators.
                else if (isOperator(c)) {
                    tokens.add(new Token("OPERATOR", "" + c, lineNumber));
                    index++;
                }
                // Punctuation.
                else if (isPunctuation(c)) {
                    tokens.add(new Token("PUNCTUATION", "" + c, lineNumber));
                    index++;
                } else {
                    tokens.add(new Token("UNKNOWN", "" + c, lineNumber));
                    index++;
                }
            }
            lineNumber++;
        }
        return tokens;
    }

    // Removes comments.
    private String removeComments(String code) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (index < code.length()) {
            if (index + 1 < code.length() && code.charAt(index) == '/' && code.charAt(index + 1) == '*') {
                // Multi-line comment.
                index += 2;
                while (index + 1 < code.length() && !(code.charAt(index) == '*' && code.charAt(index + 1) == '/')) {
                    index++;
                }
                index += 2;
            } else if (index + 1 < code.length() && code.charAt(index) == '/' && code.charAt(index + 1) == '/') {
                // Single-line comment.
                while (index < code.length() && code.charAt(index) != '\n') {
                    index++;
                }
            } else {
                result.append(code.charAt(index));
                index++;
            }
        }
        return result.toString();
    }

    // Splits the code into lines using '\n'.
    private String[] splitLines(String code) {
        return code.split("\n");
    }

    // Trims leading and trailing whitespace.
    private String trim(String s) {
        int start = 0, end = s.length() - 1;
        while (start < s.length() && isWhitespace(s.charAt(start)))
            start++;
        while (end >= 0 && isWhitespace(s.charAt(end)))
            end--;
        if (start > end)
            return "";
        return s.substring(start, end + 1);
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r';
    }

    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' ||
               c == '%' || c == '^' || c == '=' || c == '(' || c == ')';
    }

    private boolean isPunctuation(char c) {
        return c == ',' || c == ';';
    }
}
