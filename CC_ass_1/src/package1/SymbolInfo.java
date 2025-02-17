package package1;

public class SymbolInfo {
    public String name;
    public String type;       // Variable, Function, I/O Function, Arithmetic Operator, etc.
    public String scope;      // global or local
    public int memoryLocation;
    public String additionalInfo;  // Stores specific details like data type, return type, etc.

    public SymbolInfo(String name, String type, String scope, int memoryLocation, String additionalInfo) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.memoryLocation = memoryLocation;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-25s %-10s %-10d %-20s", 
                name, type, scope, memoryLocation, additionalInfo);
    }
}
