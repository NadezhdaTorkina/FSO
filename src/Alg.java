import java.awt.*;
import java.util.ArrayList;

public class Alg extends Thread{

    public static boolean PresHHErr;
    public static boolean FilterErr;
    public static boolean TempOilErr;
    static int control;
    static int autoSet;
    static boolean Hand; //2
    static boolean Auto = true; //1
    static boolean AutoStartCmd; //3
    static boolean HandLoPumpSpeed; //4
    static boolean HandDrainValve; //5
    static boolean HandFeedValve; // 6
    static boolean HandReturnValve; //7
    static boolean propHandCmd; //8
    static boolean HandRegCmd;  //9
    static boolean HandFrqCmd;  // 10 уставка задания частоты насоса вручную
    // 11 - ошибка связи с динамометром
    static boolean errReset;    //12
    static boolean FSOsuccess; // 13
    static boolean extraClamp; // 14  CLAMP from old facility
    static boolean HandClampValve; // 15 clamp hand on cmd

    static final String S0 = " ";  // выключен вообще режим АВТО
    static final String S1 = " НАЖМИТЕ ПЕДАЛЬ ";
    static final String S2 = " НАБОР ДАВЛЕНИЯ ";
    static final String S3 = " ИЗМЕРЕНИЕ ЧСК ";
    static final String S4 = " ЧСК ЗАПИСАНО: ";
    static final String S5 = " ВЫКЛЮЧЕНО ";  // режим АВТО но нажали стоп
    static final String S6 = " ЧСК НЕ ЗАПИСАНО "; // не удалось записать ЧСК в автоматическом режиме

    static int stage = 0;
    static boolean enHandFsoMeasure; // разрешение измерения ЧСК в ручном режиме

    public static Color colorStage(int i) {
        switch (i) {
            case 0: return Color.LIGHT_GRAY;
            case 1: return Color.LIGHT_GRAY;
            case 2: return Color.yellow;
            case 3: return Color.CYAN;
            case 4: return Color.green;
            case 5: return Color.LIGHT_GRAY;
            case 6: return Color.ORANGE;
        }
        return Color.LIGHT_GRAY;
    }

    static String printStage(int i) {
        switch (i) {
            case 0: return S0;
            case 1: return S1;
            case 2: return S2;
            case 3: return S3;
            case 4: return S4 + String.valueOf(Alg.lastMeasure)+" Гц";
            case 5: return S5;
            case 6: return S6;
        }
        return "";
    }


    static boolean FsoIsWritten;
    static double propHandSet = 0.0d; // уставка положения пропорционального клапана в ручном режиме
    static double RegHandSet = 0.0d;  // уставка регулятора в ручном режиме
    static double FrqHandSet = 25.0d; // уставка частоты вращения двигателя вручную
    static double maxPres = 10000.0d/9.80665d; // диапозон работы датчика в кгс
    static double maxFrq = 50.0d;  // максимальная частота вращения двигателя
    static double minFrq = 5.0d;  // минимальная частота вращения
    static double propAutoSet;
    static int id = -1;
    static int lastMeasure;
    static ArrayList<Integer> measures;

    static {
        measures = new ArrayList<>();
       // for (int i = 0; i < 80; i++) {
        //    measures.add(100+i);}
    }
    static int minMeasure;
    static int maxMeasure;


    static boolean do100_00; // разрешение включения гидростанции
    static boolean do100_01; // низкая скорость насоса
    static boolean do100_02; // клапан сливной
    static boolean do100_03; // гидрораспределитель подачи
    static boolean do100_04; // включить клапан обратной подачи ( разгрузка )
    static boolean do100_05; // включить клапан на доп зажим CLAMP
    static double DD1 = 0d;  // давление в гидростанции, от ПЛК
    static boolean DD1Error = false;  // ошибка преобразования данных
    static boolean PresIsSet = false;  // давление вышло на уставку
    static boolean filterNC = false;   // 0.05 контроль масляного фильтра
    static boolean oilTempCont = false; // 0.06 контроль температуры масла
    static boolean stationIsOn = false; // 0.10 контроль работы гидростанции
    static boolean teststart;

    static boolean runFSOmeasure;

    @Override
    public void run() {
        while (true) {

            enHandFsoMeasure = (Alg.stage == 6);

            if (!Alg.stationIsOn ) {
                Hand = false;
                Auto = false;
            }
            else {
                if (!Hand) {
                    Auto = true;
                }
            }
            if (!Hand) {
                HandLoPumpSpeed = false;
                HandDrainValve = false;
                HandFeedValve = false;
                HandReturnValve = false;
                propHandCmd = false;
                HandRegCmd = false;
                propHandSet = 0.0d;
                RegHandSet = 0.0d;
                HandFrqCmd = false;
                FrqHandSet = 50.0d;
                HandClampValve = false;
            }
            if (!Auto) {
                AutoStartCmd = false;
            }

            synchronized (this){
                control = (Auto ? 2 : 0) + (Hand ? 4 : 0) + (AutoStartCmd ? 8 : 0) + (HandLoPumpSpeed ? 16 : 0) + (HandDrainValve ? 32 : 0) +
                        (HandFeedValve ? 64 : 0) + (HandReturnValve ? 128 : 0) + (propHandCmd ? 256 : 0) + (HandRegCmd ? 512 : 0) + (HandFrqCmd ? 1024 : 0) +
                        (Main.din2.commErr ? 2048 : 0) + (errReset ? 4096 : 0) + (FSOsuccess? 8192:0) + (extraClamp? 16384:0) + (HandClampValve? 32768:0);   // CLAMP!!!!

                if (errReset) {
                    errReset = false;
                }
            }

            if ( (Auto & AutoStartCmd & PresIsSet & !FSOsuccess) ){ // | teststart ) {
                // начать считывание ЧСК до того как не будет успешного измерения
                if (!runFSOmeasure ) {
                    runFSOmeasure = true;
                    ZVUK.curRange = PROG.autoFrqRange;
                    new ZVUK().start();
                }
            }

            if (FSOsuccess) {
                if (!FsoIsWritten) {
                    Main.repo.add(Alg.lastMeasure);

                    FsoIsWritten = true;
                }
                teststart = false;
            }

            if (!PresIsSet ){   // | !teststart) {
                FsoIsWritten = false;
                FSOsuccess = false;
            }
            // CLAMP
            if (PROG.current != null) {
                if (PROG.current.getClamp() == 2) {
                    extraClamp = true;
                }
                else {
                    extraClamp = false;
                }
            }
            else {
                extraClamp = false;
            }


            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int countMinMeasure(){
        int result = 0;
        if (measures != null) {
            result = measures.get(0);
            for (int i = 0; i < measures.size(); i++) {
                if(result > measures.get(i)) {
                    result = measures.get(i);
                }
            }
        }
        return result;
    }

    public static int countMaxMeasure() {
        int result = 0;
        if (measures != null) {
            for (int i = 0; i < measures.size(); i++) {
                if(result < measures.get(i)) {
                    result = measures.get(i);
                }
            }
        }
        return result;
    }




}
