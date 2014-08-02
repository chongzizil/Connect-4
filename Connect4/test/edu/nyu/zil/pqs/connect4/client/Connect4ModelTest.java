package edu.nyu.zil.pqs.connect4.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Connect4ModelTest {
	Connect4Model connect4Model = Connect4Model.getModel();

	@Before
	public void setUp() throws Exception {
		connect4Model.initialize();
	}

	@After
	public void tearDown() throws Exception {
		connect4Model.initialize();
	}

	// Assume there are still 6 rows.
	@Test
	public void testPlaceMoreThanSevenInAColumn() throws Exception {
		assertEquals("Should be 5", 5, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should be 4", 4, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should be 3", 3, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should be 2", 2, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should be 1", 1, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should be 0", 0, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should be -1", -1, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
		assertEquals("Should still be -1", -1, connect4Model.placePiece(Connect4Constant.PLAYER1, 0));
	}

	@Test
	public void testIfPlaceRightColor() throws Exception {
		connect4Model.placePiece(Connect4Constant.PLAYER1, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.AI, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 2);

		assertEquals("Should be red", Connect4Constant.COLOR.RED, connect4Model.getCell(5, 3));
		assertEquals("Should be yellow", Connect4Constant.COLOR.YELLOW, connect4Model.getCell(4, 3));
		assertEquals("Should be yellow", Connect4Constant.COLOR.YELLOW, connect4Model.getCell(3, 3));
		assertNotEquals("Should not be yellow", Connect4Constant.COLOR.YELLOW, connect4Model.getCell(5, 2));
	}

	@Test
	public void testCheckHorizontalInRow5() throws Exception {
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 1);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 2);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 3);
		assertTrue("Should be true", connect4Model.checkHasWon(5, 3));
	}

	@Test
	public void testCheckHorizontalInRow1() throws Exception {
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 1);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 2);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 1);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 2);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 3);
		assertTrue("Should be true", connect4Model.checkHasWon(4, 3));
	}

	@Test
	public void testCheckVertical() throws Exception {
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		assertTrue("Should be true", connect4Model.checkHasWon(2, 0));
	}

	@Test
	public void testCheckDiagonalIncreased() throws Exception {
		connect4Model.placePiece(Connect4Constant.PLAYER1, 0);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 1);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 1);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 2);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 2);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 2);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 3);
		assertTrue("Should be true", connect4Model.checkHasWon(2, 3));
	}

	@Test
	public void testCheckDiagonalDecreased() throws Exception {
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 3);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 4);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 4);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 4);
		connect4Model.placePiece(Connect4Constant.PLAYER2, 5);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 5);
		connect4Model.placePiece(Connect4Constant.PLAYER1, 6);
		assertTrue("Should be true", connect4Model.checkHasWon(0, 6));
	}
}