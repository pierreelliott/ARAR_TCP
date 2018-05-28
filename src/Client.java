
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
    
    public Client() throws Exception {
        super(new Socket(InetAddress.getByName("127.0.0.1"), 2000));
    }
    
    @Override
    public void process() {
        String res = "index2.html";
        getResource(res);
        String response = read();
//        System.out.println("Reponse : \n" + response + "\nFin");
        String contenu = HTTPProtocol.getContent(response);
        System.out.println("Reponse\n" + contenu + "\nFin");
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(new File("reponse_" + res)));
            file.write(contenu);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFinished();
    }
    
    public static void main(String[] args) throws Exception {
        (new Thread(new Client())).start();
    }
}
