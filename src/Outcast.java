import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * Semantic relatedness refers to the degree to which two concepts are related.
 *  We define the semantic relatedness of two wordnet nouns A and B as follows:
 *  distance(A, B) = distance is the minimum length of any ancestral path
 *  between any synset v of A and any synset w of B.
 *  Outcast detection. Given a list of wordnet nouns A1, A2, ..., An,
 *  which noun is the least related to the others?
 *  To identify an outcast, compute the sum of the distances between each noun and every other one:
 *  di = dist(Ai, A1) + dist(Ai, A2) + ... + dist(Ai, An)
 *  and return a noun At for which dt is maximum.
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordNet) {
        if (wordNet == null) {
            throw new IllegalArgumentException("Outcast Constructor Null Argument IS Not Allowed");
        }
        this.wordNet = wordNet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)
    {
        if (nouns == null || nouns.length == 0) {
            throw new IllegalArgumentException("Outcast Null Argument IS Not Allowed");
        }
        int n = nouns.length;
        int maxDistance = -1;
        int maxSap = -1;
        int distsSize = n*(n+1)/2 - n;
        int[] subIn = new int[n];
        subIn[0] = 1;
        int[] dists = new int[distsSize];
        for (int i = 1; i < n; i++)
        {
            subIn[i] = subIn[i-1] + i +1;
        }
        for (int i = 0; i < distsSize; i++) {
            dists[i] = -1;
        }
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < i; j++) {
                sum += getDistance(j, i, nouns, dists, subIn);
            }
            for (int k = i + 1; k < n; k++) {
                sum += getDistance(i, k, nouns, dists, subIn);
            }
            if (maxDistance < sum) {
                maxDistance = sum;
                maxSap = i;
            }
        }
        if (maxSap != -1) {
            return nouns[maxSap];
        }
        return null;
    }

    private int getDistance(int x, int y, String[] nouns, int[] dists, int[] subIn) {
        int key = x * nouns.length + y;
        int index = key - subIn[x];
        if (dists[index] == -1) {
            dists[index] = wordNet.distance(nouns[x], nouns[y]);
        }
        return dists[index];
    }
    public static void main(String[] args) {
        /*WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        while (!StdIn.isEmpty()) {
            String filepath = "D:\\codeproject\\githubProject\\algsII\\test-data\\datas\\wordnet-testing\\wordnet\\";
            String file = StdIn.readString();
            String filename = filepath + file;
            In in = new In(filename);
            String[] nouns = in.readAllStrings();
            StdOut.println(file + ": " + outcast.outcast(nouns));
        }*/
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
