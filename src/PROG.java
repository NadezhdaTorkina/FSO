import java.util.List;

public class PROG {
    static String [] programs = new String[]{"empty"};
    static boolean editCurrProg = false;
    static PROG current;
    static void getMockProgs() {
        programs = new String[5];
        programs[0] = "prog1";
        programs[1] = "prog2";
        programs[2] = "prog3";
        programs[3] = "prog4";
        programs[4] = "prog5";
    }

    public static boolean checkMeasure(int lastMeasure) {
        if (PROG.current != null) {
            if (lastMeasure > PROG.current.maxFrq | lastMeasure < PROG.current.minFrq) {
                return true;
            } else return false;
        }
        else {
               return false;
        }

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMinFrq() {
        return minFrq;
    }

    public void setMinFrq(double minFrq) {
        this.minFrq = minFrq;
    }

    public double getMaxFrq() {
        return maxFrq;
    }

    public void setMaxFrq(double maxFrq) {
        this.maxFrq = maxFrq;
    }

    public double getMinforce() {
        return minforce;
    }

    public void setMinforce(double minforce) {
        this.minforce = minforce;
    }

    public double getMaxforce() {
        return maxforce;
    }

    public void setMaxforce(double maxforce) {
        this.maxforce = maxforce;
    }

    public double getGis() {
        return gis;
    }

    public void setGis(double gis) {
        this.gis = gis;
    }

    public int getDin() {
        return din;
    }

    public void setDin(int din) {
        this.din = din;
    }

    public int getClamp() {
        return clamp;
    }

    public void setClamp(int clamp) {
        this.clamp = clamp;
    }

    // data structure read from data base about a program
    private int id;
    private String name;
    private double length;
    private double width;
    private double height;
    private double mass;
    private double minFrq;
    private double maxFrq;
    private double minforce;
    private double maxforce;
    private double gis;
    private int din; // 1 - low scale 2 - high scale
    private int clamp;




    public PROG(int id, String name, double length, double width, double height, double mass, double minFrq, double maxFrq, double minforce, double maxforce, double gis, int din, int clamp) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.minFrq = minFrq;
        this.maxFrq = maxFrq;
        this.minforce = minforce;
        this.maxforce = maxforce;
        this.gis = gis;
        this.din = din;
        this.clamp = clamp;
    }

    public PROG (int id, String name) {
        this.id = id;
        this.name = name;
        this.length = 0;
        this.width = 0;
        this.height = 0;
        this.mass = 0;
        this.minFrq = 0;
        this.maxFrq = 0;
        this.minforce = 0;
        this.maxforce = 0;
        this.gis = 0;
        this.din = 0;
        this.clamp = 1;
    }

    static List<PROG> allProgs;

    static double autoPresSet;
    static double autoPresGis;
    static int autoFrqRange;

    static int AutoSet(){
        if (current == null) return -1;

        if (current.getMaxforce()!= 0 & current.getMinforce() == 0 & current.getGis() == 0) {
            autoPresSet = current.getMaxforce();
            autoPresGis = current.getMaxforce()*0.1;
        }
        if (current.getMaxforce()!= 0 & current.getMinforce() != 0 & current.getGis() == 0) {
            autoPresSet = (current.getMaxforce()+current.getMinforce())/2.0;
            autoPresGis = autoPresSet - current.getMinforce();
        }
        if (current.getMaxforce()!= 0 & current.getMinforce() == 0 & current.getGis() != 0) {
            autoPresSet = current.getMaxforce();
            autoPresGis = current.getGis();
        }

        autoFrqRange = ZVUK.getRange((int)current.minFrq,(int)current.maxFrq);
        return 0;
    }

    static void ZeroAutoSet() {
        autoPresSet = 0;
        autoPresGis = 0;
        autoFrqRange = 0;
    }

}
