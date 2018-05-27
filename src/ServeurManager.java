
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
public class ServeurManager implements Runnable {
    
    public ServeurManager() {
        
    }

    @Override
    public void run() {
        while(true) {
            try{
                ServerSocket srvSock = new ServerSocket(2000);
                Socket accept = srvSock.accept();
                Serveur comm = new Serveur(accept);
                (new Thread(comm)).start();


            }catch(Exception ex){
                System.err.println("Exception in Server : " + ex);
            }
        }
    }
    
    public static void main(String[] args) {
        (new Thread(new ServeurManager())).start();
    }
    
}
