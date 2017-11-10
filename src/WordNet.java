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
    //=======以下变量为检查G是否为rootedDAG=======
    //假设所有点起始都为根节点
    //如果rootedV = 1,则表示只有一个根节点
    private int rootedV;
    private boolean[] marked;
    private boolean[] onStack;
    private boolean hasCycle = false;

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
        //检查是否为cycle
        rootedV = G.V();
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        if (!validateGraph()) {
            throw new IllegalArgumentException("Graph NOT ROOTED DAG!");
        }
        sap = new SAP(G);
    }

    // returns all WrokdNet nouns
    public Iterable<String> nouns() {
        return st.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (null == word) {
            throw new IllegalArgumentException("word Not Null!");
        }
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

    /**
     * 检查图片是否为rooted DAG
     * 不能包含cycle，只能有一个根节点，所有点都能联通
     */
    private boolean validateGraph() {
        for (int v = 0; v < G.V(); v++) {
            if (hasCycle) {
                break;
            }
            if (!marked[v]) {
                dfs(v);
            }
        }
        //rooted DAG
        if (!hasCycle && rootedV == 1) {
            return true;
        }
        return false;
    }

    private void dfs(int v) {
        marked[v] = true;
        //判断节点是否有连出的线
        if (!G.adj(v).iterator().hasNext()) {
            return;
        }
        onStack[v] = true;
        //节点有连出的线，那么节点就不是根节点
        rootedV--;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(w);
            } else if (onStack[w]) { //存在cycle
                hasCycle = true;
                return;
            }
        }
        onStack[v] = false;
        return;
    }


    public static void main(String[] args) { }

}
