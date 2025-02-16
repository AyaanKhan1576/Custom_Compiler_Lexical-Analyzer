package package1;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, SymbolInfo> symbols;

    public SymbolTable() {
        symbols = new HashMap<>();
    }

    public void addSymbol(SymbolInfo info) {
        // If the symbol already exists in the current scope, you could report an error.
        symbols.put(info.name, info);
    }

    public int getSize() {
        return symbols.size();
    }

    public void printSymbols() {
        System.out.println("Symbol Table Contents:");
        for (SymbolInfo info : symbols.values()) {
            System.out.println(info);
        }
    }
}
