package persistence;

import java.sql.Timestamp;
import java.util.Hashtable;
import model.Chat;
import model.User;


public interface DataDAO {
    
    public boolean setUp(String url, String user, String password);

    public boolean disconnect();
    
    public boolean isUserRegistered(User u)throws Exception;
    
    public boolean isUserUnique(String username) throws Exception;
    
    public void createNewUser(User u);
    
    public User getUserFromDB(String username);
    
    public Chat getRecord(int recordID);
    
    public Hashtable<String, Timestamp> getRecordDatesByUsername(String username);
    
    public void saveChatHistory(Chat chat);

}
