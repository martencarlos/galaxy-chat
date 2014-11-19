
package model;

import java.io.Serializable;

/**
 *
 * @author Carlos Marten
 */
public class User implements Serializable {
    
    private String username=null;
    private String name=null;
    private String lastname=null;
    private String mail=null;
    private String password=null;

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String toString(){
        StringBuilder b= new StringBuilder();
        b.append(this.username);
        b.append(",");
        b.append(this.name);
        b.append(",");
        b.append(this.lastname);
        b.append(",");
        b.append(this.mail);
        b.append(",");
        b.append(this.password);
        return b.toString();
    }
    
    
}
