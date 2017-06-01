import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.io.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by evanphoward on 3/25/17.
 */
public class Panel extends JPanel {
    private boolean[] isFilled;
    private int currentyear;
    private int[] countrynums;
    private JSlider year;
    private Polygon[][] countries;
    private Data data;
    private JLabel title;
    private JToggleButton toggle;
    private BufferedImage myImage;
    private Graphics buffer;
    private CountryPolygons coords;
    private JLabel yearCount;
    private Timer t;
    //private Infoboard board;
    private static final int NUMCOUNT = 236;
    public Panel() throws Exception {
        setLayout(new BorderLayout());

        JPanel north = new JPanel();
        add(north,BorderLayout.NORTH);
        JPanel south = new JPanel();
        add(south, BorderLayout.SOUTH);
        myImage = new BufferedImage(1900, 3601, BufferedImage.TYPE_INT_RGB);
        buffer = myImage.getGraphics();


        year = new JSlider();
        year.addChangeListener(new Listener());
        Dimension d = year.getPreferredSize();
        year.setPreferredSize(new Dimension(d.width + 500, d.height + 50));
        south.add(new JLabel("Year:"));
        south.add(year);

        yearCount = new JLabel("" + year.getValue());
        south.add(yearCount);
        south.add(Box.createHorizontalStrut(400));

        toggle = new JToggleButton("Start");
        toggle.addActionListener(new Toggle());
        south.add(toggle);


        buffer.setColor(new Color(66, 191, 237));
        buffer.fillRect(120, 0, 1800, 3601);
        buffer.setColor(getBackground());
        buffer.fillRect(0, 0, 120, 3601);



        data = new Data();

       // board = new Infoboard(data);
       // add(board, BorderLayout.EAST);

        coords = new CountryPolygons();
        countries = new Polygon[NUMCOUNT][];
        countrynums = new int[data.getCountries().length];
        isFilled = new boolean[NUMCOUNT];


        for(int i = 0;i<isFilled.length;i++)
            isFilled[i] = false;

        for (int i = 0; i < countrynums.length; i++) {
            countrynums[i] = getCountry(data.getCountries()[i]);
        }

        JButton filechooser = new JButton("Choose New Data Set");
        filechooser.setHorizontalAlignment(SwingConstants.LEFT);
        filechooser.addActionListener(new newFile());
        north.add(filechooser);
        title = new JLabel(data.getTitle());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        north.add(title);

        year.setMinimum(data.getYears()[0]);
        year.setMaximum(data.getYears()[data.getYears().length - 1]);
        year.setMinorTickSpacing(1);
        year.setMajorTickSpacing((int) (year.getMaximum() - year.getMinimum() / 10));
        year.setPaintTicks(true);

        for (int i = 0; i < countrynums.length; i++) {

            if (countrynums[i] != -1) {
                isFilled[countrynums[i]] = true;
                if (coords.getYPoint(countrynums[i]).length > 1) {
                    countries[countrynums[i]] = new Polygon[coords.getYPoint(countrynums[i]).length];
                    currentyear = year.getValue() - year.getMinimum();
                        buffer.setColor(data.getColorNum(i,currentyear));

                        for (int k = 0; k < countries[countrynums[i]].length; k++) {
                            countries[countrynums[i]][k] = new Polygon(coords.getYPoint(countrynums[i])[k], coords.getXPoints(countrynums[i])[k], coords.getLength(countrynums[i], k));
                            buffer.fillPolygon(countries[countrynums[i]][k]);
                        }
                } else {
                    countries[countrynums[i]] = new Polygon[1];
                    currentyear = year.getValue() - year.getMinimum();
                    buffer.setColor(data.getColorNum(i,currentyear));
                    countries[countrynums[i]][0] = new Polygon(coords.getYPoint(countrynums[i])[0], coords.getXPoints(countrynums[i])[0], coords.getLength(countrynums[i], 0));
                    buffer.fillPolygon(countries[countrynums[i]][0]);

                }
            } else {
                System.out.println(data.getCountries()[i] + " is not recognized.");
            }
        }

        for (int i = 0; i < NUMCOUNT; i++) {
            if (!isFilled[i]) {
                buffer.setColor(Color.white.darker());
                if (coords.getYPoint(i).length > 1) {
                    countries[i] = new Polygon[coords.getYPoint(i).length];
                    for (int k = 0; k < countries[i].length; k++) {
                        countries[i][k] = new Polygon(coords.getYPoint(i)[k], coords.getXPoints(i)[k], coords.getLength(i, k));
                        buffer.fillPolygon(countries[i][k]);
                    }
                } else {
                    countries[i] = new Polygon[1];
                    countries[i][0] = new Polygon(coords.getYPoint(i)[0], coords.getXPoints(i)[0], coords.getLength(i, 0));
                    buffer.fillPolygon(countries[i][0]);
                }
            }
        }




    t = new Timer(20, new Listener2());


    }
    private class newFile implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xlsx");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(Panel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    data = new Data(chooser.getSelectedFile(), Panel.this);
                    year.setValue(year.getMinimum());

                }
                catch (Exception e) {

                }
            }
        }

    }
    public void reset() {
        t.start();
       // toggle.setText("Stop");
        toggle.doClick();
    }
    private int getCountry(String s) throws Exception {
        Scanner infile = new Scanner(new File("countrynames"));
        String[] standard = new String[NUMCOUNT];
        for(int i=0;i<NUMCOUNT;i++)
            standard[i]=infile.nextLine();
        for(int i=0;i<NUMCOUNT;i++)
            if(standard[i].equals(s))
                return i;
        return -1;

    }
    private class Toggle implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            if(toggle.getText().equals("Start")) {
                t.start();
                toggle.setText("Stop");
            }
            else {
                t.stop();
                toggle.setText("Start");
            }
        }
    }
    private class Listener2 implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(year.getValue()!=year.getMaximum())
        year.setValue(year.getValue()+1);
            else {
                year.setValue(year.getMinimum());
            }
        }
    }
    private class Listener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e) {
               // board.update();
                int temp = -1;
                title.setText(data.getTitle());
                yearCount.setText("" + year.getValue());
                currentyear = year.getValue() - year.getMinimum();
                for (int i = 0; i < NUMCOUNT; i++) {
                    if (isFilled[i]) {
                        for (int k = 0; k < countrynums.length; k++)
                            if (countrynums[k] == i)
                                temp = k;
                        buffer.setColor(data.getColorNum(i,currentyear));
                        if (coords.getYPoint(i).length > 1) {
                            countries[i] = new Polygon[coords.getYPoint(i).length];
                            for (int k = 0; k < countries[i].length; k++) {
                                countries[i][k] = new Polygon(coords.getYPoint(i)[k], coords.getXPoints(i)[k], coords.getLength(i, k));
                                buffer.fillPolygon(countries[i][k]);
                            }
                        } else {
                            countries[i] = new Polygon[1];
                            countries[i][0] = new Polygon(coords.getYPoint(i)[0], coords.getXPoints(i)[0], coords.getLength(i, 0));
                            buffer.fillPolygon(countries[i][0]);
                        }
                    }
                }
                repaint();
            }

    }
    public void paintComponent(Graphics g)
    {
        AffineTransform at = new AffineTransform();
        at.translate(getWidth() / 2, getHeight() / 2);
        at.rotate(3*(Math.PI/2));
        at.scale(0.5, 0.5);
        at.translate(-myImage.getWidth()/2, -myImage.getHeight()/2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(myImage, at, null);
    }
}

