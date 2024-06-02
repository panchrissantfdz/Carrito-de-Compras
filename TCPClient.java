import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import java.lang.Iterable;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class TCPClient {
    public static void main(String[] args) {
        String hostName = "localhost";
        int port = 9090;

        try (
            Socket socket = new Socket(hostName, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {

            Catalog catalog = (Catalog) in.readObject(); // Recibir el catálogo del servidor
            ImageIcon ig = new ImageIcon("Imágenes/Designer.png");
            Image igsize = ig.getImage();
            Image newig = igsize.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            ig = new ImageIcon(newig);
            String subMenu = "1. Agregar al carro \n 2. Hacer pedido (Ticket) \n5. Salir";
            //String opcionSubMenu = JOptionPane.showInputDialog(null, subMenu, "E-scom - Submenú", JOptionPane.PLAIN_MESSAGE, ig,null, null);
            String opcionSubMenu = (String) JOptionPane.showInputDialog(null, subMenu, "E-scom - Submenú", JOptionPane.PLAIN_MESSAGE, ig,null, null);

            switch (opcionSubMenu) {
                case "1":
                    StringBuilder catalogoMensaje = new StringBuilder("Catálogo de Productos:\n");
                    for (Product product : catalog.getProducts()) {
                        catalogoMensaje.append(product).append("\n");
                    }
                   // String prodSeleccionado =  JOptionPane.showInputDialog(null, catalogoMensaje.toString(), "E-scom - Catálogo \n Escoge un producto:", JOptionPane.PLAIN_MESSAGE);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    for (Product product : catalog.getProducts()) {
                        ImageIcon icon = product.getImageIcon();
                        JPanel productPanel = new JPanel();
                        productPanel.setLayout(new BorderLayout());

                        JLabel nameLabel = new JLabel(product.toString());
                        Image image = icon.getImage();
                        Image scaledImage = image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon icon = new ImageIcon(scaledImage);
                        JLabel imageLabel = new JLabel(icon);

                        productPanel.add(nameLabel, BorderLayout.NORTH);
                        productPanel.add(imageLabel, BorderLayout.CENTER);

                        panel.add(productPanel);
                    }

                    JScrollPane scrollPane = new JScrollPane(panel);
                    scrollPane.setPreferredSize(new Dimension(800, 1000));

                    JOptionPane.showMessageDialog(null, scrollPane, "Product List", JOptionPane.PLAIN_MESSAGE);



                    break;
                case "2":
                    //blablabla
                    break;
                default:

                    break;

            }


            // Modificar el catálogo (por ejemplo, actualizar la cantidad de un producto)
            if (!catalog.getProducts().isEmpty()) {
                catalog.getProducts().get(0).setQuantity(80);
            }

            out.writeObject(catalog); // Enviar el catálogo modificado al servidor
            out.flush();

            System.out.println("Catálogo enviado de vuelta al servidor: " + catalog);
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + hostName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("No se pudo obtener E/S para la conexión a " + hostName);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
