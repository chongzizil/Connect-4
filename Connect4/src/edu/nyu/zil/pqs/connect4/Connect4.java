package edu.nyu.zil.pqs.connect4;

import edu.nyu.zil.pqs.connect4.client.Connect4Controller;
import edu.nyu.zil.pqs.connect4.graphic.Connect4View;

public class Connect4 {
	public static void main(String[] args) {
		Connect4Controller connect4Controller = new Connect4Controller();
		Connect4View connect4View = new Connect4View();
		connect4Controller.addListener(connect4View);
	}
}
