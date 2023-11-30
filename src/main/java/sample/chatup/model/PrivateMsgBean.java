package sample.chatup.model;

import sample.chatup.utils.BaseFixedString;
import sample.chatup.utils.FixedField;

public class PrivateMsgBean extends BaseFixedString {
    @FixedField(size = 9)
    private int id;
    @FixedField(size = 512)
    private String msg;
    @FixedField(size = 256)
    private String sender;
    @FixedField(size = 256)
    private String receiver;

    @FixedField(size = 26)
    private String time;


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public PrivateMsgBean(int id, String msg, String sender, String receiver, String timer) {
        this.id = id;
        this.msg = msg;
        this.sender = sender;
        this.receiver = receiver;
        this.time = timer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {

        return  padString(""+id,9) +
                padString( msg,512) +
                padString( sender ,256)+
                padString( receiver ,256)+
                padString( time ,256);
    }
}

