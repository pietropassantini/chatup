package sample.chatup.utils;

public class BaseFixedString {

    protected static String padString(String input, int length) {
        return String.format("%-" + length + "s", input);
    }


}
