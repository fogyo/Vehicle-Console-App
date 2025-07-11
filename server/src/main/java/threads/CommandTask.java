package threads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import java.nio.channels.SocketChannel;

import java.util.concurrent.RecursiveTask;

import contract.Request;
import contract.Response;
import database.UserInfo;
import manager.CommandManager;
import server.LoggerConfiguration;

public class CommandTask extends RecursiveTask<String>{
	
	private UserInfo uf;
	private int usersNum;
	private SocketChannel socketChannel;
	private Request request;
	private final LoggerConfiguration logs = new LoggerConfiguration(CommandTask.class);
	
	public CommandTask(SocketChannel socketChannel, UserInfo uf, int usersNum, Request request) {
		this.uf = uf;
		this.usersNum = usersNum;
		this.socketChannel = socketChannel;
		this.request = request;
	}
	
	@Override
	protected String compute() {
		
        CommandManager cm = new CommandManager(request.getCom(), request.getArgNum(), request.getArgs(), request.getVehicle(), uf);
        cm.startCommand();
        String reply = cm.getCMS();
        boolean flagClient = true;
        if (request.getCom().equals("exit")){
          flagClient = false;
          
        }
        Response response = new Response(reply, flagClient);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
        try {
        	ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(response);
	        ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
	        socketChannel.write(response_buffer);
		} catch (IOException e) {
		}
        
        if (!flagClient) {
          logs.writeInfo("User disconnected: exit command");
          usersNum = usersNum - 1;
            try {
                socketChannel.close();
            } catch (IOException ex) {
              logs.writeError(ex.toString());
              throw new RuntimeException(ex);
        	}
        }
	        
        return "Added to thread";
    }
}
