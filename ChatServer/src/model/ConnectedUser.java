
package model;

import controller.Communicate;

/**
 *
 * @author Carlos Marten
 */
public class ConnectedUser {
    private String name;

    public ConnectedUser(String username, String name, String lastname, Communicate comm) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.comm = comm;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Communicate getsConnection() {
        return comm;
    }

    public void setsConnection(Communicate sConnection) {
        this.comm = sConnection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    private String lastname;
    private String username;
    private Communicate comm;
}
