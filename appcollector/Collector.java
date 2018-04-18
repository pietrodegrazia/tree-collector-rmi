package appcollector;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Collector extends Remote {
	public List<Integer> collect() throws RemoteException;
	public int registerChild(Collector child) throws RemoteException;
	public int id() throws RemoteException;
}