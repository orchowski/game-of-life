package org.example;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameBoard extends JComponent {
    private final int fieldsize;
    private final int gridsize;
    private final List<Thread> t;

    private List<Field> fieldList = new ArrayList<>();

    public GameBoard(int gridsize, Semaphore semaphore, boolean useVirtualThreads) {
        this.gridsize = gridsize;
        this.fieldsize = 1000 / gridsize;
        for (int row = 0; row < this.gridsize; row++) {
            for (int col = 0; col < this.gridsize; col++) {
                fieldList.add(new Field(row, col, semaphore));
            }
        }

        t = fieldList.stream().map(field -> {
            field.setNeighboursFromTheList(fieldList, this.gridsize);
            return useVirtualThreads ? Thread.startVirtualThread(field) : Thread.ofPlatform().start(field);
        }).toList();
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g.setColor(Color.BLACK);

        for (var field : fieldList) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (field.isAlive()) {
                g2.fillRect(xOf(field), yOf(field), widthOf(field), heightOf(field));
            } else {
                g2.drawRect(xOf(field), yOf(field), widthOf(field), heightOf(field));
            }
        }
    }

    private int widthOf(Field field) {
        return fieldsize;
    }

    private int heightOf(Field field) {
        return fieldsize;
    }

    private int xOf(Field field) {
        return fieldsize * field.getCol();
    }

    private int yOf(Field field) {
        return fieldsize * field.getRow();
    }

    public void nextGamePhase() {
        for (Field f : fieldList) {
            f.unblock();
        }
        while (true) {
            if (fieldList.stream().allMatch(x->x.getLock().get())) {
                fieldList.forEach(Field::switchToNewState);
                break;
            }
        }
    }
}
