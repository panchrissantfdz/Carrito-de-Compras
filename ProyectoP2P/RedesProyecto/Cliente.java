import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    private Cliente() {}
    public static void main(String[] args) {
	String host = (args.length < 1) ? null : args[0];
	try {
	    Registry registry = LocateRegistry.getRegistry(host);	
            //también puedes usar getRegistry(String host, int port)
            Suma stub = (Suma) registry.lookup("Suma");
            
	    int x=5,y=4;
	    int response = stub.suma(x,y);	    
	    System.out.println("respuesta sumar "+x+" y "+y+" : " + response);

	} catch (Exception e) {
	    System.err.println("Excepción del cliente: " +e.toString());
	    e.printStackTrace();
	}
    }
}
