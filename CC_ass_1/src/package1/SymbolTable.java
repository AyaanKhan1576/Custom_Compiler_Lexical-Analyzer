package package1;
import java.util.HashMap;

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
    public boolean hasSymbol(String name) {
        return symbols.containsKey(name) || 
               symbols.values().stream().anyMatch(info -> info.name.equals(name));
    }

    public SymbolInfo lookupSymbol(String name, String scope) {
        // Check local scope
        String key = name + "@" + scope;
        SymbolInfo info = symbols.get(key);
        
        // Check global scope
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
        int nameWidth = 15, typeWidth = 25, scopeWidth = 15, memoryWidth = 10, infoWidth = 30;

        System.out.println("\nSymbol Table:");
        System.out.printf("%-15s %-25s %-15s %-10s %-30s%n", 
                          "Name", "Type", "Scope", "Memory", "Additional Info");
        System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────");

        symbols.values().stream()
               .filter(info -> info.scope.equals("global"))
               .forEach(info -> printFormattedSymbol(info, nameWidth, typeWidth, scopeWidth, memoryWidth, infoWidth));

        System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────");

        symbols.values().stream()
               .filter(info -> !info.scope.equals("global"))
               .sorted((a, b) -> a.scope.compareTo(b.scope))
               .forEach(info -> printFormattedSymbol(info, nameWidth, typeWidth, scopeWidth, memoryWidth, infoWidth));

        System.out.println();
    }

    private void printFormattedSymbol(SymbolInfo info, int nameW, int typeW, int scopeW, int memW, int infoW) {
        System.out.printf("%-15s %-25s %-15s %-10d %-30s%n", 
                          info.name, info.type, info.scope, info.memoryLocation, info.additionalInfo);
    }

}