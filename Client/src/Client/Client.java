package Client;

import Service.Forum;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException{
		Forum stub = (Forum) Naming.lookup("rmi://localhost:1099/ch");
	}
}
