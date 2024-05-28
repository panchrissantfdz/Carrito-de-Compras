import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;



class Cliente {
    String nombre;
    int saldo;
    Carro carro;

    public Cliente(String nombre, int saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public void asignarCarro(Carro carro) {
        this.carro = carro;
        carro.asignarCliente(this);
    }

    public String toString() {
        return nombre;
    }
}


public class CarritodeC {
    public static void main(String[] args) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Carro> carros = new ArrayList<>();
        ArrayList<Producto> catalogo = new ArrayList<>();

        // Agregar productos al catálogo
        catalogo.add(new Producto("Galletas", 15.0, 20,"Imágenes/Chokis.jpg"));
        catalogo.add(new Producto("Leche", 25.0, 30, "Imágenes/Leche.jpg"));
        catalogo.add(new Producto("Nescafe", 80.0, 50, "Imágenes/Nescafe.jpg"));
        catalogo.add(new Producto("Coca-Cola", 25.0, 10, "Imágenes/Coca.jpg"));
        catalogo.add(new Producto("Pepsi", 22.0, 13, "Imágenes/Pepsi.jpg"));
        catalogo.add(new Producto("Doritos", 16.0, 28, "Imágenes/Doritos.jpg"));
        catalogo.add(new Producto("Sabritas", 18.0, 42, "Imágenes/Sabritas.jpg"));
        catalogo.add(new Producto("Chips", 15.0, 11, "Imágenes/Chips.jpg"));
        catalogo.add(new Producto("Gansito", 20.0, 29, "Imágenes/Gansito.jpg"));
        catalogo.add(new Producto("Cerveza", 40.0, 38, "Imágenes/Corona.jpg"));
        // Agregar más productos según tus necesidades

        // Registrar clientes
        Cliente cliente1 = new Cliente("Luis Garcia", 1000);
        Cliente cliente2 = new Cliente("Raúl Álvarez",2000);
        Cliente cliente3 = new Cliente("Carlos González",1500);

        clientes.add(cliente1);
        clientes.add(cliente2);
        clientes.add(cliente3);

        // Crear carros y asignarlos a los clientes
        Carro carro1 = new Carro(1);
        Carro carro2 = new Carro(2);
        Carro carro3 = new Carro(3);

        carros.add(carro1);
        carros.add(carro2);
        carros.add(carro3);

        cliente1.asignarCarro(carro1);
        cliente2.asignarCarro(carro2);
        cliente3.asignarCarro(carro3);

        // Implementar el menú principal y submenús según tus especificaciones
        boolean salir = false;

        while (!salir) {
            String menu = "Bienvenido a Escomazon\n\n1- " + cliente1.nombre + " - Carro " + carro1.numero + "\n"
                    + "2- " + cliente2.nombre + " - Carro " + carro2.numero + "\n"
                    + "3- " + cliente3.nombre + " - Carro " + carro3.numero + "\n\nSalir";

            String opcion = JOptionPane.showInputDialog(null, menu, "Escomazon - Menú Principal", JOptionPane.PLAIN_MESSAGE);

            switch (opcion) {
                case "1":
                    subMenuCliente(carro1, catalogo);
                    break;
                case "2":
                    subMenuCliente(carro2, catalogo);
                    break;
                case "3":
                    subMenuCliente(carro3, catalogo);
                    break;
                case "Salir":
                    salir = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.");
                    break;
            }
        }
    }

    private static void subMenuCliente(Carro carro, ArrayList<Producto> catalogo) {
        boolean salirSubMenu = false;

        while (!salirSubMenu) {
            String subMenu = "1. Mostrar Carro\n2. Agregar al Carro\n3. Vaciar el Carro\n4. Hacer Pedido\n5. Salir";
            String opcionSubMenu = JOptionPane.showInputDialog(null, subMenu, "Escomazon - Submenú", JOptionPane.PLAIN_MESSAGE);

            switch (opcionSubMenu) {
                case "1":
                    carro.mostrarCarro();
                    break;
                case "2":
                    StringBuilder catalogoMensaje = new StringBuilder("Catálogo de Productos:\n");
                    for (Producto producto : catalogo) {
                        catalogoMensaje.append(producto).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, catalogoMensaje.toString(), "Escomazon - Catálogo", JOptionPane.PLAIN_MESSAGE);

                    String productoElegido = JOptionPane.showInputDialog(null, "Elija un producto del catálogo:", "Escomazon - Agregar al Carro", JOptionPane.PLAIN_MESSAGE);
                    Producto productoSeleccionado = null;

                    for (Producto producto : catalogo) {
                        if (producto.nombre.equals(productoElegido)) {
                            productoSeleccionado = producto;
                            break;
                        }
                    }

                    if (productoSeleccionado != null) {
                        String cantidadString = JOptionPane.showInputDialog(null, "Especifique la cantidad:", "Escomazon - Agregar al Carro", JOptionPane.PLAIN_MESSAGE);
                        int cantidad = Integer.parseInt(cantidadString);

                        carro.agregarProducto(productoSeleccionado, cantidad, catalogo);
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no válido.", "Escomazon - Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "3":
                    carro.vaciarCarro();
                    break;
                case "4":
                    double totalPedido = carro.hacerPedido();
                    // Puedes agregar aquí la lógica para realizar el pago, generar un ticket, etc.
                    break;
                case "5":
                    salirSubMenu = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.", "Escomazon - Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}