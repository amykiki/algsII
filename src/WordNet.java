import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private Digraph G;
    private Map<String, Bag<Integer>> st; //key和index,一个单词可能对应多个释义id对应
    private Map<Integer, String> synsetsMap; //index和synset对应
    private SAP sap;
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null Argument IS Not Allowed");
        }
        st = new HashMap<>();
        synsetsMap = new HashMap<>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] arr = in.readLine().split(",");
            Integer index = Integer.valueOf(arr[0]);
            String[] keys = arr[1].split(" ");
            synsetsMap.put(index, arr[1]);
            for (String key : keys) {
                Bag<Integer> bag;
                if (st.containsKey(key)) {
                    bag = st.get(key);
                    bag.add(index);
                }
                else {
                    bag = new Bag<>();
                    bag.add(index);
                    st.put(key, bag);
                }
            }
        }
        G = new Digraph(synsetsMap.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] arr = in.readLine().split(",");
            int v = Integer.parseInt(arr[0]);
            for (int i = 1; i < arr.length; i++) {
                int w = Integer.parseInt(arr[i]);
                G.addEdge(v, w);
            }
        }
        sap = new SAP(G);
    }

    // returns all WrokdNet nouns
    public Iterable<String> nouns() {
        return st.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return st.containsKey(word);
    }

    //distance between nounA and nounB
    /**
     * Semantic relatedness refers to the degree to which two concepts are related.
     * We define the semantic relatedness of two wordnet nouns A and B as follows:
     * distance(A, B) = distance is the minimum length of any ancestral path
     * between any synset v of A and any synset w of B.
     */
    public int distance(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        Bag<Integer> v = st.get(nounA);
        Bag<Integer> w = st.get(nounB);
//        printBag(v);
//        printBag(w);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is
    // the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        checkArguments(nounA, nounB);
        Bag<Integer> v = st.get(nounA);
        Bag<Integer> w = st.get(nounB);
        int ancestor = sap.ancestor(v, w);
        return synsetsMap.get(ancestor);
    }

    private void checkArguments(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Null Argument IS Not Allowed");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Not Wordnet Noun!");
        }
    }

    private void printBag(Bag<Integer> bag) {
        System.out.println();
        for (Integer i : bag) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) { }

}
