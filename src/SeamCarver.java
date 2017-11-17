import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.*;

public class SeamCarver {
    Picture picture;
    private int height;
    private int width;
    private int[] indexes;
    private int[] toplogicOrder;
    private double[] energySquares;
    public SeamCarver(Picture pic) {
        picture = new Picture(pic);
        height = picture.height();
        width = picture.width();
        int N = height * width;
        indexes = new int[N];
        toplogicOrder = new int[N];
        energySquares = new double[N];
        for (int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                toplogicOrder[row * width + col] = -1;
                indexes[row * width + col] = -1;
                energySquares[row * width + col] = getEnergySquare(col, row);
            }
        }
        genToplogicalOrder(width, height);

    }

    public Picture picture() {
        return this.picture;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public double energy(int col, int row) {
        validateHeight(row);
        validateWidth(col);

        int index = row*width + col;
        return Math.sqrt(energySquares[index]);
    }

    /**
     * @return 水平seam
     */
    public int[] findHorizontalSeam(){
        return null;
    }

    /**
     * @return 垂直seam
     */
    public int[] findVerticalSeam(){
        int N = width * height;
        double[] distTo = new double[N + 1];
        int[] edegeTo = new int[N + 1];
        for(int i = 0; i < width; i++) {
            distTo[i] = energySquares[i];
            edegeTo[i] = -1;
        }
        for(int row = 1; row < height; row++) {
            for(int col = 0; col < width; col++) {
                distTo[row * width + col] = Double.MAX_VALUE;
                edegeTo[row * width + col] = -1;
            }
        }
        distTo[N] = Double.MAX_VALUE;
        edegeTo[N] = -1;
        for(int i = 0; i < toplogicOrder.length; i++) {
            int index = toplogicOrder[i];
            int col = index % width;
            int row = index / width;
            if (row == (height - 1)) {
                relaxLastRow(distTo, edegeTo, N, index);
            }else {
                row++;
                relax(distTo, edegeTo, col + 1, row, index);
                relax(distTo, edegeTo, col - 1, row, index);
                relax(distTo, edegeTo, col, row, index);
            }

        }
        int[] verticalSeam = new int[height];
        int v = N;
        for(int i = height - 1; i>= 0; i--) {
            v = edegeTo[v];
            verticalSeam[i] = v % width;
        }
        CommonUtil.printArray(distTo, 6);
        return verticalSeam;
    }

    private void relax(double[] distTo, int[] edgeTo, int wCol, int wRow, int v){
        if (!validateCol(wCol) || !validateRow(wRow)) {
            return;
        }
        int w = wRow * width + wCol;
        if (distTo[w] >= distTo[v] + energySquares[w]) {
            distTo[w] = distTo[v] + energySquares[w];
            edgeTo[w] = v;
        }
    }

    private void relaxLastRow(double[] distTo, int[] edgeTo, int dest, int v) {
        if (distTo[dest] >= distTo[v]) {
            distTo[dest] = distTo[v];
            edgeTo[dest] = v;
        }
    }

    /**
     * 去掉水平seam
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {

    }

    /**
     * 去掉垂直的seam
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {

    }

    // TODO: 2017/11/17 仅作为debug用 
    public double getEnergySquare(int col, int row) {
        validateHeight(row);
        validateWidth(col);
        if (row == 0 || row == (height - 1) || col == 0 || col == (width - 1)) {
            return 1000 * 1000;
        }
        Color colorUp = picture.get(col, row - 1);
        Color colorDown = picture.get(col, row + 1);
        Color colorLeft = picture.get(col - 1, row);
        Color colorRight = picture.get(col + 1, row);

        double xSquare = Math.pow(colorRight.getRed() - colorLeft.getRed(), 2)
                + Math.pow(colorRight.getGreen() - colorLeft.getGreen(), 2)
                + Math.pow(colorRight.getBlue() - colorLeft.getBlue(), 2);
        double ySquare = Math.pow(colorDown.getRed() - colorUp.getRed(), 2)
                + Math.pow(colorDown.getGreen() - colorUp.getGreen(), 2)
                + Math.pow(colorDown.getBlue() - colorUp.getBlue(), 2);
        return xSquare + ySquare;
    }

    private void genToplogicalOrder(int width, int height) {
        Stack<Integer> stack = new Stack<>();
        int marked = 0;
        stack.push(0);
        int next = 1;
        int N = width * height;
        int last = N - 1;
        while (marked != N) {
            while (stack.isEmpty()) {
                if(indexes[next] != -1){
                    next++;
                    continue;
                }
                stack.push(next);
                next++;
            }
            int peek = stack.peek();
            if (!pushStack(peek, stack)) {
                stack.pop();
                marked++;
                indexes[peek] = last;
                toplogicOrder[last] = peek;
                last--;
            }
        }
    }

    // TODO: 2017/11/17 仅作为debug用 
    public int[] getTopologic() {
        return toplogicOrder;
    }
    private boolean pushStack(int i, Stack<Integer> stack) {
        int col = i % width;
        int nextRow = i /width + 1;


        if(!validateRow(nextRow))
            return false;

        int nextIndex = nextRow * width + col;
        if (indexes[nextIndex] == -1) {
            stack.push(nextIndex);
            return true;
        }
        if(validateCol(col - 1) && indexes[nextIndex -1] == -1){
            stack.push(nextIndex - 1);
            return true;
        }
        if (validateCol(col + 1) && indexes[nextIndex + 1] == -1) {
            stack.push(nextIndex + 1);
            return true;
        }
        return false;
    }
    private void validateWidth(int col) {
        if (col < 0 || col >= width) {
            throw new IllegalArgumentException("given width is not between in 0 ~ " + (width - 1) + ": " + col);
        }
    }

    private void validateHeight(int row) {
        if (row < 0 || row >= height) {
            throw new IllegalArgumentException("given height is not between in 0 ~ " + (height - 1) + ": " + row);
        }
    }

    private boolean validateCol(int col) {
        if (col < 0 || col >= width) {
            return false;
        }
        return true;
    }

    private boolean validateRow(int row) {
        if (row < 0 || row >= height) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {


    }
}
