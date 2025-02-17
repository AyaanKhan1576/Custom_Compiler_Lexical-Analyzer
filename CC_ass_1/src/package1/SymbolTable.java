package package1;
import java.util.HashMap;

//public class SymbolTable {
//    private HashMap<String, SymbolInfo> symbols;
//
//    public SymbolTable() {
//        symbols = new HashMap<>();
//    }
//
//    public void addSymbol(SymbolInfo info) {
//        // If the symbol already exists in the current scope, you could report an error.
//        symbols.put(info.name, info);
//    }
//
//    public int getSize() {
//        return symbols.size();
//    }
//
//    public void printSymbols() {
//        //System.out.println("Symbol Table Contents:");
//        for (SymbolInfo info : symbols.values()) {
//            System.out.println(info);
//        }
//    }
//}
public class SymbolTable {
    private HashMap<String, SymbolInfo> symbols;
    
    public SymbolTable() {
        symbols = new HashMap<>();
    }

    public void addSymbol(SymbolInfo info) {
        // Create a unique key combining name and scope to allow same variable names in different scopes
        String key = info.name + "@" + info.scope;
        
        // Check for redeclaration in the same scope
        if (symbols.containsKey(key)) {
            System.out.println("Warning: Symbol '" + info.name + "' already declared in scope '" + info.scope + "'");
            return;
        }
        
        symbols.put(key, info);
    }

    public SymbolInfo lookupSymbol(String name, String scope) {
        // First try to find in current scope
        String key = name + "@" + scope;
        SymbolInfo info = symbols.get(key);
        
        // If not found in local scope and we're in a function, try global scope
        if (info == null && !scope.equals("global")) {
            key = name + "@global";
            info = symbols.get(key);
        }
        
        return info;
    }

    public int getSize() {
        return symbols.size();
    }

    public void printSymbols() {
        // Print header
    	System.out.println("\nSymbol Table:");
        System.out.println(String.format("%-15s %-25s %-15s %-10s %-30s", 
             "Name", "Type", "Scope", "Memory", "Additional Info"));
        System.out.println("----------------------------------------------------------------------------------------------");
        
        // First print global symbols
        symbols.values().stream()
               .filter(info -> info.scope.equals("global"))
               .forEach(System.out::println);
        
        // Then print local symbols, grouped by function
        symbols.values().stream()
               .filter(info -> !info.scope.equals("global"))
               .sorted((a, b) -> a.scope.compareTo(b.scope))
               .forEach(System.out::println);
    }
}