import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph reverseG;
    public SAP(Digraph G){
        if (G == null) {
            throw new IllegalArgumentException("Digraph G is Null!");
        }
        reverseG = G.reverse();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        validateVertex(v);
        validateVertex(w);
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null || w == null) {
            throw new IllegalArgumentException("SAP.length() argument is Null!");
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("SAP.length() argument is Null!");
        }
        return -1;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= reverseG.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (reverseG.V()-1));
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
    }

}
