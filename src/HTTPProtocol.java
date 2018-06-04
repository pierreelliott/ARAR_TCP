/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author belette
 */
public class HTTPProtocol {

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
}
