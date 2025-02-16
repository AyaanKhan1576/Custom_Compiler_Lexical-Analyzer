package package1;
import java.util.List;

public class CombinedNFA {
    public NFA combinedNFA;

    // Build a combined NFA from a list of token definitions.
    public CombinedNFA(List<TokenDefinition> definitions) {
        // Create a new start state.
        NFAState start = new NFAState();
        // For each token definition, build its NFA and mark its accept state.
        for (TokenDefinition def : definitions) {
            // Convert the regex to postfix form.
            String postfix = RegexToNFA.infixToPostfix(def.regex);
            // Build the token's NFA.
            NFA tokenNFA = RegexToNFA.buildNFAFromPostfix(postfix);
            // Mark the token NFA's accept state with the token type and priority.
            tokenNFA.accept.tokenType = def.tokenType;
            tokenNFA.accept.tokenPriority = def.priority;
            // Add an epsilon transition from the combined start state to the token NFA’s start.
            start.addTransition(Compiler.EPSILON, tokenNFA.start);
        }
        // The combined NFA’s start state is our new start; there is no single accept state.
        combinedNFA = new NFA(start, null);
    }
}
