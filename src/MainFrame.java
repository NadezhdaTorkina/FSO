import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public WindowMain windowMain;

    public MainFrame () {
        windowMain = new WindowMain();
        this.setContentPane(windowMain.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920,1080);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setTitle("Стенд испытаний ЧСК");
        this.setFont(new Font("Calibri",Font.PLAIN,24));

        ImageIcon imageIcon = new ImageIcon("src/icon.png");
        this.setIconImage(imageIcon.getImage());

        windowMain.resizeTable1(PROG.programs.length);

        this.setVisible(true);

    }
}
