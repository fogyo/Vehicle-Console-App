package useful_staff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class DataBaseInfo {
	
	private String dbName;
	private String dbLogin;
	private String dbPass;
	
	public DataBaseInfo(
			@Value("${dataBaseInfo.dbName}") String dbName,
			@Value("${dataBaseInfo.dbLogin}") String dbLogin,
			@Value("${dataBaseInfo.dbPass}") String dbPass
			) {
		this.dbLogin = dbLogin;
		this.dbName = dbName;
		this.dbPass = dbPass;
		
	}
	
	public String getDbLogin() {
		return dbLogin;
	}
	public String getDbPass() {
		return dbPass;
	}
	public String getDbName() {
		return dbName;
	}

}
