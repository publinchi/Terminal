package com.terminal.view;

import com.terminal.dto.Encargo;
import com.terminal.dto.Equipaje;
import com.terminal.dto.TipoEncargo;
import com.terminal.exception.EmptyField;
import com.terminal.service.EncargoService;
import com.terminal.service.EquipajeService;
import com.terminal.service.TipoEncargoService;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EquipajeGUI {
    public JPanel rootPanel;
    private JComboBox tipoEncargoComboBox;
    private JTextArea descripcionTextArea;
    private JButton guardarButton;
    private JFormattedTextField valorFormattedTextField;
    private JButton cancelarButton;
    private List tipoEncargos;
    private List equipajes;

    public EquipajeGUI(Equipaje equipaje) throws SQLException {
        List<TipoEncargo> tipoEncargos = TipoEncargoService.loadTipoEncargos();

        getTipoEncargoComboBox().removeAllItems();

        getTipoEncargoComboBox().addItem("--- Seleccione un Tipo ---");
        for (TipoEncargo tipoEncargo : tipoEncargos) {
            getTipoEncargoComboBox().addItem(tipoEncargo);
        }

        TipoEncargo te = new TipoEncargo(0);
        te.setNombre("OTROS");
        getTipoEncargoComboBox().addItem(te);

        tipoEncargoComboBox.addActionListener(e -> {
            TipoEncargo tipoEncargo = (TipoEncargo) tipoEncargoComboBox.getSelectedItem();
            if("OTROS" != tipoEncargo.getNombre()) {
                valorFormattedTextField.setValue(tipoEncargo.getValor());
                valorFormattedTextField.setEditable(false);
                guardarButton.setEnabled(true);
            } else {
                valorFormattedTextField.setEditable(true);
                valorFormattedTextField.setValue(0);
                guardarButton.setEnabled(false);
            }
        });

        valorFormattedTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(valorFormattedTextField.getText().trim().isEmpty()) {
                    valorFormattedTextField.setText("0");
                }

                Number number;
                NumberFormat amountFormat = NumberFormat.getCurrencyInstance(Locale.US);

                if(!valorFormattedTextField.getText().contains("$")) {
                    valorFormattedTextField.setText("$" + valorFormattedTextField.getText());
                }

                try {
                    number = amountFormat.parse(valorFormattedTextField.getText());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    guardarButton.setEnabled(false);
                    return;
                }

                valorFormattedTextField.setValue(number);
                if(Objects.nonNull(valorFormattedTextField.getValue()) && ((Number)valorFormattedTextField.getValue())
                        .doubleValue() > 0)
                    guardarButton.setEnabled(true);
                else
                    guardarButton.setEnabled(false);
            }
        });

        guardarButton.addActionListener(e -> guardarEquipaje(equipaje));

        cancelarButton.addActionListener(e -> dispose());
    }

    private void guardarEquipaje(Equipaje equipaje) {
        try {
            equipaje.setFechaIngreso(Calendar.getInstance().getTime());
            equipaje.setDetalle(descripcionTextArea.getText());
            equipaje.setTipoEncargo((TipoEncargo) tipoEncargoComboBox.getSelectedItem());
            equipaje.setValor(((Number)valorFormattedTextField.getValue()).doubleValue());

            EquipajeService.crearEquipaje(equipaje);

            Encargo encargo = equipaje.getEncargo();

            encargo.setSaldo(equipaje.getValor() + equipaje.getEncargo().getSaldo());

            EncargoService.editarEncargo(encargo);

            JOptionPane.showMessageDialog(null, "Operación realizada correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);

            List<Equipaje> equipajes = EquipajeService.buscarEquipajes(equipaje.getEncargo());

            MainGUI.cargarEquipajes(equipajes);


            dispose();
        } catch (SQLException | EmptyField e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!", "Falla",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dispose() {
        JDialog topFrame = (JDialog) SwingUtilities.getWindowAncestor(this.getRootPanel());
        topFrame.dispose();
    }

    private void createUIComponents() {
        NumberFormat amountFormat = NumberFormat.getCurrencyInstance(Locale.US);
        valorFormattedTextField = new JFormattedTextField(amountFormat);
        valorFormattedTextField.setEditable(false);
        // TODO: place custom component creation code here
    }

    public JComboBox getTipoEncargoComboBox() {
        return tipoEncargoComboBox;
    }

    public List getTipoEncargos() {
        return tipoEncargos;
    }

    public void setTipoEncargos(List tipoEncargos) {
        this.tipoEncargos = tipoEncargos;
    }

    public List getEquipajes() {
        return equipajes;
    }

    public void setEquipajes(List equipajes) {
        this.equipajes = equipajes;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
