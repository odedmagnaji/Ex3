package assignments.Ex3.mypacmangame.entities;

import exe.ex3.game.GhostCL;

public class GhostEntity implements GhostCL {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_FRIGHTENED = 1;

    private String position;
    private int status;
    private int type;

    public GhostEntity(String position, int status, int type) {
        this.position = position;
        this.status = status;
        this.type = type;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getX() {
        try {
            return Integer.parseInt(this.position.split(",")[0]);
        } catch (Exception e) { return 0; }
    }

    public int getY() {
        try {
            return Integer.parseInt(this.position.split(",")[1]);
        } catch (Exception e) { return 0; }
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String getPos(int code) {
        return this.position;
    }

    @Override
    public String getInfo() {
        return "Type:" + type + ", Status:" + status + ", Pos:" + position;
    }

    @Override
    public double remainTimeAsEatable(int i) {
        return (status == STATUS_FRIGHTENED) ? 6.0 : 0.0;
    }
}