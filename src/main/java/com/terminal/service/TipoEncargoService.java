package com.terminal.service;

import com.terminal.dto.TipoEncargo;
import com.terminal.util.DBCPDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoEncargoService {

    public static List loadTipoEncargos() throws SQLException {
        List<TipoEncargo> tipoEncargos = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from tipo_encargo");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TipoEncargo tipoEncargo = new TipoEncargo(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getDouble("valor")
                );

                // Now we can fetch the data by column name, save and use them!
                tipoEncargos.add(tipoEncargo);
            }

            return tipoEncargos;
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
