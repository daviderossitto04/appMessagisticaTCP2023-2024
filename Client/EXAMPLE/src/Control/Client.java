package Control;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
public class Client {
    private int portServer = 20000;
    private String ipServer = "localhost";
    
    public Socket connect() throws UnknownHostException, IOException {	
    	Socket socket = new Socket(ipServer, portServer);
    	return socket;
    }
}
