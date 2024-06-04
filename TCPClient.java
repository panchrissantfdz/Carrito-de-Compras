import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class TCPClient {
    public static void main(String[] args) {
        String hostName = "localhost";
        int port = 9090;

        try (
            Socket socket = new Socket(hostName, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            // Recibir el catálogo del servidor
            Catalog[] catalogWrapper = new Catalog[1];
            catalogWrapper[0] = (Catalog) in.readObject();

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

            // Mapa para almacenar los productos en el carrito y sus cantidades
            Map<Product, Integer> cart = new HashMap<>();

            // Botón de comprar
            JButton buyButton = new JButton("Comprar");
            buyButton.setVisible(false); // Inicialmente oculto

            buyButton.addActionListener(e -> {
                StringBuilder ticket = new StringBuilder();
                double total = 0.0;

                ticket.append("Ticket de Compra:\n\n");
                ticket.append(String.format("%-20s %-10s %-10s %-10s\n", "Producto", "Precio", "Cantidad", "Subtotal"));

                for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                    Product cartProduct = entry.getKey();
                    int cartQuantity = entry.getValue();
                    double subtotal = cartProduct.getPrice() * cartQuantity;

                    ticket.append(String.format("%-20s $%-9.2f %-10d $%-9.2f\n", cartProduct.getName(), cartProduct.getPrice(), cartQuantity, subtotal));
                    total += subtotal;

                    // Actualizar la cantidad en el catálogo
                    for (Product product : catalogWrapper[0].getProducts()) {
                        if (product.equals(cartProduct)) {
                            product.setQuantity(product.getQuantity() - cartQuantity);
                            break;
                        }
                    }
                }

                ticket.append("\nTotal: $" + total);

                try {
                    // Enviar el catálogo actualizado al servidor
                    out.writeObject(catalogWrapper[0]);
                    out.flush();

                    JOptionPane.showMessageDialog(frame, "Compra realizada con éxito.");
                    cart.clear();
                    cartPanel.removeAll();
                    cartPanel.revalidate();
                    cartPanel.repaint();
                    buyButton.setVisible(false); // Ocultar el botón después de la compra

                    // Mostrar el ticket en la pestaña de Pedido
                    orderPanel.removeAll();
                    JTextArea ticketArea = new JTextArea(ticket.toString());
                    ticketArea.setEditable(false);
                    orderPanel.add(new JScrollPane(ticketArea));

                    // Botón para descargar el ticket
                    JButton downloadButton = new JButton("Descargar Ticket");
                    downloadButton.addActionListener(d -> {
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                                writer.write(ticket.toString());
                                JOptionPane.showMessageDialog(frame, "Ticket descargado con éxito.");
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(frame, "Error al guardar el ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    orderPanel.add(downloadButton);
                    orderPanel.revalidate();
                    orderPanel.repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error al enviar el catálogo actualizado.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            cartPanel.add(buyButton);

            for (Product product : catalogWrapper[0].getProducts()) {
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
                                // Añadir el producto al carrito
                                cart.put(product, cart.getOrDefault(product, 0) + cantidad);

                                // Actualizar la pestaña del carrito
                                cartPanel.removeAll();
                                for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                                    Product cartProduct = entry.getKey();
                                    int cartQuantity = entry.getValue();

                                    JPanel cartProductPanel = new JPanel();
                                    cartProductPanel.setLayout(new GridBagLayout());
                                    GridBagConstraints cartGbc = new GridBagConstraints();
                                    cartGbc.gridx = 0;
                                    cartGbc.gridy = 0;
                                    cartGbc.insets = new Insets(5, 5, 5, 5);
                                    cartGbc.anchor = GridBagConstraints.CENTER;

                                    JLabel cartNameLabel = new JLabel(cartProduct.getName(), SwingConstants.CENTER);
                                    JLabel cartQuantityLabel = new JLabel("Cantidad: " + cartQuantity, SwingConstants.CENTER);
                                    JLabel cartPriceLabel = new JLabel(String.format("$%.2f", cartProduct.getPrice()), SwingConstants.CENTER);

                                    ImageIcon cartIcon = cartProduct.getImageIcon();
                                    Image cartImage = cartIcon.getImage();
                                    Image scaledCartImage = cartImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                                    ImageIcon cartIcon2 = new ImageIcon(scaledCartImage);
                                    JLabel cartImageLabel = new JLabel(cartIcon2);

                                    cartGbc.gridy = 0;
                                    cartProductPanel.add(cartNameLabel, cartGbc);
                                    cartGbc.gridy = 1;
                                    cartProductPanel.add(cartImageLabel, cartGbc);
                                    cartGbc.gridy = 2;
                                    cartProductPanel.add(cartQuantityLabel, cartGbc);
                                    cartGbc.gridy = 3;
                                    cartProductPanel.add(cartPriceLabel, cartGbc);

                                    cartPanel.add(cartProductPanel);
                                }

                                cartPanel.add(buyButton); // Asegurar que el botón de comprar esté al final
                                buyButton.setVisible(true); // Mostrar el botón
                                cartPanel.revalidate();
                                cartPanel.repaint();

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

            frame.getContentPane().add(tabbedPane);
            frame.setVisible(true);

            // Mantener la conexión abierta mientras la ventana esté abierta
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    try {
                        // Enviar mensaje de cierre de sesión al servidor
                        out.writeObject("exit");
                        out.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Escuchar actualizaciones del servidor en un hilo separado
            new Thread(() -> {
                try {
                    while (true) {
                        Catalog updatedCatalog = (Catalog) in.readObject();
                        catalogWrapper[0] = updatedCatalog;
                        SwingUtilities.invokeLater(() -> {
                            catalogPanel.removeAll();
                            for (Product product : catalogWrapper[0].getProducts()) {
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
                                                // Añadir el producto al carrito
                                                cart.put(product, cart.getOrDefault(product, 0) + cantidad);

                                                // Actualizar la pestaña del carrito
                                                cartPanel.removeAll();
                                                for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                                                    Product cartProduct = entry.getKey();
                                                    int cartQuantity = entry.getValue();

                                                    JPanel cartProductPanel = new JPanel();
                                                    cartProductPanel.setLayout(new GridBagLayout());
                                                    GridBagConstraints cartGbc = new GridBagConstraints();
                                                    cartGbc.gridx = 0;
                                                    cartGbc.gridy = 0;
                                                    cartGbc.insets = new Insets(5, 5, 5, 5);
                                                    cartGbc.anchor = GridBagConstraints.CENTER;

                                                    JLabel cartNameLabel = new JLabel(cartProduct.getName(), SwingConstants.CENTER);
                                                    JLabel cartQuantityLabel = new JLabel("Cantidad: " + cartQuantity, SwingConstants.CENTER);
                                                    JLabel cartPriceLabel = new JLabel(String.format("$%.2f", cartProduct.getPrice()), SwingConstants.CENTER);

                                                    ImageIcon cartIcon = cartProduct.getImageIcon();
                                                    Image cartImage = cartIcon.getImage();
                                                    Image scaledCartImage = cartImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                                                    ImageIcon cartIcon2 = new ImageIcon(scaledCartImage);
                                                    JLabel cartImageLabel = new JLabel(cartIcon2);

                                                    cartGbc.gridy = 0;
                                                    cartProductPanel.add(cartNameLabel, cartGbc);
                                                    cartGbc.gridy = 1;
                                                    cartProductPanel.add(cartImageLabel, cartGbc);
                                                    cartGbc.gridy = 2;
                                                    cartProductPanel.add(cartQuantityLabel, cartGbc);
                                                    cartGbc.gridy = 3;
                                                    cartProductPanel.add(cartPriceLabel, cartGbc);

                                                    cartPanel.add(cartProductPanel);
                                                }

                                                cartPanel.add(buyButton); // Asegurar que el botón de comprar esté al final
                                                buyButton.setVisible(true); // Mostrar el botón
                                                cartPanel.revalidate();
                                                cartPanel.repaint();

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
                            catalogPanel.revalidate();
                            catalogPanel.repaint();
                        });
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }).start();

            frame.setVisible(true);

            // Mantener el cliente vivo
            while (true) {
                Thread.sleep(1000); // Esperar un segundo antes de verificar de nuevo
            }
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + hostName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("No se pudo obtener E/S para la conexión a " + hostName);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
