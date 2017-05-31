import javax.swing.JFrame;

/**
 * Created by evanphoward on 3/25/17.
 */
public class Driver
{
    public static void main(String[] args) throws Exception
    {
            JFrame frame = new JFrame("Data Visualization");
            frame.setSize(3400, 1800);
            frame.setLocation(200, 100);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new Panel());
            frame.setVisible(true);
    }
}

