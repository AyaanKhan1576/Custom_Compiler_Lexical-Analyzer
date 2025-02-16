package package1;

public class NFA {
    public NFAState start;
    public NFAState accept;

    public NFA(NFAState start, NFAState accept) {
        this.start = start;
        this.accept = accept;
    }

    // Build an NFA for a literal character.
    public static NFA literal(char c) {
        NFAState start = new NFAState();
        NFAState accept = new NFAState();
        start.addTransition(c, accept);
        return new NFA(start, accept);
    }

    // Concatenate two NFAs.
    public static NFA concatenate(NFA first, NFA second) {
        first.accept.addTransition(Compiler.EPSILON, second.start);
        return new NFA(first.start, second.accept);
    }

    // Create a union of two NFAs.
    public static NFA union(NFA first, NFA second) {
        NFAState start = new NFAState();
        NFAState accept = new NFAState();
        start.addTransition(Compiler.EPSILON, first.start);
        start.addTransition(Compiler.EPSILON, second.start);
        first.accept.addTransition(Compiler.EPSILON, accept);
        second.accept.addTransition(Compiler.EPSILON, accept);
        return new NFA(start, accept);
    }

    // Apply the Kleene star to an NFA.
    public static NFA kleeneStar(NFA nfa) {
        NFAState start = new NFAState();
        NFAState accept = new NFAState();
        start.addTransition(Compiler.EPSILON, nfa.start);
        start.addTransition(Compiler.EPSILON, accept);
        nfa.accept.addTransition(Compiler.EPSILON, nfa.start);
        nfa.accept.addTransition(Compiler.EPSILON, accept);
        return new NFA(start, accept);
    }
}
