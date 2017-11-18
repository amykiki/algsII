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
    public static void main(String[] args) {
        String filePath = "D:\\codeProject\\git_repo\\algsII\\test-data\\datas\\seam-testing\\seam\\";
//        String fileName = "6x5.png";
//        String fileName = "12x10.png";
//        String fileName = "10x12.png";
        String fileName = "chameleon.png";
//        String fileName = "diagonals.png";
        String picFile = filePath + fileName;
        Picture picture = new Picture(picFile);

        printPicture(picture);
//        picture.show();
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%7.2f ", sc.energy(col, row));
            StdOut.println();
        }

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

        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        int[] verticalSeam = sc.findVerticalSeam();
        double sum = 0;
        for(int i = 0; i < verticalSeam.length; i++){
            StdOut.printf("%-5d", verticalSeam[i]);
            sum += sc.energy(verticalSeam[i], i);
        }
        StdOut.printf("\nTotal energy = %f\n", sum);

        int[] horizonSeam = sc.findHorizontalSeam();
        double horsum = 0;
        for(int i = 0; i < horizonSeam.length; i++){
            StdOut.printf("%-5d", horizonSeam[i]);
            horsum += sc.energy(i, horizonSeam[i]);
        }
        StdOut.printf("\nTotal energy = %f\n", horsum);
    }

}
