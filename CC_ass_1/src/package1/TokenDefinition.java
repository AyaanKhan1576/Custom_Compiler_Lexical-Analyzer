package package1;

public class TokenDefinition {
    public String tokenType;
    public String regex;
    public int priority; 

    public TokenDefinition(String tokenType, String regex, int priority) {
        this.tokenType = tokenType;
        this.regex = regex;
        this.priority = priority;
    }
}
