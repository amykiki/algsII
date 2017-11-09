import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WordNetTest {
    private String filePath = "D:\\codeproject\\githubProject\\algsII\\test-data\\datas\\wordnet-testing\\wordnet\\";

    public void testDiagraph(String filename) {
        filename = filePath + "\\" + filename;
        StdOut.println(filename);
        In in = new In(filename);
        Digraph digraph = new Digraph(in);
        System.out.println(digraph);
        Digraph reverseDig = digraph.reverse();
        System.out.println(reverseDig);
    }

    public void testWodeNet() {
        while (!StdIn.isEmpty()) {
            String synsetFile = StdIn.readString();
            String hyperFile = StdIn.readString();
            String synsetFileName = filePath + synsetFile;
            String hyperFileName = filePath +hyperFile;
            System.out.println("synsetFile = " + synsetFile);
            System.out.println("hyperFile = " + hyperFile);
            WordNet wordNet = new WordNet(synsetFileName, hyperFileName);
        }
    }
    public static void main(String[] args) {
        WordNetTest test = new WordNetTest();
//        test.testDiagraph(args[0]);
//        String fileName = "digraph2.txt";
        test.testWodeNet();

    }
}
