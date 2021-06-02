package com.project;

import com.project.client.GoClient;
import com.project.server.GoServer;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8100; // The server's port

        Socket server = serverListening(serverAddress, PORT);//check if any server is available
        //if not create new thread with sever
        if (server == null) {
            Thread serverThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        new GoServer();
                    } catch (IOException e) {
                        System.out.println("Couldn't open server.." + e);
                    }
                }
            });
            serverThread.start();
        }
        Thread clientThread = new Thread(new Runnable() {//create playing client
            public void run() {
                boolean mainPlayer = (server == null);
                new GoClient(mainPlayer);
            }
        });
        clientThread.start();
    }

    public static Socket serverListening(String host, int port) {
        Socket s;
        try {
            s = new Socket(host, port);
            return s;
        } catch (Exception e) {
            return null;
        }
    }
}
