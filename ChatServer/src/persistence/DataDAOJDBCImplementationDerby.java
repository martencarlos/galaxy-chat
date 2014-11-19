package persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Chat;
import model.Message;
import model.User;
//@Abraham Prácticamente todos los m!todos de esta clase son vulnerables a
//inyecciones SQL. Esto podría haberse resuelto de un modo sencillo empleando
//PreparedStatement en vez de Statement
public class DataDAOJDBCImplementationDerby implements DataDAO {

    private final String strDriver = "org.apache.derby.jdbc.ClientDriver";
    private final Object lockOfTheConexion = new Object();
    private Connection conexion = null;
    private static DataDAOJDBCImplementationDerby persistenceManager = null;
    private static final Logger logger = Logger.getLogger(DataDAOJDBCImplementationDerby.class.getName());
    protected List<Chat> chats = null;

    DataDAOJDBCImplementationDerby() {
    }
//@Abraham Problema de concurrencia; podrían crearse más de una instancia
    public static DataDAOJDBCImplementationDerby getJDBCPersistenceManagerDerby() {
        if (persistenceManager == null) {
            persistenceManager = new DataDAOJDBCImplementationDerby();
        }
        return persistenceManager;
    }

    @Override
    public boolean setUp(String url, String user, String password) {
        try {
            Class.forName(strDriver);
            conexion = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "No se encontro el driver para la base de datos", ex);
            return false;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "No se pudo establecer la conexion con la base de datos", ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean disconnect() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Conexion a la base de datos no cerrada", ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean isUserUnique(String username) throws SQLException {

        String query = "select \"username\" from CHAT.CLIENT"
                + " where \"username\" = '" + username + "'";
        Statement statement;
        ResultSet resultSet;

        synchronized (lockOfTheConexion) {
            statement = conexion.createStatement();
        }
        resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return false;
        } else {
            return true;
        }
        //@Abraham no se cierra el statement, y lo mismo para todos los demás métodos de esta clase
    }

    @Override
    public void createNewUser(User user) {
        Statement statement;
        String query = "insert into CHAT.CLIENT"
                + " values('" + user.getUsername() + "','"
                + user.getName() + "','"
                + user.getLastName() + "','"
                + user.getMail() + "','"
                + user.getPassword() + "')";
        try {
            synchronized (lockOfTheConexion) {
                statement = conexion.createStatement();
                statement.executeUpdate(query);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error while creating new User", ex);
        }
    }

    @Override
    public boolean isUserRegistered(User user) throws SQLException {
        ResultSet resultSet = null;
        Statement statement;
        String query = "select \"username\", \"password\" from CHAT.CLIENT"
                + " where \"username\" = '" + user.getUsername() + "'"
                + " and \"password\" = '" + user.getPassword() + "'";

        synchronized (lockOfTheConexion) {
            statement = conexion.createStatement();
        }
        resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
//@Abraham no se cierra el resultSet (ni el statement), y lo mismo para todos los demás métodos de esta clase
    }

    @Override
    public User getUserFromDB(String username) {

        String strSQL;
        Statement statement;
        ResultSet resultSet = null;
        User user = new User();

        strSQL = "Select * from CHAT.CLIENT where \"username\"='" + username + "'";

        try {
            synchronized (lockOfTheConexion) {
                statement = conexion.createStatement();
            }
            resultSet = statement.executeQuery(strSQL);
            while (resultSet.next()) {
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setMail(resultSet.getString("MAIL"));
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error while getting user " + username + " from DB", ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE,
                            "Error while closing connection", ex);
                }
            }
        }

        return user;
    }

    @Override
    public void saveChatHistory(Chat chat) {
        Statement statement;
        Statement stm;
        ResultSet rsGeneratedKeys;
        int intRecordID;

        try {
            String query = "insert into CHAT.HISTORY (\"date\",\"log\")"
                    + " values('" + chat.getFecha() + "','"
                    + chat.getLog() + "')";
            System.out.println(query);
            synchronized (lockOfTheConexion) {
                statement = conexion.createStatement();
            }
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            
            synchronized (lockOfTheConexion) {
                stm = conexion.createStatement();
            }
            //gets the generated index
            rsGeneratedKeys = statement.getGeneratedKeys();
            while (rsGeneratedKeys.next()) {
                intRecordID = rsGeneratedKeys.getInt(1); //get the first index  

                //stores users from the chatroom
                for (String username : chat.getHistUsers()) {
                    query = "insert into CHAT.CLIENTHISTORY"
                            + " values(" + String.valueOf(intRecordID) + ",'"
                            + username + "')";
                    stm.executeUpdate(query);
                }

                //stores messages from the chatroom
                for (Message message : chat.getRecords()) {
                    query = "insert into chat.messagechat (\"record_id\",\"username\",\"messagecontent\",\"date\")"
                            + " values(" + String.valueOf(intRecordID) + ",'"
                            + message.getStrUsername() + "','"
                            + message.getStrText() + "','"
                            + message.getDatTime() + "')";
                    stm.executeUpdate(query);
                }
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error while creating new User", ex);
        }
    }

    @Override
    public Chat getRecord(int recordID) {
        String query;
        Statement statement;
        ResultSet resultSet = null;
        Chat chat = new Chat();
        List<String> lstUsers = new ArrayList<String>();

        try {
            //get all the messages for this user and chatroom
            query = "select \"username\", \"messagecontent\", \"date\""
                    + " from chat.messagechat"
                    + " where \"record_id\" = " + recordID;

            synchronized (lockOfTheConexion) {
                statement = conexion.createStatement();
            }
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Message m = new Message();
                m.setStrUsername(resultSet.getString(ConstantesDAO.COLUMN_USERNAME));
                m.setStrText(resultSet.getString(ConstantesDAO.COLUMN_MESSAGECONTENT));
                m.setDatTime(resultSet.getTimestamp(ConstantesDAO.COLUMN_DATE));

                chat.addMessageToList(m);
            }

            //get all the users for this chatroom
            query = "select c.\"name\""
                    + " from chat.clienthistory ch, client c"
                    + " where ch.\"username\"=c.\"username\""
                    + " and \"record_id\" = " + recordID;
            synchronized (lockOfTheConexion) {
                statement = conexion.createStatement();
            }
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                lstUsers.add(resultSet.getString(ConstantesDAO.COLUMN_NAME));
            }
            chat.setHistUsers(lstUsers);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error while getting record " + recordID, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE,
                            "Error while closing connection", ex);
                }
            }
        }

        return chat;
    }

    @Override
    public Hashtable<String, Timestamp> getRecordDatesByUsername(String username) {

        String query;
        Statement statement;
        ResultSet resultSet = null;
        Hashtable<String, Timestamp> lstChats = new Hashtable<String, Timestamp>();

        query = "select h.\"record_id\", h.\"date\""
                + " from chat.history as h, chat.clienthistory as ch"
                + " where h.\"record_id\" = ch.\"record_id\""
                + " and ch.\"username\" = '" + username + "'"
                + " order by h.\"date\" asc";
        try {
            synchronized (lockOfTheConexion) {
                statement = conexion.createStatement();
            }
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                lstChats.put(resultSet.getString(ConstantesDAO.COLUMN_RECORDID), resultSet.getTimestamp(ConstantesDAO.COLUMN_DATE));
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error while getting user " + username + " from DB", ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE,
                            "Error while closing connection", ex);
                }
            }
        }

        return lstChats;
    }
}