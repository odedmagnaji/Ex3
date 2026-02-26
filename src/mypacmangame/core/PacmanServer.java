package assignments.Ex3.mypacmangame.core;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacmanGame;

public class PacmanServer implements PacmanGame {

    private int[][] board;

    public PacmanServer(int[][] board) {
        this.board = board;
    }

    @Override
    public int[][] getGame(int code) {
        return this.board;
    }

    @Override
    public String move(int i) {
        return "";
    }

    @Override
    public void play() {

    }

    @Override
    public String end(int i) {
        return "";
    }

    @Override
    public String getData(int i) {
        return "";
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public boolean isCyclic() {
        return false;
    }

    @Override
    public String init(int i, String s, boolean b, long l, double v, int i1, int i2) {
        return "";
    }

    @Override
    public Character getKeyChar() {
        return null;
    }

    @Override
    public String getPos(int code) {
        return "0,0";
    }

    @Override
    public GhostCL[] getGhosts(int code) {
        return new GhostCL[0];
    }

}