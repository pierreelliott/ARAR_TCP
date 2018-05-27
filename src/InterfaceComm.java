
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author belette
 */
public class InterfaceComm implements Runnable {
    
    private Socket socket;
    private boolean finished = false;
    private DataOutputStream out;
    BufferedReader in;
    
    public InterfaceComm(Socket sock) throws IOException {
        socket = sock;
        out = new DataOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while(!finished) {
            try {
                process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void process() {
        String msg = read();
    }
    
    public String read() {
        try {
            String msg = in.readLine();
            return msg;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    
    public void send(String text) {
        try {
            out.writeChars(text);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(InterfaceComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getResource(String url) {
        String text = HTTPProtocol.getResource(url);
        send(text);
    }
    
    public void respond(int code, String data) {
        String text = HTTPProtocol.getResponse(code, data);
        send(text);
    }
    
}
