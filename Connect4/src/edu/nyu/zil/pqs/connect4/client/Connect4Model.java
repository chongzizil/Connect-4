package edu.nyu.zil.pqs.connect4.client;

public class Connect4Model {
	private Connect4Constant.COLOR[][] board;
	private Connect4Constant.COLOR currColor;
	private Connect4Constant.COLOR winner;
	private boolean isRunning;

	/**
	 * Default constructor
	 */
	public Connect4Model() {
		this(new Connect4Constant.COLOR[Connect4Constant.ROW][Connect4Constant.COLUMN],
				Connect4Constant.COLOR.RED);
	}

	/**
	 * Constructor for copy a new Connect4Model.
	 *
	 * @param board     the game board
	 * @param currColor the current player's color
	 */
	public Connect4Model(Connect4Constant.COLOR[][] board, Connect4Constant.COLOR currColor) {
		Connect4Constant.COLOR[][] tmp = board.clone();
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			tmp[i] = board[i].clone();
		}

		this.board = tmp;
		this.currColor = currColor;
	}

	/**
	 * Initialize the game board.
	 */
	public void initialize() {
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				board[i][j] = Connect4Constant.COLOR.EMPTY;
			}
		}

		currColor = Connect4Constant.COLOR.RED;
		isRunning = true;
	}

	/**
	 * Place a new piece in the column and return the row number of that piece, if the column is full,
	 * return -1.
	 *
	 * @param playerID the player who made the move..
	 * @param col      the column where the piece is going to be dropped.
	 * @return the row of the placed piece.
	 */
	public int placePiece(int playerID, int col) {
		int row = -1;

		for (int i = Connect4Constant.ROW - 1; i >= 0; i--) {
			if (row == -1 && board[i][col] == Connect4Constant.COLOR.EMPTY) {
				switch (playerID) {
					case 1:
						board[i][col] = Connect4Constant.COLOR.RED;
						row = i;
						break;
					default:
						board[i][col] = Connect4Constant.COLOR.YELLOW;
						row = i;
						break;
				}
			}
		}

		if (row != -1) {
			flipCurrentColor();
		}

		return row;
	}

	/**
	 * Get the color of a specific cell of the board through its row and column.
	 * @param row the row of the cell.
	 * @param col the column of the cell.
	 * @return the color of that cell.
	 */
	public Connect4Constant.COLOR getCell(int row, int col) {
		return board[row][col];
	}

	/**
	 * Check if the game is ended when the new piece is placed. If the game is ended, set isRunning to
	 * false.
	 *
	 * @param row the new piece's row number.
	 * @param col the new piece's column number.
	 * @return true if the game is ended and a winner is determined, otherwise false.
	 */
	public boolean checkHasWon(int row, int col) {
		if (checkHorizontal(row, col) || checkVertical(row, col) || checkDiagonal(row, col)) {
			isRunning = false;
			winner = board[row][col];
			return true;
		}
		return false;
	}

	/**
	 * Check the row of the new placed piece.
	 *
	 * @param row the new piece's row number.
	 * @param col the new piece's column number.
	 * @return true if the game is ended and a winner is determined base on the row, otherwise false.
	 */
	private boolean checkHorizontal(int row, int col) {
		Connect4Constant.COLOR currentCell = board[row][col];
		int connectNum = 0;

		for (int i = 0; i < Connect4Constant.COLUMN; i++) {
			if (currentCell == board[row][i]) {
				connectNum++;
				if (connectNum == 4) {
					return true;
				}
			} else {
				connectNum = 0;
			}
		}
		return false;
	}

	/**
	 * Check the column of the new placed piece.
	 *
	 * @param row the new piece's row number.
	 * @param col the new piece's column number.
	 * @return true if the game is ended and a winner is determined base on the column, otherwise false.
	 */
	private boolean checkVertical(int row, int col) {
		Connect4Constant.COLOR currentCell = board[row][col];
		int connectNum = 0;

		for (int i = 0; i < Connect4Constant.ROW; i++) {
			if (currentCell == board[i][col]) {
				connectNum++;
				if (connectNum == 4) {
					return true;
				}
			} else {
				connectNum = 0;
			}
		}
		return false;
	}

	/**
	 * Check the diagonal cells of the new placed piece.
	 *
	 * @param row the new piece's row number.
	 * @param col the new piece's column number.
	 * @return true if the game is ended and a winner is determined base on the diagonal, otherwise false.
	 */
	private boolean checkDiagonal(int row, int col) {
		Connect4Constant.COLOR currentCell = board[row][col];
		int connectNum = 0;

		for (int i = 0;
		     i < Connect4Constant.WIN_NUM
				     && row + Connect4Constant.WIN_NUM <= Connect4Constant.ROW
				     && col + Connect4Constant.WIN_NUM <= Connect4Constant.COLUMN;
		     i++) {
			if (currentCell == board[row + i][col + i]) {
				connectNum++;
				if (connectNum == 4) {
					return true;
				}
			} else {
				connectNum = 0;
			}
		}

		for (int i = 0;
		     i < Connect4Constant.WIN_NUM
				     && row - Connect4Constant.WIN_NUM >= -1
				     && col - Connect4Constant.WIN_NUM >= -1;
		     i++) {
			if (currentCell == board[row - i][col - i]) {
				connectNum++;
				if (connectNum == 4) {
					return true;
				}
			} else {
				connectNum = 0;
			}
		}

		for (int i = 0;
		     i < Connect4Constant.WIN_NUM
				     && row - Connect4Constant.WIN_NUM >= -1
				     && col + Connect4Constant.WIN_NUM <= Connect4Constant.COLUMN;
		     i++) {
			if (currentCell == board[row - i][col + i]) {
				connectNum++;
				if (connectNum == 4) {
					return true;
				}
			} else {
				connectNum = 0;
			}
		}

		for (int i = 0;
		     i < Connect4Constant.WIN_NUM
				     && row + Connect4Constant.WIN_NUM <= Connect4Constant.ROW
				     && col - Connect4Constant.WIN_NUM >= -1;
		     i++) {
			if (currentCell == board[row + i][col - i]) {
				connectNum++;
				if (connectNum == 4) {
					return true;
				}
			} else {
				connectNum = 0;
			}
		}

		return false;
	}

	/**
	 * @return a copy of the connect4Model.
	 */
	public Connect4Model copy() {
		return new Connect4Model(this.board, this.currColor);
	}

	/**
	 * Check if the current player's color is red.
	 *
	 * @return true if the current player's color is red, otherwise false.
	 */
	public boolean isRed() {
		return currColor == Connect4Constant.COLOR.RED;
	}

	/**
	 * Check if the current player's color is yellow.
	 *
	 * @return true if the current player's color is yellow, otherwise false.
	 */
	public boolean isYellow() {
		return currColor == Connect4Constant.COLOR.YELLOW;
	}

	/**
	 * Flip the current player.
	 */
	public void flipCurrentColor() {
		currColor = currColor == Connect4Constant.COLOR.RED
				? Connect4Constant.COLOR.YELLOW : Connect4Constant.COLOR.RED;
	}

	public Connect4Constant.COLOR getWinner() {
		return winner;
	}

	/**
	 * Check if the player has the turn.
	 *
	 * @param playerID the player ID to be checked.
	 * @return true if the player has the turn, otherwise false.
	 */
	public boolean hasTurn(int playerID) {
		switch (playerID) {
			case Connect4Constant.PLAYER1:
				return currColor == Connect4Constant.COLOR.RED;
			default:
				return currColor == Connect4Constant.COLOR.YELLOW;
		}
	}

	/**
	 * Check if the game is running.
	 *
	 * @return true if the game is running, otherwise false.
	 */
	public boolean isRunning() {
		return isRunning;
	}
}
