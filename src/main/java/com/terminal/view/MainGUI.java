package com.terminal.view;

import com.terminal.dto.Cliente;
import com.terminal.dto.Encargo;
import com.terminal.dto.Equipaje;
import com.terminal.dto.TipoEncargo;
import com.terminal.service.ClienteService;
import com.terminal.service.EncargoService;
import com.terminal.service.EquipajeService;
import com.terminal.service.TipoEncargoService;
import com.terminal.table.NoEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainGUI {
    private JPanel rootPanel;
    private JTabbedPane menuTabbedPane;
    private JPanel clientesPanel;
    private JScrollPane downPanel;
    private JTable clienteTable;
    private JButton buscarButton;
    private JButton crearButton;
    private JButton eliminarButton;
    private JButton editarButton;
    private JPanel encargosPanel;
    private JTextField clienteField;
    private JButton buscarEncargoButton;
    private JLabel clienteId;
    private JTextField totalField;
    private JButton crearEncargoButton;
    private JButton imprimirButton;
    private JButton abonarButton;
    private JLabel encargoId;
    private JTextField porcentajeField;
    private JTable encargosTable;
    private JTable equipajesTable;
    private JPanel administracionPanel;
    private JTextField buscarTextField;
    private JPanel clientePanel;
    private JScrollPane encargosScrollPane;
    private JPanel cabeceraPanel;
    private JScrollPane equipajesScrollPane;
    private JPanel encargoPanel;
    private JPanel menuPanel;
    private JPanel menuEncargoPanel;
    private JTextField cantidadField;
    private JButton guardarButton;
    private JButton agregarButton;
    private DefaultTableModel clienteModel;
    private static DefaultTableModel encargoModel;
    private static DefaultTableModel equipajeModel;

    public MainGUI() {
        buscarButton.setPreferredSize(new Dimension(100, 80));
        crearButton.setPreferredSize(new Dimension(100, 80));
        editarButton.setPreferredSize(new Dimension(100, 80));
        eliminarButton.setPreferredSize(new Dimension(100, 80));

        buscarEncargoButton.setPreferredSize(new Dimension(100, 80));
        crearEncargoButton.setPreferredSize(new Dimension(100, 80));
        abonarButton.setPreferredSize(new Dimension(100, 80));
        imprimirButton.setPreferredSize(new Dimension(100, 80));

        clienteModel = new NoEditableTableModel();
        String[] columnNamesCliente = {"ID", "IDENTIFICACIÓN", "NOMBRE", "APELLIDO", "TELÉFONO", "TIPO IDENTIFICACIÓN"};
        clienteModel.setColumnIdentifiers(columnNamesCliente);
        clienteTable.setModel(clienteModel);
        clienteTable.setEnabled(true);

        encargoModel = new NoEditableTableModel();
        String[] columnNamesEncargo = {"ID", "DETALLE", "FECHA INGRESO", "FECHA SALIDA", "CLIENTE", "$ ABONADO"
                , "$ SALDO", "% DESCUENTO", "$ TOTAL"};
        encargoModel.setColumnIdentifiers(columnNamesEncargo);
        encargosTable.setModel(encargoModel);
        encargosTable.setEnabled(true);

        equipajeModel = new NoEditableTableModel();
        String[] columnNamesEquipaje = {"ID", "ENCARGO", "TIPO ENCARGO", "DETALLE", "FECHA INGRESO" , "VALOR"};
        equipajeModel.setColumnIdentifiers(columnNamesEquipaje);
        equipajesTable.setModel(equipajeModel);
        equipajesTable.setEnabled(true);

        clienteTable.getSelectionModel().addListSelectionListener(e -> {
            if(Objects.equals(e.getValueIsAdjusting(), true))
                seleccionarCliente();
        });

        encargosTable.getSelectionModel().addListSelectionListener(e -> {
            if(Objects.equals(e.getValueIsAdjusting(), true))
                seleccionarEncargo();
        });

        buscarButton.addActionListener(e -> buscarClientes(false));

        getBuscarTextField().addActionListener(e -> buscarClientes(false));

        crearButton.addActionListener(e -> crearCliente());

        eliminarButton.addActionListener(e -> eliminarCliente());

        editarButton.addActionListener(e -> editarCliente());

        buscarEncargoButton.addActionListener(e -> buscarEncargo(false, false,
                true) );

        crearEncargoButton.addActionListener(e -> crear(false));

        abonarButton.addActionListener(e -> abonarEncargo(false));

        imprimirButton.addActionListener(e -> imprimir());

        guardarButton.addActionListener(e -> guardarDescuento());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Terminal");
        frame.setContentPane(new MainGUI().rootPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void limpiarClienteTable() {
        this.getClienteModel().setRowCount(0);
    }

    private static void limpiarEncargoTable() {
        getEncargoModel().setRowCount(0);
    }

    public static void limpiarEquipajeTable() {
        getEquipajeModel().setRowCount(0);
    }

    private void buscarClientes() {
        buscarClientes(true);
    }

    private void buscarClientes(boolean mostrarMensaje) {
        limpiarClienteTable();
        List<Cliente> clientes;
        try {
            clientes = ClienteService.buscarClientes(getBuscarTextField().getText());
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
            return;
        }

        for (Cliente c : clientes) {
            Object[] o = new Object[6];
            o[0] = c.getId();
            o[1] = c.getIdentificacion();
            o[2] = c.getNombre();
            o[3] = c.getApellido();
            o[4] = c.getTelefono();
            if(c.getTipoIdentificacion().getId() == 1)
                o[5] = "Cédula";
            else if(c.getTipoIdentificacion().getId() == 2)
                o[5] = "Pasaporte";
            this.getClienteModel().addRow(o);
        }
        clienteId.setText(null);
        if(mostrarMensaje)
            JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                    + clientes.size(), "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void seleccionarCliente() {
        if (clienteModel.getRowCount() <= 0 || clienteTable.getSelectedRow() == -1)
            return;
        clienteId.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 0).toString());
        clienteField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 2).toString() + " "
                + clienteTable.getValueAt(clienteTable.getSelectedRow(), 3).toString());
    }

    private void seleccionarEncargo() {
        if (encargoModel.getRowCount() <= 0 || encargosTable.getSelectedRow() == -1)
            return;
        encargoId.setText(encargosTable.getValueAt(encargosTable.getSelectedRow(), 0).toString());
        //clienteField.setText(clienteTable.getValueAt(clienteTable.getSelectedRow(), 2).toString() + " "
        //        + clienteTable.getValueAt(clienteTable.getSelectedRow(), 3).toString());

        Encargo encargo = null;
        try {
            encargo = EncargoService.buscarEncargo(new Encargo(Integer.valueOf(encargoId.getText())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        List<Equipaje> equipajes = null;
        try {
            equipajes = EquipajeService.buscarEquipajes(encargo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        cargarEquipajes(equipajes);

        cargarCantidad(equipajes.size());
        porcentajeField.setText(encargo.getDescuento().toString());
        if(encargo.getDescuento() > 0) {
            Double total = (encargo.getSaldo() - (encargo.getSaldo() * encargo.getDescuento() / 100));
            BigDecimal bd = new BigDecimal(Double.toString(total));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            total = bd.doubleValue();
            totalField.setText("$ " + total + "");
        }
        else {
            totalField.setText("$ " + encargo.getSaldo().toString());
        }

        if(equipajes.size() > 0)
            guardarButton.setEnabled(true);
        else
            guardarButton.setEnabled(false);
    }

    private void crearCliente() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.getRootPanel());
        final JDialog jDialog = new JDialog(topFrame, "Crear Cliente",true);
        jDialog.setContentPane(new ClienteGUI().getRootPanel());
        jDialog.pack();
        jDialog.setVisible(true);
        jDialog.setLocationRelativeTo(null);
    }

    private void eliminarCliente() {
        if(Objects.isNull(clienteId.getText()) || clienteId.getText().trim().length() == 0)
            return;
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Desea eliminar el cliente?","Advertencia", YES_NO_OPTION);
        if(dialogResult == JOptionPane.NO_OPTION)
            return;

        try {
            ClienteService.eliminarCliente(Integer.valueOf(clienteId.getText()));
            clienteId.setText(null);
            buscarClientes(false);
            JOptionPane.showMessageDialog(null, "Operación realizada correctamente ",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
    }

    private void editarCliente() {
        try {
            if(Objects.isNull(clienteId.getText()))
                return;
            Cliente cliente = ClienteService.buscarCliente(Integer.valueOf(clienteId.getText()));
            if(Objects.isNull(clienteId.getText()))
                return;
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.getRootPanel());
            final JDialog jDialog = new JDialog(topFrame, "Editar Cliente", true);
            jDialog.setContentPane(new ClienteGUI(cliente).getRootPanel());
            jDialog.pack();
            jDialog.setVisible(true);
            jDialog.setLocationRelativeTo(null);

            limpiarClienteTable();

            buscarClientes(false);
            clienteId.setText(null);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
    }

    private void buscarEncargo(boolean mostrarMensaje, boolean evitarCamara, boolean conIdentificacion) {
        List<Encargo> encargos;

        int reply = -1;

        if(!evitarCamara)
            reply = JOptionPane.showConfirmDialog(null, "Utilizar cámara?",
                    "Tipo de Búsqueda", JOptionPane.YES_NO_OPTION);

        try {
            Cliente cliente;
            if (reply == JOptionPane.YES_OPTION) {
                EncargoService.buscarEncargos(null, true);
                return;
            } else if(conIdentificacion) {
                String identificacion = JOptionPane.showInputDialog("Ingrese Identificación:");
                cliente = ClienteService.buscarClientePorIdentificacion(identificacion);
                if(Objects.isNull(cliente)) {
                    JOptionPane.showMessageDialog(null, "Operación fallida, " +
                            "seleccione cliente!!!","Falla", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                clienteId.setText(cliente.getId().toString());
                clienteField.setText(cliente.getNombre() + " " + cliente.getApellido());
            } else {
                cliente = new Cliente(Integer.valueOf(getClienteId().getText()));
            }

            if(Objects.nonNull(cliente)) {
                encargos = EncargoService.buscarEncargos(cliente, false);

                cargarEncargos(cliente.getId(), encargos);

                limpiarEquipajeTable();
            }
        } catch (SQLException | NullPointerException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!", "Falla",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
            return;
        }

        if(mostrarMensaje)
            JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                    + getEncargoModel().getRowCount(), "Exito", JOptionPane.INFORMATION_MESSAGE);

    }

    private void crear(boolean mostrarMensaje) {
        int seleccion = JOptionPane.showOptionDialog(
                this.getRootPanel(),
                "Seleccione opción:",
                "Selector de opciones",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,    // null para icono por defecto.
                new Object[] { "Encargo", "Equipaje" },   // null para YES, NO y CANCEL
                null);

        switch (seleccion) {
            case 0 : crearEncargo(mostrarMensaje);
                break;
            case 1 : crearEquipaje(mostrarMensaje);
                break;
        }
    }

    private void crearEncargo(boolean mostrarMensaje) {
        try {
            if(Objects.isNull(getClienteId().getText()) || Objects.nonNull(getClienteId().getText()) &&
                    getClienteId().getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación fallida, seleccione cliente!!!",
                        "Falla", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente cliente = ClienteService.buscarCliente(Integer.valueOf(clienteId.getText()));

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.getRootPanel());
            final JDialog jDialog = new JDialog(topFrame, "Crear Encargo",true);

            Encargo encargo = new Encargo();
            encargo.setCliente(cliente);
            encargo.setFechaIngreso(new Date());

            System.out.println(cliente.getNombre() + " " + cliente.getApellido());

            jDialog.setContentPane(new EncargoGUI(encargo).getRootPanel());
            jDialog.pack();
            jDialog.setVisible(true);
            jDialog.setLocationRelativeTo(null);

            if(mostrarMensaje)
                JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                        , "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
    }

    private void crearEquipaje(boolean mostrarMensaje) {
        try {
            if(Objects.isNull(encargoId.getText()) || Objects.nonNull(encargoId.getText()) &&
                    encargoId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación fallida, seleccione encargo!!!",
                        "Falla", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Encargo encargo =  EncargoService.buscarEncargo(new Encargo(Integer.valueOf(encargoId.getText())));

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.getRootPanel());
            final JDialog jDialog = new JDialog(topFrame, "Crear Equipaje",true);

            Equipaje equipaje = new Equipaje();
            equipaje.setEncargo(encargo);

            jDialog.setContentPane(new EquipajeGUI(equipaje).getRootPanel());
            jDialog.pack();
            jDialog.setVisible(true);
            jDialog.setLocationRelativeTo(null);

            Cliente cliente = new Cliente(Integer.valueOf(getClienteId().getText()));
            List<Encargo> encargos = EncargoService.buscarEncargos(cliente, false);

            cargarEncargos(Integer.valueOf(getClienteId().getText()), encargos);

            List<Equipaje> equipajes = null;
            try {
                equipajes = EquipajeService.buscarEquipajes(encargo);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            cargarCantidad(equipajes.size());

            if(mostrarMensaje)
                JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                        , "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
    }

    private void imprimir() {
        if(Objects.isNull(encargoId.getText()) || Objects.nonNull(encargoId.getText()) &&
                encargoId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operación fallida, seleccione encargo!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Encargo encargo = EncargoService.buscarEncargo(new Encargo(Integer.valueOf(encargoId.getText())));
            List<Equipaje> equipajes = EquipajeService.buscarEquipajes(encargo);
            encargo.setEquipajes(equipajes);
            EncargoService.imprimirEncargo(encargo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void cargarCantidad(int cantidad) {
        getCantidadField().setText(String.valueOf(cantidad));

        if(cantidad > 1) {
            porcentajeField.setEditable(true);
        } else {
            porcentajeField.setEditable(false);
        }
    }

    private void guardarDescuento() {
        if(Objects.isNull(porcentajeField.getText()) || Objects.nonNull(porcentajeField.getText()) &&
                porcentajeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operación fallida, ingrese el porcentaje " +
                            "de descuento!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Encargo encargo = EncargoService.buscarEncargo(new Encargo(Integer.valueOf(encargoId.getText())));

            encargo.setDescuento(Integer.valueOf(porcentajeField.getText()));

            EncargoService.editarEncargo(encargo);

            buscarEncargo(false, true, false);

            limpiarEquipajeTable();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
    }

    public static void cargarEncargos(Integer clienteId, List<Encargo> encargos) {
        limpiarEncargoTable();

        Cliente cliente;

        try {
            cliente = ClienteService.buscarCliente(clienteId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }

        for (Encargo c : encargos) {
            if(Objects.isNull(c.getFechaSalida()) && c.getSaldo() >= 0.0) {
                Object[] o = new Object[9];
                o[0] = c.getId();
                o[1] = c.getDetalle();
                o[2] = c.getFechaIngreso();
                o[3] = c.getFechaSalida();
                o[4] = cliente.getNombre() + " " + cliente.getApellido();
                o[5] = "$ " + c.getAbono();
                o[6] = "$ " + c.getSaldo();
                o[7] = "% " + c.getDescuento();
                if(c.getDescuento() > 0) {
                    Double total = (c.getSaldo() - (c.getSaldo() * c.getDescuento() / 100));
                    BigDecimal bd = new BigDecimal(Double.toString(total));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    total = bd.doubleValue();
                    o[8] = "$ " +total;
                } else
                    o[8] = "$ " + c.getSaldo();

                getEncargoModel().addRow(o);
            }
        }
    }

    public static void cargarEquipajes(List<Equipaje> equipajes) {
        limpiarEquipajeTable();

        Map map = new HashMap<>();

        try {
            List<TipoEncargo> tipoEncargos = TipoEncargoService.loadTipoEncargos();
            for (TipoEncargo tipoEncargo : tipoEncargos) {
                map.put(tipoEncargo.getId(), tipoEncargo.getNombre());
            }
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }

        for (Equipaje e : equipajes) {
            Object[] o = new Object[6]; //TODO
            o[0] = e.getId();
            o[1] = e.getEncargo().getId(); //TODO
            o[2] = map.get(e.getTipoEncargo().getId()); //TODO
            o[3] = e.getDetalle();
            o[4] = e.getFechaIngreso();
            //o[5] = e.getFechaSalida(); //TODO
            o[5] = e.getValor(); //TODO

            getEquipajeModel().addRow(o);
        }
    }

    private void abonarEncargo(boolean monstrarMensaje) {
        if(Objects.isNull(encargoId.getText()) || Objects.nonNull(encargoId.getText()) &&
                encargoId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operación fallida, seleccione encargo!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Encargo encargo = EncargoService.buscarEncargo(new Encargo(Integer.valueOf(encargoId.getText())));

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.getRootPanel());
            final JDialog jDialog = new JDialog(topFrame, "Abonar", true);
            jDialog.setContentPane(new AbonoGUI(encargo).getRootPanel());
            jDialog.pack();
            jDialog.setVisible(true);
            jDialog.setLocationRelativeTo(null);

            buscarEncargo(monstrarMensaje, true, false);

            limpiarEquipajeTable();

            cantidadField.setText("");
            porcentajeField.setText("");
            totalField.setText("");
            porcentajeField.setEditable(false);
            guardarButton.setEnabled(false);
            encargoId.setText("");
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
    }

    public DefaultTableModel getClienteModel() {
        return clienteModel;
    }

    public static DefaultTableModel getEncargoModel() {
        return encargoModel;
    }

    public static DefaultTableModel getEquipajeModel() {
        return equipajeModel;
    }

    public JTextField getBuscarTextField() {
        return buscarTextField;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JLabel getClienteId() {
        return clienteId;
    }

    public JTextField getCantidadField() {
        return cantidadField;
    }
}
