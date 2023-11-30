package sample.chatup.client.gui;

import javax.swing.*;
import java.awt.*;

public class ClientForm extends JFrame {
    JList listUser;
    DefaultListModel listModel = new DefaultListModel();
    public ClientForm(){
        listUser = new JList(listModel);
        listUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listUser.setSelectedIndex(0);

        listModel.addElement("nickname");
        listModel.addElement("nickname moososooso");
        listModel.addElement("nickname moososooso");
        listModel.addElement("nickname moososooso");
        listModel.addElement("nickname moososooso");
        listModel.addElement("nickname moososooso");

       setSize(400,400);
       setTitle("Chat-up v1.0.0");
       setLayout(new BorderLayout());
       add(BorderLayout.WEST,new JScrollPane(listUser));
       add(BorderLayout.CENTER, new ChatPanel());
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setVisible(true);
    }

    public static void main(String[] args) {
        new ClientForm();
    }

}
