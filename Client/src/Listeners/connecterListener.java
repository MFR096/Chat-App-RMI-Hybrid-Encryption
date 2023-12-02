package Listeners;

import Cryptosystem.KeyGenerator;
import Service.Forum;
import Service.Proxy;
import Service.ProxyImp;
import Service.UserImp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class connecterListener implements ActionListener {
    private UserImp user;

    public connecterListener(UserImp user) {
        this.user = user;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (user.getId() != 0)
                user.ecrire("Forum: Vous êtes déjà connecté avec ID: @" + user.getId() + "\n");
            else {

                user.setForum((Forum) Naming.lookup("rmi://localhost:1099/ch"));
                Proxy proxy = new ProxyImp(user);
                user.setKeyPair(KeyGenerator.generateAsymmetricKeyPair(2048));
                user.setId(user.getForum().entrer(proxy, user.getPublicKey()));

                user.intervenants.setEnabled(true);
                user.quitter.setEnabled(true);
                user.envoyer.setEnabled(true);

                DefaultListModel model = new DefaultListModel();
                if (user.getForum() != null) {
                    model.addElement("tous");
                    for (Object user_id : user.getForum().qui().split("\n")) {
                        model.addElement(user_id);
                    }
                } else {
                    model.addElement("---");
                    model.addElement("---");
                }
                user.list.setModel(model);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}