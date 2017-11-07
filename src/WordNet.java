import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private Digraph G;
    private ST<String, Integer> st; //key和index对应
    private String[] synsetsArr; //keys <---> index,通过index获取synset
    private SAP sap;
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null Argument IS Not Allowed");
        }
        st = new ST<>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] arr = in.readLine().split(",");
            Integer index = Integer.valueOf(arr[0]);
            String[] keys = arr[1].split(" ");
            for (int i = 0; i < keys.length; i++) {
                st.put(keys[i], index);
            }
        }
        synsetsArr = new String[st.size()];
        for (String key : st.keys()) {
            int index = st.get(key);
            if (synsetsArr[index] == null) {
                synsetsArr[index] = key;
            }else {
                synsetsArr[index] = key + " " + synsetsArr[index];
            }
        }
        G = new Digraph(st.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] arr = in.readLine().split(",");
            int v = Integer.valueOf(arr[0]);
            for(int i = 1; i < arr.length; i++) {
                int w = Integer.valueOf(arr[i]);
                G.addEdge(v, w);
            }
        }
        sap = new SAP(G);
    }

    // returns all WrokdNet nouns
    public Iterable<String> nouns() {
        return st.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return st.contains(word);
    }

    //distance between nounA and nounB
    /**
     * Semantic relatedness refers to the degree to which two concepts are related.
     * We define the semantic relatedness of two wordnet nouns A and B as follows:
     * distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
     */
    public int distance(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        int v = st.get(nounA);
        int w = st.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        int v = st.get(nounA);
        int w = st.get(nounB);
        int ancestor = sap.ancestor(v, w);
        return synsetsArr[ancestor];
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
