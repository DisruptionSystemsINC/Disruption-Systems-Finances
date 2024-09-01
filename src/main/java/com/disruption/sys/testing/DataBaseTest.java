package com.disruption.sys.testing;

import com.disruption.sys.DB.DatabaseManager;
import com.disruption.sys.Main;
import com.disruption.sys.Window.MainWindow;

public class DataBaseTest {
    public void verifyDatabaseFunctionality(DatabaseManager manager){

        manager.createBaseDatabaseStructure();
        System.out.println("Testing value placement");
        manager.addEntry("TEST FIELD", 25.55f);
        System.out.println("Values placed.");
        System.out.println(manager.getPosField("TEST FIELD").equals("TEST FIELD")  ? "Entry Placement Okay." : "Entry Placement Not Okay.");
        System.out.println(manager.getValue("TEST FIELD") == 25.55f ? "Value Placement Okay." : "Value Placement Not Okay.");
        System.out.println("Dropping test value.");
        manager.delByPos("TEST FIELD");
        System.out.println(manager.getPosField("TEST FIELD") != null ? "Not Okay" : "Okay");
        new MainWindow().openWindow(manager);
    }

}
