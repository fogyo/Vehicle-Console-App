package useful_staff;

import org.springframework.stereotype.Component;

@Component
public class DataBaseInfo {
	
	private String dbName = "laba7";
	private String dbLogin = "postgres";
	private String dbPass = "1234";
	
	public DataBaseInfo() {
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
