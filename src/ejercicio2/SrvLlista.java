package ejercicio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SrvLlista {

    int port;
    Llista llista;

    public SrvLlista(int port){
        this.port = port;
    }

    public void listen(){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket= new ServerSocket(port);
            while (true){
                clientSocket = serverSocket.accept();
                ThreadSrvLlista threadSrvLlista = new ThreadSrvLlista(clientSocket);
                Thread client = new Thread(threadSrvLlista);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SrvLlista srvLlista = new SrvLlista(5557);
        srvLlista.listen();
    }

}
