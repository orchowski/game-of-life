package org.example;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("GameOfLife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1010);
        int gridsize = 30;

        Semaphore semaphore = new Semaphore(100000, true);
        var board = new GameBoard(gridsize, semaphore, true);
        frame.getContentPane().add(board);

        frame.setVisible(true);
        while (true) {
            if (semaphore.availablePermits() == 100000) {
                board.nextGamePhase();
                board.repaint();
            }
        }
    }
}