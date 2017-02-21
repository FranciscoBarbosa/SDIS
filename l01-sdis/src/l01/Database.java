package l01;

import java.util.ArrayList;
import java.util.List;

public class Database {
	
	List<Info> clients = new ArrayList<Info>();

	public
	 List<Info> getClients(){
		return clients;
	}
	Database(){
	
		Info c1=new Info("65-HT-75","Francisco");
		Info c2=new Info("00-IA-12","Joao");
		
		clients.add(c1);
		clients.add(c2);
	}
	
	int Register(Info i1){
		clients.add(i1);
		for(int i=0;i<clients.size();i++){
			if(clients.get(i).getPlate().equals(i1.getPlate())){
				return -1;
			}
		}
		return clients.size();
	}
	
	String lookup(Info i1){
		for(int i=0;i<clients.size();i++){
			if(clients.get(i).getPlate().equals(i1.getPlate())){
				return clients.get(i).getName();
			}
		}
		return "not found";
	}
}
