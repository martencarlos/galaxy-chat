package persistence;

public class DataPersistFactory {

    public static DataDAO getDataDAO(String persistenceMechanism){
        persistenceMechanism = persistenceMechanism.toUpperCase();
        
        if (persistenceMechanism.equals(ConstantesDAO.DB_TYPE_FILES)){
            //return FriendDAOFileImplementation.getJDBCPersistenceManager();
            return null;//eliminar
        }
        else if  (persistenceMechanism.equals(ConstantesDAO.DB_TYPE_DERBY)){
            return DataDAOJDBCImplementationDerby.getJDBCPersistenceManagerDerby();
        }
        else if  (persistenceMechanism.equals(ConstantesDAO.DB_TYPE_MYSQL)){
            return DataDAOJDBCImplementationMySQL.getJDBCPersistenceManagerMySQL();
        }
        else{
            return null;
        }
    }
}
