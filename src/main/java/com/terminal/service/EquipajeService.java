package com.terminal.service;

import com.terminal.dto.Encargo;
import com.terminal.dto.Equipaje;
import com.terminal.dto.TipoEncargo;
import com.terminal.exception.EmptyField;
import com.terminal.util.DBCPDataSource;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EquipajeService {

    public static void crearEquipaje(Equipaje equipaje) throws SQLException, EmptyField {
        Connection connection = null;

        try {
            validarEquipaje(equipaje);

            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into equipaje (detalle, " +
                    "fecha_ingreso, encargo, tipo_encargo, valor) values (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, equipaje.getDetalle());
            preparedStatement.setDate(2, new java.sql.Date(equipaje.getFechaIngreso().getTime()));
            preparedStatement.setInt(3, equipaje.getEncargo().getId());
            preparedStatement.setInt(4, equipaje.getTipoEncargo().getId());
            preparedStatement.setDouble(5, equipaje.getValor());

            preparedStatement.execute();
        } finally {
            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static List buscarEquipajes(Encargo encargo) throws SQLException {
        final List[] equipajes = new List[1];

        equipajes[0] = _buscarEquipajes(encargo, null);
        System.out.println(equipajes[0].size());
        return equipajes[0];
    }

    public static Equipaje buscarEquipaje(Equipaje equipaje) throws SQLException {
        List<Equipaje> equipajes;

        equipajes = _buscarEquipajes(null, equipaje.getId());
        return equipajes.get(0);
    }

    private static List<Equipaje> _buscarEquipajes(Encargo encargo, Integer equipajeId) throws SQLException {
        Equipaje equipaje = new Equipaje();
        if(encargo.getId() != null && equipajeId != null) {
            equipaje.setId(equipajeId);
            equipaje.setEncargo(encargo);
        } else if(equipajeId != null) {
            equipaje.setId(equipajeId);
        } else
            equipaje.setEncargo(encargo);

        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement;
            if(Objects.nonNull(encargo) && Objects.nonNull(encargo.getId()) && equipajeId != null) {
                preparedStatement = connection.prepareStatement("select * from equipaje where " +
                        "encargo = ? and id = ?");
                preparedStatement.setInt(1, equipaje.getEncargo().getId());
                preparedStatement.setInt(2, equipaje.getId());
            } else if(equipajeId != null) {
                preparedStatement = connection.prepareStatement("select * from equipaje where " +
                        "id = ?");
                preparedStatement.setInt(1, equipaje.getId());
            } else {
                preparedStatement = connection.prepareStatement("select * from equipaje where " +
                        "encargo = ?");
                preparedStatement.setInt(1, equipaje.getEncargo().getId());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Equipaje> equipajes = new ArrayList();

            while (resultSet.next()) {
                Equipaje e = new Equipaje();
                e.setId(resultSet.getInt("id"));
                e.setDetalle(resultSet.getString("detalle"));
                e.setFechaIngreso(resultSet.getDate("fecha_ingreso"));
                e.setFechaSalida(resultSet.getDate("fecha_salida"));
                e.setEncargo(new Encargo(resultSet.getInt("encargo")));
                e.setTipoEncargo(new TipoEncargo(resultSet.getInt("tipo_encargo")));
                e.setValor(resultSet.getDouble("valor"));
                equipajes.add(e);
            }

            return equipajes;
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void editarEquipaje(Equipaje equipaje) {
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection(); //TODO
            //PreparedStatement preparedStatement = connection.prepareStatement("update encargo set " + //TODO
            //        "saldo = ?, abono = ?, fecha_salida = ? where id = ?");//TODO
            //preparedStatement.setDouble(1, encargo.getSaldo());//TODO
            //preparedStatement.setDouble(2, encargo.getAbono());//TODO
            //preparedStatement.setDate(3, (java.sql.Date) encargo.getFechaSalida());//TODO
            //preparedStatement.setInt(4, encargo.getId());//TODO
            //preparedStatement.execute();//TODO

            //clearTable(terminalGUI); //TODO

            JOptionPane.showMessageDialog(null, "Operación realizada correctamente ",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!", "Falla",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }
}
