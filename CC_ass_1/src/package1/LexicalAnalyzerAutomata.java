package package1;
import java.util.ArrayList;

public class LexicalAnalyzerAutomata {
    private DFA dfa;

    public LexicalAnalyzerAutomata(DFA dfa) {
        this.dfa = dfa;
    }

    // Tokenize the input string using the DFA (maximal munch) with special handling for string literals
    // and automatic conversion of uppercase letters (outside of string literals) to lowercase.
    public ArrayList<Token> tokenize(String input) {
        // Remove comments from the input.
        input = removeComments(input);

        ArrayList<Token> tokens = new ArrayList<>();
        int pos = 0;
        int lineNumber = 1;
        while (pos < input.length()) {
            // If current character is not part of a string literal and is uppercase, warn and convert it.
            if (input.charAt(pos) != '"' && Character.isUpperCase(input.charAt(pos))) {
                System.out.println("Lexical Warning (line " + lineNumber + " pos " + pos + "): "
                        + "Uppercase letter '" + input.charAt(pos) + "' detected. Converting to lowercase.");
                input = input.substring(0, pos) 
                        + Character.toLowerCase(input.charAt(pos)) 
                        + input.substring(pos + 1);
                // Do not advance pos, recheck the character (now lowercase)
                continue;
            }
            
            // Skip whitespace.
            while (pos < input.length() && isWhitespace(input.charAt(pos))) {
                if (input.charAt(pos) == '\n')
                    lineNumber++;
                pos++;
            }
            if (pos >= input.length())
                break;
            
            // Special handling for string literals:
            if (input.charAt(pos) == '"') {
                int start = pos;
                pos++; // Skip the opening quote
                while (pos < input.length() && input.charAt(pos) != '"') {
                    pos++;
                }
                if (pos < input.length() && input.charAt(pos) == '"') {
                    pos++; // Include the closing quote
                }
                String lexeme = input.substring(start, pos);
                tokens.add(new Token("STRING", lexeme, lineNumber));
                continue;
            }
            
            // Use DFA-based scanning for other tokens.
            int currentState = dfa.getStartState();
            int lastAcceptPos = -1;
            String lastTokenType = null;
            int currentPos = pos;
            while (currentPos < input.length()) {
                char c = input.charAt(currentPos);
                // Also check for uppercase characters during DFA scanning.
                if (c != '"' && Character.isUpperCase(c)) {
                    System.out.println("Lexical Warning (line " + lineNumber + " pos " + currentPos + "): "
                            + "Uppercase letter '" + c + "' detected. Converting to lowercase.");
                    input = input.substring(0, currentPos)
                            + Character.toLowerCase(c)
                            + input.substring(currentPos + 1);
                    c = input.charAt(currentPos);
                }
                int nextState = dfa.getTransition(currentState, c);
                if (nextState == -1)
                    break;
                currentState = nextState;
                currentPos++;
                String tokenType = dfa.getTokenType(currentState);
                if (tokenType != null) {
                    lastAcceptPos = currentPos;
                    lastTokenType = tokenType;
                }
            }
            if (lastAcceptPos == -1) {
                System.err.println("Lexical error at position " + pos);
                pos++; // Skip error character.
            } else {
                String lexeme = input.substring(pos, lastAcceptPos);
                tokens.add(new Token(lastTokenType, lexeme, lineNumber));
                pos = lastAcceptPos;
            }
        }
        return tokens;
    }

    // Remove single-line (// ...) and multi-line (/* ... */) comments.
    private String removeComments(String input) {
        StringBuilder output = new StringBuilder();
        int pos = 0;
        while (pos < input.length()) {
            if (pos + 1 < input.length() && input.charAt(pos) == '/' && input.charAt(pos + 1) == '*') {
                // Multi-line comment.
                pos += 2;
                while (pos + 1 < input.length() && !(input.charAt(pos) == '*' && input.charAt(pos + 1) == '/')) {
                    pos++;
                }
                pos += 2; // Skip closing "*/"
            } else if (pos + 1 < input.length() && input.charAt(pos) == '/' && input.charAt(pos + 1) == '/') {
                // Single-line comment.
                pos += 2;
                while (pos < input.length() && input.charAt(pos) != '\n') {
                    pos++;
                }
            } else {
                output.append(input.charAt(pos));
                pos++;
            }
        }
        return output.toString();
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }
}
