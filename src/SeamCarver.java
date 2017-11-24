import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private int height;
    private int width;
    private double [][] energy2D;
    private int[][] rgbs2D;

    public SeamCarver(Picture pic) {
        if (pic == null) {
            throw new IllegalArgumentException("Construtor pic should not be null!");
        }
        height = pic.height();
        width = pic.width();
        rgbs2D = new int[height][width];
        energy2D = new double[height][width];
        picToRgbs(pic);
        //计算初始energy
        caclPicEngery();
    }

    public Picture picture() {
        return rgbsToPic();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int col, int row) {
        validateHeight(row);
        validateWidth(col);

        return energy2D[row][col];
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
        int[] edgeTo = findEdgeTo(true);
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
        if (width == 1) {
             for(int row = 0; row < height(); row++) {
                 verticalSeam[row] = 0;
             }
             return verticalSeam;
        }

        int v = width*height;
        int[] edgeTo = findEdgeTo(false);
        for(int row = height - 1; row>= 0; row--) {
            v = edgeTo[v];
            verticalSeam[row] = v % width;
        }
        return verticalSeam;
    }

    private int[] findEdgeTo(boolean horizontal) {
        int N = width * height;
        double[] distTo = new double[N + 1];
        int[] edgeTo = new int[N + 1];
        distTo[N] = Double.MAX_VALUE;
        edgeTo[N] = -1;
        if (horizontal) {
            for(int row = 0; row < height; row++) {
                distTo[row*width] = energy2D[row][0];
                edgeTo[row*width] = -1;
            }
        }else {
            for(int col = 0; col < width; col++) {
                distTo[col] = energy2D[0][col];
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

        int[] toplogicOrder;
        if (horizontal) {
            toplogicOrder = quickHorizontalTopoOrder();
        }else {
            toplogicOrder = quickVerticalTopoOrder();
        }
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
        return edgeTo;
    }
    private void relax(double[] distTo, int[] edgeTo, int wCol, int wRow, int v){
        if (!validateCol(wCol) || !validateRow(wRow)) {
            return;
        }
        int w = wRow * width + wCol;
        if (distTo[w] >= distTo[v] + energy2D[wRow][wCol]) {
            distTo[w] = distTo[v] + energy2D[wRow][wCol];
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
        if (seam == null) {
            throw new IllegalArgumentException("seam Indices should not be null!");
        }
        int[][] carvedRgbs2D;
        if (height <= 1) {
            throw new IllegalArgumentException("picture height <= 1, can not be removed horizontal");
        }
        if (seam.length != width) {
            throw new IllegalArgumentException("remove horizontal seam length " + seam.length + " not equal to " + width);
        }
        int lastRow = -1;
        carvedRgbs2D = new int[height - 1][width];

        int nextHeight = height - 1;
        double[][] nextEnergy2D = new double[nextHeight][width];
        int[] changes = new int[width * 2];
        initArray(changes, -1);
        for(int col = 0; col < width; col++) {
            int currentRow = seam[col];
            if (!validateRow(currentRow)) {
                throw new IllegalArgumentException("remove Horizon Index is invalid:" + currentRow);
            }
            if (col != 0 && Math.abs(currentRow - lastRow) >= 2) {
                throw new IllegalArgumentException("remove Horizon adjecnt index is invalid: lastRow" + lastRow
                        + ", current Row" + currentRow);
            }
            for(int row = 0; row < currentRow; row++) {
                carvedRgbs2D[row][col] = rgbs2D[row][col];
                if (row == currentRow - 1) {
                    changes[col * 2] = currentRow - 1;
                }else {
                    nextEnergy2D[row][col] = energy2D[row][col];
                }
            }
            for(int row = currentRow + 1; row < height; row++) {
                carvedRgbs2D[row - 1][col] = rgbs2D[row][col];
                if (row == currentRow + 1) {
                    changes[col * 2 + 1] = currentRow;
                }else {
                    nextEnergy2D[row -1][col] = energy2D[row][col];
                }
            }
            lastRow = currentRow;
        }
        height = height - 1;
        rgbs2D = carvedRgbs2D;
        for(int col = 0; col < width; col++) {
            for(int j = 0; j < 2; j++) {
                int row = changes[col * 2 + j];
                if(!validateRow(row))
                    continue;
                nextEnergy2D[row][col] = caclPixelEnergy(col, row);
            }
        }
        energy2D = nextEnergy2D;
    }

    /**
     * 去掉垂直的seam
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam Indices should not be null!");
        }
        int[][] carvedRgbs2D;
        if (width <= 1) {
            throw new IllegalArgumentException("picture width <= 1, can not be removed vertical");
        }
        if (seam.length != height) {
            throw new IllegalArgumentException("remove vertical seam length " + seam.length + " not equal to " + height);
        }
        int lastCol = -1;
        carvedRgbs2D = new int[height][width - 1];
        int[] changes = new int[height * 2];
        double[][] nextEnergy2D = new double[height][width - 1];
        initArray(changes, -1);
        for(int row = 0; row < height; row++) {
            int currentCol = seam[row];
            if (!validateCol(currentCol)) {
                throw new IllegalArgumentException("removeVertical Index is invalid:" + currentCol);
            }
            if (row != 0 && Math.abs(currentCol - lastCol) >= 2) {
                throw new IllegalArgumentException("removeVertical adjecnt index is invalid: lastcol" + lastCol
                        + ", current Col" + currentCol);
            }

            System.arraycopy(rgbs2D[row], 0, carvedRgbs2D[row], 0, currentCol);
            if ((currentCol + 1) < width) {
                System.arraycopy(rgbs2D[row], currentCol + 1, carvedRgbs2D[row], currentCol, width - currentCol - 1);
            }
            if (currentCol - 1 > 0) {
                System.arraycopy(energy2D[row], 0, nextEnergy2D[row], 0, currentCol - 1);
            }
            if (currentCol + 2 < width) {
                System.arraycopy(energy2D[row], currentCol + 2, nextEnergy2D[row], currentCol +1, width - (currentCol + 2));
            }
            changes[row * 2] = currentCol - 1;
            changes[row * 2 + 1] = currentCol;
            lastCol = currentCol;
        }
        width = width - 1;
        rgbs2D = carvedRgbs2D;
        for(int row = 0; row < height; row++) {
            for(int j = 0; j < 2; j++) {
                int col = changes[row * 2 + j];
                if(!validateCol(col))
                    continue;
                nextEnergy2D[row][col] = caclPixelEnergy(col, row);
            }
        }
        energy2D = nextEnergy2D;

    }

    private int[] quickVerticalTopoOrder() {
        int[] quickTopo = new int[width * height];
        int startRow = height - 1;
        int lastCol = 0;
        quickTopo[width * height - 1] = startRow * width;
        int index = width * height -2;
        while (startRow >= 0) {
            int col = lastCol + 1;
            if (col >= width) {
                startRow--;
                col = width - 1;
            }
            int row = startRow;
            while (row >= 0 && col >= 0) {
                quickTopo[index] = row * width + col;
                index--;
                row--;
                col--;
            }
            lastCol++;
        }
        return quickTopo;
    }

    private int[] quickHorizontalTopoOrder() {
        int[] quickTopo = new int[width * height];
        int startCol = width - 1;
        quickTopo[width*height - 1] = startCol;
        int lastRow = 0;
        int index = width * height -2;
        while (startCol >= 0) {
            int row = lastRow + 1;
            if (row >= height) {
                row = height - 1;
                startCol--;
            }
            int col = startCol;
            while (col >= 0 && row >= 0) {
                quickTopo[index] = row * width + col;
                index--;
                row--;
                col--;
            }
            lastRow++;
        }
        return quickTopo;
    }

    private void picToRgbs(Picture pic) {
        for(int row = 0; row < pic.height(); row++) {
            for(int col = 0; col < pic.width(); col++) {
                rgbs2D[row][col] = pic.getRGB(col, row);
            }
        }
    }

    private Picture rgbsToPic() {
        Picture pic = new Picture(width, height);
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                pic.setRGB(col, row,  rgbs2D[row][col]);
            }
        }
        return pic;
    }
    private void initArray(int[] arr, int value) {
        for(int i = 0; i < arr.length; i++) {
            arr[i] = value;
        }
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

    private void caclPicEngery() {
        for (int col = 0; col < width; col++) {
            for(int row = 0; row < height; row++) {
                energy2D[row][col] = caclPixelEnergy(col, row);
            }
        }
    }
    private double caclPixelEnergy(int col, int row) {
        if (row == 0 || row == (height - 1) || col == 0 || col == (width - 1)) {
            return 1000;
        }
        Color colorUp = new Color( rgbs2D[row-1][col]);
        Color colorDown = new Color(rgbs2D[row + 1][col]);
        Color colorLeft = new Color(rgbs2D[row][col - 1]);
        Color colorRight = new Color(rgbs2D[row][col + 1]);

        double xSquare = Math.pow(colorRight.getRed() - colorLeft.getRed(), 2)
                + Math.pow(colorRight.getGreen() - colorLeft.getGreen(), 2)
                + Math.pow(colorRight.getBlue() - colorLeft.getBlue(), 2);
        double ySquare = Math.pow(colorDown.getRed() - colorUp.getRed(), 2)
                + Math.pow(colorDown.getGreen() - colorUp.getGreen(), 2)
                + Math.pow(colorDown.getBlue() - colorUp.getBlue(), 2);
        return Math.sqrt(xSquare + ySquare);
    }

    /*private void genToplogicalOrder(boolean horizontal) {
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
    }*/

    /*public int[] topoOrder(boolean horizontal) {
        genToplogicalOrder(horizontal);
        return toplogicOrder;
    }*/
    /*private boolean pushStack(int i, Stack<Integer> stack, boolean[] visited) {
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
    }*/
}
