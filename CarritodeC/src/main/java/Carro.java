
import java.util.ArrayList;
import javax.swing.JOptionPane;

class Carro {
    int numero;
    Cliente cliente;
    ArrayList<Item> articulos = new ArrayList<>();

    public Carro(int numero) {
        this.numero = numero;
    }

    public void asignarCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void agregarProducto(Producto producto, int cantidad, ArrayList<Producto> catalogo) {
        if (producto.stock >= cantidad) {
            Item item = new Item(producto, cantidad);
            item.asignarCarro(this);
            articulos.add(item);
            producto.stock -= cantidad;
            JOptionPane.showMessageDialog(null, "Producto agregado al carro: " + producto.nombre);
        } else {
            JOptionPane.showMessageDialog(null, "No hay suficiente stock disponible para este producto.");
        }
    }

    public void mostrarCarro() {
        StringBuilder mensaje = new StringBuilder("Carro de " + cliente.nombre + " - Número: " + numero + "\n");
        for (Item item : articulos) {
            mensaje.append(item).append("\n");
        }
        JOptionPane.showMessageDialog(null, mensaje.toString());
    }

    public void vaciarCarro() {
        articulos.clear();
        JOptionPane.showMessageDialog(null, "Carro vaciado correctamente.");
    }

    public double hacerPedido() {
        double total = 0;
        StringBuilder mensaje = new StringBuilder("Descripción / Precio unitario / Cantidad / Subtotal\n");
        for (Item item : articulos) {
            total += item.calcularSubtotal();
            mensaje.append(item.descripcion()).append("\n");
        }
        mensaje.append(" Total = $").append(total);
        JOptionPane.showMessageDialog(null, mensaje.toString());
        return total;
    }
}