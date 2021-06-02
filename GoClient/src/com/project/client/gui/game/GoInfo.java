package com.project.client.gui.game;

import com.project.client.gui.score.ScoreFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GoInfo extends JPanel {
    final GoGame frame;
    PlayerInfo player1;
    PlayerInfo player2;
    JButton submitBtn = new JButton("Submit Move");
    JButton passBtn = new JButton("Resign");
    boolean mainPlayer;
    String playerName;
    String opponentName;
    String colour;
    PrintWriter out;
    BufferedReader in;

    public GoInfo(GoGame frame, PrintWriter out, BufferedReader in, String playerName1, String playerName2, String colour, boolean mainPlayer) {
        this.frame = frame;
        this.out = out;
        this.in = in;
        this.mainPlayer = mainPlayer;
        this.playerName = playerName1;
        this.opponentName = playerName2;
        this.colour = colour;
        init();
    }

    private void init() {
        setPreferredSize(new Dimension(300, 900));
        setLayout(null);
        if (mainPlayer) {
            player1 = new PlayerInfo(playerName, colour, 1);
            player2 = new PlayerInfo(opponentName, getOpponentColour(colour), 2);
        } else {
            player2 = new PlayerInfo(playerName, colour, 2);
            player1 = new PlayerInfo(opponentName, getOpponentColour(colour), 1);
        }
        player1.setLayout(new BoxLayout(player1, BoxLayout.PAGE_AXIS));
        player2.setLayout(new BoxLayout(player2, BoxLayout.PAGE_AXIS));

        int tempH = 0;
        Dimension size = player1.getPreferredSize();
        player1.setBounds(5, 5, size.width, size.height);

        tempH += size.height;

        size = player2.getPreferredSize();
        player2.setBounds(5, tempH + 10, size.width, size.height);

        add(player1);
        add(player2);

        passBtn.setFont(new Font("Courier", Font.PLAIN, 25));
        passBtn.setBackground(Color.blue);
        passBtn.setForeground(Color.white);

        tempH += size.height;

        size = passBtn.getPreferredSize();
        passBtn.setBounds(100, 850, size.width, size.height);

        add(passBtn);
        passBtn.addActionListener(this::pass);

        //should be grey when move not made
        submitBtn.setFont(new Font("Courier", Font.PLAIN, 25));
        submitBtn.setBackground(Color.green);

        tempH += size.height;

        size = submitBtn.getPreferredSize();
        submitBtn.setBounds(60, tempH + 10, size.width, size.height);

        add(submitBtn);
        submitBtn.addActionListener(this::submitMove);
        submitBtn.setEnabled(false);
        passBtn.setEnabled(false);

    }

    private void submitMove(ActionEvent e) {
        out.println("OK");
        out.println(frame.board.currentPieceX);
        out.println(frame.board.currentPieceY);
        out.flush();
        int x;
        int y;
        try {
            String answer = in.readLine();
            if (answer.equals("OK")) {
                String readLineX = in.readLine();
                String readLineY = in.readLine();
                if (readLineX.equals("err")) {
                    errorWindow();
                } else {
                    x = Integer.parseInt(readLineX);
                    y = Integer.parseInt(readLineY);
                    okMove(x, y);
                    frame.board.readPiece();
                }
            } else {
                endGame(answer);
            }
        } catch (IOException err) {
            System.out.println("Couldn't get move" + err);
        }
    }

    private void pass(ActionEvent e) {
        out.println("PASS");
        out.flush();
        try {
            String answer = in.readLine();
            endGame(answer);
        } catch (IOException err) {
            System.out.println("Couldn't read answer from server: " + err);
        }
        passBtn.setEnabled(false);
        submitBtn.setEnabled(false);
        frame.board.currentPlayer = false;
    }


    private String getOpponentColour(String colour) {
        if (colour.equals("white")) {
            return "black";
        } else {
            return "white";
        }
    }

    private void errorWindow() {
        JOptionPane.showMessageDialog(frame,
                "Invalid move",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        //TODO code in case of error
    }

    private void okMove(int x, int y) {
        frame.board.currentPieceX = x;
        frame.board.currentPieceY = y;
        submitBtn.setEnabled(false);
        passBtn.setEnabled(false);
        frame.board.readPiece();
        frame.board.currentPlayer = false;
    }

    public void endGame(String answer) throws IOException {
        if (answer.equals("YES")) {
            String winner = in.readLine();
            new ScoreFrame(winner).setVisible(true);
            this.frame.dispose();
        }
    }
}
