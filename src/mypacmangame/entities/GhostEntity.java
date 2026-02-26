package assignments.Ex3.mypacmangame.entities;

import exe.ex3.game.GhostCL;

/**
 * Represents a single Ghost entity in the game.
 */
public class GhostEntity implements GhostCL {

    private String position;
    private int status;
    private int type;

    public GhostEntity(String position, int status, int type) {
        this.position = position;
        this.status = status;
        this.type = type;
    }

    // --- Setters for updating the ghost ---
    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // --- Custom Getters for our internal logic and tests ---
    public String getGhostPosition() { return this.position; }
    public int getGhostStatus() { return this.status; }
    public int getGhostType() { return this.type; }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getPos(int i) {
        return "";
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public double remainTimeAsEatable(int i) {
        return 0;
    }

    @Override
    public int getStatus() {
        return 0;
    }


}