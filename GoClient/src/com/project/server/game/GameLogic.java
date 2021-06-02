package com.project.server.game;

import com.project.server.exceptions.InvalidMoveException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class GameLogic {
    private int[][] goBoard;
    // the current player who is playing and who is his opposition
    private int currentPlayer;
    private int opponent;
    //a chain of pieces
    private List<Pair<Integer, Integer>> pieceChain;
    private boolean chainHasLiberty;
    private boolean captured;
    private int size;
    final private int EMPTY_SPACE = 0;
    final private int BLACK = 1;
    final private int WHITE = 2;
    private Game game;

    public GameLogic(int size, Game game) {
        this.size = size;
        this.goBoard = new int[size][size];
        this.game = game;

        this.currentPlayer = BLACK;
        this.opponent = WHITE;
        game.setPlayerWhiteScore(2);
        game.setPlayerBlackScore(2);
    }

    //place piece in a synchronized way
    public synchronized void placePiece(int x, int y, String type) throws InvalidMoveException {
        while (!game.transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (type.equals("OK")) {
            if (!isValidMove(x, y)) {
                game.setCurrentX(-1);
                game.setCurrentY(-1);
                throw new InvalidMoveException(x, y);
            }
            goBoard[x][y] = currentPlayer;
            captured = false;
            checkOpponentCaptures(x, y);
            System.out.println();
            swapPlayers();
            game.setCurrentX(x);
            game.setCurrentY(y);
            //game.isPlaying = false;
            System.out.println("place");
        } else {
            game.isOver++;
            game.isPlaying = true;
        }
        game.transfer = false;
        notifyAll();

    }

    //wait for opponent to place piece
    public synchronized void getPermissionToMove() {
        while (game.transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("end");
        game.transfer = true;
        notifyAll();
    }

    //method to check that move is valid
    private boolean isValidMove(int x, int y) {
        //if the place is occupied, move is invalid
        if (goBoard[x][y] != EMPTY_SPACE)
            return false;
        if (countLiberties(x, y) == 0) {
            captured = false;
            goBoard[x][y] = this.currentPlayer;
            checkOpponentCaptures(x, y);//check if piece captures a piece
            goBoard[x][y] = EMPTY_SPACE;
            //if yes move is valid
            if (captured)
                return true;

            goBoard[x][y] = currentPlayer;
            if (checkChain(x, y, currentPlayer)) {
                goBoard[x][y] = EMPTY_SPACE;
                return false;
            }
            //if piece has liberties occupied by same colour move is valid
            BiPredicate<Integer, Integer> checkLeft = (i, j) -> (goBoard[i - 1][j] == currentPlayer ||
                    goBoard[i - 1][j] == 0);
            BiPredicate<Integer, Integer> checkUp = (i, j) -> (goBoard[i][j - 1] == currentPlayer ||
                    goBoard[i][j - 1] == 0);
            BiPredicate<Integer, Integer> checkDown = (i, j) -> (goBoard[i][j + 1] == currentPlayer ||
                    goBoard[i][j + 1] == 0);
            BiPredicate<Integer, Integer> checkRight = (i, j) -> (goBoard[i + 1][j] == currentPlayer ||
                    goBoard[i + 1][j] == 0);

            if (x == (size - 1)) {
                return checkSurroundPieces(x, y, checkLeft, checkUp, checkDown);
            } else if (x == 0) {
                return checkSurroundPieces(x, y, checkRight, checkUp, checkDown);
            } else {
                return checkLeft.and(checkDown).and(checkRight).and(checkUp).test(x, y);
            }
        }
        return true;
    }

    private boolean checkSurroundPieces(int x, int y, BiPredicate<Integer, Integer> check, BiPredicate<Integer, Integer> checkUp, BiPredicate<Integer, Integer> checkDown) {
        if (y == (size - 1)) {
            return check.and(checkUp).test(x, y);
        } else if (y == 0) {
            return check.and(checkDown).test(x, y);
        } else {
            return check.and(checkUp).and(checkDown).test(x, y);
        }
    }


    private void checkOpponentCaptures(int x, int y) {
        if (x < (size - 1)) {
            checkCapture(x + 1, y, opponent);
        }
        if (x > 0) {
            checkCapture(x - 1, y, opponent);
        }
        if (y < (size - 1)) {
            checkCapture(x, y + 1, opponent);
        }
        if (y > 0) {
            checkCapture(x, y - 1, opponent);
        }
    }

    private int countLiberties(int x, int y) {
        int liberties = 0;

        if (x < (size - 1)) {
            if (goBoard[x + 1][y] == 0)
                liberties++;
        }
        if (x > 0) {
            if (goBoard[x - 1][y] == 0)
                liberties++;
        }
        if (y < (size - 1)) {
            if (goBoard[x][y + 1] == 0)
                liberties++;
        }
        if (y > 0) {
            if (goBoard[x][y - 1] == 0)
                liberties++;
        }
        return liberties;
    }

    private void checkCapture(int x, int y, int player) {
        //if chain is captured
        if (!this.checkChain(x, y, player)) {
            captured = true;
            //copy chain
            if (game.capturedChain.size() == 0) {
                game.capturedChain = pieceChain.stream().collect(Collectors.toList());
            }
            //update scores
            if (currentPlayer == BLACK) {
                int whiteScore = game.getPlayerWhiteScore();
                game.setPlayerWhiteScore(whiteScore + pieceChain.size());
            } else {
                int whiteScore = game.getPlayerBlackScore();
                game.setPlayerBlackScore(whiteScore + pieceChain.size());
            }
            //empty chains
            for (Pair<Integer, Integer> piece : pieceChain) {
                goBoard[piece.getKey()][piece.getValue()] = EMPTY_SPACE;
            }
        }
    }

    private boolean checkChain(int x, int y, int player) {
        pieceChain = new ArrayList<>();

        chainHasLiberty = goBoard[x][y] == 0;
        if (goBoard[x][y] == player) {
            pieceChain.add(new Pair<>(x, y));
        }

        //iterate through pieceChain, while also adding new pieces that belong to the chain
        for (ListIterator<Pair<Integer, Integer>> it = pieceChain.listIterator(); it.hasNext(); ) {
            Pair<Integer, Integer> piece = it.next();
            checkPiece(piece.getKey(), piece.getValue(), player, it);
        }
        return chainHasLiberty;
    }

    private void checkPiece(int x, int y, int player, ListIterator<Pair<Integer, Integer>> piece) {
        if (x < (size - 1)) {
            if (checkLiberty(x + 1, y, player, piece)) {
                chainHasLiberty = true;
            }
        }
        if (x > 0) {
            if (checkLiberty(x - 1, y, player, piece)) {
                chainHasLiberty = true;
            }
        }
        if (y < (size - 1)) {
            if (checkLiberty(x, y + 1, player, piece)) {
                chainHasLiberty = true;
            }
        }
        if (y > 0) {
            if (checkLiberty(x, y - 1, player, piece)) {
                chainHasLiberty = true;
            }
        }
    }

    private boolean checkLiberty(int x, int y, int player, ListIterator<Pair<Integer, Integer>> piece) {
        if (goBoard[x][y] == EMPTY_SPACE)
            return true;
        Pair<Integer, Integer> newPiece = new Pair<>(x, y);
        //if piece belongs to player add to chain
        if (goBoard[x][y] == player && !pieceChain.contains(newPiece)) {
            piece.add(newPiece);
            piece.previous();
        }
        return false;
    }

    //change players for next rules
    private void swapPlayers() {
        if (currentPlayer == WHITE) {
            currentPlayer = BLACK;
            opponent = WHITE;
        } else {
            currentPlayer = WHITE;
            opponent = BLACK;
        }
    }

    //compute scores by counting the number of pieces in the board left
    private void computeScores() {

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (goBoard[i][j] == WHITE) {
                    int whiteScore = game.getPlayerWhiteScore();
                    game.setPlayerWhiteScore(whiteScore + 1);
                } else if (goBoard[i][j] == BLACK) {
                    int blackScore = game.getPlayerWhiteScore();
                    game.setPlayerBlackScore(blackScore + 1);
                }
            }
        }
    }

    public int determineWinner() {
        computeScores();
        if (game.getPlayerWhiteScore().equals(game.getPlayerWhiteScore())) {
            return 0;
        } else if (game.getPlayerWhiteScore() > game.getPlayerWhiteScore()) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

}
