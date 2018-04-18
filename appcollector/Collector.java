package appcollector;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Collector extends Remote {
	public int collect(int parent) throws RemoteException;
	public int registerChild(int child) throws RemoteException;
}