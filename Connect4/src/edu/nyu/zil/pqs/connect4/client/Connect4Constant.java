package edu.nyu.zil.pqs.connect4.client;

public class Connect4Constant {
	public enum VIEW_OPERATION {
		MSG,
		CURR_PLAYER,
		PLAYERS_INFO,
		BOARD
	}

	public enum MODE {
		PLAYER,
		AI
	}

	public enum AI_DIFFICULTY {
		EASY,
		MEDIUM,
		DIFFICULT
	}

	public enum COLOR {
		RED,
		YELLOW,
		EMPTY
	}

	public static final int WIN_NUM = 4;
	public static final int ROW = 6;
	public static final int COLUMN = 7;
	public static final int PLAYER1 = 1;
	public static final int PLAYER2 = 2;
	public static final int AI = 0;
}
