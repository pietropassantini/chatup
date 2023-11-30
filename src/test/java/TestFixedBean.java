import sample.chatup.utils.BaseFixedString;
import sample.chatup.utils.FixedField;

import java.sql.Timestamp;

public class TestFixedBean extends BaseFixedString {

    @FixedField(size = 9)
    private int id;
    @FixedField(size = 512)
    private String msg;
    @FixedField(size=26)
    private String time;

    public TestFixedBean(int id, String msg, String time) {
        this.id = id;
        this.msg = msg;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return
                padString(""+id,9) +
                padString( msg,512) +
                padString( time ,26);
    }
}
