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
        llista = new Llista("", new ArrayList<>());
    }

    public void listen(){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket= new ServerSocket(port);
            while (true){
                clientSocket = serverSocket.accept();
                ThreadSrvLlista threadSrvLlista = new ThreadSrvLlista(clientSocket, llista);
                Thread client = new Thread(threadSrvLlista);
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
