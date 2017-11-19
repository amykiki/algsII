import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int height;
    private int width;
    private double[] energy;

    /**
     * 注意以下属性是全局使用的，这些数组初始大小为picture的原始大小，
     * 后续picture的缩减不会减小数组大小
     * 所以需要注意坐标的边界值
     */
    private int[] toplogicOrder;
    private double[] distTo;
    private int[] edgeTo;
    public SeamCarver(Picture pic) {
        if (pic == null) {
            throw new IllegalArgumentException("Construtor pic should not be null!");
        }
        picture = new Picture(pic);
        height = picture.height();
        width = picture.width();
        int N = width * height;
        energy = new double[N];
        //计算初始energy
        caclPicEngery();
        toplogicOrder = new int[N];
        distTo = new double[N + 1];
        edgeTo = new int[N + 1];
    }

    public Picture picture() {
        return this.picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int col, int row) {
        validateHeight(row);
        validateWidth(col);

        int index = row*width + col;
        return energy[index];
    }

    /**
     * @return 水平seam
     */
    public int[] findHorizontalSeam(){
        int[] horizonSeam = new int[width()];
        if (height() == 1) {
            for(int col = 0; col < width(); col++) {
                horizonSeam[col] = 0;
            }
            return horizonSeam;
        }

        int v = width*height;
        findEdgeTo(true);
        for(int col = width() - 1; col >= 0; col--) {
            v = edgeTo[v];
            horizonSeam[col] = v / width;
        }
        return horizonSeam;
    }

    /**
     * @return 垂直seam
     */
    public int[] findVerticalSeam(){
        int[] verticalSeam = new int[height()];
        if (picture.width() == 1) {
             for(int row = 0; row < height(); row++) {
                 verticalSeam[row] = 0;
             }
             return verticalSeam;
        }

        int v = width*height;
        findEdgeTo(false);
        for(int row = height - 1; row>= 0; row--) {
            v = edgeTo[v];
            verticalSeam[row] = v % width;
        }
        return verticalSeam;
    }

    private void findEdgeTo(boolean horizontal) {
        int N = width * height;
        distTo[N] = Double.MAX_VALUE;
        edgeTo[N] = -1;
        if (horizontal) {
            for(int row = 0; row < height; row++) {
                distTo[row*width] = energy[row*width];
                edgeTo[row*width] = -1;
            }
        }else {
            for(int col = 0; col < width; col++) {
                distTo[col] = energy[col];
                edgeTo[col] = -1;
            }
        }

        int rowStart,colStart;
        if (horizontal) {
            rowStart = 0;
            colStart = 1;
        }else {
            rowStart = 1;
            colStart = 0;
        }
        for(int row = rowStart; row < height; row++) {
            for(int col = colStart; col < width; col++) {
                distTo[row * width + col] = Double.MAX_VALUE;
                edgeTo[row * width + col] = -1;
            }
        }

        /**
         * 计算topological order注意这个数组是会被持续使用的，
         * 因此i的最大值应为当前picture的大小 - 1
         * 而不为topological数组的length
         */
        genToplogicalOrder(horizontal);
        for(int i = 0; i < N; i++) {
            int index = toplogicOrder[i];
            int col = index % width;
            int row = index / width;
            if (horizontal) {
                if (col == (width - 1)) {
                    relaxLast(distTo, edgeTo, N, index);
                }else {
                    col++;
                    relax(distTo, edgeTo, col, row, index);
                    relax(distTo, edgeTo, col, row - 1, index);
                    relax(distTo, edgeTo, col, row + 1, index);
                }
            }else {
                if (row == (height - 1)) {
                    relaxLast(distTo, edgeTo, N, index);
                }else {
                    row++;
                    relax(distTo, edgeTo, col + 1, row, index);
                    relax(distTo, edgeTo, col - 1, row, index);
                    relax(distTo, edgeTo, col, row, index);
                }
            }
        }
    }
    private void relax(double[] distTo, int[] edgeTo, int wCol, int wRow, int v){
        if (!validateCol(wCol) || !validateRow(wRow)) {
            return;
        }
        int w = wRow * width + wCol;
        if (distTo[w] >= distTo[v] + energy[w]) {
            distTo[w] = distTo[v] + energy[w];
            edgeTo[w] = v;
        }
    }

    private void relaxLast(double[] distTo, int[] edgeTo, int dest, int v) {
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
        //[1]picture去除seam
        this.picture = carvedPicture(this.picture, true, seam);
        //获取engergy数组
        caclPicEngery();
    }

    /**
     * 去掉垂直的seam
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        //picture去除seam
        this.picture = carvedPicture(this.picture, false, seam);
        //获取engergy数组
        caclPicEngery();
    }

    private void genToplogicalOrder(boolean horizontal) {
        Stack<Integer> stack = new Stack<>();
        int marked = 0;
        stack.push(0);
        int next = 1;
        int N = width * height;
        boolean[] visited = new boolean[N];
        int last = N - 1;
        while (marked != N) {
            while (stack.isEmpty()) {
                if(visited[next]){
                    next++;
                    continue;
                }
                stack.push(next);
                next++;
            }
            int peek = stack.peek();
            boolean pushResult;
            if (horizontal) {
                pushResult = pushHorizontalStack(peek, stack, visited);
            }else {
                pushResult = pushStack(peek, stack, visited);
            }
            if (!pushResult) {
                stack.pop();
                marked++;
                visited[peek] = true;
                toplogicOrder[last] = peek;
                last--;
            }
        }
    }
    
    private boolean pushStack(int i, Stack<Integer> stack, boolean[] visited) {
        int col = i % width;
        int nextRow = i /width + 1;


        if(!validateRow(nextRow))
            return false;

        int nextIndex = nextRow * width + col;
        if (!visited[nextIndex]) {
            stack.push(nextIndex);
            return true;
        }
        if(validateCol(col - 1) && !visited[nextIndex - 1]){
            stack.push(nextIndex - 1);
            return true;
        }
        if (validateCol(col + 1) && !visited[nextIndex + 1]) {
            stack.push(nextIndex + 1);
            return true;
        }
        return false;
    }

    private boolean pushHorizontalStack(int i, Stack<Integer> stack, boolean[] visited) {
        int row = i / width;
        int nextCol = i % width + 1;

        if (!validateCol(nextCol)) {
            return false;
        }

        int nextIndex = i + 1;
        if (!visited[nextIndex]) {
            stack.push(nextIndex);
            return true;
        }
        if (validateRow(row - 1) && !visited[nextIndex - width]) {
            stack.push(nextIndex - width);
            return true;
        }

        if (validateRow(row + 1) && !visited[nextIndex + width]) {
            stack.push(nextIndex + width);
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

    /**
     * picture去掉seam，返回去除seam的picture，更新height和width
     * @param picture
     * @param horizontal
     * @param seamIndices
     * @return
     */
    private Picture carvedPicture(Picture picture, boolean horizontal, int[] seamIndices) {
        if (seamIndices == null) {
            throw new IllegalArgumentException("seam Indices should not be null!");
        }
        Picture carvedPic;

        if (horizontal) {
            validateHorizontalSeamIndices(seamIndices);
            carvedPic = new Picture(picture.width(), picture.height() - 1);
            for(int col = 0; col < carvedPic.width(); col++) {
                for (int row = 0; row < carvedPic.height(); row++) {
                    if (row < seamIndices[col]) {
                        carvedPic.set(col, row, picture.get(col, row));
                    }else {
                        carvedPic.set(col, row, picture.get(col, row + 1));
                    }
                }
            }
        }else {
            validateVerticalSeamIndices(seamIndices);
            carvedPic = new Picture(picture.width() - 1, picture.height());
            for(int row = 0; row < carvedPic.height(); row++) {
                for(int col = 0; col < carvedPic.width(); col++) {
                    if (col < seamIndices[row]) {
                        carvedPic.set(col, row, picture.get(col, row));
                    }else {
                        carvedPic.set(col, row, picture.get(col+1, row));
                    }
                }
            }
        }
        height = carvedPic.height();
        width = carvedPic.width();
        return carvedPic;
    }

    private void validateVerticalSeamIndices(int[] seamIndices) {
        if (picture.width() <= 1) {
            throw new IllegalArgumentException("picture width <= 1, can not be removed vertical");
        }
        if (seamIndices.length != picture.height()) {
            throw new IllegalArgumentException("remove vertical seam length " + seamIndices.length + " not equal to " + height);
        }
        int lastCol = -1;
        for(int i = 0; i < seamIndices.length; i++) {
            int currentCol = seamIndices[i];
            if (!validateCol(currentCol)) {
                throw new IllegalArgumentException("removeVertical Index is invalid:" + currentCol);
            }
            if (i == 0) {
                lastCol = currentCol;
            }else {
                if (Math.abs(currentCol - lastCol) >= 2) {
                    throw new IllegalArgumentException("removeVertical adjecnt index is invalid: lastcol" + lastCol
                            + ", current Col" + currentCol);
                }
                lastCol = currentCol;
            }
        }
    }

    private void validateHorizontalSeamIndices(int[] seamIndices) {
        if (picture.height() <= 1) {
            throw new IllegalArgumentException("picture height <= 1, can not be removed horizontal");
        }
        if (seamIndices.length != picture.width()) {
            throw new IllegalArgumentException("remove horizontal seam length " + seamIndices.length + " not equal to " + width);
        }
        int lastRow = -1;
        for(int i = 0; i < seamIndices.length; i++) {
            int currentRow = seamIndices[i];
            if (!validateRow(currentRow)) {
                throw new IllegalArgumentException("remove Horizon Index is invalid:" + currentRow);
            }
            if (i == 0) {
                lastRow = currentRow;
            }else {
                if (Math.abs(currentRow - lastRow) >= 2) {
                    throw new IllegalArgumentException("remove Horizon adjecnt index is invalid: lastRow" + lastRow
                            + ", current Row" + currentRow);
                }
                lastRow = currentRow;
            }
        }
    }
    private void caclPicEngery() {
        for (int col = 0; col < width; col++) {
            for(int row = 0; row < height; row++) {
                energy[row * width + col] = caclPixelEnergy(col, row);
            }
        }
    }
    private double caclPixelEnergy(int col, int row) {
        if (row == 0 || row == (height - 1) || col == 0 || col == (width - 1)) {
            return 1000;
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
        return Math.sqrt(xSquare + ySquare);
    }
}
