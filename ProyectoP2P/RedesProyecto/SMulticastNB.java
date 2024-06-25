/*import java.net.*;

public class SMulticastB {
    public static void main(String[] args ){
        InetAddress gpo;
        try{
            MulticastSocket s= new MulticastSocket(9875);
            s.setReuseAddress(true);
            s.setTimeToLive(5);
            String msj ="hola, soy el servidor 2";
            byte[] b = msj.getBytes();
            gpo = InetAddress.getByName("228.1.1.1");
            s.joinGroup(gpo);
            for(;;){
                DatagramPacket p = new DatagramPacket(b,b.length,gpo,9999);
                s.send(p);
                System.out.println("Enviando mensaje "+msj+ " con un TTL= "+s.getTimeToLive());
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }//for
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
*/

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class SMulticastNB {
    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.printf("Interfaz: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses =  netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses))
            System.out.printf("InetAddress: %s\n", inetAddress);
        System.out.printf("\n");
    }

    public static void main(String[] args){
        try{
            int pto=2000;
            Enumeration<NetworkInterface> nets =  NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets))
                displayInterfaceInformation(netint);
            NetworkInterface ni = NetworkInterface.getByName("wireless_32768");
            InetSocketAddress dir = new InetSocketAddress(pto);
            DatagramChannel s =  DatagramChannel.open(StandardProtocolFamily.INET);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            InetAddress group = InetAddress.getByName("230.0.0.1");
            s.join(group, ni);
            s.configureBlocking(false);
            s.socket().bind(dir);
            Selector sel = Selector.open();
            s.register(sel, SelectionKey.OP_READ);
            ByteBuffer b = ByteBuffer.allocate(4);
            System.out.println("Servidor listo.. Esperando datagramas...");
            while(true){
                sel.select();
                Iterator<SelectionKey>it = sel.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey k = (SelectionKey)it.next();
                    it.remove();
                    if(k.isReadable()){
                        DatagramChannel ch = (DatagramChannel)k.channel();
                        b.clear();
                        SocketAddress emisor = ch.receive(b);
                        b.flip();
                        InetSocketAddress d = (InetSocketAddress)emisor;
                        System.out.println("Datagrama recibido desde "+ d.getAddress()+":"+d.getPort());
                        System.out.println("Dato: "+b.getInt());
                        continue;
                    }
                }//while
            }//while
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
