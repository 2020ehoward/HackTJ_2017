/**
 * Created by evanphoward on 3/25/17.
 */
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class CountryPolygons {
    private static int[][][] xpoints;
    private static int[][][] ypoints;
    public CountryPolygons() throws Exception {
        int numCount=0;
        Scanner pinfile = new Scanner(new File("country"));
    String[] coords;
    String[] temp;
    String[] islands;
while(pinfile.hasNext()) {
    numCount++;
    pinfile.nextLine();
}
pinfile.close();
pinfile.reset();

xpoints = new int[numCount][][];
ypoints = new int[numCount][][];

        Scanner infile = new Scanner(new File("country"));
        Scanner test = new Scanner(new File("country"));

for(int i=0;i<numCount;i++) {
    if(test.nextLine().contains("</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>")) {
        islands=infile.nextLine().split(",0</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>");
        xpoints[i]=new int[islands.length][];
        ypoints[i]=new int[islands.length][];
        for(int k=0;k<islands.length;k++) {
            coords = islands[k].split(",0 ");

            int count = coords.length;
            double[] x = new double[count];
            double[] y = new double[count];

            for (int j = 0; j < count; j++) {
                temp = coords[j].split(",");
                x[j] = Double.parseDouble(temp[0]);
                y[j] = Double.parseDouble(temp[1]);
            }

            xpoints[i][k] = new int[x.length];
            ypoints[i][k] = new int[y.length];
            for (int j = 0; j < x.length; j++) {
                x[j] = (x[j] + 180.0) * 10.0;
                xpoints[i][k][j] = (int) (x[j]);
            }
            for (int j = 0; j < x.length; j++) {
                y[j] = (y[j] + 100.0) * 10.0;
                ypoints[i][k][j] = (int) (y[j]);
            }
        }
    }
    else {
        xpoints[i]=new int[1][];
        ypoints[i]=new int[1][];
        coords = infile.nextLine().split(",0 ");
        int count = coords.length;
        double[] x = new double[count];
        double[] y = new double[count];

        for (int j = 0; j < count; j++) {
            temp = coords[j].split(",");
            x[j] = Double.parseDouble(temp[0]);
            y[j] = Double.parseDouble(temp[1]);
        }

        xpoints[i][0] = new int[x.length];
        ypoints[i][0] = new int[y.length];
        for (int j = 0; j < x.length; j++) {
            x[j] = (x[j] + 180.0) * 10.0;
            xpoints[i][0][j] = (int) (x[j]);
        }
        for (int j = 0; j < x.length; j++) {
            y[j] = (y[j] + 100.0) * 10.0;
            ypoints[i][0][j] = (int) (y[j]);
        }
    }
}
infile.close();
infile.reset();


}
public int[][] getXPoints(int x) {
        return xpoints[x];
}
public int[][] getYPoint(int y) {
        return ypoints[y];
}
public int getLength(int i,int k) {
        return xpoints[i][k].length;
}
}
