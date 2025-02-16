package package1;

public class Transition {
    public char symbol;
    public NFAState dest;

    public Transition(char symbol, NFAState dest) {
        this.symbol = symbol;
        this.dest = dest;
    }
}
