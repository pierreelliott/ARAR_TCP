
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author belette
 */
public class Client extends InterfaceComm {
    
    private Socket socket;
    
    public Client() throws IOException {
        super(new Socket());
    }
    
    @Override
    public void process() {
        getResource("/index.html");
        String response = read();
        System.out.println(response);
    }
}
