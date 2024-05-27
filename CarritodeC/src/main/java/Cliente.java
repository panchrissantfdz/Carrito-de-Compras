
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