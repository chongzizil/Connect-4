package edu.nyu.zil.pqs.connect4;

/**
 * Created by Zil on 2014/7/31.
 */
public class Connect4 {
    public static void main(String[] args) {
        Connect4Model model = Connect4Model.getModel();
        Connect4View view = new Connect4View(model);
    }
}
