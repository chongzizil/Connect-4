package edu.nyu.zil.pqs.connect4.client;

import edu.nyu.zil.pqs.connect4.ai.AlphaBetaPruning;
import edu.nyu.zil.pqs.connect4.ai.DateTimer;
import edu.nyu.zil.pqs.connect4.ai.Heuristic;
import edu.nyu.zil.pqs.connect4.graphic.Connect4Listener;

import java.util.ArrayList;
import java.util.List;

public class Connect4Controller {
	private List<Connect4Listener> connect4Listeners = new ArrayList<Connect4Listener>();
	private Connect4Model connect4Model;
	private Connect4Constant.MODE mode;
	private Connect4Constant.AI_DIFFICULTY ai_difficulty;
	private AlphaBetaPruning ai;
	private Heuristic heuristic;

	private String startModeMessage;
	private int[] playerIDs;

	public Connect4Controller() {
		connect4Model = new Connect4Model();
	}

	/**
	 * Set the game mode and initialize the playerIDs.
	 *
	 * @param mode the game mode the player chose.
	 */
	public void setMode(Connect4Constant.MODE mode) {
		this.mode = mode;
		switch (mode) {
			case PLAYER:
				playerIDs = new int[]{Connect4Constant.PLAYER1, Connect4Constant.PLAYER2};
				startModeMessage = "VS Player";
				break;
			case AI:
				playerIDs = new int[]{Connect4Constant.PLAYER1, Connect4Constant.AI};
				break;
		}
	}

	/**
	 * Set the AI difficulty.
	 * @param ai_difficulty the ai difficulty the player chose.
	 */
	public void setAI(Connect4Constant.AI_DIFFICULTY ai_difficulty) {
		this.ai_difficulty = ai_difficulty;

		switch (ai_difficulty) {
			case EASY:
				startModeMessage = "VS Easy AI";
				break;
			case MEDIUM:
				startModeMessage = "VS Medium Player";
				break;
			case DIFFICULT:
				startModeMessage = "VS Difficult Player";
				break;
		}
	}

	/**
	 * Initialize the game in order for play.
	 */
	public void play() {
		connect4Model.initialize();

		updateView(Connect4Constant.VIEW_OPERATION.PLAYERS_INFO, playerIDs);
		updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, Connect4Constant.PLAYER1);
		updateView(Connect4Constant.VIEW_OPERATION.MSG, startModeMessage);
	}

	/**
	 * Place the next piece.
	 * @param col the column where the piece will be dropped.
	 */
	public void placeNextPiece(int col) {
		int row;
		int currentPlayer = connect4Model.hasTurn(playerIDs[0]) ? playerIDs[0] : playerIDs[1];
		Connect4Constant.COLOR color;

		if (!connect4Model.isRunning()) {
			throw new UnsupportedOperationException("The game is already ended.");
		}

		row = connect4Model.placePiece(currentPlayer, col);

		if (row != -1) {
			switch (currentPlayer) {
				case Connect4Constant.PLAYER1:
					color = Connect4Constant.COLOR.RED;
					break;
				default:
					color = Connect4Constant.COLOR.YELLOW;
					break;
			}

			currentPlayer = connect4Model.hasTurn(playerIDs[0]) ? playerIDs[0] : playerIDs[1];
			updateView(Connect4Constant.VIEW_OPERATION.BOARD, color, row, col);
			updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, currentPlayer);

			if (connect4Model.checkHasWon(row, col)) {
				updateView(Connect4Constant.VIEW_OPERATION.MSG, "Player" + currentPlayer + " has won!");
			}
		}

		// If the second player is AI, then ai will make the next move.
		if (mode == Connect4Constant.MODE.AI && connect4Model.hasTurn(Connect4Constant.AI)) {
			makeMoveByAI();
		}
	}

	/**
	 * Ai make the move.
	 */
	public void makeMoveByAI() {
		int col, row;
		heuristic = new Heuristic();
		ai = new AlphaBetaPruning(heuristic, connect4Model.copy());
		// Max response time is 500ms, it more than enough.
		DateTimer timer = new DateTimer(500);

		if (!connect4Model.isRunning()) {
			throw new UnsupportedOperationException("The game is already ended.");
		}

		// According to the difficulty, the AI will calculate different depth.
		switch (ai_difficulty) {
			case EASY:
				col = ai.findBestMove(1, timer);
				break;
			case MEDIUM:
				col = ai.findBestMove(3, timer);
				break;
			case DIFFICULT:
				col = ai.findBestMove(6, timer);
				break;
			case NIGHTMARE:
				col = ai.findBestMove(9, timer);
				break;
			default:
				throw new IllegalArgumentException();
		}

		row = connect4Model.placePiece(Connect4Constant.AI, col);

		int currentPlayer = connect4Model.hasTurn(playerIDs[0]) ? playerIDs[0] : playerIDs[1];

		updateView(Connect4Constant.VIEW_OPERATION.BOARD, Connect4Constant.COLOR.YELLOW, row, col);
		updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, currentPlayer);

		if (connect4Model.checkHasWon(row, col)) {
			updateView(Connect4Constant.VIEW_OPERATION.MSG, "AI has won!");
		}
	}

	/**
	 * Add a new listener to the model.
	 *
	 * @param connect4Listener
	 * @return true if the listener is successfully added,otherwise false.
	 */
	public boolean addListener(Connect4Listener connect4Listener) {
		if (!connect4Listeners.contains(connect4Listener)) {
			connect4Listeners.add(connect4Listener);
			connect4Listener.setConnect4Controller(this);
			return true;
		}
		return false;
	}

	/**
	 * remove the listener from the model.
	 *
	 * @param connect4Listener
	 * @return true if the listener is successfully removed, otherwise false.
	 */
	public boolean removeListener(Connect4Listener connect4Listener) {
		return connect4Listeners.remove(connect4Listener);
	}

	/**
	 * Update the views.
	 * @param viewOperation the exact operation for the view.
	 * @param message the message need to be display in the message box.
	 * @param currPlayerId the current player ID.
	 * @param playerIDs the playerIDs.
	 * @param color the color of the newly dropped piece.
	 * @param row the row of the newly dropped piece.
	 * @param col the col of the newly dropped piece.
	 */
	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation,
	                        String message,
	                        int currPlayerId,
	                        int[] playerIDs,
	                        Connect4Constant.COLOR color,
	                        int row,
	                        int col) {
		for (Connect4Listener connect4Listener : connect4Listeners) {
			switch (viewOperation) {
				case MSG:
					connect4Listener.updateMessage(message);
					break;
				case CURR_PLAYER:
					connect4Listener.updateCurrentPlayer(currPlayerId);
					break;
				case BOARD:
					connect4Listener.updateBoard(color, row, col);
					break;
				case PLAYERS_INFO:
					connect4Listener.updatePlayers(playerIDs, ai_difficulty);
					break;
			}
			if (!connect4Model.isRunning()) {
				connect4Listener.disableButtons();
			}
		}
	}

	/**
	 * Update the views.
	 * @param viewOperation the exact operation for the view.
	 * @param playerIDs the playerIDs.
	 */
	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, int[] playerIDs) {
		updateView(viewOperation, "", -1, playerIDs, Connect4Constant.COLOR.EMPTY, -1, -1);
	}

	/**
	 * Update the views.
	 * @param viewOperation the exact operation for the view.
	 * @param message the message need to be display in the message box.
	 */
	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, String message) {
		updateView(viewOperation, message, -1, playerIDs, Connect4Constant.COLOR.EMPTY, -1, -1);
	}

	/**
	 * Update the views.
	 *
	 * @param viewOperation the exact operation for the view.
	 * @param currPlayerId  the current player ID.
	 */
	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, int currPlayerId) {
		updateView(viewOperation, "", currPlayerId, playerIDs, Connect4Constant.COLOR.EMPTY, -1, -1);
	}

	/**
	 * Update the views.
	 * @param viewOperation the exact operation for the view.
	 * @param color the color of the newly dropped piece.
	 * @param row the row of the newly dropped piece.
	 * @param col the col of the newly dropped piece.
	 */
	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, Connect4Constant.COLOR color, int row, int col) {
		updateView(viewOperation, "", -1, playerIDs, color, row, col);
	}
}
