package edu.nyu.zil.pqs.connect4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zil on 2014/7/31.
 */
public class Connect4Model {
    public enum MODE {
        PLAYER,
        AI
    }

    public enum COLOR {
        RED,
        YELLOW,
        EMPTY
    }

    private enum VIEW_OPERATION {
        MSG,
        PLAYER,
        BOARD
    }

    private static Connect4Model connect4Model;
    private static final int WIN_NUM = 4;
    public static final int ROW = 6;
    public static final int COLUMN = 7;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;
    private static final int AI = 0;

    private COLOR[][] board;
    private boolean isRunning;
    private COLOR winner;
    private int[] players;
    private int currentPlayer;

    List<Connect4Listener> connect4Listeners = new ArrayList<Connect4Listener>();

    public static Connect4Model getModel() {
        if (connect4Model == null) {
            return new Connect4Model();
        } else {
            return connect4Model;
        }
    }

    private Connect4Model() {
        initialize();
    }

    /**
     * Initialize the game board, set <code>isRunning</code> to false and winner to <code>COLOR.EMPTY</code>.
     */
    private void initialize() {
        board = new COLOR[ROW][COLUMN];
        initialBoard();
        isRunning = false;
        winner = COLOR.EMPTY;
        currentPlayer = PLAYER1;

        updateView(VIEW_OPERATION.PLAYER, PLAYER1);
        updateView(VIEW_OPERATION.MSG, "");
    }

    public void play(MODE mode) {
        initialize();

        switch (mode) {
            case PLAYER:
                players = new int[]{PLAYER1, PLAYER2};
                break;
            case AI:
                players = new int[]{PLAYER1, AI};
                break;
        }

        isRunning = true;
    }

    public void reset() {
        initialize();

        isRunning = true;
    }

    /**
     * Initialize the game board, set all cells to <code>COLOR.EMPTY</code>
     */
    private void initialBoard() {
        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COLUMN; j++) {
                updateView(VIEW_OPERATION.BOARD, COLOR.EMPTY, i, j);
                board[i][j] = COLOR.EMPTY;
            }
        }
    }

    /**
     * Add a new listener to the model.
     * @param connect4Listener
     * @return true if the listener is successfully added,otherwise false.
     */
    public boolean addListener(Connect4Listener connect4Listener) {
        if (!connect4Listeners.contains(connect4Listener)) {
            connect4Listeners.add(connect4Listener);
            return true;
        }
        return false;
    }

    /**
     * remove the listener from the model.
     * @param connect4Listener
     * @return true if the listener is successfully removed, otherwise false.
     */
    public boolean removeListener(Connect4Listener connect4Listener) {
        return connect4Listeners.remove(connect4Listener);
    }

    /**
     * Place a new piece in the column, if the column is full. If the piece is successfully dropped, update the game
     * board first and then check if this move results an end of the game (if the player made the move won).
     * @param playerID the player who made the move..
     * @param col the column where the piece is going to be dropped.
     */
    public void placePiece(int playerID, int col) {
        int row;
        COLOR color;

        if(!isRunning) {
            throw new UnsupportedOperationException("The game is already ended.");
        }

        if(currentPlayer != playerID) {
            throw new IllegalArgumentException("It's not your turn.");
        }

        row = dropPiece(playerID, col);

        if(row != -1) {
            switch (playerID) {
                case PLAYER1:
                    color = COLOR.RED;
                    break;
                default:
                    color = COLOR.YELLOW;
                    break;
            }

            updateView(VIEW_OPERATION.BOARD, color, row, col);

            currentPlayer = players[0] == currentPlayer ? players[1] : players[0];
            updateView(VIEW_OPERATION.PLAYER, currentPlayer);

            if (checkHasWon(playerID, row, col)) {
                isRunning = false;
                updateView(VIEW_OPERATION.MSG, playerID + " has won!");
            }
        }
    }

    private void updateView(VIEW_OPERATION viewOperation, String message, int playerId, COLOR color, int row, int col) {
        for (Connect4Listener connect4Listener : connect4Listeners) {
            switch (viewOperation) {
                case MSG:
                    connect4Listener.updateMessage(message);
                    break;
                case PLAYER:
                    connect4Listener.updateCurrentPlayer(playerId);
                    break;
                case BOARD:
                    connect4Listener.updateBoard(color, row, col);
                    break;
            }
        }
    }

    private void updateView(VIEW_OPERATION viewOperation, String message){
        updateView(viewOperation, message, -1, COLOR.EMPTY, -1, -1);
    }

    private void updateView(VIEW_OPERATION viewOperation, int playerId){
        updateView(viewOperation, "", playerId, COLOR.EMPTY, -1, -1);
    }

    private void updateView(VIEW_OPERATION viewOperation, COLOR color, int row, int col){
        updateView(viewOperation, "", -1, color, row, col);
    }

    /**
     * Drop the piece in the column
     * @param playerID the player who place the piece
     * @param col the column where the piece is going to be dropped.
     * @return the row where the piece is placed, otherwise -1 indicates the column is full.
     */
    private int dropPiece(int playerID, int col) {
        int row = -1;

        for (int i = ROW - 1; i >=0; i--) {
            if (row == -1 && board[i][col] == COLOR.EMPTY) {
                switch (playerID) {
                    case 1:
                        board[i][col] = COLOR.RED;
                        row = i;
                        return row;
                    default:
                        board[i][col] = COLOR.YELLOW;
                        row = i;
                        return row;
                }
            }
        }

        return row;
    }

    private boolean checkHasWon(int playerId, int row, int col) {
        if(checkHorizontal(row, col) || checkVertical(row, col) || checkDiagonal(row, col)) {
            return true;
        }

        return false;
    }

    private boolean checkHorizontal(int row, int col) {
        COLOR currentCell = board[row][col];
        int connectNum = 0;

        for (int i = 0; i < COLUMN; i++) {
            if (currentCell == board[row][i]) {
                connectNum++;
                if (connectNum == 4) {
                    winner = currentCell;
                    return true;
                }
            } else {
                connectNum = 0;
            }
        }

        return false;
    }

    private boolean checkVertical(int row, int col) {
        COLOR currentCell = board[row][col];
        int connectNum = 0;

        for (int i = 0; i < ROW; i++) {
            if (currentCell == board[i][col]) {
                connectNum++;
                if (connectNum == 4) {
                    winner = currentCell;
                    return true;
                }
            } else {
                connectNum = 0;
            }
        }

        return false;
    }

    private boolean checkDiagonal(int row, int col) {
        COLOR currentCell = board[row][col];
        int connectNum = 0;

        for (int i = 1; i < WIN_NUM && row + WIN_NUM <= ROW && col + WIN_NUM <= COLUMN; i++) {
            if (currentCell == board[row + i][col + i]) {
                connectNum++;
                if (connectNum == 4) {
                    winner = currentCell;
                    return true;
                }
            } else {
                connectNum = 0;
            }
        }

        for (int i = 1; i < WIN_NUM && row - WIN_NUM >= -1 && col - WIN_NUM >= -1; i++) {
            if (currentCell == board[row - i][col - i]) {
                connectNum++;
                if (connectNum == 4) {
                    winner = currentCell;
                    return true;
                }
            } else {
                connectNum = 0;
            }
        }

        for (int i = 1; i < WIN_NUM && row - WIN_NUM >= -1 && col + WIN_NUM <= COLUMN; i++) {
            if (currentCell == board[row - i][col + i]) {
                connectNum++;
                if (connectNum == 4) {
                    winner = currentCell;
                    return true;
                }
            } else {
                connectNum = 0;
            }
        }

        for (int i = 1; i < WIN_NUM && row + WIN_NUM <= ROW && col - WIN_NUM >= -1; i++) {
            if (currentCell == board[row + i][col - i]) {
                connectNum++;
                if (connectNum == 4) {
                    winner = currentCell;
                    return true;
                }
            } else {
                connectNum = 0;
            }
        }

        return false;
    }
}
