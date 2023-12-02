package Service;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ProxyImp extends UnicastRemoteObject implements Serializable, Proxy{

	private static final long serialVersionUID = 1L;
	User user;
	
	protected ProxyImp() throws RemoteException {
		super();
	}
	
	public ProxyImp(User user) throws RemoteException {
		this.user = user;
	}
	
	public void ecouter(String msg) throws RemoteException {
		user.ecrire(msg);
	}

}
