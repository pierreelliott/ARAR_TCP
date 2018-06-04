
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
public class InterfaceComm implements Runnable {
    
    protected Socket socket;
    private boolean finished = false;
    private DataOutputStream out;
    InputStreamReader in;
    
    public InterfaceComm(Socket sock) throws IOException {
        socket = sock;
        out = new DataOutputStream(socket.getOutputStream());
        in = new InputStreamReader(socket.getInputStream());
    }

    @Override
    public void run() {
        while(!finished) {
            try {
                process();
            } catch (Exception e) {
                e.printStackTrace();
                setFinished();
            }
        }
        out = null;
        in = null;
    }

    public void setFinished() { finished = true; }
    public boolean hasFinished() { return finished; }
    
    public void process() {
        String msg = read(in);
    }
    
    public String read(Reader reader) {
        // TODO
        // A modifier pour lire (et plus tard écrire) directement des bytes
        // Ca posera moins de soucis pour savoir comment interpréter les caractères
        try {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[512];
            int nbRead = reader.read(buffer);
            int offset = 0;
            while(nbRead > 0) {
                builder.append(buffer, offset, nbRead);
                if(!in.ready()) {
                    break;
                }
                offset += nbRead;
                nbRead = reader.read(buffer);
            }
            String msg = builder.toString();
            return msg;
        } catch (Exception ex) {
//            ex.printStackTrace();
            setFinished();
        }
        return "";
    }

    /*public ByteArrayOutputStream read(Reader reader, boolean second) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(reader);
    }*/
    
    public void send(String text) {
        try {
            out.write(text.getBytes("UTF-8"));
            out.flush();
        } catch (IOException ex) {
//            ex.printStackTrace();
            setFinished();
        }
    }
    
    public void getResource(String url) {
        String text = HTTPProtocol.getResource(url);
        System.out.println(text);
        send(text);
    }

    public void setResource(String url, String localPath) throws FileNotFoundException {
        String data = read(new InputStreamReader(
                new FileInputStream(new File(localPath))));
        String text = HTTPProtocol.setResource(url, data);
        System.out.println(text);
        send(text);
    }
    
    public void respond(int code, String data) {
        String text = HTTPProtocol.getResponse(code, data);
        send(text);
    }
    
}
