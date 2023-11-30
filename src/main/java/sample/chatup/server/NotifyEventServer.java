package sample.chatup.server;

import java.io.IOException;

public interface NotifyEventServer {

    void notifyNewUser(String user) throws IOException;
    void notifyGlobalMessage(String message,String from) throws IOException;
    void notifyPivateMessage(String message,String from,String to) throws IOException;

    void endComunication();
    boolean checkNickname(String nickName);
    String getNickname();
}
