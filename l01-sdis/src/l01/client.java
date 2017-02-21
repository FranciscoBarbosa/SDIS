package l01;

import java.io.*;
import java.net.*;
import java.util.*;

class client{

public static void main(String[] args) throws IOException{
	String oper,owner_name,plate_number,line;
	Info c1;
	byte[] sbuf,rbuf=new byte[1024];
		
	BufferedReader operation = new BufferedReader(new InputStreamReader(System.in));
	
	System.out.println("what operation you want? (registed/lookup)");

	while((oper=operation.readLine()).equals(null)){
		Scanner scanner=new Scanner(oper);			
	}

	
	System.out.println("what is your plate number?");	
	BufferedReader number = new BufferedReader(new InputStreamReader(System.in));
	while((plate_number=number.readLine()).equals(null)){
		Scanner scanner1=new Scanner(plate_number);
	}

	c1=new Info(plate_number);

	
	if(oper.equals("register")){
		System.out.println("what is your name?");	
		BufferedReader name = new BufferedReader(new InputStreamReader(System.in));
		while((owner_name=name.readLine()).equals(null)){
			Scanner scanner2=new Scanner(owner_name);
		}
		c1= new Info(plate_number,owner_name);
	}
	
		
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	ObjectOutputStream os = new ObjectOutputStream(outputStream);
	os.writeObject(c1);
	sbuf = outputStream.toByteArray();
	
	InetAddress address=InetAddress.getByName("localhost");
	
	DatagramSocket socket=new DatagramSocket();
	DatagramPacket packet=new DatagramPacket(sbuf,sbuf.length,address,4445);
	
	socket.send(packet);
	
	DatagramPacket Rpacket = new DatagramPacket(rbuf,rbuf.length);
	socket.receive(Rpacket);
	
	String received = new String(Rpacket.getData());
	System.out.println(received.toString());
	
	}
}