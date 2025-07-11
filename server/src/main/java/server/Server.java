package server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.util.Iterator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import contract.MyBuffer;
import contract.Request;
import contract.UserResponse;
import contract.UserTransfer;
import database.EntryRoom;
import database.UserInfo;
import threads.CommandProxy;
import threads.CommandTask;
import useful_staff.ProgramStarter;

public class Server {

    public static boolean script = false;
	
    public static void main(String[] args) throws IOException {
    	
    	//New code
    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
    	ProgramStarter ps = context.getBean(ProgramStarter.class);
    	Connection conn = ps.getConnect();
    	LoggerConfiguration logs = new LoggerConfiguration(Server.class);
    	final String SERVER_HOST = ps.getsInfo().getSERVER_HOST();
    	final int SERVER_PORT = ps.getsInfo().getSERVER_PORT();   	
    	//End of new code
    	
    	logs.writeInfo("DataBase connected");
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        serverSocketChannel.configureBlocking(false);
        logs.writeInfo("Server started. PORT: " + SERVER_PORT);
        
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        int usersNum = 0;
        SocketChannel socketChannel = null;;
        
		while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {

                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                    	socketChannel = ((ServerSocketChannel) key.channel()).accept();
                        usersNum = usersNum + 1;
                        logs.writeInfo("Connected users: " + String.valueOf(usersNum));
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    else if (key.isReadable()) {            	
                    	socketChannel = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        MyBuffer myBuffer = new MyBuffer();
                        while ((socketChannel.read(buffer) > 0)) {
                            buffer.flip();
                            myBuffer.addBytes(buffer.array());
                            buffer.clear();
                        }
                        
                      //Работа с зареганным пользователем
                        
                        try {
                        	ByteArrayInputStream bis = new ByteArrayInputStream(myBuffer.toByteBuffer().array());
                            ObjectInputStream ois = new ObjectInputStream(bis);
                        	Request request = (Request) ois.readObject();
                            UserInfo uf = new UserInfo(conn, request.getUID());
                            logs.writeInfo("User "+uf.getUID()+" requested "+request.getCom());
                            CommandTask ct = new CommandTask(socketChannel, uf, usersNum, request);
                            logs.writeInfo("User "+uf.getUID()+" created command task ");
                            CommandProxy cp = context.getBean(CommandProxy.class);
                            cp.CommandToPool(ct);
                            logs.writeInfo("User "+uf.getUID()+" forked in pool ");
                        }
                        
                      //Регистрация пользователя                      
                        
                        catch (Exception e) {
                        	logs.writeError(e.toString());
                        	ByteArrayInputStream bis = new ByteArrayInputStream(myBuffer.toByteBuffer().array());
                            ObjectInputStream ois = new ObjectInputStream(bis);
                        	UserTransfer ut;
							try {
								ut = (UserTransfer) ois.readObject();
								String username = ut.getUsername();
	                            String password = ut.getPassword();
	                            boolean signing = ut.getSigning();
	                            EntryRoom er = context.getBean(EntryRoom.class);
	                            UserResponse usresp = er.registry(username, password, signing, conn);
	                            logs.writeInfo("User registried "+usresp.getSuccess());
	                            
	                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                            ObjectOutputStream oos = new ObjectOutputStream(bos);
	                            oos.writeObject(usresp);
	                            ByteBuffer response_buffer = ByteBuffer.wrap(bos.toByteArray());
	                            socketChannel.write(response_buffer);
							} catch (ClassNotFoundException e1) {
							}                           
                        }   
                    }
                }
                } catch (IOException e) {
            	try {
					socketChannel.close();
		        	logs.writeError("User disconnected: " + e.toString());
		        	usersNum = usersNum - 1;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					logs.writeError(e1.toString());
				}
            }

        }

    }
}