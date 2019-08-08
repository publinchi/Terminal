import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    public static void crearCliente(Terminal terminal) {
        Cliente cliente = new Cliente();
        cliente.setId(null);
        cliente.setApellido(terminal.getApellidoField().getText());
        cliente.setIdentificacion(terminal.getIdentificacionField().getText());
        cliente.setNombre(terminal.getNombreField().getText());
        cliente.setTipoIdentificacion(new TipoIdentificacion(((TipoIdentificacion)terminal.getTipoBox()
                .getSelectedItem()).getId()));
        cliente.setTelefono(terminal.getTelefonoField().getText());

        Connection connection = null;

        try {
            validarCliente(cliente);

            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into cliente " +
                    "(identificacion, nombre, apellido, telefono, tipo_identificacion) values (?, ?, ? ,?, ?)");
            preparedStatement.setString(1, cliente.getIdentificacion());
            preparedStatement.setString(2, cliente.getNombre());
            preparedStatement.setString(3, cliente.getApellido());
            preparedStatement.setString(4, cliente.getTelefono());
            preparedStatement.setInt(5, cliente.getTipoIdentificacion().getId());
            preparedStatement.execute();

            terminal.getNombreField().setText("");
            terminal.getApellidoField().setText("");
            terminal.getIdentificacionField().setText("");
            terminal.getTipoBox().setSelectedIndex(0);
            terminal.getTelefonoField().setText("");

            clearTable(terminal);

            JOptionPane.showMessageDialog(null, "Operación realizada correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException | EmptyField e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void buscarCliente(Terminal terminal) {
        List<Cliente> clientes = new ArrayList<>();

        Cliente cliente = new Cliente();
        cliente.setApellido(terminal.getApellidoField().getText());
        cliente.setIdentificacion(terminal.getIdentificacionField().getText());
        cliente.setNombre(terminal.getNombreField().getText());
        cliente.setTipoIdentificacion(new TipoIdentificacion(((TipoIdentificacion)terminal.getTipoBox()
                .getSelectedItem()).getId()));
        cliente.setTelefono(terminal.getTelefonoField().getText());

        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from cliente where " +
                    "nombre = ? or apellido = ? or identificacion = ? or telefono = ?");
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getApellido());
            preparedStatement.setString(3, cliente.getIdentificacion());
            preparedStatement.setString(4, cliente.getTelefono());
            ResultSet resultSet = preparedStatement.executeQuery();

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

            clearTable(terminal);

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
                terminal.getClienteModel().addRow(o);
            }
            JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                    + clientes.size(), "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void eliminarCliente(Terminal terminal) {
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from cliente where id = ?");
            preparedStatement.setInt(1, Integer.valueOf(terminal.getClienteTable()
                    .getValueAt(terminal.getClienteTable().getSelectedRow(), 0).toString()));
            preparedStatement.execute();

            terminal.getiIdentificacionField().setText("");
            terminal.getiNombreField().setText("");
            terminal.getiApellidoField().setText("");
            terminal.getiTelefonoField().setText("");
            terminal.getiTipoField().setText("");

            clearTable(terminal);

            JOptionPane.showMessageDialog(null, "Operación realizada correctamente ",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void editarCliente(Terminal terminal) {
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update cliente set " +
                    "identificacion = ?, nombre = ?, apellido = ?, telefono = ? where id = ?");
            preparedStatement.setString(1, terminal.getiIdentificacionField().getText());
            preparedStatement.setString(2, terminal.getiNombreField().getText());
            preparedStatement.setString(3, terminal.getiApellidoField().getText());
            preparedStatement.setString(4, terminal.getiTelefonoField().getText());
            preparedStatement.setInt(5, Integer.valueOf(terminal.getClienteTable()
                    .getValueAt(terminal.getClienteTable().getSelectedRow(), 0).toString()));
            preparedStatement.execute();

            terminal.getiIdentificacionField().setText("");
            terminal.getiNombreField().setText("");
            terminal.getiApellidoField().setText("");
            terminal.getiTelefonoField().setText("");
            terminal.getiTipoField().setText("");

            clearTable(terminal);

            JOptionPane.showMessageDialog(null, "Operación realizada correctamente ",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Operación fallida!!!",
                    "Falla", JOptionPane.ERROR_MESSAGE);
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

    private static void clearTable(Terminal terminal) {
        terminal.getClienteModel().setRowCount(0);
    }
}

