import org.joda.time.Days;
import org.joda.time.Instant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Service {
    JPanel panel1;
    private JButton ConfirmButton;
    private JButton Done1Button;
    private JCheckBox checkBox1;
    private JButton Done2Button;
    private JButton Done3Button;
   // private JButton Done4Button;
    private JButton Done8Button;
    private JLabel LastDateLbl1;
    private JLabel LastDateLbl2;
    private JLabel LastDateLbl3;
   // private JLabel LastDateLbl4;
    private JLabel LastDateLbl8;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
   // private JCheckBox checkBox4;
    private JCheckBox checkBox8;
    private JLabel LastDateLbl7;
    private JLabel LastDateLbl6;
    private JLabel LastDateLbl5;
    private JButton Done5Button;
    private JButton Done6Button;
    private JButton Done7Button;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JCheckBox checkBox7;
    private JLabel left8Lbl;
    private JLabel left7Lbl;
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    public Service() {

        renewDates();


        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
        UIManager.put("OptionPane.noButtonText"    , "Нет"   );
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText"    , "Готово");

        ConfirmButton.requestFocusInWindow();

        Done1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(1);
                    if (send == 1) {
                        Done1Button.setEnabled(false);
                        checkBox1.setSelected(true);
                        Date date1 = SQL.getLastDate(1);
                        LastDateLbl1.setText(df.format(date1));
                    }
                }
            }
        });
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBox1.isSelected() & checkBox2.isSelected() &
                        checkBox3.isSelected() & //checkBox4.isSelected() &
                        checkBox5.isSelected() & checkBox6 .isSelected() &
                         checkBox7.isSelected() & checkBox8.isSelected()) {
                    // закрыть окно, открыть другое, рабочее
                    Main.serviceIsDone = true;
                }
                else {
                    // показать сообщение, что работа запрещена
                    JOptionPane.showMessageDialog(null,"Подтвердите проведение необходимых работ","Техническое обслуживание",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        Done2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(2);
                    if (send == 1) {
                        Done2Button.setEnabled(false);
                        checkBox2.setSelected(true);
                        Date date2 = SQL.getLastDate(2);
                        LastDateLbl2.setText(df.format(date2));
                    }
                }
            }
        });
        Done3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(3);
                    if (send == 1) {
                        Done3Button.setEnabled(false);
                        checkBox3.setSelected(true);
                        Date date3 = SQL.getLastDate(3);
                        LastDateLbl3.setText(df.format(date3));
                    }
                }
            }
        });
        /*Done4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(4);
                    if (send == 1) {
                        Done4Button.setEnabled(false);
                        checkBox4.setSelected(true);
                        Date date4 = SQL.getLastDate(4);
                        LastDateLbl4.setText(df.format(date4));
                    }
                }
            }
        });*/
        Done8Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(8);
                    if (send == 1) {
                        Done8Button.setEnabled(false);
                        checkBox8.setSelected(true);
                        Date date8 = SQL.getLastDate(8);
                        LastDateLbl8.setText(df.format(date8));
                    }
                }
            }
        });
        Done7Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(7);
                    if (send == 1) {
                        Done7Button.setEnabled(false);
                        checkBox7.setSelected(true);
                        Date date7 = SQL.getLastDate(7);
                        LastDateLbl7.setText(df.format(date7));
                    }
                }
            }
        });
        Done6Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(6);
                    if (send == 1) {
                        Done6Button.setEnabled(false);
                        checkBox6.setSelected(true);
                        Date date6 = SQL.getLastDate(6);
                        LastDateLbl6.setText(df.format(date6));
                    }
                }
            }
        });
        Done5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int b = JOptionPane.showConfirmDialog(null,"Работы успешно завершены?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (b==0) {
                    int send = SQL.setNewDate(5);
                    if (send == 1) {
                        Done5Button.setEnabled(false);
                        checkBox5.setSelected(true);
                        Date date5 = SQL.getLastDate(5);
                        LastDateLbl5.setText(df.format(date5));
                    }
                }
            }
        });
    }

    private void renewDates() {

        Date date1 = SQL.getLastDate(1);
        Date date2 = SQL.getLastDate(2);
        Date date3 = SQL.getLastDate(3);
        //Date date4 = SQL.getLastDate(4);
        Date date5 = SQL.getLastDate(5);
        Date date6 = SQL.getLastDate(6);
        Date date7 = SQL.getLastDate(7);
        Date date8 = SQL.getLastDate(8);

        LastDateLbl1.setText(df.format(date1));
        LastDateLbl2.setText(df.format(date2));
        LastDateLbl3.setText(df.format(date3));
        //LastDateLbl4.setText(df.format(date4));
        LastDateLbl5.setText(df.format(date5));
        LastDateLbl6.setText(df.format(date6));
        LastDateLbl7.setText(df.format(date7));
        LastDateLbl8.setText(df.format(date8));

        int days;
        Days d = Days.daysBetween(Instant.parse(date1.toString()),Instant.now());
        days = d.getDays();
        if (days < 1) checkBox1.setSelected(true);


        d = Days.daysBetween(Instant.parse(date2.toString()),Instant.now());
        days = d.getDays();
        if (days < 1) checkBox2.setSelected(true);

        d = Days.daysBetween(Instant.parse(date3.toString()),Instant.now());
        days = d.getDays();
        if (days < 1) checkBox3.setSelected(true);

        //d = Days.daysBetween(Instant.parse(date4.toString()),Instant.now());
        //days = d.getDays();
        //if (days < 1) checkBox4.setSelected(true);

        d = Days.daysBetween(Instant.parse(date5.toString()),Instant.now());
        days = d.getDays();
        if (days < 30) checkBox5.setSelected(true);

        d = Days.daysBetween(Instant.parse(date6.toString()),Instant.now());
        days = d.getDays();
        if (days < 365) checkBox6.setSelected(true);

        d = Days.daysBetween(Instant.parse(date7.toString()),Instant.now());
        days = d.getDays();
        if (days < 365) {
            checkBox7.setSelected(true);
            //осталось меньше 2 недель
            if (days > 365 - 14) left7Lbl.setText("Осталось "+(365 - days)+" дней");
        }

        d = Days.daysBetween(Instant.parse(date8.toString()),Instant.now());
        days = d.getDays();
        if (days < 365*2) {
            checkBox8.setSelected(true);
            //осталось меньше 2 недель
            if (days > 365*2 - 14) left8Lbl.setText("Осталось "+(365*2 - days)+" дней");
        }
    }
}
