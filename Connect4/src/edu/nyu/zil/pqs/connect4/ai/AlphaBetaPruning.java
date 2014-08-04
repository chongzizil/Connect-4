// Base codes copied from: http://bit.ly/1kGOZh2

//Copyright 2012 Google Inc.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package edu.nyu.zil.pqs.connect4.ai;

import edu.nyu.zil.pqs.connect4.client.Connect4Constant;
import edu.nyu.zil.pqs.connect4.client.Connect4Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * http://en.wikipedia.org/wiki/Alpha-beta_pruning<br>
 * This algorithm performs both A* and alpha-beta pruning.<br>
 * The set of possible moves is maintained ordered by the current heuristic
 * value of each move. We first use depth=1, and update the heuristic value of
 * each move, then use depth=2, and so on until we get a timeout or reach
 * maximum depth. <br>
 * If a state has TurnBasedState#whoseTurn null (which happens in
 * backgammon when we should roll the dice), then I treat all the possible moves
 * with equal probabilities. <br>
 *
 * @author yzibin@google.com (Yoav Zibin)
 */

/**
 * This is a alpha beta pruning algorithm for the connect 4.
 */
public class AlphaBetaPruning {
	private Connect4Model connect4Model;
	private Heuristic heuristic;

	static class TimeoutException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@SuppressWarnings("hiding")
	static class MoveScore<Integer> implements Comparable<MoveScore<Integer>> {
		int move;
		int score;

		@Override
		public int compareTo(MoveScore<Integer> o) {
			return o.score - score; // sort DESC (best score first)
		}
	}

	public AlphaBetaPruning(Heuristic heuristic, Connect4Model connect4Model) {
		this.heuristic = heuristic;
		this.connect4Model = connect4Model.copy();

	}

	public int findBestMove(int depth, Timer timer) {
		// Do iterative deepening (A*), and slow get better heuristic values for the states.
		List<MoveScore<Integer>> scores = new ArrayList<MoveScore<Integer>>();

		Iterable<Integer> possibleMoves = heuristic.getAllMoves(connect4Model.copy());
		for (int move : possibleMoves) {
			MoveScore<Integer> score = new MoveScore<Integer>();
			score.move = move;
			score.score = Integer.MIN_VALUE;
			scores.add(score);
		}

		try {
//      long startTime = System.currentTimeMillis();

			for (int i = 0; i < depth; i++) {
				// Get the fullState
//				System.out.println(i + ": " + (System.currentTimeMillis() - startTime));
				for (int j = 0; j < scores.size(); j++) {
					Integer move = null;
					MoveScore<Integer> moveScore = scores.get(j);
					move = moveScore.move;
					int score = findMoveScore(makeMove(connect4Model.copy(), move), move,
							i, Integer.MIN_VALUE, Integer.MAX_VALUE, timer);
					if (!connect4Model.isYellow()) {
						// the scores are from the point of view of the Yellow, so for Red
						// we need to switch.
						score = -score;
					}
					moveScore.score = score;
				}
				// This will give better pruning on the next iteration.
				Collections.sort(scores);
			}
		} catch (TimeoutException e) {
			// OK, it should happen
		}

		Collections.sort(scores);

		int bestMove = scores.get(0).move;

		return bestMove;
	}

	/**
	 * If we get a timeout, then the score is invalid.
	 */
	private int findMoveScore(Connect4Model connect4Model, int moveCol,
	                          int depth, int alpha, int beta, Timer timer) throws TimeoutException {
		int row = findRow(connect4Model, moveCol);

		if (timer.didTimeout()) {
			throw new TimeoutException();
		}

		if (depth == 0 || connect4Model.checkHasWon(row, moveCol)) {
			return heuristic.getStateValue(connect4Model, row, moveCol);
		}

		Iterable<Integer> possibleMoves = heuristic.getAllMoves(connect4Model);
		for (int possibleMove : possibleMoves) {
			int childScore = findMoveScore(makeMove(connect4Model.copy(), possibleMove), possibleMove, depth - 1, alpha, beta, timer);

			if (connect4Model.isYellow()) {
				alpha = Math.max(alpha, childScore);
				if (beta <= alpha) {
					break;
				}
			} else {
				beta = Math.min(beta, childScore);
				if (beta <= alpha) {
					break;
				}
			}
		}

		return connect4Model.isYellow() ? alpha : beta;
	}

	/**
	 * Find the row of the newly dropped piece.
	 *
	 * @param connect4Model the connect 4 model.
	 * @param moveCol       the column of the newly dropped piece.
	 * @return the row of the newly dropped piece.
	 */
	private int findRow(Connect4Model connect4Model, int moveCol) {
		for (int i = 0; i < Connect4Constant.ROW; i++) {
			if (connect4Model.getCell(i, moveCol) != Connect4Constant.COLOR.EMPTY) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Make the move and return the new state.
	 */
	public Connect4Model makeMove(Connect4Model connect4Model, int move) {
		connect4Model.placePiece(connect4Model.isRed() ? Connect4Constant.PLAYER1 : Connect4Constant.AI, move);
		return connect4Model;
	}
}