import java.io.*;
import java.net.*;

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
            System.out.println("Catálogo recibido del servidor: " + catalog);

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
