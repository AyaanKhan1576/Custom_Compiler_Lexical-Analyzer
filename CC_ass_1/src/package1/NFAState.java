package package1;
import java.util.ArrayList;

public class NFAState {
    public int id;
    public ArrayList<Transition> transitions;
    // If this state is an accepting state, tokenType is non-null.
    public String tokenType;
    // tokenPriority helps resolve conflicts when multiple token types match.
    public int tokenPriority;

    private static int stateCounter = 0;
    private static ArrayList<NFAState> allStates = new ArrayList<>();

    public NFAState() {
        this.id = stateCounter++;
        transitions = new ArrayList<>();
        tokenType = null;
        tokenPriority = Integer.MAX_VALUE;
        allStates.add(this);
    }

    public void addTransition(char symbol, NFAState dest) {
        transitions.add(new Transition(symbol, dest));
    }

    public static int getStateCount() {
        return stateCounter;
    }

    public static ArrayList<NFAState> getAllStates() {
        return allStates;
    }
}
