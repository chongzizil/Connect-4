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

	public void play() {
		connect4Model.initialize();
		initialize();

		updateView(Connect4Constant.VIEW_OPERATION.PLAYERS_INFO, playerIDs);
		updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, Connect4Constant.PLAYER1);
		updateView(Connect4Constant.VIEW_OPERATION.MSG, startModeMessage);
	}

	public void reset() {
		connect4Model.initialize();
		initialize();

		updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, Connect4Constant.PLAYER1);
		updateView(Connect4Constant.VIEW_OPERATION.MSG, startModeMessage);
	}

	public void initialize() {
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				updateView(Connect4Constant.VIEW_OPERATION.BOARD, Connect4Constant.COLOR.EMPTY, i, j);
			}
		}
	}

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

			updateView(Connect4Constant.VIEW_OPERATION.BOARD, color, row, col);
			updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, currentPlayer);

			if (connect4Model.checkHasWon(row, col)) {
				updateView(Connect4Constant.VIEW_OPERATION.MSG, currentPlayer + " has won!");
			}
		}

		currentPlayer = connect4Model.hasTurn(playerIDs[0]) ? playerIDs[0] : playerIDs[1];

		// AI
		if (mode == Connect4Constant.MODE.AI && currentPlayer == Connect4Constant.AI) {
			makeMoveByAI();
		}
	}

	public void makeMoveByAI() {
		heuristic = new Heuristic();
		int col, row;
		ai = new AlphaBetaPruning(heuristic, connect4Model.copy());
		DateTimer timer = new DateTimer(100);

		if (!connect4Model.isRunning()) {
			throw new UnsupportedOperationException("The game is already ended.");
		}

		switch (ai_difficulty) {
			case EASY:
				timer = new DateTimer(10000000);
				break;
			case MEDIUM:
				timer = new DateTimer(20000000);
				break;
			case DIFFICULT:
				timer = new DateTimer(30000000);
				break;
		}

		col = ai.findBestMove(10, timer);

		row = connect4Model.placePiece(Connect4Constant.AI, col);

		if (row != -1) {
			int currentPlayer = connect4Model.hasTurn(playerIDs[0]) ? playerIDs[0] : playerIDs[1];

			updateView(Connect4Constant.VIEW_OPERATION.BOARD, Connect4Constant.COLOR.YELLOW, row, col);
			updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, currentPlayer);

			if (connect4Model.checkHasWon(row, col)) {
				updateView(Connect4Constant.VIEW_OPERATION.MSG, currentPlayer + " has won!");
			}
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

	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation,
	                        String message,
	                        int playerId,
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
					connect4Listener.updateCurrentPlayer(playerId);
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

	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, int[] playerIDs) {
		updateView(viewOperation, "", -1, playerIDs, Connect4Constant.COLOR.EMPTY, -1, -1);
	}

	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, String message) {
		updateView(viewOperation, message, -1, playerIDs, Connect4Constant.COLOR.EMPTY, -1, -1);
	}

	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, int playerId) {
		updateView(viewOperation, "", playerId, playerIDs, Connect4Constant.COLOR.EMPTY, -1, -1);
	}

	private void updateView(Connect4Constant.VIEW_OPERATION viewOperation, Connect4Constant.COLOR color, int row, int col) {
		updateView(viewOperation, "", -1, playerIDs, color, row, col);
	}
}
