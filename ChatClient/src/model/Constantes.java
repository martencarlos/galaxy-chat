package model;

/**
 *
 * @author Carlos Marten
 */
public class Constantes {

    public static final String MSG_INVITATION_CHAT = "El siguiente usuario quiere establecer una conversacion: ";
    public static final String MSG_INVITATION_ACCEPTED = "Invitacion aceptada";
    public static final String MSG_INVITATION_DENIED = "Invitacion rechazada";
    public static final String CONN_IP = "ip";
    public static final String CONN_PORT= "port";

    public enum Eoption {

        crearcuenta,
        conectarse,
        peticionChat,
        respuestaPeticionChat,
        mensajeChat,
        sendLogMessage,
        addConnectedUser,
        removeConnectedUser,
        leaveChatroom,
        peticionHistoriales,
        peticionHistorial
    }
}
