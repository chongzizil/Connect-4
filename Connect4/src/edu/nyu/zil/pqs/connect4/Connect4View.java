package edu.nyu.zil.pqs.connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Zil on 2014/7/31.
 */
public class Connect4View implements Connect4Listener {
    private Connect4Model connect4Model;
    private int currentPlayerId;

    private JFrame frame = new JFrame("Connect 4");

    JPanel jPanel = new JPanel();

    JPanel infoPanel = new JPanel();
    JPanel displayPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    JPanel buttonPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton[] buttons = new JButton[Connect4Model.COLUMN];
    JPanel[][] cells = new JPanel[Connect4Model.ROW][Connect4Model.COLUMN];

    JTextField currentPlayerField = new JTextField();
    JTextField player1Field = new JTextField();
    JTextField player2Field = new JTextField();
    JTextField messageField = new JTextField();

    JButton resetButton = new JButton("Reset");

    Connect4Model.MODE mode;

    public Connect4View(final Connect4Model connect4Model) {
        this.connect4Model = connect4Model;
        this.connect4Model.addListener(this);

        jPanel.setLayout(new BorderLayout());
        infoPanel.setLayout(new GridLayout(1, 3));
        displayPanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new GridLayout(1, Connect4Model.COLUMN));
        boardPanel.setLayout(new GridLayout(Connect4Model.ROW, Connect4Model.COLUMN));

        jPanel.add(infoPanel, BorderLayout.NORTH);
        jPanel.add(displayPanel, BorderLayout.CENTER);
        jPanel.add(bottomPanel, BorderLayout.SOUTH);

        infoPanel.add(player1Field);
        infoPanel.add(currentPlayerField);
        infoPanel.add(player2Field);

        displayPanel.add(buttonPanel, BorderLayout.NORTH);
        displayPanel.add(boardPanel, BorderLayout.CENTER);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.EAST);

        for (int i = 0; i < connect4Model.COLUMN; i++) {
            buttons[i] = new JButton(i + "");
            final int finalI = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    connect4Model.placePiece(currentPlayerId, finalI);
                }
            });
            buttonPanel.add(buttons[i]);
        }

        for (int i = 0; i < connect4Model.ROW; i++) {
            for (int j = 0; j < connect4Model.COLUMN; j++) {
                cells[i][j] = new JPanel();
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                cells[i][j].setBackground(java.awt.Color.gray);
                boardPanel.add(cells[i][j]);
            }
        }

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Model.reset();
                for (int i = 0; i < connect4Model.ROW; i++) {
                    for (int j = 0; j < connect4Model.COLUMN; j++) {
                        cells[i][j].setBackground(java.awt.Color.gray);
                    }
                }
            }
        });

        Object[] options = {"PLAYER VS PLAYER", "PLAYER VS AI"};
        int modeIndex = JOptionPane.showOptionDialog(frame,
                "Please choose the play mode :)",
                "Choose the mode",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        switch (modeIndex) {
            case 0:
                mode = Connect4Model.MODE.PLAYER;
                break;
            case 1:
                mode = Connect4Model.MODE.AI;
                break;
        }

        connect4Model.play(mode);

        frame.add(jPanel);
//        frame.getContentPane().add(jPanel);
        frame.setSize(500, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
     * @param playerId the current playerID which will be updated int the current player box.
     */
    @Override
    public void updateCurrentPlayer(int playerId) {
        currentPlayerId = playerId;
        currentPlayerField.setText(playerId + "");
    }

    /**
     * Update the game board.
     *
     * @param color the color which is going to be updated for the cell.
     * @param row the row of the cell which will be updated.
     * @param col the column of the cell which will be updated.
     */
    @Override
    public void updateBoard(Connect4Model.COLOR color, int row, int col) {
        switch (color) {
            case RED:
                cells[row][col].setBackground(java.awt.Color.red);
                break;
            case YELLOW:
                cells[row][col].setBackground(java.awt.Color.yellow);
                break;
        }
    }
}
