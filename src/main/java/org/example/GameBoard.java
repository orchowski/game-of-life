package org.example;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JComponent {
    private final int fieldSize;
    private final int gridSize;

    private List<Field> fieldList = new ArrayList<>();

    public GameBoard(int gridSize) {
        this.gridSize = gridSize;
        this.fieldSize = 1000/gridSize;
        for (int row = 0; row < gridSize - 1; row++) {
            for (int col = 0; col < gridSize - 1; col++) {
                fieldList.add(new Field(row, col));
            }
        }
        fieldList.forEach(field -> field.setNeighboursFromTheList(fieldList, gridSize));
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g.setColor(Color.BLACK);

        for (var field : fieldList) {
            if (field.isAlive()) {
                g2.fillRect(xOf(field), yOf(field), widthOf(field), heightOf(field));
            } else {
                g2.drawRect(xOf(field), yOf(field), widthOf(field), heightOf(field));
            }
        }
    }

    private int widthOf(Field field) {
        return fieldSize;
    }

    private int heightOf(Field field) {
        return fieldSize;
    }

    private int xOf(Field field) {
        return fieldSize * field.getCol();
    }

    private int yOf(Field field) {
        return fieldSize * field.getRow();
    }

    public void nextGamePhase() {
        fieldList.forEach(Field::nextPhase);
        fieldList.forEach(Field::switchToNewState);
    }

    public void nextGamePhaseInThreads() {
        fieldList.parallelStream().map(field -> Thread.ofPlatform().start(field)).forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        fieldList.forEach(Field::switchToNewState);
    }

    public void nextGamePhaseInVirtualThreads() {
        fieldList.parallelStream().map(Thread::startVirtualThread).forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        fieldList.forEach(Field::switchToNewState);
    }
}
