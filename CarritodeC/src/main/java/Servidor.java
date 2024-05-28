
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Servidor {
    public static void main(String[] args){
        ArrayList<Producto> catalogo = new ArrayList<>();
        // Agregar productos al catálogo
        catalogo.add(new Producto("Galletas", 15.0, 20));
        catalogo.add(new Producto("Leche", 25.0, 30));
        catalogo.add(new Producto("Nescafe", 80.0, 50));
        catalogo.add(new Producto("Coca-Cola", 25.0, 10));
        catalogo.add(new Producto("Pepsi", 22.0, 13));
        catalogo.add(new Producto("Doritos", 16.0, 28));
        catalogo.add(new Producto("Sabritas", 18.0, 42));
        catalogo.add(new Producto("Chips", 15.0, 11));
        catalogo.add(new Producto("Gansito", 20.0, 29));
        catalogo.add(new Producto("Cerveza", 40.0, 38));
        // Agregar más productos según tus necesidades

        try{
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Servidor preparadado.");
            while(true) {
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde" + cl.getInetAddress() + ":" + cl.getPort());

                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
                InputStream is = cl.getInputStream();
                DataInputStream dis = new DataInputStream(is);


                dis.close();
                cl.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }





}