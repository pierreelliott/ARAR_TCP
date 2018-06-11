/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.Arrays;

/**
 *
 * @author belette
 */
public class HTTPProtocol {

    private String url;
    private String method;
    private String version;
    private byte[] content;
    private boolean isResponse = false;

    public HTTPProtocol(byte[] data) {
        try {
            String msg = new String(data, "UTF-8");

            if(msg.startsWith("HTTP/")) { // Si c'est une réponse
                version = msg.split(" ")[0];
                isResponse = true;

            } else { // Sinon c'est une requête
                String[] m = msg.split(" ");
                switch(m[0]) {
                    case "GET": case "PUT": case "HEAD": case "POST": case "DELETE": case "OPTIONS": case "CONNECT":
                        method = m[0]; break;
                    default: throw new Exception("Erreur, requête invalide");
                }
                url = m[1];
                version = m[2].split("\r\n")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== STATIC =======================================

    public static String setResource(String url, String data) {
        String msg;
        msg = "PUT " + url + " HTTP/1.1\r\n";
        msg += "\r\n";
        msg += data;
        return msg;
    }
    
    public static String getResource(String url) {
        String msg;
        msg = "GET " + url + " HTTP/1.1\r\n";
        msg += "\r\n";
        return msg;
    }
    
    public static String getResponse(int code, String data) {
        String msg = "HTTP/1.1 ";
        switch(code) {
            case 200: msg += getResponse200(data); break;
            case 201: msg += getResponse201(data); break;
            case 403: msg += getResponse403(data); break;
            case 404:
            default: msg += getResponse404(data); break;
        }
        msg += "\r\n";
        return msg;
    }
    
    private static String getResponse200(String data) {
        String msg = "200 OK\r\n";
        msg += "\r\n" + data;
        return msg;
    }
    
    private static String getResponse201(String data) {
        String msg = "201 Created\r\n";
        return msg;
    }

    private static String getResponse403(String data) {
        String msg = "403 No Permissions\r\n";
        msg += "\r\nNot Allowed !";
        return msg;
    }
    
    private static String getResponse404(String data) {
        String msg = "404 Not Found\r\n";
        msg += "\r\nNot Found !";
        return msg;
    }
    
    public static boolean isGET(String data) { return data.split(" ")[0].equals("GET"); }
    public static boolean isPUT(String data) { return data.split(" ")[0].equals("PUT"); }
    
    public static String getResURL(String request) {
        if(isGET(request)) {
            String res = request.split(" ")[1];
            if(res.endsWith("/")) {
                res += "index.html";
            }
            return res;
        } else {
            return "index.html";
        }
    }

    public static String getContent(String request) {
        String[] res = request.split("\r\n\r\n");
        return request.substring(res[0].length()+4);
    }

    public static void main(String[] args) throws Exception {
        String filePath = "fb.png";
        File resDemandee = new File(filePath);
        Reader file = new InputStreamReader(
                    new FileInputStream(resDemandee));
        byte[] fileContent = read(file);
        System.out.println("======================");
        FileOutputStream fileW = new FileOutputStream(new File("aha_" + filePath));
        for(int i = 0; i < fileContent.length; i++) {
            fileW.write(fileContent[i]);
            System.out.println(fileContent[i]);
        }
        fileW.flush();
        System.out.println("Finished !");

        System.out.println("======================");
        FileOutputStream f = new FileOutputStream(new File("aha_test"));
        for(int i = -128; i < 128; i++) {
            f.write((byte)i);
            System.out.println("char : " + ((byte) i));
        }
    }

    public static byte[] read(Reader reader) throws Exception {
        byte[] builder = { };
        int length;
        byte[] buffer = new byte[512];
        int b = reader.read();
        Integer c;
        int i = 0;
        while(b != -1) {
            if(i == 2) break;
//            if(i == 512) {
//                length = builder.length;
//                builder = Arrays.copyOf(builder, length + buffer.length);
//                for(int j = 0; j < buffer.length; j++) {
//                    builder[length+j] = buffer[j];
//                }
//                i = 0;
//            }

            System.out.println("int :" + b);
            buffer[i] = (byte)(b&0x000000FF);
            System.out.println(buffer[i]);
            i++;
            b = reader.read();
        }

        if(builder.length == 0) {
            length = builder.length;
            builder = Arrays.copyOf(builder, length + i);
            for(int j = 0; j < i; j++) {
                builder[length+j] = buffer[j];
            }
        }

        return builder;
    }
}
