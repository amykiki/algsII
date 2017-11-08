import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class SAP {
    private Digraph G;
    public SAP(Digraph G){
        if (G == null) {
            throw new IllegalArgumentException("Digraph G is Null!");
        }
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        return findSapSingle(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return findSapSingle(v, w)[1];
    }

    private int[] findSapSingle(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        List<Integer> vArr = new ArrayList<>(1);
        vArr.add(v);
        List<Integer> wArr = new ArrayList<>(1);
        wArr.add(w);
        return findSap(vArr, wArr);
    }
    private int[] findSap(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("findSap() argument is Null!");
        }

        //result[0]表示path, result[1] 表示ancestor
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;
        //初始化两个输入参数的祖先数组，数组的index表示图的点
        // 数组默认值为-1，表示该点还没被访问过
        // 数组的值不为1时，表示该点已经被访问过，且值表示距离输入参数的值
        int[] vAncestorArr = new int[G.V()];
        int[] wAncestorArr = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            vAncestorArr[i] = -1;
            wAncestorArr[i] = -1;
        }
        //sap表示两个点的最小公共祖先的坐标
        int sap = -1;
        Queue<Integer> vQueue = new Queue<>();
        for (Integer vi : v) {
            validateVertex(vi);
            vQueue.enqueue(vi);
        }
        Queue<Integer> wQueue = new Queue<>();
        for (Integer wi : w) {
            validateVertex(wi);
            wQueue.enqueue(wi);
        }
        //距离输入参数的path
        int path = 0;
        //被访问过的祖先值，包括两个输入参数的祖先
        int[] ancestors = new int[G.V()];
        while (!(vQueue.isEmpty() && wQueue.isEmpty()) && sap == -1 ) {
            //v BFS查找sap
            sap = findSapByBFS(vAncestorArr, vQueue, ancestors, path);
            if (sap != -1) {
                break;
            }
            //w BFS查找sap
            sap = findSapByBFS(wAncestorArr, wQueue, ancestors, path);
            path++;
        }
        if (sap != -1) {
            result[0] = vAncestorArr[sap] + wAncestorArr[sap];
            result[1] = sap;
        }
        return result;
    }

    /**
     * 寻找最短路径
     * @param ancestorArr 所有ancestor的数组，数组的值为ancestor与输入参数的距离
     * @param queue  BFS的队列
     * @param ancestors
     * @param path vertex和输入参数的距离
     * @return
     */
    private int findSapByBFS(int[]ancestorArr, Queue<Integer> queue, int[] ancestors, int path) {
        int size = queue.size();
        while (size > 0) {
            int ancestor = queue.dequeue();
            if (ancestorArr[ancestor] == -1 ) {
                ancestorArr[ancestor] = path;
                if (ancestors[ancestor] != 0) {
                    return ancestor;
                }
                ancestors[ancestor] = 1;
                Iterator<Integer> iterator = G.adj(ancestor).iterator();
                while (iterator.hasNext()) {
                    queue.enqueue(iterator.next());
                }

            }
            size--;
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        return findSap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return findSap(v, w)[1];
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V() -1));
    }


    public static void main(String[] args) {
        //D:\codeproject\githubProject\algsII\test-data\datas\wordnet-testing\wordnet\digraph1.txt
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
