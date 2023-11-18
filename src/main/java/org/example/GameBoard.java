package org.example;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard extends JComponent {
    private final static int FIELD_SIZE = 6;
    private final static int GRID_SIZE = 30 * 5;

    private List<Field> fieldList = new ArrayList<>();

    public GameBoard() {
        for (int row = 0; row < GRID_SIZE - 1; row++) {
            for (int col = 0; col < GRID_SIZE - 1; col++) {
                fieldList.add(new Field(row, col));
            }
        }
        fieldList.forEach(field -> field.setNeighboursFromTheList(fieldList, GRID_SIZE));
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
        return FIELD_SIZE;
    }

    private int heightOf(Field field) {
        return FIELD_SIZE;
    }

    private int xOf(Field field) {
        return FIELD_SIZE * field.getCol();
    }

    private int yOf(Field field) {
        return FIELD_SIZE * field.getRow();
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
