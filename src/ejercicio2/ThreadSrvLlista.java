package ejercicio2;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;

public class ThreadSrvLlista implements Runnable {

    Socket clientSocket = null;
    Llista msgIn, msgOut;
    InputStream in;
    ObjectInputStream oiStream;
    OutputStream out;
    ObjectOutputStream ooStream;
    boolean acabado;

    public ThreadSrvLlista(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        acabado = false;
        in = clientSocket.getInputStream();
        oiStream = new ObjectInputStream(in);
        out = clientSocket.getOutputStream();
        ooStream = new ObjectOutputStream(out);
    }

    @Override
    public void run() {
        while (!acabado) {
            try {
                msgIn = (Llista) oiStream.readObject();
                msgOut = generarRespuesta(msgIn);
                ooStream.writeObject(msgOut);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                ooStream.writeObject(msgOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Llista generarRespuesta(Llista msgIn) {
        System.out.println(msgIn.getNom());
        Llista ret = null;

        if (msgIn != null && msgIn.getNumberList().size() > 0) {
            ret = new Llista(msgIn.getNom(), msgIn.getNumberList().stream().sorted().distinct().collect(Collectors.toList()));
            acabado = true;
        }
        return ret;
    }
}
