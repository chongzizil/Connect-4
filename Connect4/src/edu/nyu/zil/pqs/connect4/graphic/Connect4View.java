package edu.nyu.zil.pqs.connect4.graphic;

import edu.nyu.zil.pqs.connect4.client.Connect4Constant;
import edu.nyu.zil.pqs.connect4.client.Connect4Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4View implements Connect4Listener {
	Connect4Controller connect4Controller;

	private JFrame frame;
	// Base panel
	private JPanel jPanel;

	// Info panel, positions at the top of the base panel.
	// Contains 3 text fields to display the two players name and the current player name.
	private JPanel infoPanel;
	private JTextField currentPlayerField;
	private JTextField player1Field;
	private JTextField player2Field;

	// Main panel, positions at the middle of the base panel.
	// Contains the operation panel (for game operation) and board panel (display the game board)..
	private JPanel mainPanel;
	private JPanel operationPanel;
	private JButton[] buttons;
	private JPanel boardPanel;
	private JPanel[][] cells;

	// Bottom panel, positions at the bottom of the base panel.
	// Contains a game mode panel (for choosing game mode) and message panel (display message and
	// a reset or replay button)
	private JPanel bottomPanel;
	private JPanel gameModePanel;
	private JButton vsPlayerButton;
	private JPanel messagePanel;
	private JTextField messageField;
	private JButton replayButton;

	public Connect4View() {
		frame = new JFrame("Connect 4");

		jPanel = new JPanel();

		infoPanel = new JPanel();
		currentPlayerField = new JTextField();
		currentPlayerField.setEnabled(false);
		player1Field = new JTextField();
		player1Field.setEnabled(false);
		player2Field = new JTextField();
		player2Field.setEnabled(false);

		mainPanel = new JPanel();
		operationPanel = new JPanel();
		buttons = new JButton[Connect4Constant.COLUMN];
		boardPanel = new JPanel();
		cells = new JPanel[Connect4Constant.ROW][Connect4Constant.COLUMN];

		bottomPanel = new JPanel();
		gameModePanel = new JPanel();
		vsPlayerButton = new JButton("Player");

		messagePanel = new JPanel();
		messageField = new JTextField();
		messageField.setEnabled(false);
		replayButton = new JButton("Replay");

		jPanel.setLayout(new BorderLayout());

		infoPanel.setLayout(new GridLayout(1, 3));

		mainPanel.setLayout(new BorderLayout());
		boardPanel.setLayout(new GridLayout(Connect4Constant.ROW, Connect4Constant.COLUMN));

		bottomPanel.setLayout(new GridLayout(2, 1));
		gameModePanel.setLayout(new GridLayout(1, 5));
		messagePanel.setLayout(new GridLayout(1, 2));

		jPanel.add(infoPanel, BorderLayout.NORTH);
		jPanel.add(mainPanel, BorderLayout.CENTER);
		jPanel.add(bottomPanel, BorderLayout.SOUTH);

		infoPanel.add(player1Field);
		infoPanel.add(currentPlayerField);
		infoPanel.add(player2Field);

		mainPanel.add(operationPanel, BorderLayout.NORTH);
		mainPanel.add(boardPanel, BorderLayout.CENTER);

		gameModePanel.add(vsPlayerButton);

		messageField.setText("Please choose a mode first!");
		messagePanel.add(messageField);
		messagePanel.add(replayButton);
		bottomPanel.add(gameModePanel);
		bottomPanel.add(messagePanel);

		for (int i = 0; i < Connect4Constant.COLUMN; i++) {
			buttons[i] = new JButton(i + "");
			final int finalI = i;
			buttons[i].setEnabled(false);
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connect4Controller.placeNextPiece(finalI);
				}
			});
			operationPanel.add(buttons[i]);
		}

		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				cells[i][j] = new JPanel();
				cells[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				cells[i][j].setBackground(java.awt.Color.gray);
				boardPanel.add(cells[i][j]);
			}
		}

		replayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.play();
				initialize();
			}
		});

		vsPlayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect4Controller.setMode(Connect4Constant.MODE.PLAYER);
				connect4Controller.play();
				initialize();
			}
		});

		// Add the ai mode buttons
		for (final Connect4Constant.AI_DIFFICULTY ai_difficulty : Connect4Constant.AI_DIFFICULTY.values()) {
			JButton tmp = new JButton(ai_difficulty.name());
			tmp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connect4Controller.setMode(Connect4Constant.MODE.AI);
					connect4Controller.setAI(ai_difficulty);
					connect4Controller.play();
					initialize();
				}
			});
			gameModePanel.add(tmp);
		}

		frame.add(jPanel);
		frame.setSize(500, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void setConnect4Controller(Connect4Controller connect4Controller) {
		this.connect4Controller = connect4Controller;
	}

	/**
	 * Initialize the game. Set all button enabled and initialize the game board.
	 */
	public void initialize() {
		for (int i = 0; i < Connect4Constant.COLUMN; i++) {
			buttons[i].setEnabled(true);
		}

		for (int i = 0; i < Connect4Constant.ROW; i++) {
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				cells[i][j].setBackground(java.awt.Color.gray);
			}
		}
	}

	/**
	 * Disable all the buttons.
	 */
	public void disableButtons() {
		for (int i = 0; i < Connect4Constant.COLUMN; i++) {
			buttons[i].setEnabled(false);
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
	 * Update the current player info.
	 *
	 * @param playerID the current playerID which will be updated int the current player box.
	 */
	@Override
	public void updateCurrentPlayer(int playerID) {
		switch (playerID) {
			case Connect4Constant.PLAYER1:
				currentPlayerField.setText("Player " + playerID);
				break;
			case Connect4Constant.AI:
				currentPlayerField.setText("AI");
				break;
			case Connect4Constant.PLAYER2:
				currentPlayerField.setText("Player " + playerID);
				break;
			default:
				throw new IllegalArgumentException("Illegal player ID");
		}

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
