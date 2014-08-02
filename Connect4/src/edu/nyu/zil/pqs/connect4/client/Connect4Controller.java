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
	private Connect4Constant.COLOR winner;
	private Connect4Constant.MODE mode;
	private Connect4Constant.AI_DIFFICULTY ai_difficulty;
	private AlphaBetaPruning ai;
	private Heuristic heuristic;

	private String startModeMessage;
	private boolean isRunning;
	private int[] playerIDs;
	private int currentPlayer;

	public Connect4Controller() {
		this.connect4Model = Connect4Model.getModel();
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
		isRunning = true;

		updateView(Connect4Constant.VIEW_OPERATION.PLAYERS_INFO, playerIDs);
		updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, Connect4Constant.PLAYER1);
		updateView(Connect4Constant.VIEW_OPERATION.MSG, startModeMessage);
	}

	public void reset() {
		connect4Model.initialize();
		initialize();
		isRunning = true;

		updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, Connect4Constant.PLAYER1);
		updateView(Connect4Constant.VIEW_OPERATION.MSG, startModeMessage);
	}

	public void initialize() {
		winner = Connect4Constant.COLOR.EMPTY;
		currentPlayer = Connect4Constant.PLAYER1;

		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				updateView(Connect4Constant.VIEW_OPERATION.BOARD, Connect4Constant.COLOR.EMPTY, i, j);
			}
		}
	}

	public void placeNextPiece(int playerID, int col) {
		int row;
		Connect4Constant.COLOR color;

		if (currentPlayer != playerID) {
			throw new IllegalArgumentException("It's not your turn.");
		}

		if (!isRunning) {
			throw new UnsupportedOperationException("The game is already ended.");
		}

		row = connect4Model.placePiece(playerID, col);

		if (row != -1) {
			switch (playerID) {
				case Connect4Constant.PLAYER1:
					color = Connect4Constant.COLOR.RED;
					break;
				default:
					color = Connect4Constant.COLOR.YELLOW;
					break;
			}

			updateView(Connect4Constant.VIEW_OPERATION.BOARD, color, row, col);

			currentPlayer = playerIDs[0] == currentPlayer ? playerIDs[1] : playerIDs[0];
			updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, currentPlayer);

			if (connect4Model.checkHasWon(row, col)) {
				winner = connect4Model.getCell(row, col);
				isRunning = false;
				updateView(Connect4Constant.VIEW_OPERATION.MSG, playerID + " has won!");
			}
		}
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

		if (!isRunning) {
			throw new UnsupportedOperationException("The game is already ended.");
		}

		switch (ai_difficulty) {
			case EASY:
				timer = new DateTimer(100);
				break;
			case MEDIUM:
				timer = new DateTimer(200);
				break;
			case DIFFICULT:
				timer = new DateTimer(300);
				break;
		}

		col = ai.findBestMove(6, timer);

		row = connect4Model.placePiece(Connect4Constant.AI, col);

		if (row != -1) {
			currentPlayer = Connect4Constant.PLAYER1;

			updateView(Connect4Constant.VIEW_OPERATION.BOARD, Connect4Constant.COLOR.YELLOW, row, col);
			updateView(Connect4Constant.VIEW_OPERATION.CURR_PLAYER, currentPlayer);

			if (connect4Model.checkHasWon(row, col)) {
				winner = connect4Model.getCell(row, col);
				isRunning = false;
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
