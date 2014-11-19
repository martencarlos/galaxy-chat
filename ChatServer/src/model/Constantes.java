package model;

/**
 *
 * @author Carlos Marten
 */
public class Constantes {

    public static final String MSG_INVITATION_CHAT = "El siguiente usuario quiere establecer una conversacion: ";
    public static final String MSG_INVITATION_ACCEPTED = "Invitacion aceptada";
    public static final String MSG_INVITATION_DENIED = "Invitacion rechazada";
    
    public static final String DB_CONNECTIONSTRING = "connectionstring";
    public static final String DB_USERNAME = "user";
    public static final String DB_PASSWORD = "password";
    public static final String DB_TYPE="database";

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
