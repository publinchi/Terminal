import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Terminal {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JPanel clientesPanel;
    private JPanel recepcionPanel;
    private JPanel devolucionPanel;
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
    private JComboBox iTipoBox;
    private DefaultTableModel clienteModel;
    private DefaultTableModel encargoModel;
    private Terminal terminal;

    public Terminal() {
        clienteModel = new DefaultTableModel();
        String[] columnNamesCliente = { "ID", "IDENTIFICACIÓN", "NOMBRE", "APELLIDO", "TELÉFONO", "TIPO IDENTIFICACIÓN" };
        clienteModel.setColumnIdentifiers(columnNamesCliente);
        clienteTable.setModel(clienteModel);

        encargoModel = new DefaultTableModel();
        String[] columnNamesEncargo = { "ID", "DETALLE", "FECHA INGRESO", "FECHA SALIDA", "CLIENTE", "TIPO ENCARGO"
                , "ABONO", "SALDO" };
        encargoModel.setColumnIdentifiers(columnNamesEncargo);
        encargoTable.setModel(encargoModel);

        tipoBox.addItem(new TipoIdentificacion(1, "Cédula"));
        tipoBox.addItem(new TipoIdentificacion(2, "Pasaporte"));

        tipoComboBox.addItem(new TipoEncargo(1, "Maleta Grande", 15.00));
        tipoComboBox.addItem(new TipoEncargo(2, "Maleta Mediana", 10.00));
        tipoComboBox.addItem(new TipoEncargo(3, "Maleta Pequeña", 5.00));

        tipoComboBox.addItem(new TipoEncargo(4, "Hora", 1.00));

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

        clienteTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if(clienteModel.getRowCount() <= 0 || clienteTable.getSelectedRow() == -1)
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

        encargoTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if(encargoModel.getRowCount() <= 0 || encargoTable.getSelectedRow() == -1)
                    return;
                iEncargoId.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 0).toString());
                iDetalleArea.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 1).toString());
                iClienteField.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 4).toString());
                iTipoEncargoField.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 5).toString());
                iAbono.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 6).toString());
                iSaldoField.setText(encargoTable.getValueAt(encargoTable.getSelectedRow(), 7).toString());
                Double saldo = Double.parseDouble(iSaldoField.getText());
                if(saldo > 0)
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
}