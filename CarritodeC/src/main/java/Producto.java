
class Producto {
    String nombre;
    double precio;
    int stock;

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String toString() {
        return nombre + " - Precio: $" + precio + " - Stock: " + stock;
    }
}