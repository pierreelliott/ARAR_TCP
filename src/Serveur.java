
import java.io.*;
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
        String msg = read(in);
        System.out.println(msg);
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
            File resDemandee = new File(resURL);
            if(!resDemandee.exists()) {
                throw new Exception("Aaaaaaaah !");
            }
            Reader file = new InputStreamReader(
                            new FileInputStream(resDemandee));
            String fileContent = read(file);
            if(hasFinished()) {
                return;
            }

            System.out.println("File :");
            System.out.println(fileContent);

            respond(200, fileContent);
        } catch (Exception ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
            respond(404, request);
        }
    }
    
    public void respondToPUT(String request) {
        String resURL = HTTPProtocol.getResURL(request);
        try {
            File resDemandee = new File(resURL);
            if(resDemandee.exists()) {
                respond(403, request);
                return;
            }
            String contenu = HTTPProtocol.getContent(request);
            BufferedWriter file = new BufferedWriter(new FileWriter(new File("put_" + resURL)));
            file.write(contenu);
            file.flush();

            System.out.println("File :");
            System.out.println(contenu);

            respond(200, contenu);
        } catch (Exception ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
            respond(404, request);
        }
    }
    
    public void respondError(String request) {
        respond(404, request);
    }
    
}
