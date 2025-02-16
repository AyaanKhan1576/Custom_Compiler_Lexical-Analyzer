package package1;
public class SymbolInfo {
    public String name;
    public String type;       // Will store the declared data type, or "Function" or "Variable"
    public String scope;      // "global" or "local"
    public int memoryLocation;

    public SymbolInfo(String name, String type, String scope, int memoryLocation) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.memoryLocation = memoryLocation;
    }

    @Override
    public String toString() {
        return "Name: " + name 
            + ", Type: " + type 
            + ", Scope: " + scope 
            + ", Memory: " + memoryLocation;
    }
}
