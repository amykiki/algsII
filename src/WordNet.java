public class WordNet {

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null Argument IS Not Allowed");
        }

    }

    // returns all WrokdNet nouns
    public Iterable<String> nouns() {
        return null;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return false;
    }

    //distance between nounA and nounB
    /**
     * Semantic relatedness refers to the degree to which two concepts are related.
     * We define the semantic relatedness of two wordnet nouns A and B as follows:
     * distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
     */
    public int distance(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        return null;
    }

    private void checkArguments(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Null Argument IS Not Allowed");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Not Wordnet Noun!");
        }
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
    }

}
