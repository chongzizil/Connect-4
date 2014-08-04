// Base codes copied from http://bit.ly/1hnmalY

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
import java.util.List;

public class Heuristic {

	private final static int ONE = 1;
	private final static int TWO = 22;
	private final static int THREE = 463;
	private final static int FOUR = 9724;

	public Heuristic() {
	}

	/**
	 * Get the value of the current state.
	 *
	 * @return stateValue The value of the state.
	 */
	public int getStateValue(final Connect4Model connect4Model, int row, int col) {
		Connect4Constant.COLOR currColor = connect4Model.getCell(row, col);
		int stateValue = 0;

		if (connect4Model.checkHasWon(row, col)) {
			stateValue = FOUR;
			if (currColor == Connect4Constant.COLOR.YELLOW) {
				return stateValue;
			} else {
				return -stateValue;
			}
		}

		stateValue = getStateValueOfColor(connect4Model, Connect4Constant.COLOR.YELLOW)
				- getStateValueOfColor(connect4Model, Connect4Constant.COLOR.RED);

		return stateValue;
	}

	/**
	 * Get the state value of a specific color.
	 *
	 * @param connect4Model the connect 4 model.
	 * @param color         the color of the state value.
	 * @return the state value of a specific color.
	 */
	private int getStateValueOfColor(final Connect4Model connect4Model, Connect4Constant.COLOR color) {
		int stateValue = 0;

		for (int i = 0; i < Connect4Constant.ROW; i++) {
			int count = 0;
			for (int j = 0; j < Connect4Constant.COLUMN; j++) {
				if (color == connect4Model.getCell(i, j)) {
					count++;
				}

				if (color != connect4Model.getCell(i, j) || i == Connect4Constant.ROW - 1) {
					switch (count) {
						case 1:
							stateValue += ONE;
							break;
						case 2:
							stateValue += TWO;
							break;
						case 3:
							stateValue += THREE;
							break;
						default:
							break;
					}
					count = 0;
				}
			}
		}

		for (int j = 0; j < Connect4Constant.COLUMN; j++) {
			int count = 0;
			for (int i = 0; i < Connect4Constant.ROW; i++) {
				if (color == connect4Model.getCell(i, j)) {
					count++;
				}

				if (color != connect4Model.getCell(i, j) || i == Connect4Constant.ROW - 1) {
					switch (count) {
						case 1:
							stateValue += ONE;
							break;
						case 2:
							stateValue += TWO;
							break;
						case 3:
							stateValue += THREE;
							break;
						default:
							break;
					}
					count = 0;
				}
			}
		}

		return stateValue;
	}

	/**
	 * Get all possible moves..
	 *
	 * @param connect4Model
	 * @return all possible moves.
	 */
	public Iterable<Integer> getAllMoves(final Connect4Model connect4Model) {
		List<Integer> possibleMoves = new ArrayList<Integer>();

		for (int i = 0; i < Connect4Constant.COLUMN; i++) {
			if (connect4Model.getCell(0, i) == Connect4Constant.COLOR.EMPTY) {
				possibleMoves.add(i);
			}
		}

		return possibleMoves;
	}
}