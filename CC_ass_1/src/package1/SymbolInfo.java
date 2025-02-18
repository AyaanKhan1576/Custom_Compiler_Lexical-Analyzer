package package1;

public class SymbolInfo {
    public String name;
    public String type; 
    public String scope;
    public int memoryLocation;
    public String additionalInfo; 

    public SymbolInfo(String name, String type, String scope, int memoryLocation, String additionalInfo) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.memoryLocation = memoryLocation;
        this.additionalInfo = additionalInfo;
    }

    public String getScope() {
        return scope;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-25s %-10s %-10d %-20s",
            name, type, scope, memoryLocation, additionalInfo);
    }
}
