package threads;

import org.springframework.stereotype.Component;

@Component
public class CommandProxy {
	
	public void CommandToPool (CommandTask ct) {
		ct.fork();
	}
	
	
}
