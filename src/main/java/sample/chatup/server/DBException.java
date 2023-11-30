package sample.chatup.server;

import java.sql.SQLException;

public class DBException extends Exception{
    public DBException(Exception e) {
        super(e);
    }
}
