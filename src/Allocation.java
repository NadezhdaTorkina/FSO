import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public  class Allocation {

    static void lblcenter(JLabel lbl, JPanel panel, int x, int y, int width, int height) {
        Default(lbl,panel,x,y,width,height,SwingConstants.CENTER);
    }

    static void lbleft(JLabel lbl, JPanel panel, int x, int y, int width, int height) {
        Default(lbl,panel,x,y,width,height,SwingConstants.LEFT);
    }

    static void textCenter(JTextField text, JPanel panel, int x, int y, int width, int height) {
        Default(text,panel,x,y,width,height,SwingConstants.CENTER);
    }

    static void textLeft(JTextField text, JPanel panel, int x, int y, int width, int height) {
        Default(text,panel,x,y,width,height,SwingConstants.LEFT);
    }


    static void Default(JComponent component, JPanel panel, int x, int y, int width, int height,int align) {
        Border lblBorder = null;

        
        if (component instanceof JLabel) {
             ((JLabel) component).setHorizontalAlignment(align);
            lblBorder  = BorderFactory.createEtchedBorder();
        }
        if (component instanceof JTextField) {
            ((JTextField) component).setHorizontalAlignment(align);
            lblBorder  = BorderFactory.createEtchedBorder(Main.DARK_GREEN,Color.green);
        }
        panel.add(component);
        component.setBounds(x,y,width,height);
        component.setBorder(lblBorder);
        
        component.setFont(new Font("Times New Roman", 0,14));
        panel.setComponentZOrder(component,0);
    }





}
