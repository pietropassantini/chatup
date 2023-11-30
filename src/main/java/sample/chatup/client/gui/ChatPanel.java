package sample.chatup.client.gui;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {

    JTextArea chatArea = new JTextArea();
    JTextField sendMsg = new JTextField("");
    JButton sendBt = new JButton("->");

    public ChatPanel(){

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());
        sendPanel.add(BorderLayout.CENTER,sendMsg);
        sendPanel.add(BorderLayout.EAST,sendBt);

        setLayout(new BorderLayout());
        add(BorderLayout.CENTER,chatArea);
        add(BorderLayout.SOUTH,sendPanel);


    }
}
