package controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import persistence.DataDAO;
import persistence.DataPersistFactory;

/**
 *
 * @author Carlos Marten
 */
public class ValidationMethods {

    DataDAO persistenceManager = DataPersistFactory.getDataDAO(ChatServer.getDbType());

    public boolean isUserUnique(String username)  {
        boolean b = false;
        try {
            b = persistenceManager.isUserUnique(username);
            
        } catch (Exception ex) {
            Logger.getLogger(ValidationMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    public boolean isUserRegisteredInServer(User user) {
        boolean b = false;
        try {
            b = persistenceManager.isUserRegistered(user);
        } catch (Exception ex) {
            Logger.getLogger(ValidationMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    public boolean isUserConnected(String username) {
        if (ChatServer.getConnectedUsers().containsKey(username)) {
            return true;
        } else {
            return false;
        }
    }
}
