package sample.chatup.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.chatup.model.GlobalMsgBean;
import sample.chatup.model.PrivateMsgBean;
import sample.chatup.model.UserBean;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerChatUp {
    private static final List<NotifyEventServer> listConnection;
    private static boolean isAlive = false;

    private static Logger logger = LoggerFactory.getLogger(ServerChatUp.class);

    static {
        isAlive = true;
        listConnection = new ArrayList<>();

    }

    public static void main(String[] args)  {

        try {

            Connection connection = ConnectioDb.getConnectionInstance();
            Statement stm = connection.createStatement();

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });

            if (!tables.next()) {
                logger.info("Nessuna tabella è stata creata nel database.");
                initializeDatabase();
            } else {
                logger.info("Sono state create tabelle nel database.");
            }

            stm.close();

            ServerSocket server = new ServerSocket(1612);
            while(isAlive) {
                Socket client = server.accept();
                listConnection.add(new ComunicationServer(client));
            }
        } catch (Exception e) {
            logger.error("ERROR",e);
        }
        finally {
            try {
                closeConnection();
            } catch (DBException e) {
                logger.error("Error:",e);
            }
        }
    }

    private static void initializeDatabase() throws DBException {
        Connection connection = ConnectioDb.getConnectionInstance();
        Statement stm = null;
        try {
            stm = connection.createStatement();
            stm.executeUpdate("CREATE TABLE GLOBAL_MSG(id integer,time timestamp,msg varchar(512),sender varchar(256))");
            stm.executeUpdate("CREATE TABLE PRIVATE_MSG(id integer,time timestamp,msg varchar(512),sender varchar(256),receiver varchar(256))");
        } catch (SQLException e) {
            logger.error("Error:",e);
            throw new DBException(e);
        }

    }

    private static void closeConnection() throws DBException{
        try {
            ConnectioDb.getConnectionInstance().close();
        } catch (SQLException e) {
            logger.error("Error:",e);
            throw new DBException(e);
        }
    }

    public static synchronized boolean isFreeNickname(String nickname){
        for (NotifyEventServer notifyer: listConnection){
            if(notifyer.checkNickname(nickname)){
                return false;
            }
        }

        return true;
    }


    public static synchronized void removeNotifier(NotifyEventServer notifier) {
        listConnection.remove(notifier);
    }

    public static synchronized void notifyAllNewUser(String nickname) throws IOException {
        for (NotifyEventServer notifier: listConnection){
            notifier.notifyNewUser(nickname);
        }
    }

    public static synchronized void sendGlobalMessage(String message, String nickname) throws DBException {
        for (NotifyEventServer notifier: listConnection){
            try {
                insertGlobaMessage(message,nickname);
                notifier.notifyGlobalMessage(message,nickname);
            } catch (IOException e) {
                throw new DBException(e);
            }
        }
    }

    public static synchronized void sendPrivateMessage(String message, String nickname, String to) throws DBException {
        for (NotifyEventServer notifier: listConnection){
             if(notifier.checkNickname(to)){
                 try {
                     insertPrivateMessage(message, nickname,to);
                     notifier.notifyPivateMessage(message,nickname,to);
                 } catch (IOException e) {
                     throw new DBException(e);
                 }
             }
        }
    }

    public static void shutdows(){
        isAlive = false;
    }

    public static synchronized String getUsers() {
        StringBuilder users = new StringBuilder();
        for (NotifyEventServer notifier: listConnection){
            users.append(new UserBean(notifier.getNickname()));
        }
        return users.toString();
    }

    public static synchronized String getGlobalMessage() throws DBException {
        StringBuilder messages = new StringBuilder();
        Connection conn = ConnectioDb.getConnectionInstance();

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM GLOBAL_MESSAGE");

            while(rs.next()){
                int id = rs.getInt("id");
                String msg = rs.getString("msg");
                Timestamp time = rs.getTimestamp("time");
                String sender = rs.getString("sender");
                GlobalMsgBean bean = new GlobalMsgBean(id,msg,sender,time.toString());
                messages.append(bean.toString());
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }

        return messages.toString();
    }

    public static synchronized String getPrivatelMessage(String nickname) throws DBException {
        StringBuilder messages = new StringBuilder();
        Connection conn = ConnectioDb.getConnectionInstance();

        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM PRIVATE_MESSAGE WHERE sender=? OR receiver=?");
            stm.setString(1,nickname);
            stm.setString(2,nickname);

            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String msg = rs.getString("msg");
                Timestamp time = rs.getTimestamp("time");
                String sender = rs.getString("sender");
                String receiver = rs.getString("receiver");

                PrivateMsgBean bean = new PrivateMsgBean(id,msg,sender,receiver ,time.toString());
                messages.append(bean.toString());
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }

        return messages.toString();
    }

    private static void insertPrivateMessage(String message, String nickname, String to) throws DBException {
        Connection conn = ConnectioDb.getConnectionInstance();

        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO PRIVATE_MESSAGE(id,time,msg,sender,receiver) VALUES(?,?,?,?,?");
            stm.setInt(1,getLastPrivateID()+1);
            stm.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
            stm.setString(3,message);
            stm.setString(4,nickname);
            stm.setString(5,to);

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static void insertGlobaMessage(String message, String nickname) throws DBException {
        Connection conn = ConnectioDb.getConnectionInstance();

        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO GLOBAL_MESSAGE(id,time,msg,sender) VALUES(?,?,?,?)");
            stm.setInt(1,getLastGlobalID()+1);
            stm.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
            stm.setString(3,message);
            stm.setString(4,nickname);

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static int getLastPrivateID() throws DBException {
        Connection conn = ConnectioDb.getConnectionInstance();
        int id = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(id) FROM PRIVATE_MESSAGE");

            while (rs.next()){
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }        
        return id;
    }

    private static int getLastGlobalID() throws DBException {
        Connection conn = ConnectioDb.getConnectionInstance();
        int id = 0;
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(id) FROM GLOBAL_MESSAGE");

            while (rs.next()){
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }
        return id;
    }
}
