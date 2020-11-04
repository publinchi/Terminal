package com.terminal.view;

import com.terminal.dto.Cliente;
import com.terminal.dto.TipoIdentificacion;
import com.terminal.exception.EmptyField;
import com.terminal.service.ClienteService;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Objects;

public class ClienteGUI {
    private JPanel rootPanel;
    private JTextField nombreTextField;
    private JTextField apellidoTextField;
    private JTextField telefonoTextField;
    private JTextField identificacionTextField;
    private JComboBox tipoComboBox;
    private JButton guardarButton;
    private JButton cancelarButton;

    public ClienteGUI(Cliente cliente) {
        tipoComboBox.addItem(new TipoIdentificacion(1, "Cédula"));
        tipoComboBox.addItem(new TipoIdentificacion(2, "Pasaporte"));

        if(Objects.nonNull(cliente)) {
            setData(cliente);
            tipoComboBox.setSelectedIndex(cliente.getTipoIdentificacion().getId() - 1);
        }

        guardarButton.addActionListener(e -> guardarCliente(cliente));

        cancelarButton.addActionListener(e -> dispose());

        nombreTextField.addActionListener(e -> guardarCliente(cliente));

        apellidoTextField.addActionListener(e -> guardarCliente(cliente));

        telefonoTextField.addActionListener(e -> guardarCliente(cliente));

        identificacionTextField.addActionListener(e -> guardarCliente(cliente));
    }

    public ClienteGUI() {
        this(null);
    }

    private void guardarCliente(Cliente cliente) {
        if(Objects.isNull(cliente))
            crearCliente();
        else
            editarCliente(cliente);
    }

    private void crearCliente() {
        try {
            evaluarCampo(nombreTextField);
            evaluarCampo(apellidoTextField);
            evaluarCampo(telefonoTextField);
            evaluarCampo(identificacionTextField);
            evaluarCampo(tipoComboBox);
        } catch (EmptyField emptyField) {
            return;
        }

        Cliente cliente = new Cliente();
        getData(cliente);

        try {
            ClienteService.crearCliente(cliente);
            JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                    , "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
        dispose();
    }

    private void editarCliente(Cliente cliente) {
        try {
            if(isModified(cliente)) {
                getData(cliente);
                ClienteService.editarCliente(cliente);
                JOptionPane.showMessageDialog(null, "Operación realizada correctamente ",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "No se realizaron cambios!!! ",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            System.err.println(throwables);
        }
        dispose();
    }

    private void evaluarCampo(JComponent component) throws EmptyField {
        if(component instanceof JTextField) {
            if (((JTextField) component).getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el " + component.getName(),
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                throw new EmptyField();
            }
        } else if(component instanceof JComboBox)
            if(Objects.isNull(((JComboBox) component).getSelectedItem())) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el " + component.getName(),
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                throw new EmptyField();
            }
    }

    private void dispose() {
        JDialog topFrame = (JDialog) SwingUtilities.getWindowAncestor(this.getRootPanel());
        topFrame.dispose();
    }

    public void setData(Cliente data) {
        nombreTextField.setText(data.getNombre());
        apellidoTextField.setText(data.getApellido());
        telefonoTextField.setText(data.getTelefono());
        identificacionTextField.setText(data.getIdentificacion());
        tipoComboBox.setSelectedItem(data.getTipoIdentificacion());
    }

    public void getData(Cliente data) {
        data.setNombre(nombreTextField.getText());
        data.setApellido(apellidoTextField.getText());
        data.setTelefono(telefonoTextField.getText());
        data.setIdentificacion(identificacionTextField.getText());
        data.setTipoIdentificacion((TipoIdentificacion) tipoComboBox.getSelectedItem());
    }

    public boolean isModified(Cliente data) {
        if (nombreTextField.getText() != null ? !nombreTextField.getText().equals(data.getNombre())
                : data.getNombre() != null)
            return true;
        if (apellidoTextField.getText() != null ? !apellidoTextField.getText().equals(data.getApellido())
                : data.getApellido() != null)
            return true;
        if (telefonoTextField.getText() != null ? !telefonoTextField.getText().equals(data.getTelefono())
                : data.getTelefono() != null)
            return true;
        if (identificacionTextField.getText() != null ? !identificacionTextField.getText().equals(data.getIdentificacion())
                : data.getIdentificacion() != null)
            return true;

        Integer idTipoIdentificacion = tipoComboBox.getSelectedIndex() + 1;

        if (idTipoIdentificacion != null ? !idTipoIdentificacion.equals(data.getTipoIdentificacion().getId())
                : data.getTipoIdentificacion().getId() != null)
            return true;
        return false;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
