package edu.nyu.zil.pqs.connect4.client;

public class Connect4Model {
	private static Connect4Model connect4Model;

	private Connect4Constant.COLOR[][] board;
	private Connect4Constant.COLOR currColor;

	public static Connect4Model getModel() {
		if (connect4Model == null) {
			connect4Model = new Connect4Model();
			return connect4Model;
		} else {
			return connect4Model;
		}
	}

	private Connect4Model() {
		board = new Connect4Constant.COLOR[Connect4Constant.ROW][Connect4Constant.COLUMN];
	}

	private Connect4Model(Connect4Constant.COLOR[][] board, Connect4Constant.COLOR currColor) {
		Connect4Constant.COLOR[][] tmp = board.clone();
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			tmp[i] = board[i].clone();
		}
		this.board = tmp;
		this.currColor = currColor == Connect4Constant.COLOR.YELLOW ? Connect4Constant.COLOR.RED : Connect4Constant.COLOR.YELLOW;
	}

	/**
	 * Initialize the game board, set <code>isRunning</code> to false and winner to <code>COLOR.EMPTY</code>.
	 */
	public void initialize() {
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				board[i][j] = Connect4Constant.COLOR.EMPTY;
			}
		}
	}

	/**
	 * Place a new piece in the column, if the column is full. If the piece is successfully dropped, update the game
	 * board first and then check if this move results an end of the game (if the player made the move won).
	 *
	 * @param playerID the player who made the move..
	 * @param col      the column where the piece is going to be dropped.
	 */
	public int placePiece(int playerID, int col) {
		int row = -1;

		for (int i = Connect4Constant.ROW - 1; i >= 0; i--) {
			if (row == -1 && board[i][col] == Connect4Constant.COLOR.EMPTY) {
				switch (playerID) {
					case 1:
						board[i][col] = Connect4Constant.COLOR.RED;
						row = i;
						return row;
					default:
						board[i][col] = Connect4Constant.COLOR.YELLOW;
						row = i;
						return row;
				}
			}
		}
		return row;
	}

	public Connect4Constant.COLOR getCell(int row, int col) {
		return board[row][col];
	}

	public boolean checkHasWon(int row, int col) {
		return (checkHorizontal(row, col) || checkVertical(row, col) || checkDiagonal(row, col));
	}

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

	public Connect4Model copy() {
		return new Connect4Model(this.board, this.currColor);
	}

	public Connect4Constant.COLOR getCurrentColor() {
		return currColor;
	}

	public void setCurrentColor(Connect4Constant.COLOR currColor) {
		this.currColor = currColor;
	}
}
