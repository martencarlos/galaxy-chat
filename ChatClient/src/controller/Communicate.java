
package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import model.DataMssge;

/**
 *
 * @author carlos
 */
public class Communicate {

    private OutputStream writeOutputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    public void setSocketConnection(Socket serverSocket) throws IOException {
        writeOutputStream = serverSocket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(writeOutputStream);
    }

    public Communicate() {
    }

    public void sendRequest(DataMssge datos) throws IOException {
        objectOutputStream.writeObject(datos);
    }

    public void closeConnectionObjects() throws IOException {
        writeOutputStream.close();
        objectOutputStream.close();
    }
}
