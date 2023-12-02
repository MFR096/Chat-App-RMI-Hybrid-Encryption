package Listeners;

import Service.UserImp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class quitterListener implements ActionListener {
    private UserImp user;
    public quitterListener(UserImp user) {
        this.user = user;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            user.getForum().quitter(user.getId());
            user.intervenants.setEnabled(false);
            user.quitter.setEnabled(false);
            user.envoyer.setEnabled(false);
            user.setId(0);

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}