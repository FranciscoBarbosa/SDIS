package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPeerInterface extends Remote {
	
	void backup(String protocoloV, String filename, int replicationDeg) throws RemoteException;
	
	void delete(String filename) throws RemoteException;
	
	void restore(String filename) throws RemoteException;
	
	void reclaim(long space) throws RemoteException;
	
	String state() throws RemoteException;
}
