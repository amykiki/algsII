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

public class PrintEnergy {

    public static void main(String[] args) {
        String filePath = "D:\\codeProject\\git_repo\\algsII\\test-data\\datas\\seam-testing\\seam\\";
        String fileName = "6x5.png";
        String picFile = filePath + fileName;
        Picture picture = new Picture(picFile);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%7.2f ", sc.energy(col, row));
            StdOut.println();
        }

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%-9.0f ", sc.getEnergySquare(col, row));
            StdOut.println();
        }

        int[] topological = sc.getTopologic();
        int count = 0;
        for(int i = 0; i < topological.length; i++) {
            StdOut.printf("(%2d,%-2d)", topological[i]/picture.width(), topological[i]%picture.width());
            if((++count) % 10 == 0){
                StdOut.println();
            }
        }

        int[] verticalSeam = sc.findVerticalSeam();
        double sum = 0;
        for(int i = 0; i < verticalSeam.length; i++){
            StdOut.printf("%-3d", verticalSeam[i]);
            sum += sc.energy(verticalSeam[i], i);
        }
        StdOut.printf("Total energy = %f\n", sum);
    }

}
