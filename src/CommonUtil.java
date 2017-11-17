import edu.princeton.cs.algs4.StdOut;

public class CommonUtil {
    public static void printArray(double[] arr, int width) {
        int count = 0;
        for(int i = 0; i < arr.length; i++) {
            StdOut.printf("%-9.0f", arr[i]);
            count++;
            if (count % width == 0) {
                StdOut.println();
            }
        }
        StdOut.println();
    }
}