package com.project.client;

import com.project.client.gui.newgame.NewGameFrame;
import com.project.utils.Control;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class GoClient {
    public GoClient(boolean mainPlayer) {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8100; // The server's port
        Control control = new Control();
        try (
                Socket socket = new Socket(serverAddress, PORT);//connect to server
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {

            new NewGameFrame(out, in, mainPlayer).setVisible(true);//create window frame
            while (true) {
                if (control.available) break;
            }
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (IOException e) {
            System.err.println("Error reading or writing... " + e);
        }
    }

    public static void setLocationToTopRight(JFrame frame) {//sets frame on the right side of the screen
        GraphicsConfiguration config = frame.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int x = bounds.x + bounds.width - insets.right - frame.getWidth();
        int y = bounds.y + insets.top;
        frame.setLocation(x, y);
    }
}

