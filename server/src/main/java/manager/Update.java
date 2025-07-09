package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import contract.Vehicle;
import contract.VehicleConverter;
import database.DBFunctions;
import database.UserInfo;
import server.Server;
import useful_staff.EngagedId;
import useful_staff.ProgramStarter;
import useful_staff.VehicleList;

public class Update extends Command{
	
	private Integer id;
	private Vehicle vehicle;
	private UserInfo uf;
	private String str;
	
	public Update(Integer id, Vehicle vehicle, UserInfo uf) {
		this.id = id;
		this.vehicle = vehicle;
		this.uf = uf;
	}

	@Autowired
	private DBFunctions dbFunc;
	
	@Override
	public void run() {
		ArrayList<Integer> AvaIDs = dbFunc.get_available_els(uf.getConn(), uf.getUID());
		if (AvaIDs.contains(id)) {
			dbFunc.delete_row_by_id(uf.getConn(), id);
			EngagedId.removeId(id);
			Integer key = VehicleList.getKeyByID((long) id);
			VehicleList.vehList.remove(key);
			
			vehicle.setId(EngagedId.addId());	
			VehicleList.vehList.put(key, vehicle);					
			dbFunc.add_vehicle(uf.getConn(), key, vehicle, uf.getUID());
			str = "Element updated";
		}
		else {
			str = "You have no ability to update this id";
		}
	}
		
	
	public String getString() {
		return str;
	}

}
