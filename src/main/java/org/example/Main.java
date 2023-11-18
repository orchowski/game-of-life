package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("GameOfLife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1010);
        var board = new GameBoard();
        frame.getContentPane().add(board);
        List<Long> durations = new ArrayList<>();
        frame.setVisible(true);
        while (durations.size()<100) {
            long startTime = System.nanoTime();
            board.nextGamePhase();
            long endTime = System.nanoTime();
            durations.add(endTime - startTime);
            board.repaint();
        }

        System.out.println("Avg time: " + durations.stream().reduce((long)0, Long::sum)/durations.size());
    }
}