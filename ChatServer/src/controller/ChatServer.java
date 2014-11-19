package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import persistence.DataDAO;
import persistence.DataPersistFactory;

/**
 *
 * @author Carlos Marten
 */
public class ChatServer {

    private static String url;
    private static String user;
    private static String password;
    private static String dbType;
    private static Hashtable<String, ConnectedUser> connectedUsers = new Hashtable<String, ConnectedUser>();
    private static Hashtable<String, Chat> chatRooms = new Hashtable<String, Chat>();
    private static int intChatID = 0;
    private static Properties configFile = new Properties();

    public static String getDbType() {
        return dbType;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }
//@Abraham rompe la encapsulacion
    public static Hashtable<String, Chat> getChatRooms() {
        return chatRooms;
    }
//@Abraham rompe la encapsulacion
    public static Hashtable<String, ConnectedUser> getConnectedUsers() {
        return connectedUsers;
    }

    public static void main(String[] args) throws IOException {
        configFile.load(ChatServer.class.getResourceAsStream("ichat.properties"));

        if (!connectToDB()) {
            System.out.println("fallo al establecer conexion con base de datos");
        } else {
            System.out.println("database conection successful");
            ServerSocket socketServidor;
            socketServidor = new ServerSocket(9000);

            while (true) {
                try {
                    
                    ClientProcess cp = new ClientProcess(socketServidor.accept());
                    System.out.println("socket creado");
                    Thread t = new Thread(cp);
                    t.start();
                } catch (IOException e) {
                    System.out.println("Accept failed: 9000");
                }
            }
        }
    }

    private static boolean connectToDB() {
        url = configFile.getProperty(Constantes.DB_CONNECTIONSTRING);
        user = configFile.getProperty(Constantes.DB_USERNAME);
        password = configFile.getProperty(Constantes.DB_PASSWORD);
        dbType = configFile.getProperty(Constantes.DB_TYPE);

        DataDAO persistenceManager = DataPersistFactory.getDataDAO(dbType);
        boolean ok = persistenceManager.setUp(url, user, password);

        return ok;
    }

    /*
     * Returns the list of connected users in form of a Hashtable of <username:
     * String, Name: String>
     */
    static Hashtable<String, String> getConnectedUsersForDatos() {
        Hashtable<String, String> hashUsers = new Hashtable<String, String>();
        ConnectedUser cu;
        String username;

        /*
         * get Enumeration of keys contained in Hashtable using Enumeration
         * keys() method of Hashtable class
         */
        Enumeration e =  getConnectedUsers().keys();

        //iterate through Hashtable keys Enumeration
        while (e.hasMoreElements()) {
            username = (String) e.nextElement();
            cu = (ConnectedUser) getConnectedUsers().get(username);
            hashUsers.put(cu.getUsername(), cu.getName());
        }

        return hashUsers;
    }

    static void addConnectedUser(User user, Communicate comm) {
        DataMssge datos = new DataMssge();
        ConnectedUser cu = new ConnectedUser(user.getUsername(), user.getName(), user.getLastName(), comm);

        // add new user to list of connected users
        getConnectedUsers().put(user.getUsername(), cu);

        datos.setOption(Constantes.Eoption.addConnectedUser);
        datos.setUser(user);
        datos.setUsers(getConnectedUsersForDatos());
        datos.setStrLog(user.getName() + " se ha conectado.");

        //Send new list with logged users to all connected users
        sendMessageToConnectedUsers(datos);
    }

    static void removeConnectedUser(User user) {
        DataMssge datos = new DataMssge();

        datos.setOption(Constantes.Eoption.removeConnectedUser);
        getConnectedUsers().remove(user.getUsername());
        datos.setUser(user);
        datos.setUsers(getConnectedUsersForDatos());
        datos.setStrLog(user.getName() + " se ha desconectado.");

        //Send new list with logged users to all connected users
        sendMessageToConnectedUsers(datos);
    }

    static void sendMessageToConnectedUsers(DataMssge datos) {

        for (ConnectedUser cu : getConnectedUsers().values()) {
            try {
                if (!cu.getUsername().equals(datos.getUser().getUsername())) {
                    cu.getsConnection().sendRequest(datos);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static int addChatRoom(DataMssge datos) {
        Chat chat = new Chat();
        String username;

        //add users to chatroom
        Enumeration e = datos.getUsers().keys();

        //iterate through Hashtable keys Enumeration
        while (e.hasMoreElements()) {
            username = (String) e.nextElement();
            chat.addUserToList(username);
            chat.addHistUserToList(username);
        }
        chat.addUserToList(datos.getUser().getUsername());
        chat.addHistUserToList(datos.getUser().getUsername());

        
        getChatRooms().put(String.valueOf(intChatID), chat);

        chat.setChatID(intChatID);
        intChatID++;

        return chat.getChatID();
    }
}
