package Service;

import Cryptosystem.KeyGenerator;
import Cryptosystem.MessageEncryption;
import Cryptosystem.SecureSinglePackage;
import Listeners.*;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.security.*;

public class UserImp implements User {

	public JFrame frame;
	public JTextArea screen; // pour afficher les réponses du Forum: messages et intervenants
	public JTextField input; // pour écrire les messages
	public JList list; // list des intervenants
	public JButton connecter, intervenants, quitter, envoyer;

	private Forum forum = null; // reference to the forum remote object
	private int id = 0; // identifier of the client allocated by the server

	private  byte[] iv = KeyGenerator.generateIV(16);
	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	private KeyPair keyPair;
	public UserImp() {
		init_GUI();
	}

	public void init_GUI(){
		// creation of the GUI
		frame = new JFrame();
		frame.setLayout(new BorderLayout());

		screen = new JTextArea(20, 70);
		screen.setEditable(false);
		screen.setForeground(new Color(253, 253, 253));
		screen.setBackground(new Color(38, 39, 57));
		screen.setFont(new Font("Bahnschrift", 1, 14));
		frame.add(new JScrollPane(screen), BorderLayout.CENTER);

		JPanel panel_up = new JPanel();
		panel_up.setLayout(new FlowLayout());
		frame.add(panel_up, BorderLayout.PAGE_START);

		connecter = new JButton("Se Connecter");
		connecter.addActionListener(new connecterListener(this));
		panel_up.add(connecter);

		intervenants = new JButton("Intervenants");
		intervenants.addActionListener(new intervenantsListener(this));
		intervenants.setEnabled(false);
		panel_up.add(intervenants);

		quitter = new JButton("Quitter");
		quitter.addActionListener(new quitterListener(this));
		quitter.setEnabled(false);
		panel_up.add(quitter);

		JPanel panel_down = new JPanel();
		panel_down.setLayout(new FlowLayout());
		frame.add(panel_down, BorderLayout.PAGE_END);

		panel_down.add(new JLabel("Votre Message: "));

		input = new JTextField(50);
		panel_down.add(input);

		panel_down.add(new JLabel("à: "));

		list = new JList();
		list.setVisibleRowCount(3);
		list.setSelectedIndices(new int[] { 0, 3 });
		DefaultListModel model = new DefaultListModel();
		model.addElement("-------------");
		model.addElement("-------------");
		model.addElement("-------------");
		list.setModel(model);
		list.addMouseListener(new updateListListener(this));
		panel_down.add(new JScrollPane(list));

		envoyer = new JButton("Envoyer");
		envoyer.addActionListener(new envoyerListener(this));
		envoyer.setEnabled(false);
		panel_down.add(envoyer);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Forum");
		frame.pack();
		frame.setVisible(true);
	}

	public Forum getForum() {
		return forum;
	}
	public void setForum(Forum forum) {
		this.forum = forum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}
	public PublicKey getPublicKey()
	{
		return keyPair.getPublic();
	}
	public int extractSenderId(String msg){
		String id = msg.substring(msg.indexOf("@")+1, msg.indexOf("[")-1);
		if(util.isValidInteger(id)){
			return Integer.parseInt(id);
		}
		else return 0;
	}

	// pour afficher les messages recus
	public void ecrire(String msg) {
		try {
			if (util.isValidJSONObject(msg)) {
				if(SecureSinglePackage.verifyIntegrity(msg)){
					String dec_msg = MessageEncryption.decryptMessage(msg, this.keyPair.getPrivate());
					int id = extractSenderId(dec_msg);
					if(id>0){
						if(SecureSinglePackage.verifySignature(msg,forum.recupererPK(id))){
							screen.setForeground(new Color(85,132,224));
							screen.append(dec_msg);
						}
						else {
							screen.setForeground(new Color(133,20,20));
							screen.append("[Utilisateur Non Vérifié]"+dec_msg.substring(dec_msg.indexOf("]")+1));
							forum.bloquerUser(id);
						}
					}
				}
				else {
					screen.setForeground(new Color(133,70,20));
					screen.append("Alerte : Le contenu d'un message semble altéré.\n");
				}
			} else screen.append(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}
	public String encrypt(int id, String msg) throws RemoteException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		return MessageEncryption.encryptMessage(msg, forum.recupererPK(id), iv, keyPair.getPrivate());
	}

	public static void main(String args[]) {
		new UserImp();
	}

}
