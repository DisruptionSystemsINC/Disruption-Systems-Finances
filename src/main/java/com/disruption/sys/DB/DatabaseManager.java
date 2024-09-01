package com.disruption.sys.DB;

import com.disruption.sys.Main;
import com.disruptionsystems.logging.LogLevel;
import org.sqlite.jdbc4.JDBC4ResultSet;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    Connection conn;

    public Connection establishConnection(String databaseAddress) {
        try {
            Connection connection = DriverManager.getConnection(databaseAddress);
            conn = connection;
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

    public void setStatement(String sql) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
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

    public void createBaseDatabaseStructure(){
        this.setStatement("CREATE TABLE IF NOT EXISTS FINANCES (pos VARCHAR NOT NULL, value FLOAT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT)");
    }

    public void addEntry(String pos, float value){
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO finances ('pos', value) VALUES ('" + pos + "', "+ value +")");
            statement.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }
}
