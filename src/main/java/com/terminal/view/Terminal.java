package com.terminal.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.terminal.dto.TipoEncargo;
import com.terminal.dto.TipoIdentificacion;
import com.terminal.service.ClienteService;
import com.terminal.service.EncargoService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Terminal {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JPanel clientesPanel;
    private JPanel recepcionPanel;
    private JPanel administracionPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JScrollPane downPanel;
    private JLabel nombreLabel;
    private JLabel apellidoLabel;
    private JTextField telefonoField;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField iApellidoField;
    private JTextField iNombreField;
    private JTextField iTelefonoField;
    private JLabel nombreRight;
    private JLabel apellidoRight;
    private JLabel cedulaLabel;
    private JComboBox tipoBox;
    private JTextField iTipoField;
    private JTextField iIdentificacionField;
    private JTextField identificacionField;
    private JButton buscarButton;
    private JButton crearButton;
    private JButton eliminarButton;
    private JButton editarButton;
    private JTable clienteTable;
    private JTextArea detalleArea;
    private JTextField clienteField;
    private JComboBox tipoComboBox;
    private JTextField abonoField;
    private JTextField saldoField;
    private JTextField iClienteField;
    private JTextArea iDetalleArea;
    private JTextField iTipoEncargoField;
    private JTextField iAbonoField;
    private JTextField iSaldoField;
    private JButton buscarEncargoButton;
    private JButton crearEncargoButton;
    private JLabel clienteId;
    private JButton imprimirButton;
    private JButton editarEncargoButton;
    private JLabel iClienteId;
    private JTable encargoTable;
    private JTextField porcentajeField;
    private JLabel iEncargoId;
    private JLabel iAbono;
    private JSpinner cantidadSpinner;
    private JTable table1;
    private JButton agregarButton;
    private JComboBox iTipoBox;
    private DefaultTableModel clienteModel;
    private DefaultTableModel encargoModel;
    private Terminal terminal;

    public Terminal() {
        $$$setupUI$$$();
        tipoBox.addItem(new TipoIdentificacion(1, "Cédula"));
        tipoBox.addItem(new TipoIdentificacion(2, "Pasaporte"));

        tipoComboBox.addItem(new TipoEncargo(1, "Maleta Grande", 15.00));
        tipoComboBox.addItem(new TipoEncargo(2, "Maleta Mediana", 10.00));
        tipoComboBox.addItem(new TipoEncargo(3, "Maleta Pequeña", 5.00));
        tipoComboBox.addItem(new TipoEncargo(4, "Hora", 1.00));

        clienteModel = new DefaultTableModel();
        String[] columnNamesCliente = {"ID", "IDENTIFICACIÓN", "NOMBRE", "APELLIDO", "TELÉFONO", "TIPO IDENTIFICACIÓN"};
        clienteModel.setColumnIdentifiers(columnNamesCliente);
        clienteTable.setModel(clienteModel);

        encargoModel = new DefaultTableModel();
        String[] columnNamesEncargo = {"ID", "DETALLE", "FECHA INGRESO", "FECHA SALIDA", "CLIENTE", "TIPO ENCARGO"
                , "ABONO", "SALDO"};
        encargoModel.setColumnIdentifiers(columnNamesEncargo);
        encargoTable.setModel(encargoModel);

        crearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClienteService.crearCliente(getTerminal());
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClienteService.buscarCliente(getTerminal());
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClienteService.eliminarCliente(getTerminal());
            }
        });

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClienteService.editarCliente(getTerminal());
            }
        });

        clienteTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if (clienteModel.getRowCount() <= 0 || clienteTable.getSelectedRow() == -1)
                    return;
                iIdentificacionField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 1).toString());
                iNombreField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 2).toString());
                iApellidoField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 3).toString());
                iTelefonoField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 4).toString());
                iTipoField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 5).toString());

                clienteId.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 0).toString());
                clienteField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 2).toString() + " "
                        + clienteTable.getValueAt(clienteTable.getSelectedRow(), 3).toString());
            }
        });

        encargoTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if (encargoModel.getRowCount() <= 0 || encargoTable.getSelectedRow() == -1)
                    return;
                iEncargoId.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 0).toString());
                iDetalleArea.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 1).toString());
                iClienteField.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 4).toString());
                iTipoEncargoField.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 5).toString());
                iAbono.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 6).toString());
                iSaldoField.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 7).toString());
                Double saldo = Double.parseDouble(iSaldoField.getText());
                if (saldo > 0)
                    iAbonoField.setEditable(true);
                else
                    iAbonoField.setEditable(false);
            }
        });

        crearEncargoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EncargoService.crearEncargo(getTerminal());
            }
        });

        buscarEncargoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EncargoService.buscarEncargo(getTerminal());
            }
        });

        imprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EncargoService.imprimirEncargo(getTerminal());
            }
        });

        editarEncargoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EncargoService.editarEncargo(getTerminal());
            }
        });
    }

    private Terminal getTerminal() {
        return this;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Terminal");
        frame.setContentPane(new Terminal().rootPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public JTextField getTelefonoField() {
        return telefonoField;
    }

    public JTextField getNombreField() {
        return nombreField;
    }

    public JTextField getApellidoField() {
        return apellidoField;
    }

    public JTextField getIdentificacionField() {
        return identificacionField;
    }

    public JComboBox getTipoBox() {
        return tipoBox;
    }

    public DefaultTableModel getClienteModel() {
        return clienteModel;
    }

    public JTextField getiApellidoField() {
        return iApellidoField;
    }

    public JTextField getiNombreField() {
        return iNombreField;
    }

    public JTextField getiTelefonoField() {
        return iTelefonoField;
    }

    public JTextField getiTipoField() {
        return iTipoField;
    }

    public JTextField getiIdentificacionField() {
        return iIdentificacionField;
    }

    public JTable getClienteTable() {
        return clienteTable;
    }

    public JTextField getAbonoField() {
        return abonoField;
    }

    public JTextField getSaldoField() {
        return saldoField;
    }

    public JLabel getClienteId() {
        return clienteId;
    }

    public JTextArea getDetalleArea() {
        return detalleArea;
    }

    public JComboBox getTipoComboBox() {
        return tipoComboBox;
    }

    public JTextField getClienteField() {
        return clienteField;
    }

    public JTextArea getiDetalleArea() {
        return iDetalleArea;
    }

    public JTextField getiClienteField() {
        return iClienteField;
    }

    public JLabel getiEncargoId() {
        return iEncargoId;
    }

    public DefaultTableModel getEncargoModel() {
        return encargoModel;
    }

    public JTextField getiTipoEncargoField() {
        return iTipoEncargoField;
    }

    public JTextField getiAbonoField() {
        return iAbonoField;
    }

    public JTextField getiSaldoField() {
        return iSaldoField;
    }

    public JLabel getiAbono() {
        return iAbono;
    }

    public JSpinner getCantidadSpinner() {
        return cantidadSpinner;
    }

    public JTextField getPorcentajeField() {
        return porcentajeField;
    }

    public JTable getEncargoTable() {
        return encargoTable;
    }

    private void createUIComponents() {
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setPreferredSize(new Dimension(408, 408));
        rootPanel.setRequestFocusEnabled(true);
        rootPanel.setVisible(true);
        tabbedPane1 = new JTabbedPane();
        rootPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        clientesPanel = new JPanel();
        clientesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Clientes", clientesPanel);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        clientesPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        downPanel = new JScrollPane();
        downPanel.setEnabled(false);
        panel1.add(downPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        downPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        clienteTable = new JTable();
        clienteTable.setAutoCreateRowSorter(false);
        clienteTable.setCellSelectionEnabled(false);
        clienteTable.setEnabled(true);
        clienteTable.setFillsViewportHeight(false);
        downPanel.setViewportView(clienteTable);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(rightPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        iApellidoField = new JTextField();
        iApellidoField.setEditable(true);
        rightPanel.add(iApellidoField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        iNombreField = new JTextField();
        iNombreField.setEditable(true);
        rightPanel.add(iNombreField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        iTelefonoField = new JTextField();
        iTelefonoField.setEditable(true);
        rightPanel.add(iTelefonoField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        nombreRight = new JLabel();
        nombreRight.setText("Nombre:");
        rightPanel.add(nombreRight, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apellidoRight = new JLabel();
        apellidoRight.setText("Apellido:");
        rightPanel.add(apellidoRight, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Teléfono:");
        rightPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Tipo Iden:");
        rightPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Identificación:");
        rightPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iIdentificacionField = new JTextField();
        iIdentificacionField.setEditable(true);
        iIdentificacionField.setText("");
        rightPanel.add(iIdentificacionField, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        eliminarButton = new JButton();
        eliminarButton.setText("Eliminar");
        rightPanel.add(eliminarButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editarButton = new JButton();
        editarButton.setText("Editar");
        rightPanel.add(editarButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iTipoField = new JTextField();
        iTipoField.setEditable(false);
        rightPanel.add(iTipoField, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(leftPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        nombreLabel = new JLabel();
        nombreLabel.setText("Nombre:");
        leftPanel.add(nombreLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apellidoLabel = new JLabel();
        apellidoLabel.setText("Apellido:");
        leftPanel.add(apellidoLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nombreField = new JTextField();
        leftPanel.add(nombreField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        apellidoField = new JTextField();
        leftPanel.add(apellidoField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cedulaLabel = new JLabel();
        cedulaLabel.setText("Teléfono:");
        leftPanel.add(cedulaLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        telefonoField = new JTextField();
        leftPanel.add(telefonoField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Tipo Iden:");
        leftPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipoBox = new JComboBox();
        leftPanel.add(tipoBox, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Identificación:");
        leftPanel.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        identificacionField = new JTextField();
        leftPanel.add(identificacionField, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buscarButton = new JButton();
        buscarButton.setText("Buscar");
        leftPanel.add(buscarButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crearButton = new JButton();
        crearButton.setText("Crear");
        leftPanel.add(crearButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        recepcionPanel = new JPanel();
        recepcionPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Encargos", recepcionPanel);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        recepcionPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Detalle:");
        panel2.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detalleArea = new JTextArea();
        panel2.add(detalleArea, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("com.terminal.dto.Cliente:");
        panel2.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clienteField = new JTextField();
        clienteField.setEditable(false);
        panel2.add(clienteField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Tipo:");
        panel2.add(label8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipoComboBox = new JComboBox();
        panel2.add(tipoComboBox, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Abono:");
        panel2.add(label9, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        abonoField = new JTextField();
        panel2.add(abonoField, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Saldo:");
        panel2.add(label10, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buscarEncargoButton = new JButton();
        buscarEncargoButton.setText("Buscar");
        panel2.add(buscarEncargoButton, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crearEncargoButton = new JButton();
        crearEncargoButton.setText("Crear");
        panel2.add(crearEncargoButton, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clienteId = new JLabel();
        clienteId.setText("");
        clienteId.setVisible(true);
        panel2.add(clienteId, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saldoField = new JTextField();
        saldoField.setEditable(false);
        panel2.add(saldoField, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Cantidad:");
        panel2.add(label11, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cantidadSpinner = new JSpinner();
        panel2.add(cantidadSpinner, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Porcentaje:");
        panel2.add(label12, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        porcentajeField = new JTextField();
        panel2.add(porcentajeField, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Equipaje:");
        panel2.add(label13, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        agregarButton = new JButton();
        agregarButton.setText("Agregar");
        panel2.add(agregarButton, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 5, new Insets(0, 0, 0, 0), -1, -1));
        recepcionPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Detalle:");
        panel3.add(label14, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iClienteField = new JTextField();
        iClienteField.setEditable(false);
        panel3.add(iClienteField, new GridConstraints(1, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("com.terminal.dto.Cliente:");
        panel3.add(label15, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iDetalleArea = new JTextArea();
        iDetalleArea.setEditable(false);
        panel3.add(iDetalleArea, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Tipo:");
        panel3.add(label16, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iTipoEncargoField = new JTextField();
        iTipoEncargoField.setEditable(false);
        panel3.add(iTipoEncargoField, new GridConstraints(2, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("Abonar:");
        panel3.add(label17, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iAbonoField = new JTextField();
        iAbonoField.setEditable(false);
        panel3.add(iAbonoField, new GridConstraints(3, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Saldo:");
        panel3.add(label18, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iSaldoField = new JTextField();
        iSaldoField.setEditable(false);
        panel3.add(iSaldoField, new GridConstraints(4, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        imprimirButton = new JButton();
        imprimirButton.setText("Imprimir");
        panel3.add(imprimirButton, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editarEncargoButton = new JButton();
        editarEncargoButton.setText("Abonar");
        panel3.add(editarEncargoButton, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iClienteId = new JLabel();
        iClienteId.setText("");
        panel3.add(iClienteId, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iEncargoId = new JLabel();
        iEncargoId.setText("");
        panel3.add(iEncargoId, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        iAbono = new JLabel();
        iAbono.setText("");
        panel3.add(iAbono, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        recepcionPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        encargoTable = new JTable();
        scrollPane1.setViewportView(encargoTable);
        final JScrollPane scrollPane2 = new JScrollPane();
        recepcionPanel.add(scrollPane2, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane2.setViewportView(table1);
        administracionPanel = new JPanel();
        administracionPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Administración", administracionPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}