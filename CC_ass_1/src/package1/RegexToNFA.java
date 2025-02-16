package package1;
import java.util.Stack;

public class RegexToNFA {
    // Special marker to denote that the next character is escaped.
    public static final char ESCAPED = '\u0001';

    // Return operator precedence.
    private static int precedence(char c) {
        if (c == '*')
            return 3;
        if (c == '.')
            return 2;
        if (c == '|')
            return 1;
        return 0;
    }

    /**
     * Inserts an explicit concatenation operator ('.') where needed.
     */
    public static String insertExplicitConcatenation(String regex) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            result.append(c1);
            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);
                if ((isLiteral(c1) || c1 == '*' || c1 == ')')
                        && (isLiteral(c2) || c2 == '(' || c2 == '\\')) {
                    result.append('.');
                }
            }
        }
        return result.toString();
    }

    // In this engine, letters and digits are considered literals.
    private static boolean isLiteral(char c) {
        return Character.isLetterOrDigit(c);
    }

    /**
     * Converts an infix regex to postfix notation using the shunting-yard algorithm.
     * Supports a minimal escape mechanism: a backslash '\' causes the next character to be treated as literal.
     */
    public static String infixToPostfix(String regex) {
        String exp = insertExplicitConcatenation(regex);
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == '\\') {
                // Insert the escape marker and then the next character.
                i++;
                if (i < exp.length()) {
                    output.append(ESCAPED);
                    output.append(exp.charAt(i));
                }
            } else if (isLiteral(c)) {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(')
                    stack.pop();
            } else { // Operators: *, ., |
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    output.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }
        return output.toString();
    }

    /**
     * Builds an NFA from a postfix regular expression using Thompson's construction.
     */
    public static NFA buildNFAFromPostfix(String postfix) {
        Stack<NFA> stack = new Stack<>();
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);
            if (c == ESCAPED) {
                // Next character is a literal.
                i++;
                if (i < postfix.length()) {
                    char literal = postfix.charAt(i);
                    stack.push(NFA.literal(literal));
                }
            } else if (c == '*') {
                NFA nfa = stack.pop();
                stack.push(NFA.kleeneStar(nfa));
            } else if (c == '.') {
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                stack.push(NFA.concatenate(nfa1, nfa2));
            } else if (c == '|') {
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                stack.push(NFA.union(nfa1, nfa2));
            } else { // Literal character.
                stack.push(NFA.literal(c));
            }
        }
        return stack.pop();
    }
}
