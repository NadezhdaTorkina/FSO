import java.awt.*;
import java.nio.file.Path;

public class ZVUK extends Thread{


    static boolean connected = false;
    static boolean getmeasure;
    static int lastmeasure;
    static int[][] range = new int[16][2];
    static double[] cFrq = new double[16];
    static int curRange;
    static {
        range[0][0] = 22;range[0][1] = 40;
        range[1][0] = 33;range[1][1] = 60;
        range[2][0] = 50;range[2][1] = 90;
        range[3][0] = 75;range[3][1] = 134;
        range[4][0] = 112;range[4][1] = 202;
        range[5][0] = 168;range[5][1] = 302;
        range[6][0] = 252;range[6][1] = 454;
        range[7][0] = 378;range[7][1] = 681;
        range[8][0] = 567;range[8][1] = 1021;
        range[9][0] = 851;range[9][1] = 1531;
        range[10][0] = 1276;range[10][1] = 2297;
        range[11][0] = 1914;range[11][1] = 3445;
        range[12][0] = 2871;range[12][1] = 5168;
        range[13][0] = 4307;range[13][1] = 7752;
        range[14][0] = 6460;range[14][1] = 11628;
        range[15][0] = 9690;range[15][1] = 17441;

        cFrq[0] = 29.7;
        cFrq[1] = 44.5;
        cFrq[2] = 66.8;
        cFrq[3] = 100.2;
        cFrq[4] = 150.3;
        cFrq[5] = 225.4;
        cFrq[6] = 338.2;
        cFrq[7] = 507.2;
        cFrq[8] = 760.9;
        cFrq[9] = 1141.3;
        cFrq[10] = 1711.9;
        cFrq[11] = 2567.9;
        cFrq[12] = 3851.9;
        cFrq[13] = 5777.8;
        cFrq[14] = 8666.7;
        cFrq[15] = 13000.0;


       // System.load(Path.of("src\\audio203api.h").toAbsolutePath().toString());
        //System.load(Path.of("src\\audio203api.cpp").toAbsolutePath().toString());
        //System.load(Path.of("src\\audio203api.dll").toAbsolutePath().toString());
        //System.load(Path.of("src\\usb_cmd.h").toAbsolutePath().toString());

        //System.load(Path.of("C:\\Users\\n.torkina\\Downloads\\jlibmodbus-master\\FSO\\src\\ZVUK2.dll").toAbsolutePath().toString());
        //System.load(Path.of("C:\\src\\ZVUK2.dll").toAbsolutePath().toString());
        System.loadLibrary("ZVUK2");

    }
    native void printHelloWorld();
    native int[] readLastMeasure();
    native int open();
    native void close(int i);
    native int getMeasure(int i, int r);

    public static int getRange (int lowFrq, int highFrq) {
        double median = (double)( highFrq + lowFrq )/2.0d;
        double min_dist = Math.abs(median - cFrq[0]);
        int result = 0;
        for (int i = 1; i < 16; i++) {
            if (Math.abs(median - cFrq[i]) < min_dist) {
                min_dist = Math.abs(median - cFrq[i]);
                result = i;
            }
        }
        return result;
    }

    @Override
    public void run() {
        // получение ЧСК с прибора
        if (Main.zvukPointer == 0) {
            Main.mainFrame.windowMain.zvukLbl.setBackground(Color.red);
            Main.zvukPointer = this.open();
        }
        if (Main.zvukPointer > 0) {
            Main.mainFrame.windowMain.zvukLbl.setBackground(Main.DARK_GREEN);
            //frqLbl.setText("0000000");
            int tries = 0;
            //while ( tries < )
            Alg.lastMeasure = this.getMeasure(Main.zvukPointer,ZVUK.curRange);
            tries ++;
            if (Alg.lastMeasure > 0) {
                Main.mainFrame.windowMain.frqLbl.setText(String.valueOf(Alg.lastMeasure));
                Alg.FSOsuccess = true;
            }
            else {
                if (Alg.lastMeasure == -5) Main.mainFrame.windowMain.frqLbl.setText("Ошибка");
                if (Alg.lastMeasure == -1) Main.mainFrame.windowMain.frqLbl.setText("Ошибка");
                if (Alg.lastMeasure == -2) Main.mainFrame.windowMain.frqLbl.setText("Ошибка");
                if (Alg.lastMeasure == -3) Main.mainFrame.windowMain.frqLbl.setText("Ошибка");
                if (Alg.lastMeasure == -6) Main.mainFrame.windowMain.frqLbl.setText("Вне диапозона");
                if (Alg.lastMeasure == -4) Main.mainFrame.windowMain.frqLbl.setText("Ошибка");
            }
            this.close(Main.zvukPointer);
            Main.zvukPointer = 0;
            Alg.runFSOmeasure = false;
            this.interrupt();
        }
    }
}
