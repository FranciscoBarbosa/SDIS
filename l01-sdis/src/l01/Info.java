package l01;

import java.io.Serializable;

public class Info implements Serializable{
	String oper, plate_number, owner_name;
	
	public 
	Info(String plate_number,String owner_name){
		this.oper="register";
		this.plate_number=plate_number;
		this.owner_name=owner_name;
	}
	
	Info(String plate_number){
		this.oper="lookup";
		this.plate_number=plate_number;
	}
	
	String getOper(){
		return oper;
	}
	
	String getPlate(){
		return plate_number;
	}
	
	String getName(){
		return owner_name;
	}
}
