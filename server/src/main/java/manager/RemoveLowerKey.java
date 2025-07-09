package manager;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import contract.Vehicle;
import database.DBFunctions;
import database.UserInfo;
import server.Server;
import useful_staff.EngagedId;
import useful_staff.VehicleList;

public class RemoveLowerKey extends Command{
	
	private Integer key;
	private String str;
	private UserInfo uf;
	
	public RemoveLowerKey(Integer key, UserInfo uf) {
		this.key = key;
		this.uf = uf;
	}

	@Autowired
	private DBFunctions dbFunc;
	
	@Override
	public void run() {
		boolean flag = true;
		ArrayList<Integer> AvaIDs = dbFunc.get_available_els(uf.getConn(), uf.getUID());
		for (Integer k : VehicleList.vehList.keySet()) {
			int id = (int) VehicleList.vehList.get(k).getId();
			if (AvaIDs.contains(id) && k < key) {
				dbFunc.delete_row_by_id(uf.getConn(), id);
				EngagedId.removeId(id);
				VehicleList.vehList.remove(key);
				flag = false;
				str = "You have some elements removed";
			}
		}
		if (flag) {
			str = "There is no elements removed";
		}
	}
	public String getString() {
		return str;
	}

}
