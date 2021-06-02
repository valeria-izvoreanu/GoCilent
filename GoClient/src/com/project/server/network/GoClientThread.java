package com.project.server.network;

import com.project.server.exceptions.InvalidMoveException;
import com.project.server.game.Game;
import com.project.utils.Control;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GoClientThread extends Thread {
    private final Socket socket;
    public Game game;
    private BufferedReader in;
    private PrintWriter out;
    String name;
    private boolean mainPlayer;
    Control control;

    String colour;

    public GoClientThread(Socket socket, Game game, Control control) {
        this.socket = socket;
        this.game = game;
        this.control = control;
    }

    public void run() {
        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            init();
            playGame();

        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket" + e);
            }
        }
    }

    private void init() throws IOException {
        String response = in.readLine();
        mainPlayer = response.equals("true");
        name = in.readLine();

        if (mainPlayer) {
            game.setPlayer1(name);
            while (!control.available) {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    System.out.println("Error waiting.." + e);
                }
            }
            out.println(game.getPlayer2());
            out.flush();
            game.setBoardSize(Integer.parseInt(in.readLine()));
            game.setPlayer1Color(in.readLine());
            if (game.getPlayer1Color().equals("white")) {
                game.setPlayer2Color("black");
            } else {
                game.setPlayer2Color("white");
            }
        } else {
            game.setPlayer2(name);
            control.available = true;
            while (game.getPlayer2Color() == null) {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    System.out.println("Error waiting " + e);
                }
            }
            out.println(game.getPlayer1());
            out.println(game.getBoardSize());
            out.println(game.getPlayer2Color());
            out.flush();
        }
    }

    private void playGame() throws IOException {
        int x, y;
        if (mainPlayer) {
            colour = game.getPlayer1Color();
        } else {
            colour = game.getPlayer2Color();
        }

        if (colour.equals("black")) {
            in.readLine();
            x = Integer.parseInt(in.readLine());
            y = Integer.parseInt(in.readLine());
            move(x, y, "OK");
        } else {
            getMove();
        }
        while (game.isOver < 2) {
            if (colour.equals("white")) {
                makeMove();
                try {
                    sleep(2000);
                } catch (Exception e) {
                    System.out.println("Error waiting.." + e);
                }
                getMove();
            } else {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    System.out.println("Error waiting..." + e);
                }
                getMove();
                makeMove();
            }
        }
    }

    private void move(int x, int y, String okToMove) throws IOException {
        try {
            game.getGameLogic().placePiece(x, y, okToMove);
            if (okToMove.equals("OK")) {
                out.println("OK");
                out.println(game.getCurrentX());
                out.println(game.getCurrentY());
                getChain();
            }
        } catch (InvalidMoveException e) {
            out.println("OK");
            out.println("err");
            out.println("err");
            makeMove();
        }
        out.flush();
    }

    private void getMove() {
        game.getGameLogic().getPermissionToMove();
        if (!game.isPlaying) {
            out.println("OK");
            out.println(game.getCurrentX());
            out.println(game.getCurrentY());
            getChain();
        } else {
            out.println("YES");
            getWinner();
        }
        out.flush();

    }

    private void getChain() {
        List<Pair<Integer, Integer>> pieceChain = game.capturedChain;
        int check = game.getCheck();
        if (check == 2) {
            game.setCheck(0);
            game.capturedChain = new ArrayList<>();
        } else {
            game.setCheck(check + 1);
        }
        if (pieceChain.size() > 0) {
            out.println(pieceChain.size());
            for (Pair<Integer, Integer> piece : pieceChain) {
                out.println(piece.getKey());
                out.println(piece.getValue());
            }
        } else {
            out.println(0);
        }
    }

    private void makeMove() throws IOException {
        String okToMove = in.readLine();
        if (okToMove.equals("OK")) {
            int x = Integer.parseInt(in.readLine());
            int y = Integer.parseInt(in.readLine());
            move(x, y, okToMove);
        } else if (okToMove.equals("PASS")) {
            move(0, 0, okToMove);
            out.println("YES");
            getWinner();
            out.flush();
        }
    }

    private void getWinner() {
        int winner = game.getGameLogic().determineWinner();
        if (winner == 0 || winner == 2) {//black has penalty because they moved first
            if (mainPlayer && (colour.equals("white"))) {
                out.println(game.getPlayer1());
            } else {
                out.println(game.getPlayer2());
            }
            int score = game.getPlayerWhiteScore();

            out.println(score);
        } else {
            if (mainPlayer && (colour.equals("black"))) {
                out.println(game.getPlayer1());
            } else {
                out.println(game.getPlayer2());
            }
            int score = game.getPlayerBlackScore();
            out.println(score);
        }
    }
}
