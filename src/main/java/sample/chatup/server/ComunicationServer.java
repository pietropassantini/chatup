package sample.chatup.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ComunicationServer implements Runnable,NotifyEventServer{

    private final Socket client;
    private boolean isAlive;
    private static final String PROTOCOL_SEP = "@";
    private static final String PROTOCOL_VERSION = "1.0";

    private String nickname ;
    public ComunicationServer(Socket client){
        this.client = client;
        isAlive = true;
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            PrintStream ps = new PrintStream(output);
            DataInputStream input = new DataInputStream(client.getInputStream());
            byte[] message = new byte[1000];
            int nb = input.read(message);

            if(nb==-1){
                ps.println("Connection refused");
                ServerChatUp.removeNotifier(this);
                client.close();
                return;
            }

            String accept = new String(message);
            String[] command = accept.split("\\"+PROTOCOL_SEP);

            if(command.length!=2){
                ps.println("Connection refused protocol unknown");
                ServerChatUp.removeNotifier(this);
                client.close();
                return;
            }

            if(ChatProtocol.ACCEPT.getProtocol().equals(command[0])){
              if(!ServerChatUp.isFreeNickname(command[1])){
                  ps.println("Connection refused nickname already exist");
                  ServerChatUp.removeNotifier(this);
                  client.close();
                  return;
              }
              else {
                  this.nickname=command[1];
                  ServerChatUp.notifyAllNewUser(nickname);
                }
            }
            else {
                ps.println("Connection refused protocol unknown");
                ServerChatUp.removeNotifier(this);
                client.close();
                return;
            }

            ps.println(ChatProtocol.WELCOME +PROTOCOL_SEP+PROTOCOL_VERSION);
            while(isAlive){
                int n = input.read(message);
                if(n==-1){
                    continue;
                }
                String msg = new String(message);
                String[]  protoMsg = msg.split("\\"+PROTOCOL_SEP);

                if(ChatProtocol.GLOBAL.getProtocol().equals(protoMsg[0])){
                     ServerChatUp.sendGlobalMessage(protoMsg[1],nickname);
                }
                else if (ChatProtocol.PRIVATE.getProtocol().equals(protoMsg[0])){
                    ServerChatUp.sendPrivateMessage(protoMsg[1],nickname,protoMsg[0]);
                }
                else if(ChatProtocol.LIST.getProtocol().equals(protoMsg[0])){
                    String users = ServerChatUp.getUsers();
                    notifyRequest(ChatProtocol.RCVLIST.getProtocol()+PROTOCOL_SEP+users);
                }
                else if(ChatProtocol.GETGLOBAL.getProtocol().equals(protoMsg[0])){
                    String global = ServerChatUp.getGlobalMessage();
                    notifyRequest(ChatProtocol.RCVGLOBAL.getProtocol()+PROTOCOL_SEP+global);
                }
                else if(ChatProtocol.GETPRIVATE.getProtocol().equals(protoMsg[0])){
                    String privMsg = ServerChatUp.getPrivatelMessage(nickname);
                    notifyRequest(ChatProtocol.RCVPRIVATE.getProtocol()+PROTOCOL_SEP+privMsg);
                }
                else if(ChatProtocol.CLOSE.getProtocol().equals(protoMsg[0])){
                    ServerChatUp.removeNotifier(this);
                    client.close();
                    return;
                }



            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * @param user
     */
    @Override
    public void notifyNewUser(String user) throws IOException {
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        PrintStream ps = new PrintStream(output);
        ps.println(ChatProtocol.ACCEPT+PROTOCOL_SEP+user);
    }

    /**
     * @param message
     */
    @Override
    public void notifyGlobalMessage(String message,String from) throws IOException {
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        PrintStream ps = new PrintStream(output);
        ps.println(ChatProtocol.GLOBAL+PROTOCOL_SEP+message+PROTOCOL_SEP+from);
    }

    /**
     * @param message
     */
    @Override
    public void notifyPivateMessage(String message,String from,String to) throws IOException {
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        PrintStream ps = new PrintStream(output);
        ps.println(ChatProtocol.PRIVATE+PROTOCOL_SEP+message+PROTOCOL_SEP+to);
    }

    public void notifyRequest(String message)  throws IOException {
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        PrintStream ps = new PrintStream(output);
    }

    /**
     * @param message
     */
    /**
     *
     */
    @Override
    public void endComunication() {
        isAlive = false;
    }

    /**
     * @param nickName
     * @return
     */
    @Override
    public boolean checkNickname(String nickName) {
        return this.nickname.equals(nickName);
    }

    @Override
    public String getNickname() {
        return nickname;
    }

}
