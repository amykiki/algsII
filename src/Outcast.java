import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Semantic relatedness refers to the degree to which two concepts are related.
 *  We define the semantic relatedness of two wordnet nouns A and B as follows:
 *  distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
 *  Outcast detection. Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others?
 *  To identify an outcast, compute the sum of the distances between each noun and every other one:
 *  di = dist(Ai, A1) + dist(Ai, A2) + ... + dist(Ai, An)
 *  and return a noun At for which dt is maximum.
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns){
        return null;
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        while (!StdIn.isEmpty()) {
            String filepath = "D:\\codeproject\\githubProject\\algsII\\test-data\\datas\\wordnet-testing\\wordnet\\";
            String file = StdIn.readString();
            String filename = filepath + file;
            In in = new In(filename);
            String[] nouns = in.readAllStrings();
            StdOut.println(file + ": " + outcast.outcast(nouns));
        }
        /*for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }*/
    }
}
