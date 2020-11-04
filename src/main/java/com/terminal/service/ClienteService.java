package com.terminal.service;

import com.terminal.dto.Cliente;
import com.terminal.dto.TipoIdentificacion;
import com.terminal.exception.EmptyField;
import com.terminal.util.DBCPDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClienteService {

    public static void crearCliente(Cliente cliente) throws SQLException {
        Cliente c = new Cliente();
        c.setId(null);
        c.setApellido(cliente.getApellido());
        c.setIdentificacion(cliente.getIdentificacion());
        c.setNombre(cliente.getNombre());
        c.setTipoIdentificacion(cliente.getTipoIdentificacion());
        c.setTelefono(cliente.getTelefono());

        Connection connection = null;

        try {
            //validarCliente(cliente);

            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into cliente " +
                    "(identificacion, nombre, apellido, telefono, tipo_identificacion) values (?, ?, ? ,?, ?)");
            preparedStatement.setString(1, cliente.getIdentificacion());
            preparedStatement.setString(2, cliente.getNombre());
            preparedStatement.setString(3, cliente.getApellido());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setInt(5, cliente.getTipoIdentificacion().getId());
            preparedStatement.execute();

        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static Cliente buscarCliente(Integer id) throws SQLException {
        List<Cliente> clientes = buscarClientes(null, id.toString());
        return clientes.get(0);
    }

    public static Cliente buscarClientePorIdentificacion(String identificacion) throws SQLException {
        Cliente cliente = new Cliente();

        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement;

            if(Objects.nonNull(identificacion)) {
                if (identificacion.matches("[0-9]+")) {
                    cliente.setIdentificacion(identificacion);
                } else {
                    return null;
                }

                preparedStatement = connection.prepareStatement("select * from cliente where " +
                        "identificacion = ?");
                preparedStatement.setInt(1, Integer.valueOf(identificacion));
            } else {
                return null;
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Cliente> clientes = new ArrayList<>();

            while (resultSet.next()) {
                Cliente c = new Cliente();

                // Now we can fetch the data by column name, save and use them!
                c.setId(resultSet.getInt("id"));
                c.setNombre(resultSet.getString("nombre"));
                c.setApellido(resultSet.getString("apellido"));
                c.setIdentificacion(resultSet.getString("identificacion"));
                c.setTelefono(resultSet.getString("telefono"));
                c.setTipoIdentificacion(new TipoIdentificacion(resultSet.getInt("tipo_identificacion")));
                clientes.add(c);
            }

            if(clientes.size() == 0)
                return null;

            return clientes.get(0);
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static List<Cliente> buscarClientes(String criteria, String... criterias) throws SQLException {
        Cliente cliente = new Cliente();

        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement;

            if(Objects.nonNull(criteria)) {
                if (criteria.matches("[0-9]+")) {
                    cliente.setIdentificacion(criteria);
                    cliente.setTelefono(criteria);
                } else {
                    cliente.setApellido(criteria);
                    cliente.setNombre(criteria);
                }

                preparedStatement = connection.prepareStatement("select * from cliente where " +
                        "nombre = ? or apellido = ? or identificacion = ? or telefono = ?");
                preparedStatement.setString(1, cliente.getNombre());
                preparedStatement.setString(2, cliente.getApellido());
                preparedStatement.setString(3, cliente.getIdentificacion());
                preparedStatement.setString(4, cliente.getTelefono());
            } else {
                preparedStatement = connection.prepareStatement("select * from cliente where " +
                        "id = ?");
                preparedStatement.setInt(1, Integer.valueOf(criterias[0]));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Cliente> clientes = new ArrayList<>();

            while (resultSet.next()) {
                Cliente c = new Cliente();

                // Now we can fetch the data by column name, save and use them!
                c.setId(resultSet.getInt("id"));
                c.setNombre(resultSet.getString("nombre"));
                c.setApellido(resultSet.getString("apellido"));
                c.setIdentificacion(resultSet.getString("identificacion"));
                c.setTelefono(resultSet.getString("telefono"));
                c.setTipoIdentificacion(new TipoIdentificacion(resultSet.getInt("tipo_identificacion")));
                clientes.add(c);
            }

            return clientes;
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void eliminarCliente(int id) throws SQLException {
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from cliente where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void editarCliente(Cliente cliente) throws SQLException {
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update cliente set " +
                    "identificacion = ?, nombre = ?, apellido = ?, telefono = ?, tipo_identificacion = ? where id = ?");
            preparedStatement.setString(1, cliente.getIdentificacion());
            preparedStatement.setString(2, cliente.getNombre());
            preparedStatement.setString(3, cliente.getApellido());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setInt(5, cliente.getTipoIdentificacion().getId());
            preparedStatement.setInt(6, cliente.getId());
            preparedStatement.execute();
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    private static void validarCliente(Cliente cliente) throws EmptyField {
        if(cliente.getTipoIdentificacion().equals("") || cliente.getTelefono().equals("")
                || cliente.getApellido().equals("") || cliente.getNombre().equals("")
                || cliente.getIdentificacion().equals("")) {
            throw new EmptyField();
        }
    }
}

