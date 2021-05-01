package ejercicio2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientLlista extends Thread {
    String hostname;
    int port;
    boolean continueConnected;

    public ClientLlista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }

    public void run() {
        System.out.println("Aquí estamos");
        Llista serverData;
        Llista request;

        Socket socket;
        InputStream inStream;
        ObjectInputStream oiStream;

        OutputStream outStream;
        ObjectOutputStream ooStream;

        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            inStream = socket.getInputStream();
            oiStream = new ObjectInputStream(inStream);
            serverData = (Llista) oiStream.readObject();
            System.out.println("Justo después del socket");

            outStream = socket.getOutputStream();
            ooStream = new ObjectOutputStream(outStream);

            System.out.println("Antes del while");
            while (continueConnected) {
                System.out.println("Dentro del while");
                serverData = (Llista) oiStream.readObject();
                request = getRequest(serverData);

                ooStream.writeObject(request);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Llista getRequest(Llista serverData) {
        Llista ret = null;
        String nombre = "";
        List<Integer> numberList;
        int numero = 0;
        if (serverData == null) {
            numberList = new ArrayList<>();
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce tu nombre");
            nombre = sc.nextLine();
            do {
                System.out.println("Introduce un número (introduce 0 para terminar):");
                numero = sc.nextInt();
                numberList.add(numero);
            } while (numero > 0);
            ret = new Llista(nombre, numberList);
        } else {
            System.out.print("Lista ordenada de números enviados por " + serverData.getNom() + ": ");
            serverData.getNumberList().forEach(integer -> System.out.print(integer));
            System.out.println();
        }
        return ret;
    }

    public static void main(String[] args) {
        ClientLlista clientLlista = new ClientLlista("localhost", 5557);
        clientLlista.run();
    }
}
