package com.project.server;

import com.project.server.game.Game;
import com.project.server.network.GoClientThread;
import com.project.utils.Control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GoServer {
    // Define the port on which the server is listening
    public static final int PORT = 8100;
    public Game game = new Game();
    Control control = new Control();

    public GoServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            int i = 0;
            //wait for the two players to connect
            while (i < 2) {
                Socket socket = serverSocket.accept();
                // Execute the client's request in a new thread
                new GoClientThread(socket, game, control).start();
            }

        } catch (IOException e) {
            System.err.println("Something went wrong: " + e);
        } finally {
            System.out.println("Server stopped");
            serverSocket.close();
        }
    }
}
