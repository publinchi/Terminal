package com.terminal.table;

import javax.swing.table.DefaultTableModel;

public class NoEditableTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
