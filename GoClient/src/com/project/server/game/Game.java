package com.project.server.game;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private volatile String player1;
    private volatile String player2;
    private volatile int boardSize;
    private volatile String player1Color;
    private volatile String player2Color;
    private volatile GameLogic gameLogic;
    public volatile boolean transfer = true;
    public volatile Integer isOver = 0;
    private volatile Integer currentX;
    private volatile Integer currentY;
    private volatile int playerBlackScore;
    private volatile int playerWhiteScore;
    public volatile List<Pair<Integer, Integer>> capturedChain = new ArrayList<>();
    public volatile int check = 0;//check if all players took the captured chain
    public volatile boolean isPlaying = false;


    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }


    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        gameLogic = new GameLogic(boardSize, this);
    }

    public String getPlayer1Color() {
        return player1Color;
    }

    public void setPlayer1Color(String player1Color) {
        this.player1Color = player1Color;
    }

    public String getPlayer2Color() {
        return player2Color;
    }

    public void setPlayer2Color(String player2Color) {
        this.player2Color = player2Color;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public Integer getPlayerBlackScore() {
        return playerBlackScore;
    }

    public void setPlayerBlackScore(Integer playerBlackScore) {
        this.playerBlackScore = playerBlackScore;
    }

    public Integer getPlayerWhiteScore() {
        return playerWhiteScore;
    }

    public void setPlayerWhiteScore(Integer playerWhiteScore) {
        this.playerWhiteScore = playerWhiteScore;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
