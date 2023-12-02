package Listeners;

import Service.UserImp;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

public class updateListListener extends MouseAdapter {

    private UserImp user;

    public updateListListener(UserImp user) {
        this.user = user;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            DefaultListModel model = new DefaultListModel();
            try {
                if (user.getForum() != null) {
                    model.addElement("tous");
                    for (Object user_id : user.getForum().qui().split("\n")) {
                        model.addElement(user_id);
                    }
                } else {
                    model.addElement("-------------");
                    model.addElement("-------------");
                    model.addElement("-------------");
                }
                user.list.setModel(model);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

        }
    }
}
