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

	public Heuristic() {
	}

	/**
	 * Get the value of the current state.
	 *
	 * @return stateValue The value of the state.
	 */
	public int getStateValue(final Connect4Model connect4Model, int row, int col) {
		if (connect4Model.checkHasWon(row, col)) {
			return Integer.MAX_VALUE;
		} else {
			return 1;
		}
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