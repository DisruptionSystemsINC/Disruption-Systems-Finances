package com.disruption.sys.Window;

import io.qt.widgets.QApplication;

public class WindowManager {
    public QApplication startWindow(){
        return QApplication.initialize("Disruption Systems Finances", new String[]{});    }
}
