package package1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DFA {
    NFA nfa;
    // Transition table: mapping DFA state id → (input symbol → next DFA state id)
    HashMap<Integer, HashMap<Character, Integer>> transitionTable;
    ArrayList<ArrayList<Integer>> dfaStates;
    ArrayList<String> dfaTokenType;
    ArrayList<Integer> dfaTokenPriority;
    int dfaStateCount;
    ArrayList<Character> alphabet;

    public DFA(NFA nfa) {
        this.nfa = nfa;
        transitionTable = new HashMap<>();
        dfaStates = new ArrayList<>();
        dfaTokenType = new ArrayList<>();
        dfaTokenPriority = new ArrayList<>();
        dfaStateCount = 0;
        alphabet = new ArrayList<>();
    }

    // Convert the NFA to a DFA using subset construction.
    public void convert() {
        for (NFAState state : NFAState.getAllStates()) {
            for (Transition t : state.transitions) {
                if (t.symbol != Compiler.EPSILON && !alphabet.contains(t.symbol)) {
                    alphabet.add(t.symbol);
                }
            }
        }
        // The start DFA state is the epsilon closure of the NFA start state.
        ArrayList<Integer> startSet = epsilonClosure(stateToList(nfa.start));
        dfaStates.add(startSet);
        String tokenType = null;
        int tokenPriority = Integer.MAX_VALUE;
        for (int id : startSet) {
            NFAState s = getStateById(id);
            if (s.tokenType != null && s.tokenPriority < tokenPriority) {
                tokenType = s.tokenType;
                tokenPriority = s.tokenPriority;
            }
        }
        dfaTokenType.add(tokenType);
        dfaTokenPriority.add(tokenPriority);
        transitionTable.put(0, new HashMap<Character, Integer>());
        dfaStateCount++;

        // Process unmarked DFA states.
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        while (!queue.isEmpty()) {
            int currentDFA = queue.poll();
            ArrayList<Integer> currentSet = dfaStates.get(currentDFA);
            for (char sym : alphabet) {
                ArrayList<Integer> moveSet = new ArrayList<>();
                for (int stateId : currentSet) {
                    NFAState state = getStateById(stateId);
                    for (Transition t : state.transitions) {
                        if (t.symbol == sym && !moveSet.contains(t.dest.id)) {
                            moveSet.add(t.dest.id);
                        }
                    }
                }
                ArrayList<Integer> closureSet = epsilonClosure(moveSet);
                if (closureSet.isEmpty())
                    continue;
                int dfaState = findDFAState(closureSet);
                if (dfaState == -1) {
                    dfaState = dfaStateCount;
                    dfaStates.add(closureSet);
                    // Determine accepting token type for this new DFA state.
                    tokenType = null;
                    tokenPriority = Integer.MAX_VALUE;
                    for (int id : closureSet) {
                        NFAState s = getStateById(id);
                        if (s.tokenType != null && s.tokenPriority < tokenPriority) {
                            tokenType = s.tokenType;
                            tokenPriority = s.tokenPriority;
                        }
                    }
                    dfaTokenType.add(tokenType);
                    dfaTokenPriority.add(tokenPriority);
                    transitionTable.put(dfaState, new HashMap<Character, Integer>());
                    dfaStateCount++;
                    queue.add(dfaState);
                }
                transitionTable.get(currentDFA).put(sym, dfaState);
            }
        }
    }

    // Helper: Convert an NFA state to a list (its ID).
    private ArrayList<Integer> stateToList(NFAState state) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(state.id);
        return list;
    }

    // Compute the epsilon-closure for a set of NFA state IDs.
    private ArrayList<Integer> epsilonClosure(ArrayList<Integer> states) {
        ArrayList<Integer> closure = new ArrayList<>(states);
        Stack<Integer> stack = new Stack<>();
        for (int id : states)
            stack.push(id);
        while (!stack.isEmpty()) {
            int id = stack.pop();
            NFAState state = getStateById(id);
            for (Transition t : state.transitions) {
                if (t.symbol == Compiler.EPSILON && !closure.contains(t.dest.id)) {
                    closure.add(t.dest.id);
                    stack.push(t.dest.id);
                }
            }
        }
        return closure;
    }

    // Retrieve an NFA state by its ID.
    private NFAState getStateById(int id) {
        for (NFAState state : NFAState.getAllStates()) {
            if (state.id == id)
                return state;
        }
        return null;
    }

    // Search for a DFA state that has the same set of NFA state IDs.
    private int findDFAState(ArrayList<Integer> set) {
        for (int i = 0; i < dfaStates.size(); i++) {
            if (dfaStatesEqual(dfaStates.get(i), set)) {
                return i;
            }
        }
        return -1;
    }

    private boolean dfaStatesEqual(ArrayList<Integer> a, ArrayList<Integer> b) {
        if (a.size() != b.size())
            return false;
        for (int x : a) {
            if (!b.contains(x))
                return false;
        }
        return true;
    }

    // Returns the next DFA state from a given state on an input symbol (or -1 if none).
    public int getTransition(int dfaState, char sym) {
        if (transitionTable.get(dfaState).containsKey(sym))
            return transitionTable.get(dfaState).get(sym);
        return -1;
    }

    public String getTokenType(int dfaState) {
        return dfaTokenType.get(dfaState);
    }

    public int getStartState() {
        return 0;
    }

    public int getTotalDFAStates() {
        return dfaStateCount;
    }

    // Print the transition table along with each DFA state's underlying NFA state set.
    public void printTransitionTable() {
        System.out.println("DFA Transition Table (State | Unique NFA States | Transitions):");
        for (int i = 0; i < dfaStateCount; i++) {
            System.out.print("State " + i + " | {");
            ArrayList<Integer> set = dfaStates.get(i);
            for (int j = 0; j < set.size(); j++) {
                System.out.print(set.get(j));
                if (j < set.size() - 1)
                    System.out.print(", ");
            }
            System.out.print("} | ");
            HashMap<Character, Integer> map = transitionTable.get(i);
            for (Character sym : map.keySet()) {
                System.out.print("[" + sym + " -> " + map.get(sym) + "] ");
            }
            // If the state is accepting, print its token type.
            String tokenType = dfaTokenType.get(i);
            if (tokenType != null) {
                System.out.print("| Accepting: " + tokenType);
            }
            System.out.println();
        }
    }
}
