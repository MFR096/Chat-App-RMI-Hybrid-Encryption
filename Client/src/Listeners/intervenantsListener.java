package Listeners;

import Service.UserImp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class intervenantsListener implements ActionListener {
    private UserImp user;

    public intervenantsListener(UserImp user) {
        this.user = user;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            user.ecrire("Forum: les intervenants connectés: \n");
            user.ecrire(user.getForum().qui());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
