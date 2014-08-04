package edu.nyu.zil.pqs.connect4.client;

import edu.nyu.zil.pqs.connect4.graphic.Connect4Listener;

import java.util.logging.Logger;

/**
 * Created by Zil on 2014/8/4.
 */
public class Connect4Logger implements Connect4Listener {
	/**
	 * use a logger instead of System.out.println
	 */
	private static final Logger logger =
			Logger.getLogger("Connect 4 log.");

	/**
	 * Set the controller
	 *
	 * @param connect4Controller
	 */
	@Override
	public void setConnect4Controller(Connect4Controller connect4Controller) {

	}

	/**
	 * Update the info box.
	 *
	 * @param message the new message which will be updated in the info box.
	 */
	@Override
	public void updateMessage(String message) {
		logger.info("New message: " + message);
	}

	/**
	 * Update the current player info.
	 *
	 * @param playerID the current playerID which will be updated int the current player box.
	 */
	@Override
	public void updateCurrentPlayer(int playerID) {
		logger.info("Current playerID is " + playerID + " now.");
	}

	/**
	 * Update the players info
	 *
	 * @param playerIDs an array of the player IDs.
	 *                  1 indicate the first player,
	 *                  2 indicate the second player as human,
	 *                  0 indicate the second player as AI.
	 * @param ai
	 */
	@Override
	public void updatePlayers(int[] playerIDs, Connect4Constant.AI_DIFFICULTY ai) {
		logger.info("PlayerIDs are " + playerIDs[0] + " and " + playerIDs[1] + ".");
		if (playerIDs[1] == Connect4Constant.AI) {
			logger.info("The AI difficulty is " + ai.name());
		}
	}

	/**
	 * Update the game board.
	 *
	 * @param color the color which is going to be updated for the cell.
	 * @param row   the row of the cell which will be updated.
	 * @param col   the column of the cell which will be updated.
	 */
	@Override
	public void updateBoard(Connect4Constant.COLOR color, int row, int col) {
		logger.info("A new " + color.name() + " piece is dropped in ROW:" + row + " and COLUMN:" + col);
	}

	/**
	 * Disable all the buttons.
	 */
	@Override
	public void disableButtons() {

	}
}
