package useful_staff;

import java.sql.Connection;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import contract.Vehicle;
import contract.VehicleConverter;
import database.DBFunctions;

@Component
public class ProgramStarter {
	
	private Connection connect;
	private ServerInfo sInfo;
	
	@Autowired	
	public ProgramStarter(DataBaseInfo dbInfo, DBFunctions dbFunc, ServerInfo sInfo){
		this.sInfo = sInfo;
		Connection conn = dbFunc.connection_to_db(dbInfo.getDbName(), dbInfo.getDbLogin(), dbInfo.getDbPass());
		connect = conn;
		VehicleConverter vc = new VehicleConverter();
		ArrayList<Integer> IDs = dbFunc.read_coll_data(conn);
		
		for (Integer i : IDs) {
			String key_veh = dbFunc.read_vehicle(conn, i);
			Integer key = Integer.parseInt(key_veh.split("\\*")[0]);
			String veh_String = key_veh.split("\\*")[1];
			Vehicle vehicle = vc.StrToVeh(veh_String);
			VehicleList.vehList.put(key, vehicle);
			EngagedId.busiedId(i);
		}
	}
	
	public Connection getConnect() {
		return connect;
	}	
	
	public ServerInfo getsInfo() {
		return sInfo;
	}	
		
}
	
	
