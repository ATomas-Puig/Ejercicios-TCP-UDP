package ejercicio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.stream.Collectors;

public class ThreadSrvLlista implements Runnable {

    Socket clientSocket = null;
    BufferedReader in = null;
    PrintStream out = null;
    Llista msgIn, msgOut;
    boolean acabado;

    public ThreadSrvLlista(Socket clientSocket, Llista llista) throws IOException {
        this.clientSocket = clientSocket;
        this.msgIn = llista;
        acabado = false;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        while (!acabado) {
            msgOut = generarRespuesta(msgIn);

            out.println(msgOut);
            out.flush();
        }

    }

    private Llista generarRespuesta(Llista msgIn) {
        Llista ret = null;

        if (msgIn != null && msgIn.getNumberList().size() > 0) {
            ret = new Llista(msgIn.getNom(), msgIn.getNumberList().stream().sorted().distinct().collect(Collectors.toList()));
            acabado = true;
        }
        return ret;
    }
}
