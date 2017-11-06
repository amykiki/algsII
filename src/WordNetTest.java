import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WordNetTest {
    private String filePath = "D:\\codeproject\\githubProject\\algsII\\test-data\\wordnet-testing\\wordnet";

    public void testDiagraph(String filename) {
        filename = filePath + "\\" + filename;
        StdOut.println(filename);
        In in = new In(filename);
        Digraph digraph = new Digraph(in);
        System.out.println(digraph);
        Digraph reverseDig = digraph.reverse();
        System.out.println(reverseDig);
    }
    public static void main(String[] args) {
        WordNetTest test = new WordNetTest();
//        test.testDiagraph(args[0]);
        String fileName = "digraph1.txt";
//        String fileName = "digraph2.txt";
        test.testDiagraph(fileName);

    }
}
