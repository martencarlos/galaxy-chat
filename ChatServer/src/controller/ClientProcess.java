package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import persistence.DataDAO;
import persistence.DataPersistFactory;

/**
 *
 * @author Carlos Marten
 */
class ClientProcess implements Runnable {
    
    private final Object lockOfTheConexion = new Object();
    private Socket socketClient = new Socket();
    private Communicate comm = null;
    DataDAO persistenceManager = null;
    private User currentUser = null;

    //Constructor
    ClientProcess(Socket client) {
        this.socketClient = client;
        this.comm = new Communicate();
        this.persistenceManager = DataPersistFactory.getDataDAO(ChatServer.getDbType());
    }
    
    @Override
    public void run() {
        InputStream readInputStream;
        ObjectInputStream objectInputStream = null;
        DataMssge datos;
        boolean blnSendResponse;
        
        try {
            //Sets the objects for writing to the socket
            comm.setSocketConnection(this.socketClient);

            // creates objects to read from socket
            readInputStream = this.socketClient.getInputStream();
            objectInputStream = new ObjectInputStream(readInputStream);
            
            while (true) {
                // reads object from socket
                Object tmp = objectInputStream.readObject();

                // executes function depending on the option selected
                if (tmp != null) {
                    datos = (DataMssge) tmp;

                    //initialize variables
                    datos.setStrError(null);
                    blnSendResponse = false;
                    
                    switch (datos.getOption()) {
                        case crearcuenta:
                            datos = this.crearcuenta(datos);
                            blnSendResponse = true;
                            
                            break;
                        case conectarse:
                            datos = this.conectarse(datos);
                            blnSendResponse = true;
                            
                            break;
                        case peticionChat:
                            this.peticionChat(datos);
                            
                            break;
                        case respuestaPeticionChat:
                            this.respuestaPeticionChat(datos);
                            
                            break;
                        case mensajeChat:
                            this.sendMessage(datos);
                            
                            break;
                        case leaveChatroom:
                            this.leaveChatroom(datos);
                            
                            break;
                        case sendLogMessage:
                            this.sendMessage(datos);
                            break;
                        
                        case peticionHistoriales:
                            this.getHistoryDates(datos);
                            break;
                        case peticionHistorial:
                            this.getRecord(datos);
                            break;
                        default:
                            return;
                    }
                    
                    try {
                        if (blnSendResponse) {
                            this.comm.sendRequest(datos);
                        }
                    } catch (IOException e1) {
                        System.out.println("Imposible escribir los objetos en outputStream servidor.");
                    }
                } else {
                    System.out.println("usuario desconectado");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            try {
                userConnectionClosed();
            } catch (IOException ex1) {
                Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //@Abraham si la excepción se produce el constructor es toda un NullPointerException
                objectInputStream.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                this.socketClient.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                this.comm.closeConnectionObjects();
            } catch (IOException ex) {
                Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private DataMssge crearcuenta(DataMssge datos) {
        ValidationMethods v = new ValidationMethods();
        
//@Abraham Para garantizar que no hay usuarios duplicados habría que usar sincronizacion
        if (v.isUserUnique(datos.getUser().getUsername())) {
            persistenceManager.createNewUser(datos.getUser()); //creamos usuario
            datos.setStrLog("usuario creado correctamente");

            // get userinfo from DB (name, lastname)
            datos.setUsers(ChatServer.getConnectedUsersForDatos());
            this.currentUser = datos.getUser();

            //adds user to the list of connected users
            ChatServer.addConnectedUser(datos.getUser(), this.comm);
        } else {
            datos.setStrError("the username is already taken");
            System.out.println("enviado error, el usuario ya existe a cliente");
        }
        
        return datos;
    }
    
    private DataMssge conectarse(DataMssge datos) {
        
        ValidationMethods v = new ValidationMethods();
        
        if (v.isUserRegisteredInServer(datos.getUser())) {
            if (!v.isUserConnected(datos.getUser().getUsername())) {
                datos.setUsers(ChatServer.getConnectedUsersForDatos());

                // get userinfo from DB (name, lastname)
                datos.setUser(persistenceManager.getUserFromDB(datos.getUser().getUsername()));
                this.currentUser = datos.getUser();

                //adds user to the list of connected users
                ChatServer.addConnectedUser(datos.getUser(), this.comm);
            } else {
                datos.setStrError("Este usuario ya esta conectado.");
            }
        } else {
            datos.setStrError("contraseña o usuario incorrectos");
        }
        
        return datos;
    }
    
    private void peticionChat(DataMssge datos) throws IOException {
        Hashtable<String, String> addedusers = new Hashtable<String, String>(datos.getUsers());

        //adds a chatroom object to the server list and inserts selected users
        //single chat
        if (datos.getChatID() < 0) {
            datos.setChatID(ChatServer.addChatRoom(datos));
        } else {
            //multichat
            String username;
            Chat chat = ChatServer.getChatRooms().get(String.valueOf(datos.getChatID()));
            Enumeration e = datos.getUsers().keys();

            //iterate through Hashtable keys Enumeration
            while (e.hasMoreElements()) {
                username = (String) e.nextElement();
                if (!chat.getHistUsers().contains(username)) {
                    chat.getHistUsers().add(username);
                }
                if (!chat.getUsers().contains(username)) {
                    chat.addUserToList(username);
                } else {
                    addedusers.remove(username);
                }
            }
        }

        //sets list of users in chatroom into datos to show on tab label
        datos.getUsers().clear();
        for (String username : ChatServer.getChatRooms().get(String.valueOf(datos.getChatID())).getUsers()) {
            datos.getUsers().put(username, ChatServer.getConnectedUsers().get(username).getName());
        }
        Enumeration e = addedusers.keys();

        //sends request to all new users
        while (e.hasMoreElements()) {
            String username = (String) e.nextElement();
            
            if (!username.equals(datos.getUser().getUsername())) {
                ChatServer.getConnectedUsers().get(username).getsConnection().sendRequest(datos);
            }
        }
    }
    
    private void respuestaPeticionChat(DataMssge datos) throws IOException {
        
        this.notifyUsersInChatroom(datos);

        //remove chatroom if empty
        if (!datos.getStrLog().equals(Constantes.MSG_INVITATION_ACCEPTED)) {
            if (ChatServer.getChatRooms().get(String.valueOf(datos.getChatID())).getUsers().size() == 2) {
                ChatServer.getChatRooms().remove(String.valueOf(datos.getChatID()));
            } else {
                ChatServer.getChatRooms().get(String.valueOf(datos.getChatID())).getUsers().remove(datos.getUser().getUsername());
            }
        }
    }
    
    private void userConnectionClosed() throws IOException {
        DataMssge datos;

        // close connection objects
        try {
            comm.closeConnectionObjects();
        } catch (IOException ex) {
            System.out.println("Error al cerrar objetos de escritura en socket: " + ex.getMessage());
        }
        
        Hashtable<String, Chat> tmpList = new Hashtable<String, Chat>(ChatServer.getChatRooms());
        for (Chat chat : tmpList.values()) {
            if (chat.getUsers().contains(this.currentUser.getUsername())) {
                datos = new DataMssge();
                datos.setOption(Constantes.Eoption.leaveChatroom);
                datos.setUser(this.currentUser);
                datos.setChatID(chat.getChatID());
                this.leaveChatroom(datos);
            }
        }

        // client has closed the connection
        ChatServer.removeConnectedUser(this.currentUser);
        
        System.out.println("Conexion cerrada por " + this.currentUser.getUsername());
    }
    
    private void leaveChatroom(DataMssge datos) throws IOException {
        Chat chat = ChatServer.getChatRooms().get(String.valueOf(datos.getChatID()));
        chat.removeUserFromList(datos.getUser().getUsername());
        this.notifyUsersInChatroom(datos);
        if (chat.getUsers().size() < 2) {
            
            if (!chat.getRecords().isEmpty()) {
                persistenceManager.saveChatHistory(chat);
            }            
            ChatServer.getChatRooms().remove(String.valueOf(datos.getChatID()));
        }
    }
    
    private void sendMessage(DataMssge datos) throws IOException {
        notifyUsersInChatroom(datos);
    }
    
    private void notifyUsersInChatroom(DataMssge datos) throws IOException {
        Chat chat = ChatServer.getChatRooms().get(String.valueOf(datos.getChatID()));
        
        if (datos.getOption().equals(Constantes.Eoption.mensajeChat)) {
            //creates a message and adds it to the list of messages of the chat
            model.Message m = new Message();
            m.setStrText(datos.getStrText());
            m.setStrUsername(datos.getUser().getUsername());
            chat.addMessageToList(m);
        }
        //sets all users in chatroom to datos to display answer and update list
        datos.getUsers().clear();
        for (String username : chat.getUsers()) {
            datos.getUsers().put(username, ChatServer.getConnectedUsers().get(username).getName());
        }
        
        for (String username : chat.getUsers()) {
            if (!username.equals(datos.getUser().getUsername())) {
                ChatServer.getConnectedUsers().get(username).getsConnection().sendRequest(datos);
            }
        }
    }
    
    private void getHistoryDates(DataMssge datos) {
        datos.setRecords(persistenceManager.getRecordDatesByUsername(datos.getUser().getUsername()));
        try {
            this.comm.sendRequest(datos);
        } catch (IOException ex) {
            Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getRecord(DataMssge datos) {
        Chat chat;
        Hashtable<String, String> users = new Hashtable<String, String>();
        int i = 0;
        
        chat = persistenceManager.getRecord(datos.getChatID());
        datos.setMensajes(chat.getRecords());
        
        for (String name : chat.getHistUsers()) {
            users.put(String.valueOf(i), name);
            i++;
        }
        
        datos.setUsers(users);
        
        try {
            this.comm.sendRequest(datos);
        } catch (IOException ex) {
            Logger.getLogger(ClientProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
