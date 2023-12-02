package Service;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerDriver {
	public static void main(String[] args) throws RemoteException, MalformedURLException{
		LocateRegistry.createRegistry(1099);
		Forum od = (Forum)new ForumImp();
		Naming.rebind("rmi://localhost:1099/ch", od);
	}
}
