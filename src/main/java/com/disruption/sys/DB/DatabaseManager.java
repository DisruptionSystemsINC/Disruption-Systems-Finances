package com.disruption.sys.DB;

import com.disruption.sys.Main;
import com.disruptionsystems.logging.LogLevel;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DatabaseManager {

    private Connection conn;

    public Connection establishConnection(String databaseAddress) {
        try {
            conn = DriverManager.getConnection(databaseAddress);
            if (conn != null){
                return conn;
            }
        } catch (SQLException e){
            Main.getLogger().printToLog(LogLevel.ERROR, "COULD NOT CONNECT TO DATABASE: " + e.getMessage());
        }
        return null;
    }

    public Connection getConnection() {return this.conn;}

    private ResultSet getResultSet(String sql, Connection conn) {
        ResultSet set;
        try {
            Statement statement = conn.createStatement();
            set = statement.executeQuery(sql);
            return set;
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED," + e.getMessage());
        }
        return null;
    }

    private void setStatement() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS FINANCES (pos VARCHAR NOT NULL, value FLOAT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL)");
            statement.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }

    public String getPosField(String pos) {
        ResultSet rs = getResultSet("SELECT * FROM FINANCES WHERE POS='" + pos + "'", conn);
        String result = "";
        try {
            result = rs.getString("pos");
            rs.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return result;
    }

    public void delById(String Id) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM FINANCES WHERE id='" + Id + "'");
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }

    public void delByPos(String pos) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM FINANCES WHERE POS='" + pos + "'");
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }

    public float getValue(String pos) {
        ResultSet rs = getResultSet("SELECT * FROM FINANCES WHERE POS='" + pos + "'", conn);
        float result = 0.00f;
        try {
            result = rs.getFloat("value");
            rs.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return result;
    }

    public String[] getValues() {
        ResultSet rs = getResultSet("SELECT value FROM FINANCES", conn);
        try {
            List<String> result = new ArrayList<>();
            while (rs.next()){
                result.add(rs.getString("value"));
            }
            rs.close();
            return result.toArray(new String[0]);
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return null;
    }

    public String[] getPositions() {
        ResultSet rs = getResultSet("SELECT pos FROM FINANCES", conn);
        try {
            List<String> result = new ArrayList<>();
            while (rs.next()){
                result.add(rs.getString("pos"));
            }
            rs.close();
            return result.toArray(new String[0]);
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return null;
    }

    public String[] getIds() {
        ResultSet rs = getResultSet("SELECT id FROM FINANCES", conn);
        try {
            List<String> result = new ArrayList<>();
            while (rs.next()){
                result.add(rs.getString("id"));
            }
            rs.close();
            return result.toArray(new String[0]);
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return null;
    }

    public Date[] getDates() {
        ResultSet rs = getResultSet("SELECT date FROM FINANCES", conn);
        try {
            List<Date> result = new ArrayList<>();
            while (rs.next()){
                result.add(rs.getDate("date"));
            }
            rs.close();
            return result.toArray(new Date[0]);
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return null;
    }

    public void createBaseDatabaseStructure(){
        this.setStatement();
    }

    public void addEntry(String pos, float value, Date date){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO finances ('pos', value, date) VALUES (?, ?, ?)");
            statement.setString(1, pos);
            statement.setFloat(2, value);
            statement.setDate(3, date);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }
}
