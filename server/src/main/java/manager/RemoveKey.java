package manager;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import database.DBFunctions;
import database.UserInfo;
import useful_staff.EngagedId;
import useful_staff.VehicleList;

public class RemoveKey extends Command{
	
	private Integer key;
	private UserInfo uf;
	
	private String str = "";

	public RemoveKey(Integer key, UserInfo uf) {
		this.key = key;
		this.uf = uf;
	}
	
	@Autowired
	private DBFunctions dbFunc;
	
	@Override
	public void run() {
		ArrayList<Integer> AvaIDs = dbFunc.get_available_els(uf.getConn(), uf.getUID());
		int id = (int) VehicleList.vehList.get(key).getId();
		if (AvaIDs.contains(id)) {
			dbFunc.delete_row_by_id(uf.getConn(), id);
			EngagedId.removeId(id);
			VehicleList.vehList.remove(key);
			str = "Removed successfully";
		}
		else {
			str = "You have no ability to remove element by this key";
		}
	}
	
	public String getString() {
		return str;
	}

}
