package Listeners;

import Cryptosystem.KeyGenerator;
import Service.UserImp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class envoyerListener implements ActionListener {
    private UserImp user;

    public envoyerListener(UserImp user) {
        this.user = user;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if(user.input.getText().equals("")) JOptionPane.showOptionDialog(null, "Votre message est vide!", "Warning", JOptionPane.WARNING_MESSAGE, 0, null, null, e);
            else if(user.list.isSelectionEmpty()) JOptionPane.showOptionDialog(null, "Vous devez sélectionner un récepteur!", "Warning", JOptionPane.WARNING_MESSAGE, 0, null, null, e);
            else{
                user.setIv(KeyGenerator.generateIV(16));
                int[] users = user.list.getSelectedIndices();
                if(users.length==1) {
                    String msg = "";
                    if(users[0]==0) user.ecrire("Moi [à @tous]: " + user.input.getText()+"\n");
                    else user.ecrire("Moi [à @"+users[0]+"]: " + user.input.getText()+"\n");
                }
                else user.ecrire("Moi [à @plusieur]: " + user.input.getText()+"\n");
                for(int i: users) {
                    if(i==0) {
                        for(int j=1; j<user.list.getModel().getSize(); j++) {
                            if(j!=user.getId()) {
                                String id_string = (String) user.list.getModel().getElementAt(j);
                                int id = Integer.valueOf(id_string.substring(1));
                                String msg = "";
                                if(users.length==1) {
                                    if(users[0]==0){
                                        msg = "@" + user.getId() + " [à @tous]: " + user.input.getText()+"\n";

                                    }
                                    else msg = "@" + user.getId() + " [à toi]: " + user.input.getText()+"\n";
                                }
                                else msg = "@" + user.getId() + " [à @plusieur]: " + user.input.getText()+"\n";
                                user.getForum().dire(id, user.encrypt(id, msg));
                            }
                        }
                        break;
                    }
                    if(i!=user.getId()) {
                        String id_string = (String) user.list.getModel().getElementAt(i);
                        int id = Integer.valueOf(id_string.substring(1));
                        String msg = "";
                        if(users.length==1) {
                            if(users[0]==0) msg = "@" + user.getId() + " [à @tous]: " + user.input.getText()+"\n";
                            else msg = "@" + user.getId() + " [à toi]: " + user.input.getText()+"\n";
                        }
                        else msg = "@" + user.getId() + " [à @plusieur]: " + user.input.getText()+"\n";
                        user.getForum().dire(id, user.encrypt(id, msg));
                    }
                }
                user.input.setText("");
            }

        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (SignatureException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
    }
}