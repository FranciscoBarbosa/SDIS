package l01;

import java.io.*;
import java.net.*;
import java.util.*;

class server{
	
public static void main(String[] args) throws IOException, ClassNotFoundException{
	Database db= new Database();
	String result;
	
	
	byte[] rbuf = new byte[1024];
	byte[] sbuf = new byte[1024];
	DatagramSocket socket=new DatagramSocket(4445);
	
	while(true){
		DatagramPacket packet = new DatagramPacket(rbuf,rbuf.length);
		socket.receive(packet);
		
		byte[] received = packet.getData();
		
		ByteArrayInputStream in = new ByteArrayInputStream(received);
		ObjectInputStream is = new ObjectInputStream(in);
		
	
			Info c1 = (Info) is.readObject();
			
			if(c1.getOper().equals("register")){
				result="<"+ c1.getOper()+">"+"\n"+"<"+ c1.getName()+">"+","+"<"+ c1.getPlate()+">"+"number of vehicles="+db.Register(c1);
			}
			else{
				result="<"+ c1.getOper()+">"+"\n"+"<"+ db.lookup(c1)+">";
			}
			
			
			sbuf = result.getBytes();
			
			InetAddress IPAddress = packet.getAddress();
			int port = packet.getPort();
			
			DatagramPacket Spacket=new DatagramPacket(sbuf,sbuf.length,IPAddress,port);
			
			socket.send(Spacket);
			
	}
}
}