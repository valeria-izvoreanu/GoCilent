package com.project.server.exceptions;

public class InvalidMoveException extends Exception {
    public InvalidMoveException(int x, int y) {
        super("Invalid move:" + x + " " + y);
    }
}
