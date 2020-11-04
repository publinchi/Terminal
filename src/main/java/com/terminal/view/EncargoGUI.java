package com.terminal.view;

import com.terminal.dto.Cliente;
import com.terminal.dto.Encargo;
import com.terminal.exception.EmptyField;
import com.terminal.service.EncargoService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class EncargoGUI {
    private JPanel rootPanel;
    private JTextField clienteField;
    private JTextArea detalleTextArea;
    private JButton guardarButton;
    private JButton cancelarButton;

    public EncargoGUI(Encargo encargo){
        clienteField.setText(encargo.getCliente().getNombre() + " " + encargo.getCliente().getApellido());

        guardarButton.addActionListener(e -> guardarEncargo(encargo));

        cancelarButton.addActionListener(e -> dispose());
    }

    private void dispose() {
        JDialog topFrame = (JDialog) SwingUtilities.getWindowAncestor(this.getRootPanel());
        topFrame.dispose();
    }

    private void guardarEncargo(Encargo encargo) {
        try {
            encargo.setDetalle(getDetalleTextArea().getText());
            EncargoService.crearEncargo(encargo);
            JOptionPane.showMessageDialog(null, "Operación realizada correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);

            List<Encargo> encargos = EncargoService.buscarEncargos(encargo.getCliente(), false);

            MainGUI.cargarEncargos(encargo.getCliente().getId(), encargos);

            dispose();
        } catch (SQLException | EmptyField e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!", "Falla",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTextArea getDetalleTextArea() {
        return detalleTextArea;
    }
}
