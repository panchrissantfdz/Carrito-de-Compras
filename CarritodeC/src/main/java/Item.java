

class Item {
    Producto producto;
    int cantidad;
    Carro carro;

    public Item(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public void asignarCarro(Carro carro) {
        this.carro = carro;
    }

    public double calcularSubtotal() {
        return producto.precio * cantidad;
    }

    public String descripcion() {
        return producto.nombre + " / $" + producto.precio + " / " + cantidad + " / $" + calcularSubtotal();
    }

    public String toString() {
        return producto.nombre + " - Cantidad: " + cantidad;
    }
}