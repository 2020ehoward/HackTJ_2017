/**
 * Created by William Wang on 3/25/17.
 */
import java.io.*;      //the File class
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;
public class Data
{
    private static double[][] data;
    private static int[] years;
    private static String[] countries;
    private static String title;
    private static double sum;
    private File myFile;
    static Row.MissingCellPolicy CREATE_NULL_AS_BLANK;
    public static double highestValue()
    {
        double max = data[0][0];
        for(int r=0;r<data.length;r++)
            for(int c=0;c<data[r].length;c++)
            max=Math.max(max,data[r][c]);
            return max;
    }
    public static double lowestValue()
    {
        double min = data[0][0];
        for(int r=0;r<data.length;r++)
            for(int c=0;c<data[r].length;c++)
                min=Math.min(min,data[r][c]);
        return min;

    }
    public String getTitle() {
        return title;
    }
    public String[] getCountries() {
        return countries;
    }
    public int[] getYears() {
        return years;
    }
    public double[][] getData() {
        return data;
}
public java.awt.Color getColorNum(int x,int y) {
        int red = map(data[x][y],lowestValue(),highestValue(),16,255);
    int blue = map(data[x][y],lowestValue(),highestValue(),190,255);
    int green = map(data[x][y],lowestValue(),highestValue(),0,255);
        if(data[x][y]==-1.0)
            return java.awt.Color.white.darker();
        return new java.awt.Color(red,blue,green);
}
    private int map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (int)((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }
    public Data() throws Exception
    {
        myFile=new File("data/employ15.xlsx");
        int count=0;
        Sheet sheet1;
        if(FilenameUtils.getExtension(myFile.getName()).equals("xlsx")) {
            FileInputStream inputStream = new FileInputStream(myFile);
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            sheet1 = wb.getSheetAt(0);
        }
        else {
            NPOIFSFileSystem fs = new NPOIFSFileSystem(myFile);
            HSSFWorkbook fwb = new HSSFWorkbook(fs.getRoot(),true);
            sheet1 = fwb.getSheetAt(0);
        }

        DataFormatter formatter = new DataFormatter();

        title = formatter.formatCellValue(sheet1.getRow(0).getCell(0));
        for(int i=1;!(formatter.formatCellValue(sheet1.getRow(0).getCell(i)).equals(""));i++)
            count++;
        years=new int[count];
        for(int i=0;!(formatter.formatCellValue(sheet1.getRow(0).getCell(i+1)).equals(""));i++)
            years[i]=Integer.parseInt(formatter.formatCellValue(sheet1.getRow(0).getCell(i+1)));
        int numCount=0;
        out:
        for(int i=0;true;i++) {
            Row r = sheet1.getRow(i);
            if (r == null) {
                break out;
            }

            int cn = 1;
            Cell c = r.getCell(cn);
            if (c == null) {
                break out;
            } else {
                numCount++;
            }
        }
        countries=new String[numCount-1];
        for(int i=0;i<numCount-1;i++)
        countries[i]=formatter.formatCellValue((sheet1.getRow(i+1).getCell(0)));

        data = new double[numCount][count];

        for(int r=0;r<numCount-1;r++) {
            for(int c=0;c<count;c++) {
                if(formatter.formatCellValue(sheet1.getRow(r+1).getCell(c+1)).isEmpty())
                    data[r][c]=-1.0;
                else
                data[r][c]=Double.parseDouble(formatter.formatCellValue(sheet1.getRow(r+1).getCell(c+1)));
            }
        }
    }
    public Data(File f, Panel p) throws Exception
    {
        myFile=f;
        for(double[] dd : data)
            for(double d : dd)
                d=0.0;
        int count=0;
        Sheet sheet1;
        if(FilenameUtils.getExtension(myFile.getName()).equals("xlsx")) {
            FileInputStream inputStream = new FileInputStream(myFile);
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            sheet1 = wb.getSheetAt(0);
        }
        else {
            NPOIFSFileSystem fs = new NPOIFSFileSystem(myFile);
            HSSFWorkbook fwb = new HSSFWorkbook(fs.getRoot(),true);
            sheet1 = fwb.getSheetAt(0);
        }

        DataFormatter formatter = new DataFormatter();

        title = formatter.formatCellValue(sheet1.getRow(0).getCell(0));
        for(int i=1;!(formatter.formatCellValue(sheet1.getRow(0).getCell(i)).equals(""));i++)
            count++;
        years=new int[count];
        for(int i=0;!(formatter.formatCellValue(sheet1.getRow(0).getCell(i+1)).equals(""));i++)
            years[i]=Integer.parseInt(formatter.formatCellValue(sheet1.getRow(0).getCell(i+1)));
        int numCount=0;
        out:
        for(int i=0;true;i++) {
            Row r = sheet1.getRow(i);
            if (r == null) {
                break out;
            }

            int cn = 1;
            Cell c = r.getCell(cn);
            if (c == null) {
                break out;
            } else {
                numCount++;
            }
        }
        countries=new String[numCount-1];
        for(int i=0;i<numCount-1;i++)
            countries[i]=formatter.formatCellValue((sheet1.getRow(i+1).getCell(0)));

        data = new double[numCount][count];

        for(int r=0;r<numCount-1;r++) {
            for(int c=0;c<count;c++) {
                if(formatter.formatCellValue(sheet1.getRow(r+1).getCell(c+1)).isEmpty())
                    data[r][c]=-1.0;
                else
                    data[r][c]=Double.parseDouble(formatter.formatCellValue(sheet1.getRow(r+1).getCell(c+1)));
            }
        }
        p.reset();
    }
}
