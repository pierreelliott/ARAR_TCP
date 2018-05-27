
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class Serveur extends InterfaceComm {
    
    public Serveur(Socket sock) throws IOException {
        super(sock);
    }
    
    public void process() {
        String msg = read();
        if(HTTPProtocol.isGET(msg)) {
            respondToGET(msg);
        } else if(HTTPProtocol.isPUT(msg)) {
            respondToPUT(msg);
        } else {
            respondError(msg);
        }
    }
    
    public void respondToGET(String request) {
        String resURL = HTTPProtocol.getResURL(request);
        try {
            BufferedReader file = new BufferedReader(new FileReader(resURL));
            String line, fileContent = "";
            while( (line = file.readLine()) != null ) {
                if(line.isEmpty()) {
                    break;
                } else {
                    fileContent += line;
                }
            }
            respond(200, fileContent);
        } catch (Exception ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
            respond(404, request);
        }
    }
    
    public void respondToPUT(String request) {
        
    }
    
    public void respondError(String request) {
        respond(404, request);
    }
    
}
