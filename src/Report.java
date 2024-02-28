import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Report {
    static public String tempName ="protocolFSO.docx";
    static public String folderIn = "C:\\src\\";
    static public Path desktop = Paths.get(System.getenv("USERPROFILE") + "\\Desktop");
    static public String folderOut = desktop.toString()+"\\Отчеты\\";
    static public String lastName;
    static public int fieldCount = 145;


    static boolean save(){
        boolean result = false;
        try{
            int i = 0;
            XWPFDocument doc = new XWPFDocument(new FileInputStream(folderIn+tempName));
            List<XWPFTable> tables = doc.getTables();
            XWPFTable tbl = tables.get(0);
            List<XWPFTableRow> rowlist = tbl.getRows();
            XWPFTableRow row;
            XWPFTableCell cell;
            for (int j = 0; j < Alg.measures.size(); j++) {
                int colind = j / WindowMain.T3rowsNumb;
                int rowind =2+ j - colind*WindowMain.T3rowsNumb;
                row = tbl.getRow(rowind);
                cell = row.getCell(colind*2+1);
                cell.setText(String.valueOf(Alg.measures.get(j)));
                cell = row.getCell(colind*2);
                cell.setText(String.valueOf(j+1));
            }
            // MINIMUM AND MAXIMUM MEASURES
            row = tbl.getRow(31);
            cell = row.getCell(0);
            cell.setText(String.valueOf(Alg.minMeasure));
            cell = row.getCell(1);
            cell.setText(String.valueOf(Alg.maxMeasure));
            // NAME OF THE PRODUCT
            String name = null;
            if (PROG.current!= null) {
                name = PROG.current.getName();
            }
            else {
                name = "лопатки";
            }
            row = tbl.getRow(0);
            cell = row.getCell(0);
            for (XWPFParagraph p : cell.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    String text = r.getText(0);
                    if (text != null && text.equals("NAME")) {
                        text = text.replace("NAME",name);
                        r.setText(text, 0);
                    }
                }
            }
            //cell.getText().replaceAll("NAME", "НАЗВАНИЕ ЛОПАТКИ");

           // DateFormat df = new SimpleDateFormat("dd.MM.yy_HH.mm");
            DateFormat df = new SimpleDateFormat("HH_mm");
            lastName =folderOut +  df.format( Calendar.getInstance().getTime())+tempName;
            FileOutputStream fo = new FileOutputStream(lastName);
            doc.write(fo);
            doc.close();
            fo.close();
            result = true;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  boolean add (int value) {
        boolean result = false;
        if (Alg.measures.size()<fieldCount) {
            try {
                Alg.measures.add(value);
                Main.mainFrame.windowMain.T3CellSet(Alg.measures.size() - 1, String.valueOf(Alg.measures.size()), String.valueOf(value));

                Alg.minMeasure = Alg.countMinMeasure();
                Alg.maxMeasure = Alg.countMaxMeasure();
                Main.mainFrame.windowMain.minlbl.setText(String.valueOf(Alg.minMeasure));
                Main.mainFrame.windowMain.maxlbl.setText(String.valueOf(Alg.maxMeasure));
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    public void cancel() {
        int delInt = Alg.measures.size()-1;
        Alg.measures.remove(delInt);
        Main.mainFrame.windowMain.T3CellSet(delInt,"","");
        if (Alg.measures.size()>0) {
            Alg.minMeasure = Alg.countMinMeasure();
            Alg.maxMeasure = Alg.countMaxMeasure();
            Main.mainFrame.windowMain.minlbl.setText(String.valueOf(Alg.minMeasure));
            Main.mainFrame.windowMain.maxlbl.setText(String.valueOf(Alg.maxMeasure));
        }
        else {
            Alg.minMeasure = 0;
            Alg.maxMeasure = 0;
            Main.mainFrame.windowMain.minlbl.setText("");
            Main.mainFrame.windowMain.maxlbl.setText("");
        }
    }
}

/*            for (XWPFTable tbl : doc.getTables()) {
                for (XWPFTableRow row : tbl.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            i++;
                            p.createRun().setText(String.valueOf(i));
                        }
                    }
                }
            }*/
