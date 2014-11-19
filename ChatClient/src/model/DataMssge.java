
package model;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import model.Constantes.Eoption;

/**
 *
 * @author Carlos Marten
 */
public class DataMssge implements Serializable{
    
    private Eoption option;
    private User user;
    private Hashtable<String, String> users;
    Hashtable<String, Timestamp> records;
    private int chatID;
    private String strError;
    private String strLog;
    private String strText;
    private List<model.Message> mensajes;

    public DataMssge(){
        user = new User();
        users = new Hashtable<String, String>();
        mensajes = new ArrayList<Message>();
    }

    public List<Message> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Message> mensajes) {
        this.mensajes = mensajes;
    }
    
    
    
    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public Eoption getOption() {
        return option;
    }

    public void setOption(Eoption option) {
        this.option = option;
    }

    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }

    public String getStrLog() {
        return strLog;
    }

    public void setStrLog(String strLog) {
        this.strLog = strLog;
    }

    public String getStrText() {
        return strText;
    }

    public void setStrText(String strText) {
        this.strText = strText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//@Abraham Romperla en cancelacion; usar copia defensiva
    public Hashtable<String, String> getUsers() {
        return users;
    }
//@Abraham Romperla en cancelacion; usar copia defensiva
    public void setUsers(Hashtable<String, String> users) {
        this.users = users;
    }
    //@Abraham Romperla en cancelacion; usar copia defensiva
    public Hashtable<String, Timestamp> getRecords() {
        return records;
    }
//@Abraham Romperla en cancelacion; usar copia defensiva
    public void setRecords(Hashtable<String, Timestamp> records) {
        this.records = records;
    }
}
