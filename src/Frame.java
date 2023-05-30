import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
public Service serviceWindow;

    public Frame() throws HeadlessException {
        serviceWindow = new Service();
        this.setContentPane(serviceWindow.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1290,500);
        //this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setTitle("Техническое обслуживание стенда");
        this.setFont(new Font("Calibri",Font.PLAIN,24));

        ImageIcon imageIcon = new ImageIcon("src/icon.png");
        this.setIconImage(imageIcon.getImage());
        //frame.getContentPane().setBackground(new Color(0,0,0));


        this.setVisible(true);

    }
}
