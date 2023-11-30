package sample.chatup.model;

import sample.chatup.utils.BaseFixedString;
import sample.chatup.utils.FixedField;

public class UserBean extends BaseFixedString {

    @FixedField(size = 256)
    private String nickname;

    public UserBean(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }


    @Override
    public String toString() {
        return  padString(nickname,256);
    }
}
