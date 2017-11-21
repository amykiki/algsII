/******************************************************************************
 *  Compilation:  javac PrintEnergy.java
 *  Execution:    java PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *                
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each pixel as calculated by SeamCarver object. 
 * 
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class PrintEnergy {
    public static String filePath = "D:\\codeProject\\git_repo\\algsII\\test-data\\datas\\seam-testing\\seam\\";
    public static String[] fileNames = {"6x5.png", "4x6.png", "10x12.png", "3x7.png", "5x6.png", "7x3.png", "7x10.png",
            "12x10.png", "stripes.png", "diagonals.png", "chameleon.png", "HJocean.png",
            "1x8.png", "8x1.png", "1x1.png"};

    public static void printPicture(Picture picture) {
        int width = picture.width();
        int height = picture.height();
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                Color color = picture.get(col, row);
                StdOut.printf("(%d,%d,%d) ", color.getRed(), color.getGreen(), color.getBlue());
            }
            StdOut.println();
        }
    }

    public static void testSeamCarver() {
        for (String fileName : fileNames) {
            String picFile = filePath + fileName;
            Picture picture = new Picture(picFile);
            SeamCarver sc = new SeamCarver(picture);
            StdOut.println("\n=============START " + fileName + " START=============");
            StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
            int[] verticalSeam = sc.findVerticalSeam();
            double sum = 0;
            for(int i = 0; i < verticalSeam.length; i++){
//                StdOut.printf("%-5d", verticalSeam[i]);
                sum += sc.energy(verticalSeam[i], i);
            }
            StdOut.printf("Total vertical energy = %f\n", sum);

            int[] horizonSeam = sc.findHorizontalSeam();
            double horsum = 0;
            for(int i = 0; i < horizonSeam.length; i++){
//                StdOut.printf("%-5d", horizonSeam[i]);
                horsum += sc.energy(i, horizonSeam[i]);
            }
            StdOut.printf("Total Horizontal energy = %f\n", horsum);
            StdOut.println("=============END " + fileName + " END=============");
        }
    }

    public static void debug() {
//        String fileName = "6x5.png";
//        String fileName = "12x10.png";
//        String fileName = "10x12.png";
        String fileName = "4x6.png";
//        String fileName = "chameleon.png";
//        String fileName = "diagonals.png";
        String picFile = filePath + fileName;
        Picture picture = new Picture(picFile);

//        printPicture(picture);
//        picture.show();
        SeamCarver sc = new SeamCarver(picture);
        sc.removeVerticalSeam(null);
       /* StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%7.2f ", sc.energy(col, row));
            StdOut.println();
        }*/

        /*for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%-9.0f ", sc.getEnergySquare(col, row));
            StdOut.println();
        }*/

        /*int[] topological = sc.genToplogicalOrder(sc.width(), sc.height(), false);
        int count = 0;
        for(int i = 0; i < topological.length; i++) {
            StdOut.printf("(%2d,%-2d)", topological[i]/picture.width(), topological[i]%picture.width());
            if((++count) % 10 == 0){
                StdOut.println();
            }
        }*/
    }

    public static Picture genPic(String fileName) {
        String picFile = filePath + fileName;
        Picture picture = new Picture(picFile);
        return picture;
    }

    /*public static void testEngery(String fileName) {
        Picture picture = genPic(fileName);
        SeamCarver sc = new SeamCarver(picture);
        double[] e1 = sc.getEnergy();
        int[] seam = sc.findVerticalSeam();
        CommonUtil.printIntArray(seam, 6);
        sc.removeVerticalSeam(seam);
        double[] e2 = sc.getEnergy();
        CommonUtil.printArray(e1, 6);
        StdOut.println("==============");
        CommonUtil.printArray(e2, 5);
        StdOut.println(e2.length);

    }*/
    /*public static void testTopoOrder() {
//        Picture picture = genPic("diagonals.png");
        Picture picture = genPic("12x10.png");
        SeamCarver sc = new SeamCarver(picture);
        int[] scOrder = sc.topoOrder(false);
        int[] order = sc.quickVerticalTopoOrder();
        CommonUtil.printIntArray(scOrder, 10);
        CommonUtil.printIntArray(order, 10);

        for(int i = 0; i < scOrder.length; i++) {
            if (scOrder[i] != order[i]) {
                StdOut.println("ERROR, order not equal, scorder[" + i + "] = " + scOrder[i]
                + ", order[" + i + "] = " + order[i]);
                break;
            }
        }

        int[] schorOrder = sc.topoOrder(true);
        int[] horOrder = sc.quickHorizontalTopoOrder();
        CommonUtil.printIntArray(schorOrder, 10);
        CommonUtil.printIntArray(horOrder, 10);

        for(int i = 0; i < schorOrder.length; i++) {
            if (schorOrder[i] != horOrder[i]) {
                StdOut.println("ERROR, order not equal, schorOrder[" + i + "] = " + schorOrder[i]
                        + ", horOrder[" + i + "] = " + horOrder[i]);
                break;
            }
        }
    }*/
    public static void main(String[] args) {
//        testSeamCarver();
//        debug();
//        testTopoOrder();
    }

}
