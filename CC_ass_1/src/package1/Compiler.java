package package1;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;


public class Compiler {
    // Special symbol for epsilon transitions.
    public static final char EPSILON = '\0';

    public static void main(String[] args) {
        System.out.println("=== COMPILER CONSTRUCTION ASSIGNMENT 1 ===\n");
        
        // 1. Define token types with their regexes and priorities.
        // Lower priority value means higher precedence.
        
        List<TokenDefinition> definitions = new ArrayList<>();

        // Updated keywords for Ye Olde Code
        definitions.add(new TokenDefinition("KEYWORD", 
            "perchance|otheryonder|whilst|fortime|giveth|number|truth|letter|text|"
            + "fraction|twainfraction|forever|nothing|unmoving|callith|binding|chooseth|"
            + "maketh|shatter|forthwith|howbig", 1));


        // Boolean literals remain the same
        definitions.add(new TokenDefinition("TRUTH", "true|false", 1));

        // Identifier: letter followed by letters or digits.
        String letter = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)";
        String digit  = "(0|1|2|3|4|5|6|7|8|9)";
        String identifierRegex = letter + "(" + letter + "|" + digit + ")*";
        definitions.add(new TokenDefinition("IDENTIFIER", identifierRegex, 2));

        // Integer: one or more digits.
        String integerRegex = digit + "(" + digit + ")*";
        definitions.add(new TokenDefinition("NUMBER", integerRegex, 3));

        // Decimal: integer, a literal dot, then integer.
        String decimalRegex = integerRegex + "\\." + integerRegex;
        definitions.add(new TokenDefinition("FRACTION", decimalRegex, 3));

        // Character literal: a letter enclosed in single quotes.
        String charLiteralRegex = "'" + letter + "'";
        definitions.add(new TokenDefinition("LETTER", charLiteralRegex, 2));

        // STRING literal: a double quote, then zero or more (letter, digit, or space), then a double quote.
        definitions.add(new TokenDefinition("TEXT", "\\\"(" + letter + "|" + digit + "| )*\\\"", 1));

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

     // Display defined token patterns in a clean format
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("               Defined Token Patterns              ");
        System.out.println("═══════════════════════════════════════════════════");

        for (TokenDefinition def : definitions) {
            String postfix = RegexToNFA.infixToPostfix(def.regex);
            
            System.out.printf("▶ Token Type     : %s%n", def.tokenType);
            System.out.printf("  ├── Infix Regex  : %s%n", def.regex);
            System.out.printf("  └── Postfix Regex: %s%n%n", postfix);
        }

        System.out.println("═══════════════════════════════════════════════════\n");


        // 2. Build a combined NFA from all token definitions.
        CombinedNFA combinedNFA = new CombinedNFA(definitions);
        System.out.println("Total NFA States: " + NFAState.getStateCount());
        System.out.println();

        // 3. Convert the combined NFA into a DFA.
        DFA dfa = new DFA(combinedNFA.combinedNFA);
        dfa.convert();
        System.out.println("Total DFA States: " + dfa.getTotalDFAStates());
        System.out.println();
        
        // Uncomment this code to display transition tables
        //dfa.printTransitionTable();
        //
        
        System.out.println();


        // 4. Use the DFA in a lexical analyzer.
        LexicalAnalyzerAutomata lexer = new LexicalAnalyzerAutomata(dfa);

        // New code for file input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to your input file: ");
        String filePath = scanner.nextLine();
        
        try {
            // Read the entire file content
        	//C:/Users/ayaan/git/repository/Custom_Compiler_i220832_i220849/CC_ass_1/src/package1/test.yoc

            String input = new String(Files.readAllBytes(Paths.get(filePath)));
            
            System.out.println("\nInput Code from file: " + filePath);
            System.out.println("═══════════════════════════════════");
            System.out.println(input);
            System.out.println("═══════════════════════════════════\n");

            ArrayList<Token> tokens = lexer.tokenize(input);
            
            // Display the total number of tokens
            System.out.println("\n══════════════════════════════════════════════");
            System.out.printf("              Total Tokens: %d%n", tokens.size());
            System.out.println("══════════════════════════════════════════════");

            // Print table header
            System.out.printf("%-5s │ %-15s │ %s%n", "#", "Token Type", "Lexeme");
            System.out.println("──────────────────────────────────────────────");

            // Print each token with formatting
            int index = 1;
            for (Token t : tokens) {
                System.out.printf("%-5d │ %-15s │ %s%n", index++, t.type, t.lexeme);
            }

            System.out.println("══════════════════════════════════════════════\n");

            // Process symbol table
            processSymbolTable(tokens);

        } catch (FileNotFoundException e) {
            ErrorHandler.handleFileNotFound(filePath);
        } catch (IOException e) {
            ErrorHandler.handleFileReadError(e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Move symbol table processing to a separate method
    private static void processSymbolTable(ArrayList<Token> tokens) {
        SymbolTable symTable = new SymbolTable();
        String currentDataType = null;
        boolean isConstant = false;
        String currentScope = "global";
        boolean inFunction = false;
        String currentFunction = null;
        int currentLine = 1; // Add line tracking

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            String additionalInfo = "";

            
         // First check if it's an I/O function declaration
            if (t.type.equals("IDENTIFIER") && (t.lexeme.equals("sayeth") || t.lexeme.equals("heareth"))) {
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
                        !t.lexeme.equals("sayeth") && !t.lexeme.equals("heareth")) {
                        symbolType = "Function";
                        currentFunction = t.lexeme;
                        inFunction = true;
                        additionalInfo = "Return Type: " + (currentDataType != null ? currentDataType : "nothing");
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
                            !currentFunction.equals("sayeth") && !currentFunction.equals("heareth")) {
                            currentScope = "local:" + currentFunction;
                            additionalInfo += " (Local to " + currentFunction + ")";
                        } else {
                            currentScope = "global";
                        }
                    }
                    
                    // Assign memory location (for non-operators and non-I/O functions)
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
        return lexeme.equals("number") || lexeme.equals("truth") ||
               lexeme.equals("letter") || lexeme.equals("text") || 
               lexeme.equals("fraction") || lexeme.equals("twainfraction");
    }
}


