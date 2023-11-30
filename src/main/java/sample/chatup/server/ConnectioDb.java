package sample.chatup.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class ConnectioDb {
    private static Connection instance = null;

    private ConnectioDb() throws DBException {

        try {
            forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            throw new DBException(e);
        }

        try {
            instance = DriverManager.getConnection("jdbc:hsqldb:file:src/main/resources/chatdb", "SA", "");
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }



    public static Connection getConnectionInstance() throws DBException {
        if (instance==null){
           new ConnectioDb();
        }
        return instance;
    }

}
