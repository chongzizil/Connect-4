package edu.nyu.zil.pqs.connect4.graphic;

import edu.nyu.zil.pqs.connect4.client.Connect4Constant;
import edu.nyu.zil.pqs.connect4.client.Connect4Controller;

/**
 * This is the interface for the connect 4.
 *
 * @author Zil
 */
public interface Connect4Listener {

	/**
	 * Set the controller
	 *
	 * @param connect4Controller
	 */
	void setConnect4Controller(Connect4Controller connect4Controller);

	/**
	 * Update the info box.
	 *
	 * @param message the new message which will be updated in the info box.
	 */
	void updateMessage(String message);

	/**
	 * Update the current player info.
	 *
	 * @param playerID the current playerID which will be updated int the current player box.
	 */
	void updateCurrentPlayer(int playerID);

	/**
	 * Update the players info
	 *
	 * @param playerIDs an array of the player IDs.
	 *                  1 indicate the first player,
	 *                  2 indicate the second player as human,
	 *                  0 indicate the second player as AI.
	 */
	void updatePlayers(int[] playerIDs, Connect4Constant.AI_DIFFICULTY ai);

	/**
	 * Update the game board.
	 *
	 * @param color the color which is going to be updated for the cell.
	 * @param row   the row of the cell which will be updated.
	 * @param col   the column of the cell which will be updated.
	 */
	void updateBoard(Connect4Constant.COLOR color, int row, int col);

	/**
	 * Disable all the buttons.
	 */
	public void disableButtons();
}
