    import com.google.zxing.WriterException;

    import javax.imageio.ImageIO;
    import javax.print.*;
    import javax.swing.*;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.*;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.time.LocalDate;
    import java.time.Month;
    import java.time.format.TextStyle;
    import java.util.Base64;
    import java.util.Date;
    import java.util.Locale;

    public class EncargoService {

        public static void crearEncargo(Terminal terminal) {
            Encargo encargo = new Encargo();

            Connection connection = null;

            try {
                encargo.setId(null);

                if(terminal.getClienteId().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operación fallida, seleccione cliente!!!",
                            "Falla", JOptionPane.ERROR_MESSAGE);
                    throw new EmptyField();
                }

                if(((Integer)terminal.getCantidadSpinner().getValue()) == 0) {
                    JOptionPane.showMessageDialog(null, "Operación fallida, ingrese cantidad!!!",
                            "Falla", JOptionPane.ERROR_MESSAGE);
                    throw new EmptyField();
                }

                if(terminal.getAbonoField().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operación fallida, ingrese abono!!!",
                            "Falla", JOptionPane.ERROR_MESSAGE);
                    throw new EmptyField();
                }

                if(terminal.getPorcentajeField().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operación fallida, ingrese porcentaje!!!",
                            "Falla", JOptionPane.ERROR_MESSAGE);
                    throw new EmptyField();
                }

                /*
                if(terminal.getSaldoField().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operación fallida, saldo no calculado!!!", "Falla",
                    JOptionPane.ERROR_MESSAGE);
                    throw new EmptyField();
                }
                */

                encargo.setCliente(new Cliente(Integer.valueOf(terminal.getClienteId().getText())));
                encargo.setDetalle(terminal.getDetalleArea().getText());
                encargo.setFechaIngreso(new Date());
                encargo.setTipoEncargo(new TipoEncargo(((TipoEncargo)terminal.getTipoComboBox().getSelectedItem()).getId()));

                encargo.setAbono(Double.valueOf(terminal.getAbonoField().getText()));

                encargo.setSaldo(calcularSaldo(terminal));

                validarEncargo(encargo);

                connection = DBCPDataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("insert into encargo (detalle, " +
                        "fecha_ingreso, cliente, tipo_encargo, abono, saldo) values (?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, encargo.getDetalle());
                preparedStatement.setDate(2, new java.sql.Date(encargo.getFechaIngreso().getTime()));
                preparedStatement.setInt(3, encargo.getCliente().getId());
                preparedStatement.setInt(4, encargo.getTipoEncargo().getId());
                preparedStatement.setDouble(5, encargo.getAbono());
                preparedStatement.setDouble(6, encargo.getSaldo());

                preparedStatement.execute();

                terminal.getDetalleArea().setText("");
                terminal.getClienteField().setText("");
                terminal.getTipoComboBox().setSelectedIndex(0);
                terminal.getAbonoField().setText("");
                terminal.getSaldoField().setText("");
                terminal.getClienteId().setText("");

                clearTable(terminal);

                JOptionPane.showMessageDialog(null, "Operación realizada correctamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException | EmptyField e) {
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

        public static void buscarEncargo(Terminal terminal) {
            int reply = JOptionPane.showConfirmDialog(null, "Utilizar cámara?",
                    "Tipo de Búsqueda", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                final Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try (QrCapture qr = new QrCapture()) {
                            String[] strings = qr.getResult().split("\\|");

                            if(strings.length != 2) {
                                throw new InterruptedException("Operación fallida, código QR inválido!!!");
                            }

                            System.out.println("cliente encoded-> " + strings[0]);
                            System.out.println("encargo encoded-> " + strings[1]);
                            Cliente cliente = new Cliente(Integer.valueOf((String)fromString(strings[0])));
                            System.out.println("cliente -> " + cliente.getId());
                            Encargo encargo1 = new Encargo(Integer.valueOf((String)fromString(strings[1])));
                            System.out.println("encargo -> " + encargo1.getId());
                            Toolkit.getDefaultToolkit().beep(); // https://www.rgagnon.com/javadetails/java-0001.html

                            _buscarEncargo(terminal, cliente.getId(), encargo1.getId());
                        } catch (InterruptedException | IOException | ClassNotFoundException ex) {
                            System.err.println(ex);
                            JOptionPane.showMessageDialog(null, "Operación fallida," +
                                            " código QR inválido!!!",
                                    "Falla", JOptionPane.ERROR_MESSAGE);
                        }
                    };
                });
                thread.setDaemon(true);
                thread.start();
            } else {
                _buscarEncargo(terminal, null, null);
            }
        }

        private static void _buscarEncargo(Terminal terminal, Integer clienteId, Integer encargoId) {
            if(terminal.getClienteId().getText().trim().isEmpty() && clienteId == null) {
                JOptionPane.showMessageDialog(null, "Operación fallida, seleccione cliente!!!",
                        "Falla", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Encargo encargo = new Encargo();
            if(clienteId != null && encargoId != null) {
                encargo.setId(encargoId);
                encargo.setCliente(new Cliente(clienteId));
            }
            else
                encargo.setCliente(new Cliente(Integer.valueOf(terminal.getClienteId().getText())));

            Connection connection = null;

            try {
                connection = DBCPDataSource.getConnection();
                PreparedStatement preparedStatement;
                if(encargoId != null) {
                    preparedStatement = connection.prepareStatement("select * from encargo where " +
                            "cliente = ? and id = ?");
                    preparedStatement.setInt(1, encargo.getCliente().getId());
                    preparedStatement.setInt(2, encargo.getId());
                }
                else {
                    preparedStatement = connection.prepareStatement("select * from encargo where " +
                            "cliente = ?");
                    preparedStatement.setInt(1, encargo.getCliente().getId());
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                clearTable(terminal);

                while (resultSet.next()) {
                    Object[] o = new Object[8];
                    o[0] = resultSet.getInt("id");
                    o[1] = resultSet.getString("detalle");
                    o[2] = resultSet.getDate("fecha_ingreso");
                    o[3] = resultSet.getDate("fecha_salida");
                    o[4] = resultSet.getInt("cliente");
                    o[5] = resultSet.getInt("tipo_encargo");
                    o[6] = resultSet.getDouble("abono");
                    o[7] = resultSet.getDouble("saldo");
                    terminal.getEncargoModel().addRow(o);
                }

                JOptionPane.showMessageDialog(null, "Operación realizada correctamente "
                        + terminal.getEncargoModel().getRowCount(), "Exito", JOptionPane.INFORMATION_MESSAGE);

                if(terminal.getEncargoModel().getRowCount() == 1 && encargoId != null) {
                    terminal.getiEncargoId().setText(terminal.getEncargoTable().getValueAt(0, 0).toString());
                    terminal.getiDetalleArea().setText(terminal.getEncargoTable().getValueAt(0, 1).toString());
                    terminal.getiClienteField().setText(terminal.getEncargoTable().getValueAt(0, 4).toString());
                    terminal.getiTipoEncargoField().setText(terminal.getEncargoTable().getValueAt(0, 5).toString());
                    terminal.getiAbono().setText(terminal.getEncargoTable().getValueAt(0, 6).toString());
                    terminal.getiSaldoField().setText(terminal.getEncargoTable().getValueAt(0, 7).toString());
                    Double saldo = Double.parseDouble(terminal.getiSaldoField().getText());
                    if (saldo > 0)
                        terminal.getiAbonoField().setEditable(true);
                    else
                        terminal.getiAbonoField().setEditable(false);
                }
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

        public static void editarEncargo(Terminal terminal) {
            if(terminal.getiClienteField().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación fallida, seleccione encargo!!!",
                        "Falla", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(terminal.getiAbonoField().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación fallida, ingrese abono!!!",
                        "Falla", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Double abono = Double.parseDouble(terminal.getiAbonoField().getText());
            Double saldo = Double.parseDouble(terminal.getiSaldoField().getText());

            java.sql.Date fechaSalida = null;

            if(saldo > 0 && abono > 0 && saldo >= abono) {
                saldo = saldo - abono;
                abono = abono + Double.valueOf(terminal.getiAbono().getText());
                if(saldo == 0)
                    fechaSalida = new java.sql.Date(System.currentTimeMillis());
            }
            else {
                terminal.getiAbonoField().setText("");
                JOptionPane.showMessageDialog(null, "Operación fallida, abono superior al saldo!!!",
                        "Falla", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection connection = null;

            try {
                connection = DBCPDataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("update encargo set " +
                        "saldo = ?, abono = ?, fecha_salida = ? where id = ?");
                preparedStatement.setDouble(1, saldo);
                preparedStatement.setDouble(2, abono);
                preparedStatement.setDate(3, fechaSalida);
                preparedStatement.setInt(4, Integer.valueOf(terminal.getiEncargoId().getText()));
                preparedStatement.execute();

                terminal.getiDetalleArea().setText("");
                terminal.getiClienteField().setText("");
                terminal.getiTipoEncargoField().setText("");
                terminal.getiAbonoField().setText("");
                terminal.getiSaldoField().setText("");

                clearTable(terminal);

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

        private static Double calcularSaldo(Terminal terminal) {
            //String nombre = ((TipoEncargo)terminal.getTipoComboBox().getSelectedItem()).getNombre();
            Double valor = ((TipoEncargo)terminal.getTipoComboBox().getSelectedItem()).getValor();
            Integer cantidad = (Integer) terminal.getCantidadSpinner().getValue();
            Double porcentaje;
            if(!terminal.getPorcentajeField().getText().trim().equals(""))
                porcentaje = Double.parseDouble(terminal.getPorcentajeField().getText());
            else
                porcentaje = 0.0;

            Double parcial = cantidad * valor;

            Double descuento = null;
            if(porcentaje != 0)
                descuento = parcial * porcentaje / 100;

            Double total = parcial - descuento;
            return total;
        }

        private static void validarEncargo(Encargo encargo) throws EmptyField {
            if(encargo.getAbono().toString().trim().isEmpty() || encargo.getCliente().equals(null)
                    || encargo.getDetalle().trim().isEmpty() || encargo.getFechaIngreso().equals(null)
                    || encargo.getSaldo().toString().trim().isEmpty() || encargo.getTipoEncargo().equals(null)) {
                throw new EmptyField();
            }
        }

        private static void clearTable(Terminal terminal) {
            terminal.getEncargoModel().setRowCount(0);
        }

        public static void imprimirEncargo(Terminal terminal) {
            if(terminal.getiEncargoId().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operación fallida!!!", "Falla",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Encargo encargo = new Encargo();
            encargo.setId(Integer.valueOf(terminal.getiEncargoId().getText()));

            // Obtienes el mes actual
            int day = LocalDate.now().getDayOfMonth();
            Month mes = LocalDate.now().getMonth();
            int year = LocalDate.now().getYear();

            // Obtienes el nombre del mes
            String nombre = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

            String[] textos = new String[7];
            textos[0] = "Nombre: " + terminal.getiClienteField().getText();
            textos[1] = "Detalle: " + terminal.getiDetalleArea().getText();
            textos[2] = "Fecha: " + day + " de " + nombre + " de " + year;
            textos[3] = "Ticket: " + terminal.getiEncargoId().getText();
            textos[4] = "Abono: $1";
            textos[5] = "Saldo: $0.5";
            textos[6] = "Total: $1.5";

            System.out.println("Nombre -> " + textos[0]);
            System.out.println("Detalle -> " + textos[1]);
            System.out.println("Fecha -> " + textos[2]);
            System.out.println("Ticket -> " + textos[3]);
            System.out.println("Abono -> " + textos[4]);
            System.out.println("Saldo -> " + textos[5]);
            System.out.println("Total -> " + textos[6]);

            String encoded = null;
            try {
                encoded = toString(terminal.getiClienteField().getText())
                        + "|"
                        + toString(terminal.getiEncargoId().getText());
                System.out.println("encoded -> " + encoded);
                String[] strings = encoded.split("\\|");
                System.out.println("cliente encoded-> " + strings[0]);
                System.out.println("encargo encoded-> " + strings[1]);
                Cliente cliente = new Cliente(Integer.valueOf((String)fromString(strings[0])));
                System.out.println("cliente -> " + cliente.getId());
                Encargo encargo1 = new Encargo(Integer.valueOf((String)fromString(strings[1])));
                System.out.println("encargo -> " + encargo1.getId());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }

            boolean test = false;

            if(!test) {

                BufferedImage image = null;

                try {
                    image = QRCodeGenerator.getQRCodeImage2(
                            encoded
                            , 350, 350);
                } catch (WriterException we) {
                    System.err.println("Could not generate QR Code, WriterException :: " + we.getMessage());
                } catch (IOException ioe) {
                    System.err.println("Could not generate QR Code, IOException :: " + ioe.getMessage());
                }

                PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

                DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                DocPrintJob docPrintJob = printService.createPrintJob();

                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write((BufferedImage) concatImage(image, textos), "png", baos);
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();
                    baos.close();

                    boolean printAsPDF = false;

                    if(printAsPDF) {
                        OutputStream out = new FileOutputStream(System.getProperty("user.home") + "/out.png");
                        out.write(imageInByte);
                        out.close();
                    } else {
                        Doc imagen = new SimpleDoc(imageInByte, flavor, null);
                        docPrintJob.print(imagen, null);
                    }
                } catch (PrintException pe) {
                    System.err.println(pe);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static Image concatImage(BufferedImage image , String[] textos) {
            int imagesCount = 2;
            int textSize = 40;
            BufferedImage images[] = new BufferedImage[imagesCount];

            images[0] = image;

            images[1] = new BufferedImage(image.getWidth(), textSize * textos.length, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = images[1].createGraphics();
            g2d.fillRect(0, 0, image.getWidth(), textSize * textos.length);
            float alpha = 1f;
            AlphaComposite alcom = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha);
            g2d.setComposite(alcom);
            g2d.setPaint(Color.BLACK);
            g2d.setBackground(Color.WHITE);

            g2d.setFont(g2d.getFont().deriveFont(20f));
            int pos = 0;
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
            int fixed = 45;
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
        private static Object fromString( String s ) throws IOException ,
                ClassNotFoundException {
            byte [] data = Base64.getDecoder().decode( s );
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            Object o  = ois.readObject();
            ois.close();
            return o;
        }

        /** Write the object to a Base64 string. */
        private static String toString( Serializable o ) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( o );
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
