package cli;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import peer.IPeerInterface;
import utilities.Utilities;

public class TestApp {
	
	private static String peerAccessPoint;
	private static String operation;
	private static String fileName;
	private static int spaceSize;
	private static int replicationDeg;
	private static IPeerInterface initiatorPeer;
	
	public static void main(String[] args) {	
		
		if(!validateInput(args)){
			System.out.println("USAGE: java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
			System.out.println("<peer_ap>: Peer's access point");
			System.out.println("<sub_protocol>: BACKUP | RESTORE | DELETE | RECLAIM | STATE");
			System.out.println("<opnd_1>: Path name of file | Amount of space to reclaim (in KByte)");
			System.out.println("<opnd_2>: Integer that specified replication degree (BACKUP only)");
			System.out.println("\nExiting...");
			
			return;
		}
	
		init();
	}
	
	public static boolean validateInput(String[] args){		
		if (args.length < 2 || args.length > 4){
			return false;
		}
		
		
		
		return true;
	}
	

	public static void init(){
		
		try {			
			Registry registry = LocateRegistry.getRegistry("localhost");
			initiatorPeer =  (IPeerInterface) registry.lookup(peerAccessPoint);
			
			switch(operation){
			case "BACKUP":
				initiatorPeer.backup("1.0", fileName, replicationDeg);
				break;
			case "BACKUPENH":
				initiatorPeer.backup("2.0", fileName, replicationDeg);
				break;
			case "RESTORE":
				initiatorPeer.restore(fileName);
				break;
			case "DELETE":
				initiatorPeer.delete(fileName);
				break;
			case "RECLAIM":
				initiatorPeer.reclaim(spaceSize);
				break;
			case "STATE":
				System.out.println(initiatorPeer.state());
				break;			
			}
			
		} catch(NotBoundException | RemoteException e) {
			e.printStackTrace();			
		} 
	}

}
