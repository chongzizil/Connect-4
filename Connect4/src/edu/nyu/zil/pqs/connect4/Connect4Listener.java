package edu.nyu.zil.pqs.connect4;

/**
 * This is the interface for the connect 4.
 * @author Zil
 */
public interface Connect4Listener {
    /**
     * Update the info box.
     *
     * @param message the new message which will be updated in the info box.
     */
    void updateMessage(String message);

    /**
     * Update the current player info.
     *
     * @param playerId the current playerID which will be updated int the current player box.
     */
    void updateCurrentPlayer(int playerId);

    /**
     * Update the game board.
     *
     * @param color the color which is going to be updated for the cell.
     * @param row the row of the cell which will be updated.
     * @param col the column of the cell which will be updated.
     */
    void updateBoard(Connect4Model.COLOR color, int row, int col);
}
