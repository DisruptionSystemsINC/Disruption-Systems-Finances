package com.disruption.sys.Window;

import com.disruption.sys.DB.DatabaseManager;
import com.disruption.sys.Main;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    JFrame mainWindow;
    JButton DeleteButton;
    DatabaseManager manager;
    JTable table;
    DefaultTableModel model;

    public void openWindow(DatabaseManager manager){
        this.manager = manager;
        makeMainWindow();
    }

    public void makeMainWindow() {
        table = makeTable(new String[]{"Position", "Value", "Id"}, buildPositionTable(manager.getPositions(), manager.getValues(), manager.getIds()));
        mainWindow = new JFrame("Disruption Systems Finances");
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setSize(600, 400);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.add(createMainPanel());
        mainWindow.setVisible(true);
    }

    public JPanel createMainPanel(){
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel mainPanel = new JPanel();
        mainPanel.add(scrollPane);
        mainPanel.add(scrollPane);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel();
        addButtons(buttonPanel);
        mainPanel.add(buttonPanel);
        return mainPanel;
    }

    private void addButtons(JPanel panel){
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        DeleteButton = new JButton("Delete Entry");
        JButton AddButton = new JButton("Add Entry");
        AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Add Entry");
                JButton button = new JButton("Add");
                JLabel value = new JLabel("Value");
                JLabel position = new JLabel("Position");
                JTextField positionField = new JTextField();
                JTextField valueField = new JTextField();
                JPanel mainPanel = new JPanel();
                JPanel fieldPanel = new JPanel();
                JPanel checkBoxPanel = new JPanel();
                JLabel isNegField = new JLabel("Outgoing");
                JLabel isPosField = new JLabel("Income");
                JCheckBox PoscheckBox = new JCheckBox();
                JCheckBox NegcheckBox = new JCheckBox();
                NegcheckBox.setSelected(true);
                positionField.setToolTipText("Position");
                valueField.setToolTipText("Value");
                fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
                checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
                checkBoxPanel.add(isNegField);
                checkBoxPanel.add(NegcheckBox);
                checkBoxPanel.add(isPosField);
                checkBoxPanel.add(PoscheckBox);
                fieldPanel.add(position);
                fieldPanel.add(positionField);
                fieldPanel.add(value);
                fieldPanel.add(valueField);
                fieldPanel.add(checkBoxPanel);
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                mainPanel.add(fieldPanel);
                mainPanel.add(button);
                frame.add(mainPanel);
                NegcheckBox.addActionListener(_ -> PoscheckBox.setSelected(false));
                PoscheckBox.addActionListener(_ -> NegcheckBox.setSelected(false));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        if (NegcheckBox.isSelected()) {
                            Main.getManager().addEntry(positionField.getText(), Float.parseFloat("-" + valueField.getText()));
                        } else if (PoscheckBox.isSelected()) {
                            Main.getManager().addEntry(positionField.getText(), Float.parseFloat(valueField.getText()));
                        }
                        mainWindow.dispose();
                        makeMainWindow();
                    }
                });

                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setSize(200, 200);
                frame.setLocationRelativeTo(mainWindow);
                frame.setVisible(true);
            }
        });
        DeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = (String) table.getValueAt(table.getSelectedRow(), 2);
                manager.delById(id);
                mainWindow.dispose();
                makeMainWindow();
            }
        });
        DeleteButton.setEnabled(false);
        panel.add(AddButton);
        panel.add(DeleteButton);
    }

    private JTable makeTable(String[] columns, String[][] positionTable){
        JTable table = new JTable(positionTable, columns);
        table.setAutoResizeMode(1);
        table.setRowHeight(50);
        table.setEnabled(true);
        setColumnLayout(table);
        model = new DefaultTableModel(positionTable, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowSelectionAllowed(true);
        table.setModel(model);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                DeleteButton.setEnabled(true);
            }
        });
        return table;
    }


    public void setColumnLayout(JTable table){
        TableColumn valColumn = table.getColumn("Value");
        TableColumn idColumn = table.getColumn("Id");
        valColumn.setMaxWidth(80);
        idColumn.setMaxWidth(80);
    }

    public String[][] buildPositionTable(String[] positions, String[] values, String[] ids){
        List<String[]> tableList = new ArrayList<>();
        for (int i = 0; i<positions.length; i++) {
             tableList.add(new String[]{positions[i], values[i] + "€", ids[i]});
        }
        float valBuffer = 0.00f;
        for (String f : values){
            valBuffer += Float.parseFloat(f);
        }
        tableList.add(new String[]{"Total", valBuffer + "€"});
        String[][] table = new String[tableList.size()][];
        return tableList.toArray(table);
    }
}
