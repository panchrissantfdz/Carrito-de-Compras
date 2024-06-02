import java.io.*;
import java.net.*;
import javax.swing.ImageIcon;
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

            // Crear la ventana principal
            JFrame frame = new JFrame("E-scom");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Crear las pestañas
            JTabbedPane tabbedPane = new JTabbedPane();

            // Pestaña de Catálogo
            JPanel catalogPanel = new JPanel();
            catalogPanel.setLayout(new GridLayout(0, 3)); // Grid layout with 3 columns
            JScrollPane catalogScrollPane = new JScrollPane(catalogPanel);

            for (Product product : catalog.getProducts()) {
                JPanel productPanel = new JPanel();
                productPanel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.CENTER;

                JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
                JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()), SwingConstants.CENTER);

                ImageIcon icon = product.getImageIcon();
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                ImageIcon icon2 = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(icon2);

                JButton addToCartButton = new JButton("Agregar al carrito");
                addToCartButton.addActionListener(e -> {
                    String cantidadStr = JOptionPane.showInputDialog(
                        frame,
                        "Cantidad en existencia: " + product.getQuantity() + "\nIngrese la cantidad a agregar:",
                        "Agregar al carrito",
                        JOptionPane.PLAIN_MESSAGE
                    );

                    if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                        try {
                            int cantidad = Integer.parseInt(cantidadStr.trim());
                            if (cantidad <= product.getQuantity() && cantidad > 0) {
                                // Aquí se podría agregar la lógica para añadir el producto al carrito
                                JOptionPane.showMessageDialog(frame, "Producto añadido al carrito.");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Entrada no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                gbc.gridy = 0;
                productPanel.add(nameLabel, gbc);
                gbc.gridy = 1;
                productPanel.add(imageLabel, gbc);
                gbc.gridy = 2;
                productPanel.add(priceLabel, gbc);
                gbc.gridy = 3;
                productPanel.add(addToCartButton, gbc);

                catalogPanel.add(productPanel);
            }

            tabbedPane.addTab("Catálogo", catalogScrollPane);

            // Pestaña de Carrito
            JPanel cartPanel = new JPanel();
            cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
            JScrollPane cartScrollPane = new JScrollPane(cartPanel);
            tabbedPane.addTab("Carrito", cartScrollPane);

            // Pestaña de Pedido
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
            JScrollPane orderScrollPane = new JScrollPane(orderPanel);
            tabbedPane.addTab("Pedido", orderScrollPane);

            frame.getContentPane().add(tabbedPane);
            frame.setVisible(true);

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
