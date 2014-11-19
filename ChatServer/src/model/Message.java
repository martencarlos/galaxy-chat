
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Carlos Marten
 */
public class Message implements Serializable{

    private String strText;
    private Timestamp datTime;
    private String strUsername;
            
    public Message() {
        this.datTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public Date getDatTime() {
        return datTime;
    }

    public void setDatTime(Timestamp datTime) {
        this.datTime = datTime;
    }

    public String getStrText() {
        return strText;
    }

    public void setStrText(String strText) {
        this.strText = strText;
    }

    public String getStrUsername() {
        return strUsername;
    }

    public void setStrUsername(String strUsername) {
        this.strUsername = strUsername;
    }

    @Override
    public String toString() {
        return "Message{" + "strText=" + strText + ", datTime=" + datTime + ", strUsername=" + strUsername + '}';
    }
    
    
    
}
