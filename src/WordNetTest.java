import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WordNetTest {
    private String filePath = "D:\\codeproject\\githubProject\\algsII\\test-data\\datas\\"
             + "wordnet-testing\\wordnet\\";

    public void testDiagraph(String filename) {
        filename = filePath + "\\" + filename;
        StdOut.println(filename);
        In in = new In(filename);
        Digraph digraph = new Digraph(in);
        System.out.println(digraph);
        Digraph reverseDig = digraph.reverse();
        System.out.println(reverseDig);
    }

    public void testWordNet() {
        String[] synsetFiles = {"synsets3.txt", "synsets3.txt", "synsets6.txt", "synsets6.txt",
                                "synsets6.txt"};
        String[] hyperFiles = {"hypernyms3InvalidTwoRoots.txt", "hypernyms3InvalidCycle.txt",
                               "hypernyms6InvalidTwoRoots.txt", "hypernyms6InvalidCycle.txt",
                               "hypernyms6InvalidCycle+Path.txt"};
        for (int i = 0; i < synsetFiles.length; i++)
        {
            String synsetFile = synsetFiles[i];
            String hyperFile = hyperFiles[i];
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
        test.testWordNet();

    }
}
