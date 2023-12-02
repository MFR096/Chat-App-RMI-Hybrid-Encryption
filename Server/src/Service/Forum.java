package Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface Forum extends Remote{
	public int entrer(Proxy pr, PublicKey pk) throws RemoteException;
	public void dire(int id, String msg) throws RemoteException;
	public String qui() throws RemoteException;
	public void quitter(int id) throws RemoteException;
	public PublicKey recupererPK(int id) throws RemoteException;
	public boolean bloquerUser(int id) throws RemoteException;
}
