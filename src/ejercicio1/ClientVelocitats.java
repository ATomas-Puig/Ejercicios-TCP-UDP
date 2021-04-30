package ejercicio1;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocitats {

    int total = 0;
    int counter = 0;
    int resultado;

    private int destinationPort;
    private String serverIp;
    private InetAddress destinationIP;

    private MulticastSocket multicastSocket;
    private InetAddress multicastIP;
    boolean continueRunning = true;

    InetSocketAddress groupMulticast;
    NetworkInterface networkInterface;

    public ClientVelocitats(String serverIp, int destinationPort) {
        this.serverIp = serverIp;
        this.destinationPort = destinationPort;

        try {
            multicastSocket = new MulticastSocket(5557);
            multicastIP = InetAddress.getByName("224.0.2.1");
            groupMulticast = new InetSocketAddress(multicastIP, 5557);
            networkInterface = NetworkInterface.getByName("wlan1");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            destinationIP = InetAddress.getByName(serverIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte[] receivedData = new byte[4];

        System.out.println("dentro del run");
        DatagramPacket packet;
        DatagramSocket socket = new DatagramSocket();

        multicastSocket.joinGroup(groupMulticast, networkInterface);

        while (true) {
            packet = new DatagramPacket(receivedData, 4);
            System.out.println("dentro del bucle");
            multicastSocket.receive(packet);
            resultado = getDataToRequest(packet.getData(), packet.getLength());
        }
    }

    public int getDataToRequest(byte[] data, int length) {
        int n = ByteBuffer.wrap(data).getInt();
        int resultado = 0;
        total = total + n;
        counter++;
        System.out.println("dentro del metodo");
        if (counter % 5 == 0) {
            System.out.println("dentro del if");
            resultado = total / 5;
            System.out.println(resultado);
            total = 0;
            counter = 0;
        }
        return resultado;
    }

    public static void main(String[] args) {
        ClientVelocitats clientVelocitats = new ClientVelocitats("224.0.2.1", 5557);
        try {
            clientVelocitats.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
