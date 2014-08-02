package edu.nyu.zil.pqs.connect4.graphic;

import edu.nyu.zil.pqs.connect4.client.Connect4Constant;
import edu.nyu.zil.pqs.connect4.client.Connect4Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4View implements Connect4Listener {
	Connect4Controller connect4Controller;
	private int currentPlayerId;

	private JFrame frame = new JFrame("Connect 4");

	JPanel jPanel = new JPanel();

	JPanel infoPanel = new JPanel();
	JTextField currentPlayerField = new JTextField();
	JTextField player1Field = new JTextField();
	JTextField player2Field = new JTextField();

	JPanel mainPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JButton[] buttons = new JButton[Connect4Constant.COLUMN];
	JPanel boardPanel = new JPanel();
	JPanel[][] cells = new JPanel[Connect4Constant.ROW][Connect4Constant.COLUMN];

	JPanel bottomPanel = new JPanel();
	JPanel gameModePanel = new JPanel();
	JButton vsPlayerButton = new JButton("Player");
	JButton vsEasyAIButton = new JButton("Easy");
	JButton vsMediumAIButton = new JButton("Medium");
	JButton vsDifficultAIButton = new JButton("Difficult");
	JPanel messagePanel = new JPanel();
	JTextField messageField = new JTextField();
	JButton resetButton = new JButton("Reset");

	public Connect4View() {
		jPanel.setLayout(new BorderLayout());

		infoPanel.setLayout(new GridLayout(1, 3));

		mainPanel.setLayout(new BorderLayout());
		boardPanel.setLayout(new GridLayout(Connect4Constant.ROW, Connect4Constant.COLUMN));

		bottomPanel.setLayout(new GridLayout(2, 1));
		gameModePanel.setLayout(new GridLayout(1, 4));
		messagePanel.setLayout(new GridLayout(1, 2));

		jPanel.add(infoPanel, BorderLayout.NORTH);
		jPanel.add(mainPanel, BorderLayout.CENTER);
		jPanel.add(bottomPanel, BorderLayout.SOUTH);

		infoPanel.add(player1Field);
		infoPanel.add(currentPlayerField);
		infoPanel.add(player2Field);

		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		mainPanel.add(boardPanel, BorderLayout.CENTER);

		gameModePanel.add(vsPlayerButton);
		gameModePanel.add(vsEasyAIButton);
		gameModePanel.add(vsMediumAIButton);
		gameModePanel.add(vsDifficultAIButton);
		messageField.setText("Please choose a mode first!");
		messagePanel.add(messageField);
		messagePanel.add(resetButton);
		bottomPanel.add(gameModePanel);
		bottomPanel.add(messagePanel);

		for (int i = 0; i < Connect4Constant.COLUMN; i++) {
			buttons[i] = new JButton(i + "");
			final int finalI = i;
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connect4Controller.placeNextPiece(currentPlayerId, finalI);
				}
			});
			buttonPanel.add(buttons[i]);
		}

		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				cells[i][j] = new JPanel();
				cells[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				cells[i][j].setBackground(java.awt.Color.gray);
				boardPanel.add(cells[i][j]);
			}
		}

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.reset();
				initialBoard();
			}
		});

		vsPlayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.setMode(Connect4Constant.MODE.PLAYER);
				connect4Controller.play();
			}
		});

		vsEasyAIButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.setMode(Connect4Constant.MODE.AI);
				connect4Controller.setAI(Connect4Constant.AI_DIFFICULTY.EASY);
				connect4Controller.play();
			}
		});

		vsMediumAIButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.setMode(Connect4Constant.MODE.AI);
				connect4Controller.setAI(Connect4Constant.AI_DIFFICULTY.MEDIUM);
				connect4Controller.play();
			}
		});

		vsDifficultAIButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.setMode(Connect4Constant.MODE.AI);
				connect4Controller.setAI(Connect4Constant.AI_DIFFICULTY.DIFFICULT);
				connect4Controller.play();
			}
		});

		frame.add(jPanel);
//        frame.getContentPane().add(jPanel);
		frame.setSize(500, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void setConnect4Controller(Connect4Controller connect4Controller) {
		this.connect4Controller = connect4Controller;
	}

	public void initialBoard() {
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				cells[i][j].setBackground(java.awt.Color.gray);
			}
		}
	}

	/**
	 * Update the info box.
	 *
	 * @param message the new message which will be updated in the info box.
	 */
	@Override
	public void updateMessage(String message) {
		messageField.setText(message);
	}

	/**
	 * Update the difficulty.
	 *
	 * @param difficulty
	 */
	@Override
	public void updateAIDifficultyStatus(Connect4Constant.AI_DIFFICULTY difficulty) {

	}

	/**
	 * Update the current player info.
	 *
	 * @param playerId the current playerID which will be updated int the current player box.
	 */
	@Override
	public void updateCurrentPlayer(int playerId) {
		currentPlayerId = playerId;
		currentPlayerField.setText(playerId + "");
	}

	/**
	 * Update the players info
	 *
	 * @param playerIDs an array of the player IDs.
	 *                  1 indicate the first player,
	 *                  2 indicate the second player as human,
	 *                  0 indicate the second player as AI.
	 */
	@Override
	public void updatePlayers(int[] playerIDs, Connect4Constant.AI_DIFFICULTY ai) {
		player1Field.setText("Player 1");
		switch (playerIDs[1]) {
			case Connect4Constant.AI:
				player2Field.setText(ai.name() + " AI");
				break;
			case Connect4Constant.PLAYER2:
				player2Field.setText("Player 2");
				break;
			default:
				throw new IllegalArgumentException("Illegal player ID");
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
		switch (color) {
			case RED:
				cells[row][col].setBackground(java.awt.Color.red);
				break;
			case YELLOW:
				cells[row][col].setBackground(java.awt.Color.yellow);
				break;
			default:
				cells[row][col].setBackground(java.awt.Color.gray);
				break;
		}
	}
}
