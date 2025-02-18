package package1;
import java.util.List;

public class CombinedNFA {
    public NFA combinedNFA;

    // Build a combined NFA 
    public CombinedNFA(List<TokenDefinition> definitions) {
        NFAState start = new NFAState();
        // For each token definition, build its NFA and mark its accept state.
        for (TokenDefinition def : definitions) {
            String postfix = RegexToNFA.infixToPostfix(def.regex);
            NFA tokenNFA = RegexToNFA.buildNFAFromPostfix(postfix);
            tokenNFA.accept.tokenType = def.tokenType;
            tokenNFA.accept.tokenPriority = def.priority;
            // Add an epsilon transition from the combined start state to the token NFA’s start.
            start.addTransition(Compiler.EPSILON, tokenNFA.start);
        }
        combinedNFA = new NFA(start, null);
    }
}
