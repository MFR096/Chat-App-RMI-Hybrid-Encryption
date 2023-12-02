package Service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class ForumImp extends UnicastRemoteObject implements Forum{
	private static final long serialVersionUID = 1L;
	
	private int n=0;//pour générer les ids d'utilisateurs
	HashMap<Integer,Proxy> users = new HashMap<Integer,Proxy>();
	HashMap<Integer, PublicKey> listPKs = new HashMap<Integer,PublicKey>();

	protected ForumImp() throws RemoteException {
		super();
	}
	
	@Override
	public int entrer(Proxy pr, PublicKey pk) throws RemoteException {
		n++;
		users.put(n, pr);
		listPKs.put(n, pk);
		pr.ecouter("Forum: Vous êtes connecté(e)(s) avec ID: @"+n+", bienvenue!\n");
		pr.ecouter("Forum: Avant d'envoyer un message, vous devez effectuer une double-click sur la liste des intervenants pour la mise à jour.\n");
		pr.ecouter("Forum: Vous pouvez envoyer un message à un seul utilisateur ou bien plusieurs(SHIFT/CTRL+Click).\n");
		return n;
	}

	@Override
	public void dire(int id, String msg) throws RemoteException {
		users.get(id).ecouter(msg);
	}

	@Override
	public String qui() throws RemoteException {
		String list = new String();
		for(Map.Entry<Integer, Proxy> user: users.entrySet()) {
			list+="@"+user.getKey()+"\n";
		}
		return list;
	}
	
	@Override
	public void quitter(int id) throws RemoteException {
		users.get(id).ecouter("Forum: Vous êtes déconnecté ...\n");
		users.remove(id);
		listPKs.remove(id);
	}

	@Override
	public PublicKey recupererPK(int id) throws RemoteException {
		return listPKs.get(id);
	}

	@Override
	public boolean bloquerUser(int id) throws RemoteException {
		Proxy blocked = users.get(id);
		blocked.ecouter("Forum: vous etes bloqués!!!\n");
		quitter(id);
		for(Proxy pr: users.values()){
			if(pr!=blocked) pr.ecouter("Forum: User avec ID @"+id+" est bloqué!\n");
		}
        return users.remove(id) != null;
    }

}
