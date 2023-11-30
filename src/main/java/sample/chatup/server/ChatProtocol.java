package sample.chatup.server;

public enum ChatProtocol {
    ACCEPT("accept"),
    GLOBAL("global"),
    PRIVATE("private"),
    WELCOME("welcome"),
    LIST("list"),
    GETGLOBAL("getglobal"),
    GETPRIVATE("getprivate"),
    RCVLIST("rcvlist"),
    RCVGLOBAL("rcvglobal"),
    RCVPRIVATE("rcvprivate"),
    CLOSE("close");

    private String protocol;

    ChatProtocol(String protocol) {
        this.protocol=protocol;
    }

    public String getProtocol(){
        return protocol;
    }
}
