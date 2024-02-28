import org.joda.time.LocalDateTime;

import javax.swing.*;
import java.sql.*;

import java.util.ArrayList;

public class SQL {

    public static Date getLastDate(int i) {

        Connection connection = null;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/service_log",
                    "app", "admin");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Date date = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT MAX(date) FROM service_log.s"+ i);
            results.next();
            date = results.getDate(1);
            //System.out.println("success!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static int setNewDate(int i) {   // INSERT INTO REPORT ( NUMBER, FSO ) VALUES (" + numb + " , " + fso + ")";
        Connection connection = null;
        int result = 0;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/service_log",
                    "app", "admin");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Date date = new Date(System.currentTimeMillis());
        Statement statement = null;
        try {
            statement = connection.createStatement();
            //String sql = "INSERT INTO service_log.S" + i + "( DATE ) VALUES (" + date + ")";
            String sql = "INSERT INTO `service_log`.`s" + i +"` (`date`) VALUES ('" + date + "');";
            result = statement.executeUpdate( sql);
            //if (result == 1) System.out.println("success!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    static void getSQLProgs() {
        Connection connection = connect_FSO();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT NAME FROM PROG3");
            ArrayList<String> buf = new ArrayList<>();
            while (results.next()) {
                buf.add(results.getString(1));
            }
            PROG.programs = buf.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        close_FSO(connection,statement);
    }

    static void loadCurrProg (int id) {
        Connection connection = connect_FSO();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM PROG3 WHERE id = "+ id);
            result.next();
            PROG.current = new PROG(result.getInt(1),
                                    result.getString(2),
                                    result.getDouble(3),
                                    result.getDouble(4),
                                    result.getDouble(5),
                                    result.getDouble(6),
                                    result.getDouble(7),
                                    result.getDouble(8),
                                    result.getDouble(9),
                                    result.getDouble(10),
                                    result.getDouble(11),
                                    result.getInt(12),
                                    result.getInt(13)); // clamp

        } catch (SQLException e) {
            e.printStackTrace();
        }



        close_FSO(connection,statement);
    }

    static Connection connect_FSO () {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/fso_prog",
                    "app", "admin");
            return connection;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void close_FSO (Connection con, Statement st) {
        try {
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean saveProg() {
        boolean ret = false;
        Connection connection = connect_FSO();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            // update an existing record
            String query = "UPDATE PROG3 SET " +
                    "NAME = \"" + PROG.current.getName() + "\", " +
                    "LENGTH = " + PROG.current.getLength() + ", " +
                    "WIDTH = " + PROG.current.getWidth() + ", " +
                    "HEIGHT = " + PROG.current.getHeight() + ", " +
                    "MASS = " + PROG.current.getMass() + ", " +
                    "MIN_FRQ = " + PROG.current.getMinFrq() + ", " +
                    "MAX_FRQ = " + PROG.current.getMaxFrq() + ", " +
                    "POWER_MIN = " + PROG.current.getMinforce() + ", " +
                    "POWER_MAX = " + PROG.current.getMaxforce() + ", " +
                    "GIS = " + PROG.current.getGis() + ", " +
                    "DIN = " + PROG.current.getDin() + ", " +
                    "CLAMP = " + PROG.current.getClamp() + " " +
                    "WHERE PROG3.ID = " + PROG.current.getId() + " ;";

            int result = statement.executeUpdate(query);
            ret = (result == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Нет подключения к базе данных", "Сохранение программы",0);
        }

        close_FSO(connection,statement);
        return ret;
    }

    public static boolean addNewProg() {
        boolean ret = false;
        Connection connection = connect_FSO();
        Statement statement = null;
        int id = PROG.programs.length +1;
        try {
            statement = connection.createStatement();
            // update an existing record
            String query = "INSERT INTO PROG3 (id, name, length, width, height, mass, min_frq, max_frq, power_min, power_max, gis, din, clamp) VALUES ("+
                    id + ", \"Новая программа\" , 0,0,0,0,0,0,0,0,0,1,1 )"; // clamp
            int result = statement.executeUpdate(query);
            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Нет подключения к базе данных", "Создание программы",0);
        }

        close_FSO(connection,statement);
        return ret;
    }

    public static void reorderIndexes(int delInd) {
        Connection connection = connect_FSO();
        Statement statement = null;
        try {
            statement = connection.createStatement();

                String query = "UPDATE PROG3 SET id = id - 1 WHERE id > "+ delInd;
                int result = statement.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        close_FSO(connection,statement);
    }

    public static void delete(int ind) {
        Connection connection = connect_FSO();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query = "DELETE FROM PROG3 WHERE ID = " + ind;
            int result = statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        close_FSO(connection,statement);
    }

    public static boolean RepoAdd(int numb, int fso) {
        Connection connection = connect_FSO();
        Statement statement = null;
        boolean res= false;
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO REPORT ( NUMBER, FSO ) VALUES (" + numb + " , " + fso + ")";
            int result = statement.executeUpdate(query);
            System.out.println(result);
            if (result ==1) res = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close_FSO(connection,statement);
        return res;
    }

    public static boolean RepoDrop() {
        Connection connection = connect_FSO();
        Statement statement = null;
        boolean res= false;
        try {
            statement = connection.createStatement();
            String query = "DELETE FROM REPORT WHERE ID > 0";
            int result = statement.executeUpdate(query);
            System.out.println(result);
            if (result !=-1) res = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close_FSO(connection,statement);
        return res;
    }



}
