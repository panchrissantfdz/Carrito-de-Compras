import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private static Catalog catalog;
    private static List<ObjectOutputStream> clientOutputs = new ArrayList<>();

    static {
        // Cargar el catálogo desde un archivo de texto
        catalog = CatalogLoader.loadCatalogFromFile("catalog.txt");
        if (catalog.getProducts().isEmpty()) {
            catalog = CatalogLoader.loadCatalogManually();
            CatalogLoader.saveCatalogToFile(catalog, "catalog.txt");
        }
    }

    public static void main(String[] args) {
        int port = 9090;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
            ) {
                this.out = out;
                synchronized (clientOutputs) {
                    clientOutputs.add(out);
                }
                out.writeObject(catalog); // Enviar el catálogo al cliente
                out.flush();

                while (true) {
                    Object input = in.readObject();
                    if (input instanceof Catalog) {
                        Catalog modifiedCatalog = (Catalog) input;
                        synchronized (catalog) {
                            catalog = modifiedCatalog; // Actualizar el catálogo del servidor
                            CatalogLoader.saveCatalogToFile(catalog, "catalog.txt"); // Guardar el catálogo actualizado
                        }
                        System.out.println("Catálogo actualizado: " + catalog);
                        notifyAllClients();
                    } else if (input instanceof String && "exit".equals(input)) {
                        break;
                    }
                }
            } catch (SocketException e) {
                System.out.println("Cliente desconectado: " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    synchronized (clientOutputs) {
                        clientOutputs.remove(out);
                    }
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                    System.out.println("Conexión con el cliente cerrada: " + clientSocket.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void notifyAllClients() {
            synchronized (clientOutputs) {
                for (ObjectOutputStream clientOut : clientOutputs) {
                    try {
                        clientOut.writeObject(catalog);
                        clientOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
