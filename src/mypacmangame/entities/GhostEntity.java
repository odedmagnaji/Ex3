package assignments.Ex3.mypacmangame.entities;

import exe.ex3.game.GhostCL;

public class GhostEntity implements GhostCL {

    private String position;
    private int status;
    private int type;
    private int direction;

    public GhostEntity(String position, int status, int type) {
        this.position = position;
        this.status = status;
        this.type = type;
        this.direction = 1;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getGhostPosition() {
        return this.position;
    }

    public int getGhostStatus() {
        return this.status;
    }

    public int getGhostType() {
        return this.type;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getPos(int code) {
        return this.position;
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

    // =========================================================
    // DO NOT COPY THE RED METHODS. INSTEAD, DO THIS:
    // 1. Hover over the red 'GhostEntity' class name at the top.
    // 2. Press Alt + Enter -> "Implement methods" -> OK.
    // 3. Inside the new methods it created at the bottom, write:
    //    return this.status; (for the status method)
    //    return this.type;   (for the type method)
    //    return this.direction; (for the direction method, if any)
    // =========================================================
}