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
        definitions.add(new TokenDefinition("KEYWORD", "if|else|while|for|return|int|boolean|char|string|decimal|const|void", 1));

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

        String input = "/* This is a multi-line comment\n" +
                "   Testing multiple lines\n" +
                "   of documentation */\n" +
                "\n" +
                "// Global variable declarations\n" +
                "int a = 10;\n" +
                "const int b = 20;\n" +
                "boolean flag = true;\n" +
                "char letter = 'A';\n" +
                "string msg = \"hello world\";\n" +
                "decimal pi = 3.14;\n" +
                "\n" +
                "// Testing I/O operations\n" +
                "scanf(\"%d\", &a);\n" +
                "\n" +
                "/* Nested if blocks with\n" +
                "   local variables */\n" +
                "if (a > 5) {\n" +
                "    // Local variables in first if block\n" +
                "    int localvar = 1;\n" +
                "    const int localconst = 2;\n" +
                "    \n" +
                "    scanf(\"%d\", a);\n" +
                "    \n" +
                "    // Testing arithmetic operators\n" +
                "    if (a % 5) {\n" +
                "        /* Local variables in\n" +
                "           nested if block */\n" +
                "        int localVar = 1;\n" +
                "        const int localConst = 2;\n" +
                "        a = a + localVar;  // Addition operator\n" +
                "        return flag;\n" +
                "        print(\"hello\");\n" +
                "    } else {\n" +
                "        // Subtraction in else block\n" +
                "        a = a - 1;\n" +
                "        return false;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "// Function declaration with local variables\n" +
                "void func() {\n" +
                "    // Local function variable\n" +
                "    int localfuncvar1 = 100;\n" +
                "    pi = 3;" +
                "}\n" +
                "\n" +
                "// Function declaration with parameters\n" +
                "void foo(int param1) {\n" +
                "    // Local variable in foo function\n" +
                "    int localFuncVar = 100;\n" +
                "}\n" +
                "\n" +
                "// Function call\n" +
                "foo(param1);";
        System.out.println("Input Code:\n" + input);
        ArrayList<Token> tokens = lexer.tokenize(input);
        System.out.println("\nTotal Tokens: " + tokens.size());
        for (Token t : tokens) {
            System.out.println(t);
        }
        System.out.println();

        SymbolTable symTable = new SymbolTable();
        String currentDataType = null;
        boolean isConstant = false;
        String currentScope = "global";
        boolean inFunction = false;
        String currentFunction = null;

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            String additionalInfo = "";

            // First check if it's an I/O function declaration
            if (t.type.equals("IDENTIFIER") && (t.lexeme.equals("scanf") || t.lexeme.equals("print"))) {
                // Only add if it's the function name itself, not a call
                if (i > 0 && !tokens.get(i-1).type.equals("OPERATOR")) {
                    symTable.addSymbol(new SymbolInfo(t.lexeme, "I/O Function", "global", -1, 
                        "Standard Input/Output Operation"));
                }
                continue;
            }

            // Handle regular identifiers
            if (t.type.equals("IDENTIFIER")) {
                // Only process if we're in a declaration context (has a current data type or is a function declaration)
                if (currentDataType != null || 
                    (i + 1 < tokens.size() && tokens.get(i + 1).lexeme.equals("(") && 
                     i > 0 && !tokens.get(i-1).type.equals("OPERATOR"))) {
                    
                    String symbolType = "Variable";
                    
                    // Check if it's a function declaration (but not an I/O function)
                    if (i + 1 < tokens.size() && tokens.get(i + 1).lexeme.equals("(") && 
                        !t.lexeme.equals("scanf") && !t.lexeme.equals("print")) {
                        symbolType = "Function";
                        currentFunction = t.lexeme;
                        inFunction = true;
                        additionalInfo = "Return Type: " + (currentDataType != null ? currentDataType : "void");
                        currentScope = "global";
                    }
                    // Check if it's a parameter
                    else if (inFunction && currentFunction != null && 
                            i > 0 && tokens.get(i - 1).lexeme.equals("(")) {
                        symbolType = "Variable";
                        additionalInfo = "Parameter of " + currentFunction + " (" + 
                                       (currentDataType != null ? currentDataType : "Unknown Type") + ")";
                        currentScope = "local:" + currentFunction;
                    }
                    // Regular variable declaration
                    else if (currentDataType != null) {
                        symbolType = "Variable";
                        if (isConstant) {
                            additionalInfo = "Constant " + currentDataType;
                        } else {
                            additionalInfo = "Type: " + currentDataType;
                        }
                        // Only set local scope if we're inside a real function
                        if (inFunction && currentFunction != null && 
                            !currentFunction.equals("scanf") && !currentFunction.equals("print")) {
                            currentScope = "local:" + currentFunction;
                            additionalInfo += " (Local to " + currentFunction + ")";
                        } else {
                            currentScope = "global";
                        }
                    }
                    
                    // Assign memory location
                    int memoryLocation = (symbolType.equals("Arithmetic Operator") || 
                                        symbolType.equals("Comment") || 
                                        symbolType.equals("I/O Function")) ? -1 : 1000 + symTable.getSize() * 4;
                    
                    symTable.addSymbol(new SymbolInfo(t.lexeme, symbolType, currentScope, 
                                                     memoryLocation, additionalInfo));
                    
                    if (!symbolType.equals("Function")) {
                        currentDataType = null;
                        isConstant = false;
                    }
                }
            }
            // Handle operators
            else if (t.type.equals("OPERATOR")) {
                // Only add operator once when first encountered
                if (!symTable.hasSymbol(t.lexeme)) {
                    symTable.addSymbol(new SymbolInfo(t.lexeme, "Arithmetic Operator", 
                                      "global", -1, "Performs arithmetic operation"));
                }
            }
            // Handle comments
            else if (t.lexeme.startsWith("//")) {
                symTable.addSymbol(new SymbolInfo(t.lexeme.substring(0, Math.min(t.lexeme.length(), 20)) + "...", 
                                  "Single Line Comment", "global", -1, "Source code documentation"));
            }
            else if (t.lexeme.startsWith("/*")) {
                symTable.addSymbol(new SymbolInfo(t.lexeme.substring(0, Math.min(t.lexeme.length(), 20)) + "...", 
                                  "Multi Line Comment", "global", -1, "Source code documentation"));
            }
            // Reset function context when closing a function
            else if (t.lexeme.equals("}") && inFunction) {
                inFunction = false;
                currentFunction = null;
                currentScope = "global";
            }
            // Track data types and constant declarations
            else if (isDataType(t.lexeme)) {
                currentDataType = t.lexeme;
            }
            else if (t.lexeme.equals("const")) {
                isConstant = true;
            }
        }

        symTable.printSymbols();
    }
    
    // Helper function to determine if a lexeme is a data type.
    public static boolean isDataType(String lexeme) {
        return lexeme.equals("int") || lexeme.equals("boolean") ||
               lexeme.equals("char") || lexeme.equals("string") || lexeme.equals("decimal");
    }
}


