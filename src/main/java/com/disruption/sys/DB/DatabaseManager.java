package com.disruption.sys.DB;

import com.disruption.sys.Main;
import com.disruption.sys.utils.TableRow;
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

    public void createMainTable(){
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS FINANCES (pos VARCHAR NOT NULL, value DOUBLE NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL)");
            statement.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }

    public String getPosField(String pos) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM FINANCES WHERE POS=?");
            statement.setString(1, pos);
            ResultSet rs = statement.executeQuery();
            String result = rs.getString("pos");
            rs.close();
            statement.close();
            return result;
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return null;
    }

    public void delById(String Id) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM FINANCES WHERE id=?");
            st.setString(1, Id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }

    public void delByPos(String pos) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM FINANCES WHERE POS=?");
            statement.setString(1, pos);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }

    public double getValue(String pos) {
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM FINANCES WHERE POS=?");
            st.setString(1, pos);
            ResultSet rs = st.executeQuery();
            float result = rs.getFloat("value");
            rs.close();
            return result;
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage());
        }
        return 0.00f;
    }

    public List<TableRow> retrieveEntries(){
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM FINANCES");
            ResultSet rs = st.executeQuery();
            List<TableRow> tableRows = new ArrayList<>();
            while (rs.next()){
                tableRows.add(new TableRow(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getDate(4)));
            }
            return tableRows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEntry(String pos, double value, Date date){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO FINANCES ('pos', value, date) VALUES (?, ?, ?)");
            statement.setString(1, pos);
            statement.setDouble(2, value);
            statement.setDate(3, date);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: STATEMENT COULD NOT BE EXECUTED, " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).toList());
        }
    }
}
