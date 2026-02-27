package mypacmangame.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import assignments.Ex3.mypacmangame.core.PacmanServer;
import assignments.Ex3.mypacmangame.entities.GhostEntity;
import assignments.Ex3.mypacmangame.entities.PacmanEntity;
import exe.ex3.game.GhostCL;

public class PacmanServerTest {

    @Test
    void testInitialBoardStorage() {
        int[][] mockBoard = {{1, 1, 1}, {1, 0, 1}};
        PacmanServer server = new PacmanServer(mockBoard);
        assertArrayEquals(mockBoard, server.getGame(0));
    }

    @Test
    void testGhostIntegration() {
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);
        GhostEntity blinky = new GhostEntity("5,5", 0, 1);
        server.initGhosts(new GhostCL[]{blinky});
        assertEquals("5,5", server.getGhosts(0)[0].getPos(0));
    }

    @Test
    void testPacmanIntegration() {
        int[][] emptyBoard = {{0}};
        PacmanServer server = new PacmanServer(emptyBoard);
        server.initPacman(new PacmanEntity("7,7"));
        assertEquals("7,7", server.getPos(0));
    }

    @Test
    void testPacmanMovement() {
        // Board layout:
        // 1 1 1
        // 1 0 0
        // 1 1 1
        int[][] board = {
                {1, 1, 1},
                {1, 0, 0},
                {1, 1, 1}
        };
        PacmanServer server = new PacmanServer(board);

        PacmanEntity pacman = new PacmanEntity("1,1");
        server.initPacman(pacman);

        // Try to move UP (dir 0) into a wall (x=1, y=0)
        server.move(0);
        assertEquals("1,1", server.getPos(0), "Pacman should not move into a wall.");

        // Try to move RIGHT (dir 1) into an empty space (x=2, y=1)
        server.move(1);
        assertEquals("2,1", server.getPos(0), "Pacman should successfully move right.");
    }
}