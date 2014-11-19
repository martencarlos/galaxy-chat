
package chatclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Constantes;
import model.DataMssge;

/**
 *
 * @author Carlos Marten
 */
public class ResponseProcess implements Runnable {

    private ChatClient parent;

//Constructor
    public ResponseProcess(ChatClient cliente) {
        this.parent = cliente;
    }

    @Override
    public void run() {
        
            InputStream readInputStream = null;
            ObjectInputStream objectInputStream = null;
            DataMssge datos;
            try {
            readInputStream = this.parent.getSocketClient().getInputStream();
            objectInputStream = new ObjectInputStream(readInputStream);

            while (true) {
                Object tmp = objectInputStream.readObject();

                if (tmp != null) {
                    datos = (DataMssge) tmp;

                    if (datos.getStrError() == null) {
                        switch (datos.getOption()) {
                            case crearcuenta:
                                this.parent.OpenChat(datos);
                                break;
                            case conectarse:
                                this.parent.OpenChat(datos);
                                break;
                            case peticionChat:
                                this.requestChat(datos);
                                break;
                            case respuestaPeticionChat:
                                this.responseChatRequest(datos);

                                break;
                            case mensajeChat:
                                sentChatMessage(datos);
                                break;
                            case leaveChatroom:
                                this.leaveChatroom(datos);
                                break;
                            case sendLogMessage:
                                sentLogMessage(datos);
                                break;
                            case addConnectedUser:
                                this.addConnectedUser(datos);
                                break;
                            case removeConnectedUser:
                                this.removeConnectedUser(datos);
                                break;
                            case peticionHistoriales:
                                this.displayRecords(datos);
                                break;
                            case peticionHistorial:
                                this.displayRecord(datos);
                                break;
                            default:
                                return;
                        }
                    } else {
                        this.parent.WriteResponseError(datos);
                    }
                } else {
                    // el socket ha sido cerrado por el servidor
                    System.out.println("socket vacio");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ResponseProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.parent.ConnectionClosed();
            Logger.getLogger(ResponseProcess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //@Abraham si llegamos aquí por una excepción en el constructor
                //esto dara un NullPointerException
                objectInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ResponseProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //@Abraham idem
                readInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ResponseProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void requestChat(DataMssge datos) throws IOException {
        DataMssge respDatos = new DataMssge();

        if (Common.ConfirmMessage(Constantes.MSG_INVITATION_CHAT + datos.getUser().getName())) {
            respDatos.setStrLog(Constantes.MSG_INVITATION_ACCEPTED);
            this.parent.fChat.openChatroom(datos);
        } else {
            respDatos.setStrLog(Constantes.MSG_INVITATION_DENIED);
        }

        respDatos.setChatID(datos.getChatID());
        respDatos.setOption(Constantes.Eoption.respuestaPeticionChat);
        respDatos.setUser(this.parent.getCurrentUser());

        this.parent.getComm().sendRequest(respDatos);
    }

    private void responseChatRequest(DataMssge datos) {
        this.parent.fChat.setErrorLabel(datos.getStrLog() + " por " + datos.getUser().getName());
        if (datos.getStrLog().equals(Constantes.MSG_INVITATION_ACCEPTED)) {
            this.parent.fChat.refreshChatUserList(datos);
        }
    }

    private void addConnectedUser(DataMssge datos) {
        this.parent.fChat.setErrorLabel(datos.getStrLog());
        this.parent.fChat.refreshUCList(datos);
    }

    private void removeConnectedUser(DataMssge datos) {
        this.parent.fChat.setErrorLabel(datos.getStrLog());
        this.parent.fChat.refreshUCList(datos);
    }

    private void leaveChatroom(DataMssge datos) {
        this.parent.fChat.setErrorLabel(datos.getStrLog());
        this.parent.fChat.refreshChatUserList(datos);
    }

    private void sentChatMessage(DataMssge datos) {
        this.parent.fChat.sentChatMessage(datos);
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private void sentLogMessage(DataMssge datos) {
        this.parent.fChat.sentLogMessage(datos);
    }

    private void displayRecords(DataMssge datos) {
        this.parent.fChat.getfHistorial().displayRecords(datos);
    }

    private void displayRecord(DataMssge datos) {
        this.parent.fChat.getfHistorial().displayRecord(datos);
    }
}
