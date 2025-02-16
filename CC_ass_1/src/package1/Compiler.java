package package1;
import java.util.ArrayList;
import java.util.List;

//Testing new branch : Ayaan

public class Compiler {
    // Special symbol for epsilon transitions.
    public static final char EPSILON = '\0';

    public static void main(String[] args) {
        System.out.println("=== COMPILER CONSTRUCTION PROJECT ===\n");

        // 1. Define token types with their regexes and priorities.
        // Lower priority value means higher precedence.
        List<TokenDefinition> definitions = new ArrayList<>();

        // Combine all keywords (including data types) into one definition.
        definitions.add(new TokenDefinition("KEYWORD", "if|else|while|for|return|int|boolean|char|string|decimal", 1));

        // Boolean literals.
        definitions.add(new TokenDefinition("BOOLEAN", "true|false", 1));

        // Identifier: letter followed by letters or digits.
        String letter = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)";
        String digit  = "(0|1|2|3|4|5|6|7|8|9)";
        String identifierRegex = letter + "(" + letter + "|" + digit + ")*";
        definitions.add(new TokenDefinition("IDENTIFIER", identifierRegex, 2));

        // Integer: one or more digits.
        String integerRegex = digit + "(" + digit + ")*";
        definitions.add(new TokenDefinition("INTEGER", integerRegex, 3));

        // Decimal: integer, a literal dot, then integer.
        String decimalRegex = integerRegex + "\\." + integerRegex;
        definitions.add(new TokenDefinition("DECIMAL", decimalRegex, 3));

        // Character literal: a letter enclosed in single quotes.
        String charLiteralRegex = "'" + letter + "'";
        definitions.add(new TokenDefinition("CHARACTER", charLiteralRegex, 2));

        // STRING literal: a double quote, then zero or more (letter, digit, or space), then a double quote.
        definitions.add(new TokenDefinition("STRING", "\\\"(" + letter + "|" + digit + "| )*\\\"", 1));

        // Operators (all marked as OPERATOR).
        definitions.add(new TokenDefinition("OPERATOR", "\\+", 1));
        definitions.add(new TokenDefinition("OPERATOR", "\\-", 1));
        definitions.add(new TokenDefinition("OPERATOR", "\\*", 1));
        definitions.add(new TokenDefinition("OPERATOR", "/", 1));
        definitions.add(new TokenDefinition("OPERATOR", "%", 1));
        definitions.add(new TokenDefinition("OPERATOR", "\\^", 1));
        // NEW: Add "=" and ">" operators.
        definitions.add(new TokenDefinition("OPERATOR", "=", 1));
        definitions.add(new TokenDefinition("OPERATOR", ">", 1));
        definitions.add(new TokenDefinition("OPERATOR", "<", 1));
        definitions.add(new TokenDefinition("OPERATOR", ">=", 1));
        definitions.add(new TokenDefinition("OPERATOR", "<=", 1));
        definitions.add(new TokenDefinition("OPERATOR", "==", 1));
        // (Optionally, add more such as "<", "==", ">=", etc.)

        // Punctuation.
        definitions.add(new TokenDefinition("PUNCTUATION", ",", 1));
        definitions.add(new TokenDefinition("PUNCTUATION", ";", 1));
        definitions.add(new TokenDefinition("PUNCTUATION", "\\(", 1));
        definitions.add(new TokenDefinition("PUNCTUATION", "\\)", 1));
        definitions.add(new TokenDefinition("PUNCTUATION", "\\{", 1));
        definitions.add(new TokenDefinition("PUNCTUATION", "\\}", 1));

        // Display defined token patterns.
        System.out.println("Defined Token Patterns:");
        for (TokenDefinition def : definitions) {
            String postfix = RegexToNFA.infixToPostfix(def.regex);
            System.out.println("Token Type: " + def.tokenType);
            System.out.println("  Infix Regex: " + def.regex);
            System.out.println("  Postfix Regex: " + postfix);
        }
        System.out.println();

        // 2. Build a combined NFA from all token definitions.
        CombinedNFA combinedNFA = new CombinedNFA(definitions);
        System.out.println("Total NFA States: " + NFAState.getStateCount());
        System.out.println();

        // 3. Convert the combined NFA into a DFA.
        DFA dfa = new DFA(combinedNFA.combinedNFA);
        dfa.convert();
        System.out.println("Total DFA States: " + dfa.getTotalDFAStates());
        System.out.println();
        //dfa.printTransitionTable();
        System.out.println();

        // 4. Use the DFA in a lexical analyzer.
        LexicalAnalyzerAutomata lexer = new LexicalAnalyzerAutomata(dfa);

        // Updated test input: now includes a string literal and uses '=' and '>' operators.
        String input = "/* Sample program */\n" +
                       "int a = 10;\n" +
                       "boolean flag = true;\n" +
                       "if (a > 5) {\n" +
                       "    a = a + 1;\n" +
                       "    return flag;\n" +
                       "    print(\"hello\");\n" +
                       "} else {\n" +
                       "    a = a - 1;\n" +
                       "    return false;\n" +
                       "}\n" +
                       " func()\n" +
                       "foo(a) ";
        System.out.println("Input Code:\n" + input);
        ArrayList<Token> tokens = lexer.tokenize(input);
        System.out.println("\nTotal Tokens: " + tokens.size());
        for (Token t : tokens) {
            System.out.println(t);
        }
        System.out.println();

        // 5. Symbol Table Insertion:
        // We only add variables (IDENTIFIER tokens) to the symbol table.
        // If a KEYWORD token matches a data type, then the next IDENTIFIER is treated as that variable's type.
        SymbolTable symTable = new SymbolTable();
        String currentDataType = null;
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            // If the token's lexeme is a known data type, force its type to KEYWORD and set currentDataType.
            if (isDataType(t.lexeme)) {
                t.type = "KEYWORD";
                currentDataType = t.lexeme;
            } else if (t.type.equals("IDENTIFIER")) {
                String symbolType = "";
                // If the identifier is immediately followed by "(" then it's a function.
                if (i + 1 < tokens.size() && tokens.get(i + 1).type.equals("PUNCTUATION") &&
                        tokens.get(i + 1).lexeme.equals("(")) {
                    symbolType = "Function";
                } else {
                    symbolType = (currentDataType != null) ? currentDataType : "Variable";
                }
                symTable.addSymbol(new SymbolInfo(t.lexeme, symbolType, "global", 1000 + symTable.getSize() * 4));
                currentDataType = null; // Reset after declaration.
            }
        }
        System.out.println("Symbol Table Entries:");
        symTable.printSymbols();
        System.out.println("Ayaan Branch");
        Branch_test b = new Branch_test();
        b.print2();
        //hello
    }
    
    // Helper function to determine if a lexeme is a data type.
    public static boolean isDataType(String lexeme) {
        return lexeme.equals("int") || lexeme.equals("boolean") ||
               lexeme.equals("char") || lexeme.equals("string") || lexeme.equals("decimal");
    }
}
