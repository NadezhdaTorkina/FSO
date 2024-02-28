import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class WindowMain {


    public boolean resizeflag;
    JPanel panel1;
    private JPanel North;
    private JPanel West;
    private JPanel East;
    private JPanel Center;
    private JButton обнулитьButton;
    JLabel Din1weight;
    JLabel Din2weight;
    JLabel comm1lbl;
    JLabel comm2lbl;
    private JButton zeroDin2btn;
    private JPanel South;
    private JButton активироватьButton;
    private JButton отключитьButton;
    JLabel HandLbl;
     JLabel OUT0LBL;
     JLabel OUT1LBL;
     JLabel OUT2LBL;
     JLabel OUT3LBL;
     JLabel OUT4LBL;
     JLabel DO0LBL;

    JButton do1onbtn;
     JButton do10ffbtn;
     JButton do2onbtn;
     JButton do2offbtn;
     JButton do3onbtn;
     JButton do3offbtn;
     JButton do4onbtn;
     JButton do4offbtn;
     JButton ao1onbtn;
     JButton ao1offbtn;
     JTextField a00TextField;
     JButton buttonDEC;
     JButton buttonINC;
     JTable table1;
    private JLabel selectedLbl;
    private JButton применитьButton;
    private JLabel currProgName;
    JTable table2;
    private JButton редактироватьButton;
    JLabel AutoLbl;
    JButton ПУСКButton;
    private JTextField textField1;
    private JButton cancelEditBtn;
    private JButton addProgBtn;
    private JButton DeleteProgBtn;
    JScrollPane scrolltable1;
    private JTable table3;
    JLabel maxlbl;
    private JButton Button123;
    private JButton clearT3btn;
    JLabel minlbl;
    JLabel preslbl;
    JButton stopautobtn;
    JButton hRegOn;
    JButton hRegOff;
    JLabel hReglbl;
    JTextField hRegText;
    JButton hRegTextDecBtn;
    JButton hRegTextIncBtn;
    JLabel rangeLbl;
    JLabel PLCLbl;
    JLabel zvukLbl;
    private JButton frqGetBtn;
    JLabel frqLbl;
    private JComboBox comboBox1;
    JLabel dinTypeLbl;
    JLabel dinIsConnLbl;
    JLabel pumpIsOnLbl;
    JButton frqHandOnBtn;
    JButton FrqHandOffBtn;
    private JTextField frqHandSetText;
    private JButton frqDec;
    private JButton frqInc;
    JLabel Din2Weight2;
    JLabel valueRangelbl;
    private JButton errRstBtn;
    JLabel STAGElbl;
    JLabel AutoSetLbl;
    JLabel AutoRangeLbl;
    private JButton стартButton;
    private JButton cancelBtn;
    JPanel COLORLbl;
    JLabel err1Lbl;
    private JLabel err1lbl2;
    JPanel err1Pnl;
    private JLabel err2lbl;
    JPanel err2Pnl;
    JPanel err3Pnl;
    public JLabel CLAMP;
    JLabel YA4Lbl;
    JButton YA4OnBtn;
    JButton YA4OffBtn;
    private JButton add;
    JLabel edit;
    public static int T3columnsNumb = 10;
    public static int T3rowsNumb = 29;

    public WindowMain() {

        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
        UIManager.put("OptionPane.noButtonText"    , "Нет"   );
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText"    , "Готово");


        обнулитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.din1.zeroCmd = true;
            }
        });
        zeroDin2btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.din2.zeroCmd = true;
            }
        });
        активироватьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Alg.AutoStartCmd) {
                    Alg.Auto = false;
                    Alg.Hand = true;
                    South.setPreferredSize(new Dimension(1500,190));
                    panel1.repaint();
                    panel1.revalidate();
                }
            }
        });
        отключитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.Hand = false;
                Alg.Auto = true;
                South.setPreferredSize(new Dimension(1500,40));
                panel1.repaint();
                panel1.revalidate();
            }
        });
        buttonDEC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.propHandSet -= 0.1d;
                if(Alg.propHandSet < 0.0d) Alg.propHandSet = 0.0d;
                a00TextField.setText(String.format("%.1f",Alg.propHandSet));
            }
        });
        a00TextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewHandSet();
                a00TextField.transferFocus();
            }
        });


        buttonINC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.propHandSet += 0.1d;
                if(Alg.propHandSet > 100.0d) Alg.propHandSet = 100.0d;
                a00TextField.setText(String.format("%.1f",Alg.propHandSet));
            }
        });
        a00TextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setNewHandSet();
            }
        });

        a00TextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setNewHandSet();
            }
        });


        a00TextField.addKeyListener(new KeyAdapter() {
        });


        ao1onbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                a00TextField.setText(String.format("%.1f",Alg.propHandSet));
                Alg.propHandCmd = true;

            }
        });
        ao1offbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.propHandCmd = false;
            }
        });



        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeCurrId();
            }
        });

        table1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                changeCurrId();
            }
        });


        редактироватьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!PROG.editCurrProg & !currProgName.getText().equals("выберите...")) {
                    if (getPswd()) {
                        textField1.setVisible(true);
                        textField1.setText(currProgName.getText());
                        currProgName.setVisible(false);
                        PROG.editCurrProg = true;
                        редактироватьButton.setText("Сохранить");
                        table2.setRowSelectionAllowed(false);
                        table2.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
                            @Override
                            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                                cell.setBackground(new Color(5, 235, 5));
                                return cell;
                            }
                        });
                        table2.repaint();
                    }
                } else {
                    PROG temp = saveProg(table2, PROG.current.getId(), textField1.getText());
                    if (temp == null) {
                        JOptionPane.showMessageDialog(null, "Некорректные данные", "Сохранение программы", 0);
                    } else {

                            if (!table2.isEditing()) {
                                PROG.current = temp;
                                PROG.AutoSet();
                                if (SQL.saveProg()) {
                                    PROG.editCurrProg = false;

                                    table1.getModel().setValueAt(PROG.current.getName(), PROG.current.getId() - 1, 1);
                                    PROG.programs[PROG.current.getId() - 1] = PROG.current.getName();
                                    selectedLbl.setText("Выберите программу...");
                                    textField1.setVisible(false);
                                    currProgName.setText(textField1.getText());
                                    currProgName.setVisible(true);
                                    редактироватьButton.setText("Редактировать");
                                    table2.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
                                        @Override
                                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                                            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                                            cell.setBackground(Color.white);
                                            return cell;
                                        }
                                    });
                                    table2.repaint();
                                }

                        }
                    }
                }  /////
            }

        });

        применитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectedLbl.getText().equals("Выберите программу...") & !PROG.editCurrProg) {
                    currProgName.setText(selectedLbl.getText());
                    SQL.loadCurrProg(Alg.id);
                    renewTable2();
                    PROG.AutoSet();
                }
            }
        });
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField1.setBackground(Color.white);
            }
        });
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textField1.setBackground(new Color(5,235,5));
            }
        });
        cancelEditBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PROG.editCurrProg & !table2.isEditing()) {
                    PROG.editCurrProg = false;
                    textField1.setVisible(false);
                    currProgName.setText(PROG.current.getName());
                    currProgName.setVisible(true);
                    редактироватьButton.setText("Редактировать");
                    table2.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            cell.setBackground(Color.white);
                            return cell;
                        }
                    });
                    renewTable2();
                    table2.repaint();
                    PROG.AutoSet();
                }
            }
        });

        addProgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!PROG.editCurrProg) {
                    if (getPswd()) {
                    if (SQL.addNewProg()) {
                        renewTable1();
                        Alg.id = PROG.programs.length;
                        SQL.loadCurrProg(Alg.id);
                        selectedLbl.setText(PROG.current.getName());
                        currProgName.setText(PROG.current.getName());
                        renewTable2();
                        PROG.AutoSet();
                        //scrolltable1.getVerticalScrollBar().setValue(scrolltable1.getVerticalScrollBar().getMaximum());
                        resizeflag = true;
                    }
                }
                }
            }
        });
        DeleteProgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getPswd()) {
                if (!PROG.editCurrProg) {
                    // если строчка не является активной
                    int delInd = table1.getSelectedRow();
                    if (!(delInd == -1)) {
                        delInd++;
                        SQL.delete(delInd);
                        SQL.reorderIndexes(delInd);
                        renewTable1();
                        selectedLbl.setText("Выберите программу...");
                        Alg.id = -1;

                        if (delInd == PROG.current.getId()) {
                            PROG.current = null;
                            renewTable2();
                            PROG.ZeroAutoSet();
                            currProgName.setText("Выберите программу...");
                        }
                    }

                    // если выбранная строчка активная - удалить все из таблицы 2, из лейбла над ней.
                    // перерисовать таблицу
                }
            }
            }
        });

        clearT3btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clearT3();
            }
        });

        Button123.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Report.save()) {
                   int b = JOptionPane.showConfirmDialog(null,"Протокол сохранен успешно. Открыть для просмотра?","Сохранение протокола",
                           JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    if (b == 0) {
                        try {
                            Desktop.getDesktop().open(new File(Report.lastName));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Неудачно","Сохранение протокола",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        do1onbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandLoPumpSpeed = true;
            }
        });
        do10ffbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandLoPumpSpeed = false;
            }
        });
        do2onbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandDrainValve = true;
            }
        });
        do2offbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandDrainValve = false;
            }
        });
        do3onbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandFeedValve = true;
            }
        });
        do3offbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandFeedValve = false;
            }
        });
        do4onbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandReturnValve =true;
            }
        });
        do4offbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandReturnValve =false;
            }
        });
        ПУСКButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PROG.current !=null) {
                    Alg.AutoStartCmd = true;
                }
            }
        });
        stopautobtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.AutoStartCmd = false;
            }
        });
        hRegOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandRegCmd = true;
            }
        });
        hRegOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandRegCmd = false;
            }
        });
        hRegText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewRegHandSet();
                hRegText.transferFocus();
            }
        });
        hRegText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setNewRegHandSet();
            }
        });
        hRegText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setNewRegHandSet();
            }
        });
        hRegTextDecBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.RegHandSet -= 5d;
                if(Alg.RegHandSet < 0.0d) Alg.RegHandSet = 0.0d;
                hRegText.setText(String.format("%.1f",Alg.RegHandSet));
            }
        });
        hRegTextIncBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.RegHandSet += 5d;
                if(Alg.RegHandSet > Alg.maxPres) Alg.RegHandSet = Alg.maxPres;
                hRegText.setText(String.format("%.1f",Alg.RegHandSet));
            }
        });
        frqGetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Main.zvukPointer = Main.zvuk.open();

                    if (Main.zvukPointer == 0) {
                        zvukLbl.setBackground(Color.red);
                        Main.zvukPointer = Main.zvuk.open();
                    }
                    if (Main.zvukPointer > 0) {
                        zvukLbl.setBackground(Main.DARK_GREEN);
                        //frqLbl.setText("0000000");
                        Alg.lastMeasure = Main.zvuk.getMeasure(Main.zvukPointer, ZVUK.curRange);
                        if (Alg.lastMeasure > 0) {
                            frqLbl.setText(String.valueOf(Alg.lastMeasure));
                            if (Alg.enHandFsoMeasure) {
                                Alg.FSOsuccess = true;

                            }
                        } else {
                            if (Alg.lastMeasure == -5) frqLbl.setText("Ошибка5");
                            if (Alg.lastMeasure == -1) frqLbl.setText("Ошибка1");
                            if (Alg.lastMeasure == -2) frqLbl.setText("Ошибка2");
                            if (Alg.lastMeasure == -3) frqLbl.setText("Ошибка3");
                            if (Alg.lastMeasure == -6) frqLbl.setText("Вне диапозона6");
                            if (Alg.lastMeasure == -4) frqLbl.setText("Ошибка4");
                        }
                        Main.zvuk.close(Main.zvukPointer);
                        Main.zvukPointer = 0;
                    }


                    //new Thread(() -> {

                    //}).start();

                    //Main.zvuk.close(Main.zvukPointer);


            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ZVUK.curRange = comboBox1.getSelectedIndex();
            }
        });
        frqHandOnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandFrqCmd = true;
            }
        });
        FrqHandOffBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandFrqCmd = false;
            }
        });
        frqHandSetText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewFrqHandSet();
                frqHandSetText.transferFocus();
            }
        });
        frqHandSetText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setNewFrqHandSet();
            }
        });
        frqHandSetText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setNewFrqHandSet();
            }
        });

        frqDec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.FrqHandSet -= 0.1d;
                if (Alg.FrqHandSet < Alg.minFrq ) Alg.FrqHandSet = Alg.minFrq;
                frqHandSetText.setText(String.format("%.2f",Alg.FrqHandSet));
            }
        });
        frqInc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.FrqHandSet += 0.1d;
                if (Alg.FrqHandSet > Alg.maxFrq) Alg.FrqHandSet = Alg.maxFrq;
                frqHandSetText.setText(String.format("%.2f",Alg.FrqHandSet));
            }
        });
        errRstBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.errReset = true;
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.repo.cancel();
            }
        });


        YA4OnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandClampValve = true;
            }
        });
        YA4OffBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Alg.HandClampValve = false;
            }
        });
    }

    private boolean getPswd(){
        String pwd = JOptionPane.showInputDialog(null,"","Введите пароль",3);
        if (pwd.strip().equals("080888")) {
            return true;
        }
        else {
            return false;
        }
    }

    private void setNewRegHandSet() {
        try {
            Alg.RegHandSet = Double.parseDouble(hRegText.getText());
            if (Alg.RegHandSet > Alg.maxPres) Alg.RegHandSet = Alg.maxPres;
            if (Alg.RegHandSet < 0.0d) Alg.RegHandSet = 0.0d;
        } catch (NumberFormatException e) {

        }
        finally {
            hRegText.setText(String.format("%.1f",Alg.RegHandSet));
        }
    }

    private void setNewFrqHandSet() {
        try{
            Alg.FrqHandSet = Double.parseDouble(frqHandSetText.getText());
            if (Alg.FrqHandSet > Alg.maxFrq) Alg.FrqHandSet = Alg.maxFrq;
            if (Alg.FrqHandSet < Alg.minFrq ) Alg.FrqHandSet = Alg.minFrq;
        }
        catch (NumberFormatException e) {

        }
        finally {
            frqHandSetText.setText(String.format("%.2f",Alg.FrqHandSet));
        }
    }

    private void setNewHandSet() {
        try{
            Alg.propHandSet = Double.parseDouble(a00TextField.getText());
            if(Alg.propHandSet > 100.0d) Alg.propHandSet = 100.0d;
            if(Alg.propHandSet < 0.0d) Alg.propHandSet = 0.0d;
        }
        catch (Exception ex) {

        }
        finally {
            a00TextField.setText(String.format("%.1f",Alg.propHandSet));
        }
    }

    private void clearT3() {
        if (Alg.measures != null) {
            for (int i = 0; i < Alg.measures.size(); i++) {
                T3CellSet(i, "", "");
            }
        }
        Alg.measures = new ArrayList<>();
        minlbl.setText("");
        maxlbl.setText("");
    }

    public void T3CellSet(int i, String s1,String s2) {
        // cell number starts at 0
        try {
            int col = i / T3rowsNumb;
            int row = i - col * T3rowsNumb;
            table3.setValueAt(s1, row, col * 2);
            table3.setValueAt(s2, row, col * 2 + 1);

            table3.repaint();
        }
        catch (ArrayIndexOutOfBoundsException e ) {}
    }

    private void changeCurrId() {
        selectedLbl.setText(PROG.programs[table1.getSelectedRow()]);
        Alg.id = table1.getSelectedRow()+1;
    }


    private void renewTable1() {
        SQL.getSQLProgs();
        DefaultTableModel model = new DefaultTableModel(0,0){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[]{"№","Название"});
        table1.setModel(model);
        resizeTable1(PROG.programs.length);
        if (PROG.programs.length >0) {
            int i=0;
            for (String str :PROG.programs){
                model.addRow(new String[]{String.valueOf(++i),str});
            }
        }

    }

    private PROG saveProg(JTable t, int id, String name) {
        boolean mistake = false;
        // считать из таблицы данные во временный объект
        PROG temp = null;
        try {
            String buf;
            temp = new PROG(id,name);
            buf = (String) t.getModel().getValueAt(0,2);
            //buf =  ((String)t.getModel().getValueAt(0,2)).strip().replaceAll(",",".");
            temp.setLength(Double.parseDouble(buf));
            if ( temp.getLength() < 0d | temp.getLength() > 999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(1,2)).strip().replaceAll(",",".");
            temp.setWidth(Double.parseDouble(buf));
            if ( temp.getWidth() < 0d | temp.getWidth() > 999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(2,2)).strip().replaceAll(",",".");
            temp.setHeight(Double.parseDouble(buf));
            if ( temp.getHeight() < 0d | temp.getHeight() > 999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(3,2)).strip().replaceAll(",",".");
            temp.setMass(Double.parseDouble(buf));
            if ( temp.getMass() < 0d | temp.getMass() > 999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(4,2)).strip().replaceAll(",",".");
            temp.setMinFrq(Double.parseDouble(buf));
            if ( temp.getMinFrq() < 0d | temp.getMinFrq() > 9999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(5,2)).strip().replaceAll(",",".");
            temp.setMaxFrq(Double.parseDouble(buf));
            if ( temp.getMaxFrq() < 0d | temp.getMaxFrq() > 9999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(6,2)).strip().replaceAll(",",".");
            if (buf.equals("") | buf.equals("0")) {
                temp.setMinforce(0d);
            }
            else {
                temp.setMinforce(Double.parseDouble(buf));
            }
            if ( temp.getMinforce() < 0d | temp.getMinforce() > 9999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(7,2)).strip().replaceAll(",",".");
            temp.setMaxforce(Double.parseDouble(buf));
            if ( temp.getMaxforce() < 0d | temp.getMaxforce() > 9999999d) mistake = true;

            buf = ((String) t.getModel().getValueAt(8,2)).strip().replaceAll(",",".");
            if (buf.startsWith("±")) {
                buf = buf.substring(1);
            }
            if (buf.equals("") | buf.equals("0")) {
                temp.setGis(0d);
            }
            else {
                temp.setGis(Double.parseDouble(buf));
            }
            if ( temp.getGis() < 0d | temp.getGis() > 9999999d) mistake = true;

            Object ob = t.getModel().getValueAt(9,2);
            int d=0;
            if (ob instanceof Integer) {
                d = (int) ob;
            }
            if (ob instanceof String) {
                buf = (String)ob;
                buf  = buf.strip().replaceAll(",",".");
                d = Integer.parseInt(buf);
            }
            //buf = ((String) t.getModel().getValueAt(9,2)).strip().replaceAll(",",".");

            if (d == 1 | d == 2) {
                temp.setDin(d);
            }
            else {
                mistake = true;
            }
            // get CLAMP numb from table2 (prog parameters)
            ob = t.getModel().getValueAt(10,2);
            d=0;
            if (ob instanceof Integer) {
                d = (int) ob;
            }
            if (ob instanceof String) {
                buf = (String)ob;
                buf  = buf.strip().replaceAll(",",".");
                d = Integer.parseInt(buf);
            }
            if (d == 1 | d == 2) {
                temp.setClamp(d);
            }
            else {
                mistake = true;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            mistake = true;
        }

        if (mistake) return null;
        else return temp;
    }

    private void renewTable2() {
        if ( PROG.current == null ) {
            for (int i = 0; i < 11; i++) {
                table2.getModel().setValueAt("", i, 2);
            }
        }
        else {
            DecimalFormat df = new DecimalFormat("#.#");
            table2.getModel().setValueAt(df.format(PROG.current.getLength()), 0, 2);
            table2.getModel().setValueAt(df.format(PROG.current.getWidth()), 1, 2);
            table2.getModel().setValueAt(df.format(PROG.current.getHeight()), 2, 2);
            table2.getModel().setValueAt(df.format(PROG.current.getMass()), 3, 2);
            table2.getModel().setValueAt(df.format(PROG.current.getMinFrq()), 4, 2);
            table2.getModel().setValueAt(df.format(PROG.current.getMaxFrq()), 5, 2);
            if (!(PROG.current.getMinforce() == 0.0d)) {
                table2.getModel().setValueAt(df.format(PROG.current.getMinforce()), 6, 2);
            } else {
                table2.getModel().setValueAt("", 6, 2);
            }
            table2.getModel().setValueAt(df.format(PROG.current.getMaxforce()), 7, 2);
            if (!(PROG.current.getGis() == 0.0d)) {
                table2.getModel().setValueAt("±" + df.format(PROG.current.getGis()), 8, 2);
            } else {
                table2.getModel().setValueAt("", 8, 2);
            }
            table2.getModel().setValueAt(PROG.current.getDin(), 9, 2);
            table2.getModel().setValueAt(PROG.current.getClamp(), 10, 2);
        }
    }


    private void createUIComponents() {



        DefaultTableModel model = new DefaultTableModel(0,0){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[]{"№","Название"});


        table1 = new JTable(model);

        table1.getColumnModel().getColumn(0).setPreferredWidth(30);
        table1.getColumnModel().getColumn(1).setPreferredWidth(270);

        //PROG.getMockProgs();
        SQL.getSQLProgs();
        if (PROG.programs.length >0) {
            int i=0;
            for (String str :PROG.programs){
                model.addRow(new String[]{String.valueOf(++i),str});
            }
        }


        // the table for product parameters
        DefaultTableModel model2 = new DefaultTableModel(0,3){
            @Override
            public boolean isCellEditable(int i, int i1) {
                if (i1==2 & PROG.editCurrProg) {
                    return true;
                }
                else return false;
            }
        };
        model2.setColumnIdentifiers(new String[]{"Параметр","Размерность","Значение"});
        table2 = new JTable(model2);
        table2.getColumnModel().getColumn(0).setPreferredWidth(200);
        table2.getColumnModel().getColumn(1).setPreferredWidth(30);
        table2.getColumnModel().getColumn(2).setPreferredWidth(30);

        model2.addRow(new String[]{"Длинна","мм"});
        model2.addRow(new String[]{"Ширина","мм"});
        model2.addRow(new String[]{"Высота","мм"});
        model2.addRow(new String[]{"Масса детали","г"});
        model2.addRow(new String[]{"Минимальная частота","Гц"});
        model2.addRow(new String[]{"Максимальная частота","Гц"});
        model2.addRow(new String[]{"Усилие зажатия лопатки минимальное","кгс"});
        model2.addRow(new String[]{"Усилие зажатия лопатки максимальное","кгс"});
        model2.addRow(new String[]{"Допуск усилия зажатия лопатки","кгс"});
        model2.addRow(new String[]{"Выбор динамометра *",""});
        model2.addRow(new String[]{"Выбор гидрозажима **",""});

        // the table for measures
        DefaultTableModel model3 = new DefaultTableModel(T3rowsNumb,T3columnsNumb){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        model3.setColumnIdentifiers(new String[]{"№ лопатки","ЧСК","№ лопатки","ЧСК","№ лопатки","ЧСК","№ лопатки","ЧСК","№ лопатки","ЧСК"});


        table3 = new JTable(model3) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
               DefaultTableCellRenderer tcr = (DefaultTableCellRenderer) super.getCellRenderer(row, column);
               try {
                   if (column%2==1) {
                       int b = Alg.measures.get(column/2*T3rowsNumb + row);
                       if (PROG.checkMeasure(b)) {
                      // if (column > 5 & row > 5 | column == 2 & row == 2) {
                           tcr.setBackground(Color.orange);
                       } else {
                           tcr.setBackground(Color.white);
                       }
                   }
                   else {
                       tcr.setBackground(Color.white);
                   }
               }
               catch (Exception e) {
                   tcr.setBackground(Color.white);
               }
                return tcr;
            }
        };


    }

    public void resizeTable1(int rows){
        table1.setPreferredSize(new Dimension(300,rows*table1.getRowHeight()));
        table1.setPreferredScrollableViewportSize(new Dimension(300,rows*table1.getRowHeight()));
        table1.getColumnModel().getColumn(0).setPreferredWidth(30);
        table1.getColumnModel().getColumn(1).setPreferredWidth(270);
        table1.repaint();
    }





}
