import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
class Producto {
    String nombre;
    double precio;
    int stock;
    String nombreImagen;

    ImageIcon imagen;

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto (String nombre, double precio, int stock, String nombreImagen){
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagen = new ImageIcon(nombreImagen);
    }

    public String toString() {
        return nombre + " - Precio: $" + precio + " - Stock: " + stock;
    }
}