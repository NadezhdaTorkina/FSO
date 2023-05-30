import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;

public class Main {

    static final public int Din1_adr = 2;
    static final public int Din2_adr = 6;
    static boolean dinChoice = false;
    static Color DARK_GREEN = new Color(2,155,2);
    static int zvukPointer=0;


    static ModbusRTU modbusRTU;
    static MainFrame mainFrame;
    static RTU din1;
    static RTU din2;
    static Report repo;
    static Alg alg;
    static ZVUK zvuk;
    static ZVUKThread zvukThread;
    static boolean serviceIsDone;
    public static void main(String[] args) {
        Frame frame = new Frame();
        while (!serviceIsDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.dispose();
        //Toolkit.getDefaultToolkit().beep();

        modbusRTU = new ModbusRTU(5);
        modbusRTU.start();

        //din1 = new RTU(Din1_adr, "COM25"); // адрес 2 для малого диапозона
        //din1.start();

        din2 = new RTU(Din2_adr, "COM25"); // адрес 6 на стенде на наладке, большой диапозон
        din2.start();



        mainFrame = new MainFrame();

        zvuk = new ZVUK();
        //zvuk.printHelloWorld();
        //int[] res = zvuk.readLastMeasure();
        try {
            zvukPointer = zvuk.open();
            System.out.println("ZVUK open: " + zvukPointer);
            if (zvukPointer > 0) {
                mainFrame.windowMain.zvukLbl.setBackground(DARK_GREEN);
            }
            zvuk.close(zvukPointer);
            zvukPointer = 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Measure: "+zvuk.getMeasure(zvukPointer));
        //System.out.println(Arrays.toString(res));

        zvukThread = new ZVUKThread();
        zvukThread.start();

        repo = new Report();

        alg = new Alg();
        alg.start();

        //repo.add(666);


        while (true){

            controlMW();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mainFrame.windowMain.resizeflag) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainFrame.windowMain.scrolltable1.getVerticalScrollBar().setValue(mainFrame.windowMain.scrolltable1.getVerticalScrollBar().getMaximum());
                mainFrame.windowMain.resizeflag = false;
            }

        }

    }

    private static void controlMW() {  // processed in infinite loop

        try {
            //mainFrame.windowMain.Din1weight.setText(String.valueOf(din1.weight));
            mainFrame.windowMain.Din2weight.setText(String.format("%.3f",din2.weight));
            mainFrame.windowMain.Din2Weight2.setText(String.format("%.1f",din2.weight2));
            //mainFrame.windowMain.comm1lbl.setText((din1.commOk&!din1.commErr)?"ОК":"Ошибка");
            mainFrame.windowMain.comm2lbl.setText((din2.commOk&!din2.commErr)?"ОК":"Ошибка");


            if (din2.diaposone != 2 & din2.diaposone != 1) {
                mainFrame.windowMain.dinTypeLbl.setText( "Динамометр не найден");
            }
            else {
                if (din2.diaposone == 1) mainFrame.windowMain.dinTypeLbl.setText("Динамометр 0..10кН");
                if (din2.diaposone == 2) mainFrame.windowMain.dinTypeLbl.setText("Динамометр 0..125кН");
            }
            //mainFrame.windowMain.valueRangelbl.setText((din2.diaposone == 1)?"Показания, Н":"");
            mainFrame.windowMain.valueRangelbl.setText((din2.diaposone == 2)?"Показания, кН":"Показания, Н");

            mainFrame.windowMain.dinIsConnLbl.setBackground(din2.commOk?DARK_GREEN:Color.red);
        } catch (Exception e) {

        }
        mainFrame.windowMain.COLORLbl.setBackground(Alg.colorStage(Alg.stage));
        mainFrame.windowMain.STAGElbl.setText(Alg.printStage(Alg.stage));

        //mainFrame.windowMain.presIsSetpnl.setOpaque(Alg.PresIsSet);
        mainFrame.windowMain.rangeLbl.setText("0.."+String.format("%.1f",Alg.maxPres)+" кгс");

        mainFrame.windowMain.HandLbl.setText(Alg.Hand?" АКТИВЕН ":" НЕ АКТИВЕН ");
        mainFrame.windowMain.AutoLbl.setText(Alg.Auto?" АКТИВЕН ":" НЕ АКТИВЕН ");
        mainFrame.windowMain.ПУСКButton.setEnabled(Alg.Auto);
        mainFrame.windowMain.stopautobtn.setEnabled(Alg.Auto);

        mainFrame.windowMain.AutoSetLbl.setText(String.valueOf(PROG.autoPresSet));
        mainFrame.windowMain.AutoRangeLbl.setText(String.valueOf(PROG.autoFrqRange));

        mainFrame.windowMain.do1onbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do10ffbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do2onbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do2offbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do3onbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do3offbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do4onbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.do4offbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.ao1onbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.ao1offbtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.hRegOn.setEnabled(Alg.Hand);
        mainFrame.windowMain.hRegOff.setEnabled(Alg.Hand);
        mainFrame.windowMain.frqHandOnBtn.setEnabled(Alg.Hand);
        mainFrame.windowMain.FrqHandOffBtn.setEnabled(Alg.Hand);

        mainFrame.windowMain.hRegTextDecBtn.setEnabled(Alg.HandRegCmd);
        mainFrame.windowMain.hRegTextIncBtn.setEnabled(Alg.HandRegCmd);
        mainFrame.windowMain.hRegText.setEnabled(Alg.HandRegCmd);

        mainFrame.windowMain.buttonDEC.setEnabled(Alg.propHandCmd);
        mainFrame.windowMain.buttonINC.setEnabled(Alg.propHandCmd);
        mainFrame.windowMain.a00TextField.setEnabled(Alg.propHandCmd);

        mainFrame.windowMain.OUT4LBL.setText(Alg.propHandCmd?"ВКЛЮЧЕН":"ВЫКЛЮЧЕН");
        mainFrame.windowMain.OUT4LBL.setForeground(Alg.propHandCmd?DARK_GREEN:Color.BLACK);

        mainFrame.windowMain.hReglbl.setText(Alg.HandRegCmd?"ВКЛЮЧЕН":"ВЫКЛЮЧЕН");
        mainFrame.windowMain.hReglbl.setForeground(Alg.HandRegCmd?DARK_GREEN:Color.BLACK);
        if (Alg.HandRegCmd&Alg.PresIsSet){
        mainFrame.windowMain.hReglbl.setText("УСТАНОВЛЕНО");}
        mainFrame.windowMain.PLCLbl.setBackground(modbusRTU.connectPLC ? DARK_GREEN : Color.red);
        mainFrame.windowMain.err1Pnl.setVisible(Alg.PresHHErr);
        if (!modbusRTU.connectPLC) {

            mainFrame.windowMain.OUT0LBL.setText("***");
            mainFrame.windowMain.OUT1LBL.setText("***");
            mainFrame.windowMain.OUT2LBL.setText("***");
            mainFrame.windowMain.OUT3LBL.setText("***");
            mainFrame.windowMain.DO0LBL.setText("***");
            mainFrame.windowMain.preslbl.setText("***");
            mainFrame.windowMain.pumpIsOnLbl.setText("***");

            mainFrame.windowMain.OUT0LBL.setForeground(Color.GRAY);
            mainFrame.windowMain.OUT1LBL.setForeground(Color.GRAY);
            mainFrame.windowMain.OUT2LBL.setForeground(Color.GRAY);
            mainFrame.windowMain.OUT3LBL.setForeground(Color.GRAY);
            mainFrame.windowMain.DO0LBL.setForeground(Color.GRAY);
            mainFrame.windowMain.preslbl.setForeground(Color.GRAY);
            mainFrame.windowMain.pumpIsOnLbl.setForeground(Color.GRAY);
        }
        else {
            mainFrame.windowMain.OUT0LBL.setText(Alg.do100_01?"ВКЛЮЧЕН":"ВЫКЛЮЧЕН");
            mainFrame.windowMain.OUT1LBL.setText(Alg.do100_02?"ВКЛЮЧЕН":"ВЫКЛЮЧЕН");
            mainFrame.windowMain.OUT2LBL.setText(Alg.do100_03?"ВКЛЮЧЕН":"ВЫКЛЮЧЕН");
            mainFrame.windowMain.OUT3LBL.setText(Alg.do100_04?"ВКЛЮЧЕН":"ВЫКЛЮЧЕН");
            mainFrame.windowMain.DO0LBL.setText(Alg.do100_00?"РАЗРЕШЕНО":"ЗАПРЕЩЕНО");
            mainFrame.windowMain.pumpIsOnLbl.setText(Alg.stationIsOn?"ВКЛЮЧЕНА":"ВЫКЛЮЧЕНА");

            //mainFrame.windowMain.zvukLbl.setBackground(ZVUK.connected ? DARK_GREEN : Color.red);


            mainFrame.windowMain.OUT0LBL.setForeground(Alg.do100_01?DARK_GREEN:Color.BLACK);
            mainFrame.windowMain.OUT1LBL.setForeground(Alg.do100_02?DARK_GREEN:Color.BLACK);
            mainFrame.windowMain.OUT2LBL.setForeground(Alg.do100_03?DARK_GREEN:Color.BLACK);
            mainFrame.windowMain.OUT3LBL.setForeground(Alg.do100_04?DARK_GREEN:Color.BLACK);
            mainFrame.windowMain.DO0LBL.setForeground(Alg.do100_00?DARK_GREEN:Color.BLACK);
            mainFrame.windowMain.pumpIsOnLbl.setForeground(Alg.stationIsOn?DARK_GREEN:Color.BLACK);



            mainFrame.windowMain.preslbl.setText(String.valueOf(Alg.DD1));
            mainFrame.windowMain.preslbl.setForeground(Alg.DD1Error?Color.RED:Color.BLACK);


        }


    }
}
