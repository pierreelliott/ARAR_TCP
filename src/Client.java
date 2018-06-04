
import java.io.*;
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
        super(new Socket(InetAddress.getByName("134.214.117.160"), 2000));
    }
    
    @Override
    public void process() {
        String res = "test.png";
        getResource(res);
        String response = read(in);
        String contenu = HTTPProtocol.getContent(response);
        System.out.println("Reponse\n" + response + "\nFin");
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(new File("client_" + res)));
            file.write(contenu);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("===========================================");

        res = "putFile.html";
        try {
            setResource(res, res);
            response = read(in);
            System.out.println("Reponse\n" + response + "\nFin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFinished();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        (new Thread(new Client())).start();
    }
}
