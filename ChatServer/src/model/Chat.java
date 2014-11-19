package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Carlos Marten
 */
public class Chat {

    private int chatID;
    private Timestamp fecha;
    private List<Message> records;
    private String log;
    private List<String> users;
    private List<String> histUsers;

    public Chat() {
        this.users = new ArrayList<String>();
        this.histUsers = new ArrayList<String>();
        this.records = new ArrayList<Message>();
        fecha = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public synchronized List<String> getHistUsers() {
        List<String> copyHistUsers = new ArrayList<String>();
        Iterator iter = this.histUsers.iterator();
        while (iter.hasNext()) {
            copyHistUsers.add((String)iter.next());
        }
        return copyHistUsers;
    }

    public void setHistUsers(List<String> histUsers) {
        //@Abraham Acceso a histUsers sin sincronizar…
        this.histUsers = histUsers;
    }

    public synchronized List<String> getUsers() {
        List<String> copyUsers = new ArrayList<String>();
        Iterator iter = this.users.iterator();
        while (iter.hasNext()) {
            copyUsers.add((String)iter.next());
        }
        return copyUsers;
    }

    public void setUsers(List<String> users) {
        //@Abraham Acceso a users sin sincronizar…
        this.users = users;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public synchronized void addMessageToList(Message m) {
        this.records.add(m);
    }
    
    public synchronized void addUserToList(String u) {
        
        this.users.add(u);
    }
    
    public synchronized void removeUserFromList(String u) {
        this.users.remove(u);
    }
    
    public synchronized void addHistUserToList(String hu) {
        this.histUsers.add(hu);
    }

    public synchronized List<Message> getRecords() {
        List<Message> copyRecords = new ArrayList<Message>();
        Iterator iter = this.records.iterator();
        while (iter.hasNext()) {
            copyRecords.add((Message)iter.next());
        }
        return copyRecords;
    }

    @Override
    public String toString() {
        return "Chat{" + "chatID=" + chatID + ", record=" + records + ", log=" + log + ", users=" + users + ", histUsers=" + histUsers + '}';
    }
}
