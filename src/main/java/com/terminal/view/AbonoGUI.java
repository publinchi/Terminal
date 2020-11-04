package com.terminal.view;

import com.terminal.dto.Encargo;
import com.terminal.service.EncargoService;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AbonoGUI {
    private JPanel rootPanel;
    private JTextField abonoTextField;
    private JButton guardarButton;
    private JButton cancelarButton;

    public AbonoGUI(Encargo encargo) {
        guardarButton.addActionListener(e -> abonar(encargo));
        cancelarButton.addActionListener(e -> dispose());
    }

    private void abonar(Encargo encargo) {
        if(getAbonoTextField().getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operación fallida, ingrese abono!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double abono = Double.valueOf(getAbonoTextField().getText());
        Double saldo;
        if(encargo.getDescuento().doubleValue() > 0) {
            saldo = encargo.getSaldo() - (encargo.getSaldo() * encargo.getDescuento() / 100);
            BigDecimal bd = new BigDecimal(Double.toString(saldo));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            saldo = bd.doubleValue();
        } else
            saldo = encargo.getSaldo();

        java.sql.Date fechaSalida = null;

        System.out.println("saldo 1: " + saldo);
        System.out.println("abono 1: " + abono);
        System.out.println("new abono 1: " + getAbonoTextField().getText());

        if(saldo > 0 && abono > 0 && saldo >= abono) {
            if(encargo.getDescuento() > 0) {
                saldo = encargo.getSaldo() - (encargo.getSaldo() * abono / saldo);
                BigDecimal bd = new BigDecimal(Double.toString(saldo));
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                saldo = bd.doubleValue();
            } else
                saldo = saldo - abono;
            abono = encargo.getAbono() + abono;
            if(saldo == 0)
                fechaSalida = new java.sql.Date(System.currentTimeMillis());

            System.out.println("saldo 2: " + saldo);
            System.out.println("abono 2: " + abono);
            System.out.println("new abono 2: " + getAbonoTextField().getText());

            encargo.setAbono(abono);
            encargo.setSaldo(saldo);
            encargo.setFechaSalida(fechaSalida);
            EncargoService.editarEncargo(encargo);
            dispose();
        } else {
            getAbonoTextField().setText("");
            JOptionPane.showMessageDialog(null, "Operación fallida, abono superior al saldo!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void dispose() {
        JDialog topFrame = (JDialog) SwingUtilities.getWindowAncestor(this.getRootPanel());
        topFrame.dispose();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JTextField getAbonoTextField() {
        return abonoTextField;
    }


}
