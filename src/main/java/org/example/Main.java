package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("GameOfLife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1010);
        var board = new GameBoard(30);
        frame.getContentPane().add(board);

        frame.setVisible(true);
        while (true) {
            board.nextGamePhase();
            board.repaint();
            Thread.sleep(1000);
        }
    }
}