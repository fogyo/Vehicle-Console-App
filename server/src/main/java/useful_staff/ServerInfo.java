package useful_staff;

import org.springframework.stereotype.Component;

@Component
public class ServerInfo {
	
	private final String SERVER_HOST = "127.0.0.1";
    private final int SERVER_PORT = 5452;
    
    public String getSERVER_HOST() {
		return SERVER_HOST;
	}    
	public int getSERVER_PORT() {
		return SERVER_PORT;
	}    
    
}
