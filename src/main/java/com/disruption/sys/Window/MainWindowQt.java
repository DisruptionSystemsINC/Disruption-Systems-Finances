package com.disruption.sys.Window;

import com.disruption.sys.DB.DatabaseManager;
import com.disruption.sys.Main;
import com.disruption.sys.utils.TableRow;
import io.qt.NonNull;
import io.qt.core.*;
import io.qt.gui.*;
import io.qt.widgets.*;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MainWindowQt {
    private QMainWindow mainWindow;
    private QApplication app;

    public void init(){
        app = QApplication.initialize("Disruption Systems Finances", new String[0]);

        QWidget addEntryWindow = new QWidget();
        QWidget central = new QWidget();
        QWidget addEntryWindowWidget = new QWidget();
        QWidget deleteEntryConfirmWidget = new QWidget();

        QDoubleValidator validator = new QDoubleValidator();
        validator.setDecimals(2);

        QLabel deleteTextLabel = new QLabel();

        QPushButton addEntryButton = new QPushButton();
        QPushButton confirmAddEntryButton = new QPushButton();
        QPushButton deleteButton = new QPushButton();
        QPushButton confirmDeleteButton = new QPushButton();

        QLineEdit inputPos = new QLineEdit();
        QLineEdit inputVal = new QLineEdit();

        QCheckBox isOutgoing = new QCheckBox();
        QCheckBox isIncoming = new QCheckBox();

        QTableView table = new QTableView();

        QVBoxLayout layout = new QVBoxLayout(central);
        QVBoxLayout addEntryWindowLayout = new QVBoxLayout();
        QVBoxLayout deleteEntryWindowLayout = new QVBoxLayout();

        isOutgoing.setText("Outgoing");
        isIncoming.setText("Incoming");
        addEntryButton.setText("Add Entry");
        confirmAddEntryButton.setText("Confirm");
        deleteButton.setText("Delete Entry");
        confirmDeleteButton.setText("Confirm?");
        inputVal.setPlaceholderText("Value");
        inputVal.setValidator(validator);
        inputPos.setPlaceholderText("Position");

        generateTableView(table);

        deleteEntryConfirmWidget.setWindowTitle("CONFIRM DELETION?");
        addEntryWindowWidget.setWindowTitle("Add Entry");
        layout.addWidget(table);
        layout.addWidget(addEntryButton);
        layout.addWidget(deleteButton);
        deleteEntryWindowLayout.addWidget(deleteTextLabel);
        deleteEntryWindowLayout.addWidget(confirmDeleteButton);
        addEntryWindowLayout.addWidget(inputPos);
        addEntryWindowLayout.addWidget(inputVal);
        addEntryWindowLayout.addWidget(isOutgoing);
        addEntryWindowLayout.addWidget(isIncoming);
        addEntryWindowLayout.addWidget(confirmAddEntryButton);


        deleteEntryConfirmWidget.setLayout(deleteEntryWindowLayout);
        addEntryWindow.setLayout(addEntryWindowLayout);
        layout.setSpacing(30);



        addEntryButton.clicked.connect(() -> {
                isOutgoing.setCheckState(Qt.CheckState.Checked);
                isIncoming.setCheckState(Qt.CheckState.Unchecked);
                addEntryWindow.setWindowTitle("Add Entry");
                addEntryWindow.show();
        });

        confirmAddEntryButton.clicked.connect(() -> {
            double val = Float.parseFloat(inputVal.getText());
            String pos = inputPos.getText();
            addEntryWindow.hide();
            if (isOutgoing.isChecked()) {
                val -= 2*val;
            }
            Main.getManager().addEntry(pos, val, Date.valueOf(LocalDate.now()));
            inputVal.clear();
            inputPos.clear();
            generateTableView(table);
        });

        deleteButton.clicked.connect(() -> {
            if (table.selectionModel().hasSelection()) {
                QItemSelection sel = table.selectionModel().getSelection();
                double val = Double.parseDouble((String) sel.get(0).indexes().get(1).data());
                deleteTextLabel.setText("Are you sure you want to delete the charge/addition of " + val + "€? \n" +
                        "THIS ACTION CANNOT BE UNDONE");
                deleteEntryConfirmWidget.setVisible(true);
            }
        });

        confirmDeleteButton.clicked.connect(() -> {
            QItemSelection sel = table.selectionModel().getSelection();
            String val = (String) sel.get(0).indexes().get(2).data();
            Main.getManager().delById(val);
            table.selectionModel().clear();
            generateTableView(table);

            deleteEntryConfirmWidget.setVisible(false);
        });

        isIncoming.checkStateChanged.connect(() -> {
            if (isIncoming.isChecked()){
                isOutgoing.setCheckState(Qt.CheckState.Unchecked);
            } else {
                isOutgoing.setCheckState(Qt.CheckState.Checked);
            }
        });

        isOutgoing.checkStateChanged.connect(() -> {
            if (isOutgoing.isChecked()){
                isIncoming.setCheckState(Qt.CheckState.Unchecked);
            } else {
                isIncoming.setCheckState(Qt.CheckState.Checked);
            }
        });



        mainWindow = new QMainWindow();
        mainWindow.setMinimumSize(500, 500);
        mainWindow.setWindowTitle("Disruption Finances");
        mainWindow.setVisible(true);
        mainWindow.setCentralWidget(central);
        app.exec();
    }

    public QMainWindow getMainWindow() {
        return mainWindow;
    }

    public QApplication getApp() {
        return app;
    }

    private void generateTableView(QTableView table){
        DecimalFormat df = new DecimalFormat("0.00");
        DatabaseManager dbman = Main.getManager();
        List<TableRow> rows = dbman.retrieveEntries();
        QStandardItemModel model = new QStandardItemModel();
        model.setHorizontalHeaderLabels(Arrays.stream(new String[]{"Position", "Value", "Id", "Date"}).toList());
        for (TableRow row : rows){
            List<QStandardItem> items;
            QStandardItem pos = new QStandardItem(String.valueOf(row.getPos()));
            QStandardItem val = new QStandardItem(String.valueOf(df.format(row.getValue())));
            QStandardItem id = new QStandardItem(String.valueOf(row.getId()));
            QStandardItem date = new QStandardItem(String.valueOf(row.getDate()));


            items = List.of(pos, val, id, date);
            for (QStandardItem item : items){
                item.setEditable(false);
            }

            model.appendRow(items);
        }
        double total = 0.00;

        for (TableRow row : rows){
            total += row.getValue();
        }

        QStandardItem pos = new QStandardItem("Total:");
        QStandardItem val = new QStandardItem(String.valueOf(df.format(total)));
        QStandardItem id = new QStandardItem();
        QStandardItem date = new QStandardItem();
        pos.setSelectable(false);
        val.setSelectable(false);
        id.setSelectable(false);
        date.setSelectable(false);

        model.appendRow(List.of(pos, val, id, date));
        table.setShowGrid(true);
        table.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
        table.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        table.setModel(model);
        table.resizeRowsToContents();
        table.resizeColumnsToContents();
    }
}
