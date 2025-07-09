package threads;


public class CommandProxy {
	
	public void CommandToPool (CommandTask ct) {
		ct.fork();
	}
	
	
}
