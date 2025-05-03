package com.disruption.sys;

import java.sql.Connection;
import com.disruption.sys.DB.DatabaseManager;
import com.disruption.sys.Window.MainWindowQt;
import com.disruption.sys.testing.DataBaseTest;
import com.disruptionsystems.*;
import com.disruptionsystems.logging.LogLevel;

import javax.xml.crypto.Data;

public class Main {
   private static DragonLog logger;
   private static Connection conn;

   private static final String databaseAddress = "jdbc:sqlite:money.db";

   private static DatabaseManager manager;

   public static void main(String[] args) {
      logger = new DragonLog();
      manager = new DatabaseManager();
      conn = manager.establishConnection(databaseAddress);
      DataBaseTest test = new DataBaseTest();
      test.verifyDatabaseFunctionality(manager);
      logger.printToLog(LogLevel.INFORMATION, "Starting Application");
      new MainWindowQt().init();
   }

   public static Connection getConnection(){
      return conn;
   }

   public static DatabaseManager getManager(){return manager;}

   public static DragonLog getLogger() {return logger;}
}