package useful_staff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerInfo {
	
	private final String SERVER_HOST;
    private final int SERVER_PORT;
    
    public ServerInfo(
    		@Value("${serverInfo.SERVER_HOST}") String SERVER_HOST,
    		@Value("${serverInfo.SERVER_PORT}") int SERVER_PORT
    		) {
    	this.SERVER_HOST = SERVER_HOST;
    	this.SERVER_PORT = SERVER_PORT;
    }
    
    public String getSERVER_HOST() {
		return SERVER_HOST;
	}    
	public int getSERVER_PORT() {
		return SERVER_PORT;
	}    
    
}
