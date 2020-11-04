package com.terminal.service;

import com.google.zxing.WriterException;
import com.terminal.dto.Cliente;
import com.terminal.dto.Encargo;
import com.terminal.exception.EmptyField;
import com.terminal.util.DBCPDataSource;
import com.terminal.util.QRCodeGenerator;
import com.terminal.util.QrCapture;
import com.terminal.view.MainGUI;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class EncargoService {

    public static void crearEncargo(Encargo encargo) throws SQLException, EmptyField {
        Connection connection = null;

        try {
            validarEncargo(encargo);

            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into encargo (detalle, " +
                    "fecha_ingreso, cliente, abono, saldo) values (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, encargo.getDetalle());
            preparedStatement.setDate(2, new java.sql.Date(encargo.getFechaIngreso().getTime()));
            preparedStatement.setInt(3, encargo.getCliente().getId());
            preparedStatement.setDouble(4, 0.0);
            preparedStatement.setDouble(5, 0.0);

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

    public static List<Encargo> buscarEncargos(Cliente cliente, boolean usarCamara) throws SQLException {
        final List<Encargo>[] encargos = new List[1];

        if(usarCamara) {
            final Thread thread = new Thread(() -> {
                try (QrCapture qr = new QrCapture()) {
                    String encoded = qr.getResult();
                    Encargo encargo = new Encargo(Integer.valueOf(fromString(encoded)));
                    System.out.println("encargo -> " + encargo.getId());
                    Toolkit.getDefaultToolkit().beep(); // https://www.rgagnon.com/javadetails/java-0001.html

                    encargos[0] = _buscarEncargo(null, encargo.getId());

                    encargo = encargos[0].get(0);

                    MainGUI.cargarEncargos(encargo.getCliente().getId(), encargos[0]);

                    MainGUI.limpiarEquipajeTable();

                    if(usarCamara == true && MainGUI.getEncargoModel().getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Operación fallida," +
                                        " el encargo ya ha sido entregado!!!",
                                "Falla", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | SQLException ex) {
                    System.err.println(ex);
                    JOptionPane.showMessageDialog(null, "Operación fallida," +
                                    " código QR inválido!!!",
                            "Falla", JOptionPane.ERROR_MESSAGE);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
        } else {
            encargos[0] = _buscarEncargo(cliente, null);
            System.out.println(encargos[0].size());
        }
        return encargos[0];
    }

    public static Encargo buscarEncargo(Encargo encargo) throws SQLException {
        List<Encargo> encargos = _buscarEncargo(null, encargo.getId());
        return encargos.get(0);
    }

    private static List<Encargo> _buscarEncargo(Cliente cliente, Integer encargoId) throws SQLException {
        Encargo encargo = new Encargo();
        if(Objects.nonNull(cliente) && cliente.getId() != null && encargoId != null) {
            encargo.setId(encargoId);
            encargo.setCliente(cliente);
        } else if(encargoId != null) {
            encargo.setId(encargoId);
        } else
            encargo.setCliente(cliente);

        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement;
            if(Objects.nonNull(cliente) && cliente.getId() != null && encargoId != null) {
                preparedStatement = connection.prepareStatement("select * from encargo where " +
                        "cliente = ? and id = ?");
                preparedStatement.setInt(1, encargo.getCliente().getId());
                preparedStatement.setInt(2, encargo.getId());
            } else if(encargoId != null) {
                preparedStatement = connection.prepareStatement("select * from encargo where " +
                        "id = ?");
                preparedStatement.setInt(1, encargo.getId());
            } else {
                preparedStatement = connection.prepareStatement("select * from encargo where " +
                        "cliente = ?");
                preparedStatement.setInt(1, encargo.getCliente().getId());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Encargo> encargos = new ArrayList();

            while (resultSet.next()) {
                Encargo e = new Encargo();
                e.setId(resultSet.getInt("id"));
                e.setDetalle(resultSet.getString("detalle"));
                e.setFechaIngreso(resultSet.getDate("fecha_ingreso"));
                e.setFechaSalida(resultSet.getDate("fecha_salida"));
                if(Objects.isNull(cliente)) {
                    cliente = ClienteService.buscarCliente(resultSet.getInt("cliente"));
                }
                e.setCliente(cliente);
                e.setAbono(resultSet.getDouble("abono"));
                e.setSaldo(resultSet.getDouble("saldo"));
                e.setDescuento(resultSet.getInt("descuento"));
                encargos.add(e);
            }

            return encargos;
        } finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }

    public static void editarEncargo(Encargo encargo) {
        Connection connection = null;

        try {
            connection = DBCPDataSource.getConnection();
            if(Objects.isNull(encargo.getDescuento())) {
                PreparedStatement preparedStatement = connection.prepareStatement("update encargo set " +
                        "saldo = ?, abono = ?, fecha_salida = ? where id = ?");
                preparedStatement.setDouble(1, encargo.getSaldo());
                preparedStatement.setDouble(2, encargo.getAbono());
                preparedStatement.setDate(3, (java.sql.Date) encargo.getFechaSalida());
                preparedStatement.setInt(4, encargo.getId());
                preparedStatement.execute();
            } else {
                PreparedStatement preparedStatement = connection.prepareStatement("update encargo set " +
                        "saldo = ?, abono = ?, fecha_salida = ?, descuento = ? where id = ?");
                preparedStatement.setDouble(1, encargo.getSaldo());
                preparedStatement.setDouble(2, encargo.getAbono());
                preparedStatement.setDate(3, (java.sql.Date) encargo.getFechaSalida());
                preparedStatement.setInt(4, encargo.getDescuento());
                preparedStatement.setInt(5, encargo.getId());
                preparedStatement.execute();
            }

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

    private static void validarEncargo(Encargo encargo) throws EmptyField {
        /*
        if(encargo.getAbono().toString().trim().isEmpty() || encargo.getCliente().equals(null)
                || encargo.getDetalle().trim().isEmpty() || encargo.getFechaIngreso().equals(null)
                || encargo.getSaldo().toString().trim().isEmpty() || encargo.getTipoEncargo().equals(null)) {
            throw new EmptyField();
        }
         */
    }

    public static void imprimirEncargo(Encargo encargo) {
        if(encargo.getId() == null) {
            JOptionPane.showMessageDialog(null, "Operación fallida!!!", "Falla",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtienes el mes actual
        int day = LocalDate.now().getDayOfMonth();
        Month mes = LocalDate.now().getMonth();
        int year = LocalDate.now().getYear();

        // Obtienes el nombre del mes
        String nombre = mes.getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));

        String[] textos = new String[8];
        textos[0] = "CI: " + encargo.getCliente().getIdentificacion();
        textos[1] = "Nombre: " + encargo.getCliente().getNombre() + " " + encargo.getCliente().getApellido();
        textos[2] = "Detalle: " + encargo.getEquipajes().size() + " equipaje(s)";
        textos[3] = "Fecha: " + day + " de " + nombre + " de " + year;
        textos[4] = "Ticket: " + encargo.getId();
        textos[5] = "Abono: $" + encargo.getAbono();
        textos[6] = "Saldo: $" + encargo.getSaldo();

        Double total;
        if(encargo.getDescuento().doubleValue() > 0) {
            total = encargo.getSaldo() - (encargo.getSaldo() * encargo.getDescuento() / 100);
            BigDecimal bd = new BigDecimal(Double.toString(total));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            total = bd.doubleValue();
        } else
            total = encargo.getSaldo();

        textos[7] = "Total(- %" + encargo.getDescuento() +"): $" + total;

        System.out.println("CI -> " + textos[0]);
        System.out.println("Nombre -> " + textos[1]);
        System.out.println("Detalle -> " + textos[2]);
        System.out.println("Fecha -> " + textos[3]);
        System.out.println("Ticket -> " + textos[4]);
        System.out.println("Abono -> " + textos[5]);
        System.out.println("Saldo -> " + textos[6]);
        System.out.println("Total -> " + textos[7]);

        String encoded;
        try {
            encoded = toString(encargo.getId().toString());
            System.out.println("encoded -> " + encoded);
            System.out.println("decoded -> " + fromString(encoded));
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        BufferedImage image = null;

        try {
            image = QRCodeGenerator.getQRCodeImage2(encoded, 400, 400);
        } catch (WriterException we) {
            System.err.println("Could not generate QR Code, WriterException :: " + we.getMessage());
        }

        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) concatImage(image, textos), "png", baos);
            //ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            OutputStream out = new FileOutputStream(System.getProperty("user.home") + "/out.png");
            out.write(imageInByte);
            out.close();
            if(0 == 0) {
                DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                DocPrintJob docPrintJob = printService.createPrintJob();
                Doc imagen = new SimpleDoc(imageInByte, flavor, null);
                docPrintJob.print(imagen, null);
            }
        } catch (PrintException | IOException  pe) {
            System.err.println(pe);
        }
    }

    private static Image concatImage(BufferedImage image , String[] textos) {
        int imagesCount = 2;
        int textSize = 40;
        int fixed = 0;
        int pos = 0;
        BufferedImage images[] = new BufferedImage[imagesCount];

        System.out.println("H " + image.getHeight());
        System.out.println("W " + image.getWidth());

        images[0] = image;

        int horizontal = image.getWidth();// - (image.getWidth() * 10 / 100);

        images[1] = new BufferedImage(horizontal,textSize * textos.length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = images[1].createGraphics();
        g2d.fillRect(0, 0, horizontal,textSize * textos.length);
        float alpha = 1f;
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alphaComposite);
        g2d.setPaint(Color.BLACK);
        g2d.setBackground(Color.WHITE);

        g2d.setFont(g2d.getFont().deriveFont(20f));

        for (String texto: textos) {
            pos = pos + textSize;
            g2d.drawString(texto, 0, pos);
        }
        g2d.dispose();

        int heightTotal = 0;
        for(int j = 0; j < images.length; j++) {
            heightTotal += images[j].getHeight();
        }

        int heightCurr = 0;
        BufferedImage concatImage = new BufferedImage(image.getWidth(), heightTotal - fixed,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2df = concatImage.createGraphics();
        for(int j = 0; j < images.length; j++) {
            g2df.drawImage(images[j], 0, heightCurr, null);
            heightCurr += images[j].getHeight() - fixed;
        }
        g2df.dispose();

        return concatImage;
    }

    private static void showMessage(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    /** Read the object from Base64 string. */
    private static String fromString(String encoded) throws IOException ,
            ClassNotFoundException {
        //byte [] data = Base64.getDecoder().decode( s );
        //ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        //Object o  = ois.readObject();
        //ois.close();
        //return o;
        return new String(Base64.getDecoder().decode(encoded));
    }

    /** Write the object to a Base64 string. */
    private static String toString(String decoded) throws IOException {
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        //oos.writeObject(o);
        //oos.close();
        //return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        return Base64.getEncoder().encodeToString(decoded.getBytes());
    }
}