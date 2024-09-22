package com.disruption.sys.testing;

import com.disruption.sys.DB.DatabaseManager;
import com.disruption.sys.Window.MainWindow;

import java.sql.Date;
import java.time.LocalDate;

public class DataBaseTest {
    public void verifyDatabaseFunctionality(DatabaseManager manager){

        manager.createBaseDatabaseStructure();
        System.out.println("Testing value placement");
        manager.addEntry("TEST FIELD ee44522A45674ge4er", 25.55f, Date.valueOf(LocalDate.now()));
        System.out.println("Values placed.");
        System.out.println(manager.getPosField("TEST FIELD ee44522A45674ge4er").equals("TEST FIELD ee44522A45674ge4er")  ? "Entry Placement Okay." : "Entry Placement Not Okay.");
        System.out.println(manager.getValue("TEST FIELD ee44522A45674ge4er") == 25.55f ? "Value Placement Okay." : "Value Placement Not Okay.");
        System.out.println("Dropping test value.");
        manager.delByPos("TEST FIELD ee44522A45674ge4er");
        System.out.println(manager.getPosField("TEST FIELD ee44522A45674ge4er") != null ? "Not Okay" : "Okay");
        new MainWindow().openWindow(manager);
    }

}
