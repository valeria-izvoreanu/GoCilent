package com.project.client.gui.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class GoBoard extends JPanel {
    final GoGame frame;
    final static int W = 900, H = 900;
    Graphics2D graphics;
    BufferedImage board;
    int size;
    int cellSize;
    int marginLeft;
    int marginTop;
    int marginBottom;
    int marginRight;
    PrintWriter out;
    BufferedReader in;
    Color pieceColor;
    int[][] pieces;
    int currentPieceX = 0;
    int currentPieceY = 0;
    boolean currentPlayer;

    public GoBoard(GoGame frame, PrintWriter out, BufferedReader in) {
        this.frame = frame;
        this.size = this.frame.size;
        this.cellSize = W / (size + 1);
        //the first move is black
        if (this.frame.colour.equals("white")) {
            pieceColor = WHITE;
            currentPlayer = false;
            currentPieceY = -2;
            currentPieceX = -2;
        } else {
            pieceColor = BLACK;
            currentPlayer = true;
        }
        this.in = in;
        this.out = out;
        //margins from board lines from the window frame
        marginLeft = (W - (cellSize * (size - 1))) / 2;
        marginTop = (H - (cellSize * (size - 1))) / 2;
        marginBottom = marginTop + cellSize * (size - 1);
        marginRight = marginLeft + cellSize * (size - 1);
        pieces = new int[size][size];//matrix that memorises the pieces on the board
        createBoard();
        init();
    }

    private void createBoard() {
        String boardPath = new File("img/board.png").getAbsolutePath();
        board = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

        try {
            board = ImageIO.read(new File(boardPath));//create board background
            graphics = board.createGraphics();
            graphics.setColor(BLACK);
            drawBoardLines(graphics);
            writeNumbers(graphics);
            writeLetters(graphics);
        } catch (IOException e) {
            System.out.println("Couldn't open background image..." + e);
        }
    }

    private void drawBoardLines(Graphics2D graphics) {
        //draw vertical lines
        for (int i = 0; i < size; i++) {
            graphics.drawLine(marginLeft, marginTop + (i * cellSize),
                    marginBottom, marginLeft + (i * cellSize));
        }
        //draw horizontal lines
        for (int i = 0; i < size; i++) {
            graphics.drawLine(marginLeft + (i * cellSize), marginTop, marginLeft + (i * cellSize), marginRight);
        }
    }

    //writes numbers on the board
    private void writeNumbers(Graphics2D graphics) {
        graphics.setFont(new Font("Monaco", Font.PLAIN, cellSize / 2));
        int marginX;
        for (int i = 1; i <= size; i++) {
            if ((i - 10 >= 0 || size == 7) && size != 19) {
                marginX = marginLeft - 50;
            } else {
                marginX = marginLeft - 40;
            }
            graphics.drawString(String.valueOf(i), marginX, marginBottom + 10 - (cellSize * (i - 1)));
        }
        if (size == 13 || size == 19) {
            for (int i = 1; i <= size; i++) {
                graphics.drawString(String.valueOf(i), marginRight + 15, marginBottom + 10 - (cellSize * (i - 1)));
            }
        }
    }

    private void writeLetters(Graphics2D graphics) {
        graphics.setFont(new Font("Monaco", Font.PLAIN, cellSize / 2));
        char letter;
        int diff;
        if (size == 13 || size == 19) {
            for (int i = 1; i <= size; i++) {
                if (i - 9 >= 0) {
                    letter = (char) (65 + i);
                } else {
                    letter = (char) (65 + i - 1);
                }
                if (size == 19) {
                    diff = 17;
                } else {
                    diff = 30;
                }
                graphics.drawString(Character.toString(letter), marginLeft - 10 + (cellSize * (i - 1)), marginTop - diff);
            }
        }
        for (int i = 1; i <= size; i++) {
            if (i - 9 >= 0) {
                letter = (char) (65 + i);
            } else {
                letter = (char) (65 + i - 1);
            }
            if (size == 13 || size == 19) {
                if (size == 19) {
                    diff = 37;
                } else {
                    diff = 50;
                }
                graphics.drawString(Character.toString(letter), marginLeft - 10 + (cellSize * (i - 1)),
                        marginBottom + diff);
            } else {
                graphics.drawString(Character.toString(letter), marginLeft - 10 + (cellSize * (i - 1)),
                        marginBottom + 60);
            }
        }

    }

    private void init() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentPlayer) {
                    //enable submit button so player can make a move
                    if (!frame.infoPanel.submitBtn.isEnabled()) {
                        frame.infoPanel.submitBtn.setEnabled(true);
                    } else {
                        //if move invalid delete it
                        if (currentPieceX != -1) {
                            if (pieces[currentPieceX][currentPieceY] == (pieceColor.equals(BLACK) ? 1 : 2)) {
                                pieces[currentPieceX][currentPieceY] = 0;
                            }
                        }
                    }
                    getCoordinates(e.getX(), e.getY());//get coordinates and check if valid
                    if (currentPieceX != -1) {
                        repaint();
                    }

                } else {
                    //first move, white reads black's move
                    if (pieceColor.equals(WHITE) && currentPieceY == -2 && currentPieceX == -2) {
                        frame.infoPanel.passBtn.setEnabled(false);
                        readPiece();
                    }
                }
            }
        });
    }


    public void readPiece() {
        int x, y;
        String getMove = new String();
        try {
            if (!currentPlayer) {
                getMove = in.readLine();//check if player made move

                if (getMove.equals("OK")) {
                    //get coordinates
                    x = Integer.parseInt(in.readLine());
                    y = Integer.parseInt(in.readLine());
                    pieces[x][y] = (pieceColor.equals(BLACK) ? 2 : 1);
                    currentPieceX = x;
                    currentPieceY = y;
                } else {//if player passed check if game ends
                    frame.infoPanel.endGame(getMove);
                }
                currentPlayer = true;//become current player
                frame.infoPanel.submitBtn.setEnabled(true);
                frame.infoPanel.passBtn.setEnabled(true);
            }
            if (getMove.equals("OK") || currentPlayer) {
                int length = Integer.parseInt(in.readLine());//read captured chain's size
                if (length > 0) {
                    //delete captured pieces
                    for (int i = 0; i < length; i++) {
                        x = Integer.parseInt(in.readLine());
                        y = Integer.parseInt(in.readLine());
                        pieces[x][y] = 0;
                    }
                    if (frame.mainPlayer) {//update info panel
                        if (currentPlayer) {
                            frame.infoPanel.player1.updateCaptures(length);
                        } else {
                            frame.infoPanel.player2.updateCaptures(length);
                        }
                    } else {
                        if (currentPlayer) {
                            frame.infoPanel.player2.updateCaptures(length);
                        } else {
                            frame.infoPanel.player1.updateCaptures(length);
                        }
                    }
                }
            }
            revalidate();
            repaint();
        } catch (
                IOException err) {
            System.out.println("Couldn't read opponent's move: " + err);
        }

    }

    private void drawBoard(Graphics2D graphics) {
        //redraw board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (pieces[i][j] != 0) {
                    drawPiece(graphics, i, j);
                }
            }
        }
    }

    private void drawPiece(Graphics2D graphics, int x, int y) {
        graphics.setColor((pieces[x][y] == 1) ? BLACK : WHITE);
        graphics.fill(new GoPiece(marginLeft + x * cellSize,
                marginTop + y * cellSize, cellSize - 2));

    }

    private void getCoordinates(int x, int y) {
        int radius = cellSize / 2;
        if (y >= (marginTop - radius) && y <= (marginBottom + radius)
                && x >= (marginLeft - radius) && x <= (marginRight + radius)) {
            int cellX = Math.round((float) x / cellSize) - 1;//turn into coordinates for matrix
            int cellY = Math.round((float) y / cellSize) - 1;
            try {
                checkCoordinates(cellX, cellY);
            } catch (ArrayIndexOutOfBoundsException e) {
                if (cellX == size) cellX--;
                if (cellY == size) cellY--;
                if (cellX == -1) cellX++;
                if (cellY == -1) cellY++;
                checkCoordinates(cellX, cellY);
            }
        } else {
            currentPieceX = -1;
            currentPieceY = -1;
        }
    }

    private void checkCoordinates(int cellX, int cellY) {
        if (pieces[cellX][cellY] == 0) {
            pieces[cellX][cellY] = (pieceColor.equals(BLACK) ? 1 : 2);
            currentPieceX = cellX;
            currentPieceY = cellY;
        } else {
            currentPieceX = -1;
            currentPieceY = -1;
        }
    }

    @Override
    public void update(Graphics g) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(board, 0, 0, this);
        Graphics2D g2d = (Graphics2D) g.create();
        drawBoard(g2d);
    }

}
